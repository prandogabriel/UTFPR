#include <stdio.h>
#include <stdlib.h>

void soma(int x, int y)
{
  int soma = 0;
  soma = x + y;
  printf("%d", soma);
  x = 7;
  y = -1;
}

int main()
{
  int x;
  int y;

  x = 2;
  y = 4;
  soma(x, y);

  printf("\n\n%d \n %d ", x, y);
  return 0;
}
