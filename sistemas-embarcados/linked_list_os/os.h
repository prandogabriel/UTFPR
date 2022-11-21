#include "port.h"

#define null (void *)0

typedef enum{
  event_sem=1,
  event_queue
}event_t;

typedef void (*task_t)(void); 

struct _TCB{
  cpu_t *stk;
  cpu_t prio;
  cpu_t ready;
  void *event;
  event_t event_type;
  long long timeout;
  struct _TCB *next;
  struct _TCB *previous;  
};

typedef struct _TCB tcb_t;

typedef struct _list_t{
  tcb_t *tail;
  tcb_t *head;
}list_t;


typedef struct _sem_t_{
  cpu_t value;
  list_t list;
}sem_t;

extern tcb_t *current_task;

void os_install_task(tcb_t *ptask, task_t task, cpu_t prio, cpu_t *stk, int stk_size);

void os_delay(long long timeout);
void os_start(void);
cpu_t os_inc_and_compare(void);
cpu_t *scheduler(void);

cpu_t os_sem_init(sem_t *sem);
cpu_t os_sem_pend(sem_t *sem, long long timeout);
cpu_t os_sem_post(sem_t *sem);

cpu_t os_mutex_init(sem_t *sem);
#define os_mutex_acquire(sem, timeout) os_sem_pend(sem, timeout)
#define os_mutex_release(sem) os_sem_post(sem)

void remove_event(tcb_t *ptask);

void RemoveTaskFromList(tcb_t *ptask, list_t *list);
void IncludeTaskIntoList(tcb_t *ptask, list_t *list);

