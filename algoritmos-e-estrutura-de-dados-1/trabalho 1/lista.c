#include <stdio.h>
#include <stdlib.h>
#include "lista.h"

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
float peso_total(Lista *l, float peso_mala ){
    float aux, peso_total=0;
    int i;
    aux=0;
    for(i=0;i<=l->ultimo;i++){
        aux = aux + l->item[i].peso;
    }
    peso_total = aux+peso_mala;
    return peso_total;
}
int item_mais_pesado(Lista *l){
    int i, indice;
    float mais_p;
    mais_p = l->item[0].peso;
    indice=0;
    for(i=1; i<l->ultimo;i++){
        if(mais_p < l->item[i].peso){
            mais_p = l->item[i].peso;
            indice = i;
        }
    }
    return indice;
}
void printa_mais_pesado(Lista *l,int indice){
    printf("%d  %f\n",l->item[indice].codigo, l->item[indice].peso);
}
void remove_mais_pesado(Lista *l){
    int i;
    for(i=item_mais_pesado(l); i<=l->ultimo; i++){
        l->item[i].peso = l->item[i+1].peso;
    }
    l->ultimo--;
}
