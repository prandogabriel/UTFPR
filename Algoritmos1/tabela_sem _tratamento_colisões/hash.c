#include <stdlib.h>
#include <stdio.h>
#include "hash.h"

struct item {
  int chave;
  // nome
  // endereço...
};

struct hash {
  int quantidade;
  Item **itens;
};

Hash * cria_tabela() {
  int i;
  Hash *h = malloc(sizeof(Hash));
  h->quantidade = 0;
  h->itens = malloc(TABLESIZE * sizeof(Item*));
  for(i = 0; i < TABLESIZE; i++)
    h->itens[i] = NULL;
  return h;
}

void libera_tabela(Hash *h) {
  int i;
  for(i = 0; i < TABLESIZE; i++) {
    if(h->itens[i] != NULL)
      free(h->itens[i]);
  }
  free(h->itens);
  free(h);
}

int hashing_divisao(int chave) {
  return chave % TABLESIZE;
}

// Retorna 1 se a tabela está cheia.
int verifica_tabela_cheia(Hash *h) {
  return h->quantidade == TABLESIZE;
}

// Retorna 1 se a tabela está vazia.
int verifica_tabela_vazia(Hash *h) {
  return h->quantidade == 0;
}

Item * cadastra_item(int chave) {
  Item *novo = malloc(sizeof(Item));
  novo->chave = chave;
  return novo;
}

void imprime_item(Item *item) {
  if(item != NULL)
    printf("Chave: %d\n",item->chave);
  else
    printf("Item não existe.\n");
}

void insere_sem_tratamento(Hash *h, Item *item) {
  if(verifica_tabela_cheia(h)) {
    printf("Erro: não existem posições livres.\n");
    return;
  }
  int chave = item->chave;
  int posicao = hashing_divisao(chave);
  h->itens[posicao] = item;
  h->quantidade++;
}

Item * busca_sem_tratamento(Hash *h, int chave) {
  if(verifica_tabela_vazia(h)) {
    printf("Tabela vazia.\n");
    return NULL;
  }
  int posicao = hashing_divisao(chave);
  if(h->itens[posicao] == NULL)
    return NULL;
  else
    return h->itens[posicao];
}

void imprime_tabela(Hash *h) {
  int i;
  for(i = 0; i < TABLESIZE; i++) {
    if(h->itens[i] == NULL)
      printf("Pos: %d - Chave: NULL\n", i);
    else
      printf("Pos: %d - Chave: %d\n", i, h->itens[i]->chave);
  }
}
