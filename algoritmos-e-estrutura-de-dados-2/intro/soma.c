#include <stdio.h>
#include<stdlib.h>
#include<math.h>
int soma(int a, int b ){
  return a+b;
}
main()
{
  int x,y;
  scanf("%d %d", &x, &y);
  printf("%d",soma(x, y));
}
