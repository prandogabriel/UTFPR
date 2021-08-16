#include <stdio.h>

int soma_n(int n) {
  int i;
  int soma = n;
  for(i = n - 1; i > 0; i--)
    soma += i;
  return soma;
}

int soma_n_rec(int n){
  if(n == 1) // menor instância do problema - já retorna o resultado
    return 1;
  else // chama a função novamente para calcular n * (n - 1)
    return n + soma_n_rec(n - 1);
}

main() {
  int n = 5;
  printf("Soma dos %d primeiros inteiros: %d\n", n, soma_n(n));
  printf("Soma dos %d primeiros inteiros: %d\n", n, soma_n_rec(n));
}
