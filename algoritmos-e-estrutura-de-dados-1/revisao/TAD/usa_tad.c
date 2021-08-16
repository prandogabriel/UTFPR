#include <stdio.h>
#include "tad.h"

int main(void)
{
  Item *x;
  x = criaItem(7);
  mudaChave(x);
  printaItem(x);
  
  return 0;
}
