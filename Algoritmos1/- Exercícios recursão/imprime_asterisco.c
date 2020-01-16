#include <stdio.h>

void imprime_rec(int x) {
  if(x == 0)
    return;

  imprime_rec(x - 1);
  printf("*");
}

main() {
  int x;
  scanf("%d", &x);
  imprime_rec(x);
  printf("\n");
}
