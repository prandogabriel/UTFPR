#include <stm.h>

#define STX 0x02
#define ETX 0x03
#define MAX_BUFFER 1000

STM *NewStateMachine()
{
  STM *stm;
  stm->state = ST_STX;

  loadActions(stm);

  return stm;
}

void Exec(STM *stm, unsigned char data)
{
  stm->actions[stm->state](stm, data);
}

static void loadActions(STM *stm)
{
  stm->actions[ST_STX] = stSTX;
  stm->actions[ST_QTD] = stQtd;
  stm->actions[ST_DATA] = stData;
  stm->actions[ST_CHK] = stChk;
  stm->actions[ST_ETX] = stETX;
}

static void stSTX(STM *sm, unsigned char data)
{
  if (data == STX)
  {
    sm->indBuffer = sm->qtdBuffer = 0;
    sm->chkBuffer = 0;
    sm->state = ST_QTD;
  }
}

static void stQtd(STM *sm, unsigned char data)
{
  sm->qtdBuffer = data;
  sm->state = ST_DATA;
}

static void stData(STM *sm, unsigned char data)
{
  sm->buffer[sm->indBuffer++] = data;
  sm->chkBuffer ^= data;
  if (--sm->qtdBuffer == 0)
  {
    sm->state = ST_CHK;
  }
}

static void stChk(STM *sm, unsigned char data)
{
  if (data == sm->chkBuffer)
  {
    sm->state = ST_ETX;
  }
  else
  {
    sm->state = ST_STX;
  }
}

static void stETX(STM *sm, unsigned char data)
{
  if (data == ETX)
  {
    handlePackage(sm->buffer, sm->indBuffer);
  }
  sm->state = ST_STX;
}
