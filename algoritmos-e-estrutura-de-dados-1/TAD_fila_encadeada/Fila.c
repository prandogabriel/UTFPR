#include <stdio.h>
#include <stdlib.h>
#include "Fila.h"

struct item {
  int chave;
  // demais campos
};

struct celula {
  Item item;
  Celula *prox;
};

struct fila {
  Celula *ini;
  Celula *fim;
};

Fila * cria_fila_vazia() {
  Fila *f = malloc(sizeof(Fila));
  f->ini = NULL;
  f->fim = NULL;
  return f;
}

// retorna 1 se está vazia ou 0 se não está
int verifica_fila_vazia(Fila *f) {
  return f->ini == NULL;
}

void enfileira(Fila *f, int chave) {
  Item novo;
  novo.chave = chave;
  Celula *nova = malloc(sizeof(Celula));
  nova->item = novo;
  nova->prox = NULL;
  if(verifica_fila_vazia(f))
    f->ini = nova;
  else {
    Celula *ultima = f->fim;
    ultima->prox = nova;
  }
  f->fim = nova;
}

void imprime(Fila *f) {
  Celula *aux;
  for(aux=f->ini; aux != NULL; aux=aux->prox)
    printf("Chave: %d\n", aux->item.chave);
}

void desenfileira(Fila *f) {
  if(verifica_fila_vazia(f)) {
    printf("A fila está vazia!\n");
    return;
  }
  Celula *remover = f->ini;
  f->ini = remover->prox;
  free(remover);
  if(verifica_fila_vazia(f))
    f->fim = NULL;
}

void libera_fila(Fila *f) {
  Celula *aux = f->ini;
  Celula *liberar;
  while(aux != NULL) {
    liberar = aux;
    aux = aux->prox;
    free(liberar);
  }
  free(f);
}
