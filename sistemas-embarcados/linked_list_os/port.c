#include <stdint.h>
#include "os.h"

cpu_t *stk_tmp;

cpu_t *PrepareStack(void *task, cpu_t *stk, int stk_size){
  stk = (cpu_t*)((int)stk + stk_size);
  
  *--stk = (cpu_t)INITIAL_XPSR;                       /* xPSR                                                   */

  
  return stk;
}

/* rotinas de interrupções necessárias */
__attribute__ ((naked)) void SVC_Handler(void)
{
    /* Make PendSV and SysTick the lowest priority interrupts. */
    *(NVIC_SYSPRI3) |= NVIC_PENDSV_PRI;
    *(NVIC_SYSPRI3) |= NVIC_SYSTICK_PRI;
    RESTORE_SP();
    RESTORE_CONTEXT();
}

__attribute__ ((naked)) void SwitchContext(void){
  SAVE_CONTEXT();
  SAVE_SP();

  Clear_PendSV();

  current_task->stk=stk_tmp;
  stk_tmp = scheduler();  
  
  RESTORE_SP();
  RESTORE_CONTEXT();
}



void init_os_timer(void){
    uint32_t cpu_clock_hz = 120000000;//system_cpu_clock_get_hz();
    uint16_t valor_comparador = cpu_clock_hz/1000; //cfg_MARCA_TEMPO_HZ; //(cfg_CPU_CLOCK_HZ / cfg_MARCA_TEMPO_HZ);

    *(NVIC_SYSTICK_CTRL) = 0;                       // Desabilita SysTick Timer
    *(NVIC_SYSTICK_LOAD) = valor_comparador - 1;    // Configura a contagem
    *(NVIC_SYSTICK_CTRL) = NVIC_SYSTICK_CLK | NVIC_SYSTICK_INT | NVIC_SYSTICK_ENABLE;  // Inicia
}                                     


void TickTimer(void){

  
  if(os_inc_and_compare()){
      yield();
  }
  
}
