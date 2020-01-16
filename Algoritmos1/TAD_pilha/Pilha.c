#include <stdio.h>
#include <stdlib.h>
#include "Pilha.h"

struct item {
  int chave;
  // demais campos
};

struct pilha {
  Item item[MAXTAM];
  int topo;
};

// criar uma pilha vazia
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
void empilha(Pilha *p, int chave) {
    if(verifica_pilha_cheia(p)){
        printf("Erro: a pilha está cheia.\n");
        return;
    }
    else {
        Item novo_item;
        novo_item.chave = chave;
        p->topo++;
        p->item[p->topo] = novo_item;
    }
}

// imprime a pilha
void imprime_pilha(Pilha *p) {
    int i;
    for(i = p->topo; i >= 0; i--)
        printf("Chave: %d\n", p->item[i].chave);
}

// retorna 1 se a pilha está vazia ou 0 se não está vazia
int verifica_pilha_vazia(Pilha *p) {
    return p->topo == -1;
}

// desempilha
void desempilha(Pilha *p) {
    if (verifica_pilha_vazia(p)) {
        printf("Erro: a pilha está vazia.\n");
        return;
    }
    else {
        p->topo--;
    }
}

void libera_pilha(Pilha *p) {
    free(p);
}
