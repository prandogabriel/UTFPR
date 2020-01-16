#include <stdio.h>

void imprime_bin_rec(int x) {
  if (x == 0)
    printf("0");
  else if(x == 1)
    printf("1");
  else {
    imprime_bin_rec(x / 2);
    printf("%d", x % 2);
  }
}

main() {
  int x;
  scanf("%d", &x);
  printf("%d em binario:\n", x);
  imprime_bin_rec(x);
  printf("\n");
}
