#include <stdlib.h>
#include <stdio.h>
#include "hash.h"

struct item {
  int chave;
  // nome
  // endereço...
};

// o item não será guardado diretamente, será guardada uma célula
struct celula {
  Item item;
  Celula *prox;
};

// agora cada ponteiro aponta para a 1ª célula da lista em vez de apontar para um item
struct hash {
  int quantidade;
  Celula **celulas;
};

// quase igual no endereçamento aberto
Hash * cria_tabela() {
  int i;
  Hash *h = malloc(sizeof(Hash));
  h->quantidade = 0;
  h->celulas = malloc(TABLESIZE * sizeof(Celula*)); // em vez de sizeof(Item*), agora é sizeof(Celula*)
  for(i = 0; i < TABLESIZE; i++)
    h->celulas[i] = NULL;
  return h;
}

void libera_tabela(Hash *h) {
  int i;
  Celula *aux, *remover;
  for(i = 0; i < TABLESIZE; i++) {
      aux = h->celulas[i];
      // em vez de liberar um item, agora é preciso percorrer a lista liberando cada célula
      while(aux != NULL) {
        remover = aux;
        aux = aux->prox;
        free(remover);
      }
  }
  free(h->celulas);
  free(h);
}

// igual no endereçamento aberto
int hashing_divisao(int chave) {
  return chave % TABLESIZE;
}

// igual no endereçamento aberto
int verifica_tabela_vazia(Hash *h) {
  return h->quantidade == 0;
}

// conceito de tabela cheia não existe mais

// igual no endereçamento aberto
Item * cadastra_item(int chave) {
  Item *novo = malloc(sizeof(Item));
  novo->chave = chave;
  return novo;
}

// igual no endereçamento aberto
void imprime_item(Item *item) {
  if(item != NULL)
    printf("Chave: %d\n",item->chave);
  else
    printf("Item não existe.\n");
}

void imprime_tabela(Hash *h) {
  int i;
  for(i = 0; i < TABLESIZE; i++) {
    printf("Pos: %d:\n", i);
    if(h->celulas[i] == NULL) {
      printf("NULL");
    }
    else {
      // agora é preciso percorrer a lista, imprimindo o item de cada célula
      Celula *aux = h->celulas[i];
      while(aux != NULL) {
        printf("%d\t", aux->item.chave);
        aux = aux -> prox;
      }
    }
    printf("\n\n");
  }
}

void insere_tratamento_lista_encadeada(Hash *h, Item *item) {
  int chave = item->chave;
  int posicao = hashing_divisao(chave);
  Celula *aux = malloc(sizeof(Celula));
  aux->item = *item;
  aux->prox = h->celulas[posicao]; // aux->prox recebe tudo que já tinha na lista (inserção no começo)
  h->celulas[posicao] = aux; // o ponteiro que indica a primeira célula da lista recebe aux
  h->quantidade++; // continuamos com o campo quantidade para saber com facilidade se a tabela está vazia
}

Item * busca_tratamento_lista_encadeada(Hash *h, int chave) {
  int posicao = hashing_divisao(chave);
  if(h->celulas[posicao] == NULL) // a lista da posição está vazia?
    return NULL;
  Celula *aux = h->celulas[posicao]; // percorrer até achar a chave buscada e retornar o item correspondente
  while(aux != NULL) {
    if(aux->item.chave == chave)
      return &aux->item;
    aux = aux->prox;
  }
  return NULL; // retornar NULL se não encontrado
}

void remove_tratamento_lista_encadeada(Hash *h, int chave) {
  int posicao = hashing_divisao(chave);
  if(h->celulas[posicao] == NULL) { // a lista da posição está vazia?
    printf("Chave %d não encontrada.\n", chave);
    return;
  }
  Celula *aux = h->celulas[posicao]; // início da lista
  if(aux->item.chave == chave) // remover do início
    h->celulas[posicao] = aux->prox;
  else { // encontra a célula a ser removida
    Celula *anterior;
    while(aux != NULL && aux->item.chave != chave) {
      anterior = aux;
      aux = aux->prox;
    }
    if(aux == NULL)
      printf("Chave %d não encontrada.\n", chave);
    else { // verificar se a célula está no final ou no meio
      if(aux->prox == NULL) // remover do final
        anterior->prox = NULL;
      else // remover do meio
        anterior->prox = aux->prox;
    }
  }
  free(aux);
}
