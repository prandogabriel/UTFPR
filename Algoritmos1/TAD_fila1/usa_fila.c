#include <stdio.h>
#include "Fila.h"

main() {
  Fila *f = cria_fila_vazia();
  printf("Enfileirando 5 itens:\n");
  enfileira(f, 1);
  enfileira(f, 10);
  enfileira(f, 3);
  enfileira(f, 22);
  enfileira(f, 4);
  imprime(f);
  printf("\nTetando enfileirar mais um item:\n");
  enfileira(f, 50);
  printf("\nDesenfileira um item:\n");
  desenfileira(f);
  imprime(f);
  printf("\nDesenfileira um item:\n");
  desenfileira(f);
  imprime(f);
  printf("\nDesenfileira um item:\n");
  desenfileira(f);
  imprime(f);
  printf("\nEnfileira um item:\n");
  enfileira(f, 13);
  imprime(f);
  printf("\nEnfileira um item:\n");
  enfileira(f, 98);
  imprime(f);
  libera(f);
}

