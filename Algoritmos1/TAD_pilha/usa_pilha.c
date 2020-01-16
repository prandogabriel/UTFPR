#include <stdio.h>
#include "Pilha.h"

main() {
  Pilha *p = cria_pilha_vazia();
  printf("Empilhando 5 itens:\n");
  empilha(p, 1);
  empilha(p, 2);
  empilha(p, 3);
  empilha(p, 4);
  empilha(p, 5);
  imprime_pilha(p);
  printf("\nTentando empilhar mais um item:\n");
  empilha(p, 6);
  printf("\nDesempilhando:\n");
  desempilha(p);
  imprime_pilha(p);
  printf("\nDesempilhando:\n");
  desempilha(p);
  imprime_pilha(p);
  printf("\nDesempilhando:\n");
  desempilha(p);
  imprime_pilha(p);
  printf("\nDesempilhando:\n");
  desempilha(p);
  imprime_pilha(p);
  printf("\nDesempilhando:\n");
  desempilha(p);
  imprime_pilha(p);
  printf("\nDesempilhando:\n");
  desempilha(p);
  imprime_pilha(p);
  libera_pilha(p);
}
