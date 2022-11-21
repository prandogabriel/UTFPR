#include "os.h"
#include <stdlib.h>

tcb_t tcb_idle;

cpu_t idle_stk[128];

volatile long long os_time = 0;

list_t readylist = {null, null};
list_t timelist = {null, null};

tcb_t *current_task;


#define sched sched_prio

void idle(void) {
  for(;;){
    // dorme aqui
  }
}

cpu_t *scheduler(void){
  tcb_t *ptask = readylist.head;
  cpu_t higher_prio = 0;
  tcb_t *higher_task = 0;
  
  while(ptask != null)
  { 
    if (ptask->prio >= higher_prio){
      higher_prio = ptask->prio;
      higher_task = ptask;
    }
    ptask = ptask->next;
  }
  current_task = higher_task;
  return current_task->stk;
}

 
   

void os_start(void){
  os_install_task(&tcb_idle, idle, 0, idle_stk, sizeof(idle_stk));
  init_os_timer();
  stk_tmp = scheduler();
  dispatcher();
}


void os_delay(long long timeout){
  tcb_t *ptask = current_task;
  ptask->timeout = timeout + os_time;
  ENTER_CRITICAL();
  RemoveTaskFromList(ptask, &readylist);
  IncludeTaskIntoList(ptask, &timelist);
  EXIT_CRITICAL();
  yield();
}


cpu_t os_inc_and_compare(void){
  tcb_t *ptask = timelist.head;
  tcb_t *tmp_task;
  cpu_t ready = 0; 

  os_time++;
  while(ptask != null){ 
    if (ptask->timeout == os_time){
      tmp_task = ptask->next;
      RemoveTaskFromList(ptask, &timelist);
      IncludeTaskIntoList(ptask, &readylist);
      if (ptask->event != null){
        remove_event(ptask);
      }
      ready = 1;
      ptask = tmp_task;
    }else{ 
      ptask = ptask->next;
    }
  }  
  
  return ready;
}


void os_install_task(tcb_t *ptask, task_t task, cpu_t prio, cpu_t *stk, int stk_size){
  ptask->stk = PrepareStack(task, stk, stk_size);
  ptask->prio = prio;
  ptask->event = null;
  ptask->event_type = 0;
  ENTER_CRITICAL();
  IncludeTaskIntoList(ptask, &readylist);
  EXIT_CRITICAL();
}


cpu_t os_sem_init(sem_t *sem){
  sem->value = 0;
  sem->list.head = null;
  sem->list.tail = null;
  return 0;
}

cpu_t os_sem_pend(sem_t *sem, long long timeout){
  ENTER_CRITICAL();

  tcb_t *ptask = current_task;
  if (sem->value){
    sem->value--;
    EXIT_CRITICAL();
    return 0;
  }

  ptask->timeout = timeout + os_time;
  RemoveTaskFromList(ptask, &readylist);
  IncludeTaskIntoList(ptask, &(sem->list));
  EXIT_CRITICAL();

  if (timeout){
    ENTER_CRITICAL();
    IncludeTaskIntoList(ptask, &timelist);
    ptask->event = (void *)sem;
    ptask->event_type = event_sem;
    EXIT_CRITICAL();
  }
  yield();
  
  return 0;
}

cpu_t os_sem_post(sem_t *sem){
  ENTER_CRITICAL();
  tcb_t *ptask = sem->list.head;
  if (sem->list.tail == null){
    if (!(sem->value)){
      sem->value++;
    }
    EXIT_CRITICAL();
    return 0;
  }
  
  RemoveTaskFromList(ptask, &(sem->list));
  if (ptask->event != null){
    ptask->event = null;
    ptask->event_type = 0;
    RemoveTaskFromList(ptask, &timelist);
  }
  IncludeTaskIntoList(ptask, &readylist);
  EXIT_CRITICAL();
  
  yield();
  
  return 0;
}

cpu_t os_mutex_init(sem_t *sem){
  sem->value = 1;
  sem->list.head = null;
  sem->list.tail = null;
  return 0;
}

void remove_event(tcb_t *ptask){
  sem_t *sem_tmp;
  switch(ptask->event_type){
    case event_sem:
      sem_tmp = (sem_t *)ptask->event;
      ENTER_CRITICAL();
      RemoveTaskFromList(ptask, &(sem_tmp->list));
      ptask->event = null;
      ptask->event_type = 0;
      EXIT_CRITICAL();
    break;
    
    case event_queue:
    break;
  }
}


void RemoveTaskFromList(tcb_t *ptask, list_t *list){
    if(ptask == list->head){               
      if(ptask == list->tail){                
        list->tail = NULL;   
        list->head = NULL;   
      }                
      else{                
        list->head = ptask->next;
        list->head->previous = NULL;
      }                       
    }                         
    else{                         
      if(ptask == list->tail){                       
        list->tail = ptask->previous;
        list->tail->next = NULL;    
      }                       
      else{                       
        ptask->next->previous = ptask->previous;
        ptask->previous->next = ptask->next;   
      }                                       
    }
}


void IncludeTaskIntoList(tcb_t *ptask, list_t *list){
    if(list->tail != NULL){
      /* Insert task into list */
      list->tail->next = ptask;
      ptask->previous = list->tail;
      list->tail = ptask;
      list->tail->next = NULL;
    }
    else{
       /* Init delay list */
       list->tail = ptask;
       list->head = ptask;
       ptask->next = NULL;
       ptask->previous = NULL;
    }
}
