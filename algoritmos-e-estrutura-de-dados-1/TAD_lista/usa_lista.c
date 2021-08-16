#include <stdio.h>
#include "Lista.h"

main() {
    Lista *l;
    int vazia, cheia, chave;

    printf("Foi criada a lista vazia.\n");
    l = cria_lista_vazia();

    vazia = verifica_lista_vazia(l);
    printf("Lista vazia? %d\n", vazia);

    cheia = verifica_lista_cheia(l);
    printf("Lista cheia? %d\n", cheia);

    printf("\nAdicionando o 1o item.\n");
    chave = 1;
    adiciona_item_fim_lista(l, chave);
    vazia = verifica_lista_vazia(l);
    printf("Lista vazia? %d\n", vazia);

    printf("\nAdicionando o 2o item.\n");
    chave = 2;
    adiciona_item_fim_lista(l, chave);

    printf("Adicionando o 3o item.\n");
    chave = 3;
    adiciona_item_fim_lista(l, chave);

    printf("Adicionando o 4o item.\n");
    chave = 4;
    adiciona_item_fim_lista(l, chave);

    printf("Adicionando o 5o item.\n");
    chave = 5;
    adiciona_item_fim_lista(l, chave);

    cheia = verifica_lista_cheia(l);
    printf("Lista cheia? %d\n", cheia);

    printf("\nTentando adicionar 6o item.\n");
    chave = 6;
    adiciona_item_fim_lista(l, chave);

    printf("\nItens da lista:\n");
    imprime_lista(l);

    printf("\nBuscando posição do item a partir da chave.\n");
    printf("Posição do item com chave = 1: %d\n", busca_item_por_chave(l, 1));
    printf("Posição do item com chave = 2: %d\n", busca_item_por_chave(l, 2));
    printf("Posição do item com chave = 3: %d\n", busca_item_por_chave(l, 3));
    printf("Posição do item com chave = 4: %d\n", busca_item_por_chave(l, 4));
    printf("Posição do item com chave = 5: %d\n", busca_item_por_chave(l, 5));

    printf("\nTentando remover item com chave = 6.\n");
    remove_item(l, 6);
    imprime_lista(l);

    printf("\nTentando remover item com chave = 3.\n");
    remove_item(l, 3);
    imprime_lista(l);

    libera_lista(l);
}
