#include "stdio.h"
#include"./stm.h"

int main() {
  STM *stm1 = NewStateMachine();

  Exec(stm1, "");
  return 0;
}
