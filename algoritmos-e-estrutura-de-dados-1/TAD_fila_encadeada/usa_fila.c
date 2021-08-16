#include <stdio.h>
#include "Fila.h"

int main() {
  Fila *f = cria_fila_vazia();

  printf("Vazia: %d\n", verifica_fila_vazia(f));

  printf("Enfileirando...\n");
  enfileira(f, 2);
  enfileira(f, 20);
  enfileira(f, 30);
  enfileira(f, 23);
  imprime(f);

  printf("Desenfileira...\n");
  desenfileira(f);
  desenfileira(f);
  imprime(f);

  printf("Desenfileira...\n");
  desenfileira(f);
  desenfileira(f);
  desenfileira(f);
  imprime(f);

  libera_fila(f);
}
