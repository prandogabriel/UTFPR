typedef struct StateMachine STM;

typedef void (*Action)(STM *sm, unsigned char data);

/* possiveis estados */
typedef enum
{
  ST_STX = 0,
  ST_QTD,
  ST_DATA,
  ST_CHK,
  ST_ETX,
  ST_END
} States;

struct StateMachine
{
  States state;
  unsigned char buffer[MAX_BUFFER];
  unsigned char chkBuffer;
  int indBuffer;
  int qtdBuffer;
  Action actions[ST_END];
};

STM *NewStateMachine();

void Exec(STM *stm, unsigned char data);
