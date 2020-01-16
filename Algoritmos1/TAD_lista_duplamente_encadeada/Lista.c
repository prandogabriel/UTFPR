#include <stdlib.h>
#include <stdio.h>
#include "Lista.h"

struct item {
  int chave;
  // demais campos
};
struct celula {
  Item item;
  Celula *ant;
  Celula *prox;
};
struct lista {
  Celula *primeira;
};

Lista * cria_lista_vazia() {
  Lista *l = malloc(sizeof(Lista));
  l->primeira = NULL;
  return l;
}

// retorna 1 se está vazia, ou 0 se não está
int verifica_lista_vazia(Lista *l) {
  return l->primeira == NULL;
}

void inserir_inicio(Lista *l, int chave) {
  Item novo;
  novo.chave = chave;

  Celula *nova = malloc(sizeof(Celula));
  Celula *primeira = l->primeira;

  nova->item = novo;
  nova->ant = NULL;
  nova->prox = primeira;

  // se a lista não está vazia, a a anterior da que era a primeira será a nova
  if(!verifica_lista_vazia(l))
    primeira->ant = nova;

  l->primeira = nova;
}

void imprime_esq_dir(Lista *l) {
  Celula *aux = l->primeira;
  while(aux != NULL) {
    printf("Chave: %d\n", aux->item.chave);
    aux = aux->prox;
  }
}

void imprime_dir_esq(Lista *l) {
  if(verifica_lista_vazia(l))
    return;
  Celula *ultima = l->primeira;
  while(ultima->prox != NULL) { // achar a última
    ultima = ultima->prox;
  }
  while(ultima != NULL) {
    printf("Chave: %d\n", ultima->item.chave);
    ultima = ultima->ant;
  }
}

void insere_final(Lista *l, int chave) {
  if(verifica_lista_vazia(l))
    inserir_inicio(l, chave);
  else {
    Item novo;
    novo.chave = chave;

    Celula *nova = malloc(sizeof(Celula));
    nova->item = novo;
    nova->prox = NULL;

    Celula *ultima = l->primeira;
    while(ultima->prox != NULL) { // achar a última
      ultima = ultima->prox;
    }
    nova->ant = ultima;

    ultima->prox = nova;
  }
}

Celula * busca_chave(Lista *l, int chave) {
  Celula *aux = l->primeira;
  while(aux != NULL && aux->item.chave != chave) {
    aux = aux->prox;
  }
  return aux;
}

void insere_meio(Lista *l, int chave_b, int chave_i) {
  Celula *anterior = busca_chave(l, chave_b);
  if(anterior == NULL) {
    printf("Chave não encontrada ou lista vazia.\n");
    return;
  }
  Item novo;
  novo.chave = chave_i;

  Celula *nova = malloc(sizeof(Celula));
  nova->item = novo;

  Celula *proxima = anterior->prox;

  nova->ant = anterior;
  nova->prox = proxima;

  anterior->prox = nova;

  // se a inserção não for no fim, a célula seguinte tem nova como anterior
  if(proxima != NULL)
    proxima->ant = nova;
}

void remove_inicio(Lista *l) {
  if(verifica_lista_vazia(l)) {
    printf("A lista está vazia.\n");
    return;
  }
  Celula *primeira = l->primeira;
  if(primeira->prox == NULL) // se era a única
    l->primeira = NULL;
  else {
    Celula *proxima = primeira->prox;
    proxima->ant = NULL;
    l->primeira = proxima;
  }
  free(primeira);
}

void remove_final(Lista *l) {
  if(verifica_lista_vazia(l)) {
    printf("A lista está vazia.\n");
    return;
  }
  Celula *ultima = l->primeira;
  while(ultima->prox != NULL)
    ultima = ultima->prox;
  if(ultima->ant == NULL) // se era a única
    l->primeira = NULL;
  else {
    Celula *anterior = ultima->ant;
    anterior->prox = NULL;
  }
  free(ultima);
}

void remove_meio(Lista *l, int chave) {
  Celula *remover = busca_chave(l, chave);
  if(remover == NULL) {
    printf("Chave não encontrada.\n");
    return;
  }
  if(remover->ant == NULL)
    remove_inicio(l);
  else if(remover->prox == NULL)
    remove_final(l);
  else {
    Celula *anterior = remover->ant;
    Celula *proxima = remover->prox;
    anterior->prox = proxima;
    proxima->ant = anterior;
    free(remover);
  }
}

void libera_lista(Lista *l) {
  Celula *aux = l->primeira;
  Celula *liberar;
  while(aux != NULL) {
    liberar = aux;
    aux = aux->prox;
    free(liberar);
  }
  free(l);
}
