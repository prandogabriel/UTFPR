#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "Pilha.h"

struct item {
  int numero;
};
struct pilha {
  Item item[MAXTAM];
  int topo;
};

// cria pilha vazia
Pilha * cria_pilha_vazia() {
  Pilha *p = malloc(sizeof(Pilha));
  p->topo = -1;
  return p;
}

// retorna 1 se a lista está cheia ou 0 se não está cheia
int verifica_pilha_cheia(Pilha *p) {
    return p->topo == MAXTAM - 1;
}

// empilha
void empilha(Pilha *p, int numero) {
    Item novo_item;

    if(verifica_pilha_cheia(p)){
        printf("Erro: a pilha está cheia.\n");
        return;
    }
    else {
        novo_item.numero = numero;
        p->topo++;
        p->item[p->topo] = novo_item;
    }
}

// imprime a pilha
void imprime_pilha(Pilha *p) {
    int i;
    for(i = 0; i <= p->topo; i++)
        printf("%d ", p->item[i].numero);
    printf("\n");
}

// retorna 1 se a pilha está vazia ou 0 se não está vazia
int verifica_pilha_vazia(Pilha *p) {
    return p->topo == -1;
}

// desempilha e retorna o item desempilhado
Item * desempilha(Pilha *p) {
    Item *removido;
    if (verifica_pilha_vazia(p)) {
        printf("Erro: a pilha está vazia.\n");
        return 0;
    }
    else {
        removido = &(p->item[p->topo]);
        p->topo--;
        return removido;
    }
}

void libera_pilha(Pilha *p) {
  free(p);
}

void imprime_item(Item *i) {
  printf("%d ", i->numero);
}

int soma(Item *penultimo, Item *ultimo) {
  return penultimo->numero + ultimo->numero;
}

int subtrai(Item *penultimo, Item *ultimo) {
  return penultimo->numero - ultimo->numero;
}

int multiplica(Item *penultimo, Item *ultimo) {
  return penultimo->numero * ultimo->numero;
}

int divide(Item *penultimo, Item *ultimo) {
  return penultimo->numero / ultimo->numero;
}
