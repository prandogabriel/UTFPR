#include <stdio.h>

int fatorial(int n) {
  int i;
  int fat = n;
  for(i = n - 1; i > 0; i--)
    fat *= i;
  return fat;
}

int fatorial_rec(int n){
  if(n == 1) // menor instância do problema - já retorna o resultado
    return 1;
  else // chama a função novamente para calcular n * (n - 1)
    return n * fatorial_rec(n - 1);
}

main() {
  int n = 5;
  printf("Fatorial de %d: %d\n", n, fatorial(n));
  printf("Fatorial de %d: %d\n", n, fatorial_rec(n));
}
