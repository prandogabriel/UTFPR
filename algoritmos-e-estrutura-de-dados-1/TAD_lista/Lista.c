#include <stdio.h>
#include <stdlib.h>
#include "Lista.h"

struct item {
    int chave;
    // demais campos desejados
};

struct lista {
    Item item[MAXTAM];
    int ultimo;
};

// criar uma lista vazia
Lista * cria_lista_vazia() {
    Lista *l = malloc(sizeof(Lista));
    l->ultimo = -1;
    return l;
}

// retorna 1 se a lista está cheia ou 0 se não está cheia
int verifica_lista_cheia(Lista *l) {
    return l->ultimo == MAXTAM - 1;
}

// adiciona um elemento no fim da lista
void adiciona_item_fim_lista(Lista *l, int chave) {
    if(verifica_lista_cheia(l)){
        printf("Erro: a lista está cheia.\n");
        return;
    }
    else {
        Item novo_item;
        novo_item.chave = chave;
        l->ultimo++;
        l->item[l->ultimo] = novo_item;
    }
}

// imprime a lista
void imprime_lista(Lista *l) {
    int tam = l->ultimo + 1;
    int i;
    for(i = 0; i < tam; i++)
        printf("Chave: %d\n", l->item[i].chave);
}

// retorna indice que contém o item com a chave buscada
int busca_item_por_chave(Lista *l, int chave) {
    int tam, i, posicao;
    tam = l->ultimo + 1;
    posicao = -1;
    for(i = 0; i < tam; i++)
        if(l->item[i].chave == chave)
            posicao = i;
    return posicao;
}

// retorna 1 se a lista está vazia ou 0 se não está vazia
int verifica_lista_vazia(Lista *l) {
    return l->ultimo == -1;
}

// remove um item qualquer da lista
void remove_item(Lista *l, int chave) {
    int posicao, vazia, i, tam;
    vazia = verifica_lista_vazia(l);
    posicao = busca_item_por_chave(l, chave);
    if (vazia || posicao == -1) {
        printf("Erro: a lista está vazia ou o item não existe.\n");
        return;
    }
    else {
        tam = l->ultimo + 1;
        for(i = posicao; i < tam - 1; i++)
            l->item[i] = l->item[i + 1];
        l->ultimo--;
    }
}

void libera_lista(Lista *l) {
    free(l);
}
