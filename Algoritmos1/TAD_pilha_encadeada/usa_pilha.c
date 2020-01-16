#include <stdio.h>
#include "Pilha.h"

int main() {
  Pilha *p = cria_pilha_vazia();

  printf("Vazia: %d\n", verifica_pilha_vazia(p));

  printf("Empilhando...\n");
  empilha(p, 10);
  empilha(p, 5);
  empilha(p, 15);
  empilha(p, 1);
  empilha(p, 12);
  imprime(p);

  printf("Desempilhando...\n");
  desempilha(p);
  desempilha(p);
  desempilha(p);
  desempilha(p);
  desempilha(p);
  desempilha(p);
  imprime(p);

  libera_pilha(p);
}
