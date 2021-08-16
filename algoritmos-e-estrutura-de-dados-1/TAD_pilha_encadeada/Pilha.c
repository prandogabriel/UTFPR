#include <stdlib.h>
#include <stdio.h>
#include "Pilha.h"

struct item {
  int chave;
  // demais campos
};

struct celula {
  Item item;
  Celula *prox;
};

struct pilha {
  Celula *topo;
};

Pilha * cria_pilha_vazia(){
  Pilha *p = malloc(sizeof(Pilha));
  p->topo = NULL;
  return p;
}

// retorna 1 se está vazia ou 0 se não está
int verifica_pilha_vazia(Pilha *p) {
  return p->topo == NULL;
}

void empilha(Pilha *p, int chave) {
  Item novo;
  novo.chave = chave;
  Celula *nova = malloc(sizeof(Celula));
  nova->item = novo;
  nova->prox = p->topo;
  p->topo = nova;
}

void imprime(Pilha *p) {
  Celula *aux = p->topo;
  while(aux != NULL) {
    printf("Chave: %d\n", aux->item.chave);
    aux = aux->prox;
  }
}

void desempilha(Pilha *p) {
  if(verifica_pilha_vazia(p)) {
    printf("A pilha está vazia!\n");
    return;
  }
  Celula *remover = p->topo;
  p->topo = remover->prox;
  free(remover);
}

void libera_pilha(Pilha *p) {
  Celula *aux = p->topo;
  Celula *liberar;
  while(aux != NULL) {
    liberar = aux;
    aux = aux->prox;
    free(liberar);
  }
  free(p);
}
