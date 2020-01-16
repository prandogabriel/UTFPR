#include <stdio.h>
#include <stdlib.h>
#include "Lista.h"

struct item {
    int codigo;
    float peso;
};
struct lista {
    Item item[MAXTAM];
    int ultimo;
};

Lista * cria_lista_vazia() {
    Lista *l = malloc(sizeof(Lista));
    l->ultimo = -1;
    return l;
}

// retorna 1 se a lista está vazia ou 0 se não está vazia
int verifica_lista_vazia(Lista *l) {
    return l->ultimo == -1;
}

// retorna 1 se a lista está cheia ou 0 se não está cheia
int verifica_lista_cheia(Lista *l) {
    return l->ultimo == MAXTAM - 1;
}

void adiciona_item_fim_lista(Lista *l, int codigo, float peso) {
    if(verifica_lista_cheia(l)){
        printf("Lista cheia!\n");
        return;
    }
    Item novo_item;
    novo_item.codigo = codigo;
    novo_item.peso = peso;
    l->ultimo++;
    l->item[l->ultimo] = novo_item;
}

void imprime_lista(Lista *l) {
    int tam = l->ultimo + 1;
    int i;
    for(i = 0; i < tam; i++)
        printf("%d %.3f ", l->item[i].codigo, l->item[i].peso);
}

void libera_lista(Lista *l) {
    free(l);
}
