

/* Constants required to set up the initial stack. */
#define INITIAL_XPSR                    0x01000000

/* Cortex-M specific definitions. */
#define PRIO_BITS                       4                           // 15 priority levels
#define LOWEST_INTERRUPT_PRIORITY       0xF
#define KERNEL_INTERRUPT_PRIORITY       (LOWEST_INTERRUPT_PRIORITY << (8 - PRIO_BITS) )

/* Constants required to manipulate the NVIC PendSV */
#define NVIC_PENDSVSET                  0x10000000                  // Value to trigger PendSV exception.
#define NVIC_PENDSVCLR                  0x08000000                  // Value to clear PendSV exception.

// Constants required to manipulate the NVIC SysTick
#define NVIC_SYSTICK_CLK                0x00000004
#define NVIC_SYSTICK_INT                0x00000002
#define NVIC_SYSTICK_ENABLE             0x00000001

// ARM Cortex-Mx registers
#define NVIC_SYSTICK_CTRL               ( ( volatile unsigned long *) 0xe000e010 )
#define NVIC_SYSTICK_LOAD               ( ( volatile unsigned long *) 0xe000e014 )
#define NVIC_INT_CTRL                   ( ( volatile unsigned long *) 0xe000ed04 )
#define FPU_FPCCR                       ( ( volatile unsigned long *) 0xE000EF34 )
#define NVIC_SYSPRI3                    ( ( volatile unsigned long *) 0xe000ed20 )

// Kernel interrupt priorities
#define NVIC_PENDSV_PRI                 ( ( ( unsigned long ) KERNEL_INTERRUPT_PRIORITY ) << 16 )
#define NVIC_SYSTICK_PRI                ( ( ( unsigned long ) KERNEL_INTERRUPT_PRIORITY ) << 24 )


#define FPU_ENABLE 0

typedef unsigned int cpu_t;
extern cpu_t *stk_tmp;

void init_os_timer(void);
cpu_t *PrepareStack(void *task, cpu_t *stk, int stk_size);


#if FPU_ENABLE == 1
#define SAVE_CONTEXT()    

#define RESTORE_CONTEXT()    
#else
#define SAVE_CONTEXT()    

#define RESTORE_CONTEXT()    

#endif
        
        
#define SAVE_SP() 
        
#define RESTORE_SP() 


#define dispatcher() 

#define ENTER_CRITICAL()   
#define EXIT_CRITICAL()    
        

#define yield()    

#define Clear_PendSV() 


        
