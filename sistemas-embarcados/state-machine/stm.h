typedef struct xStateMachine xSTM;

typedef void (*Action)(xSTM *sm, unsigned char data);

/* possiveis estados */
typedef enum
{
  ST_STX = 0,
  ST_QTD,
  ST_DATA,
  ST_CHK,
  ST_ETX,
  ST_END
} xStates;

struct xStateMachine
{
  States state;
  unsigned char buffer[MAX_BUFFER];
  unsigned char chkBuffer;
  int indBuffer;
  int qtdBuffer;
  Action actions[ST_END];
};

xSTM *NewStateMachine();

void Exec(xSTM *stm, unsigned char data);
