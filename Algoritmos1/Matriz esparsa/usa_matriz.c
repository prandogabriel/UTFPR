#include <stdio.h>
#include "matriz.h"

main() {
  Matriz *m = inicializa_matriz(3, 4);
  adiciona_celula(m, 5, 1, 20); // 5, 1 - posição inexistente na matriz
  adiciona_celula(m, 1, 2, 20); // 1, 2
  adiciona_celula(m, 2, 2, 50);
  adiciona_celula(m, 1, 3, 90);
  adiciona_celula(m, 2, 3, 45);
  imprime_matriz(m);

  adiciona_celula(m, 2, 3, 0);
  adiciona_celula(m, 1, 3, 15);
  imprime_matriz(m);

  printf("\n");
  printf("%.2f\n", busca_valor(m, 2, 2));
  printf("%.2f\n", busca_valor(m, 1, 3));
  printf("%.2f\n", busca_valor(m, 1, 1));

  libera_matriz(m);
}
