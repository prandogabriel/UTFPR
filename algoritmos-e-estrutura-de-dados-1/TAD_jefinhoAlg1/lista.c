#include <stdio.h>
#include <stdlib.h>
#include "lista.h"

// Estrutura para uma unidade da lista estática
struct ItemL
{
    int chave;

    /*
    Aqui pode conter outros campos, mas para fins didáticos e agilidade na implementação,
    nos exemplos foram considerados apenas um campo
    */
};

// Estrutura para uma lista estática
struct Lista
{
    ItemL item[MAX_SIZE];
    int tam; // tamanho da lista
};

// Função para criar uma lista estática
Lista *criar()
{
    Lista *l = (Lista *)malloc(sizeof(Lista));

    // Ao iniciar a lista, a mesma estará "vazia"
    l->tam = 0;

    return l;
}

// Retorna 1 se a lista está vazia ou 0, caso contrário
int lista_vazia(Lista *l)
{
    return (l == NULL) || (l->tam == 0);
}

// Retorna 1 se a lista está cheia ou 0, caso contrário
int lista_cheia(Lista *l)
{
    return (l != NULL) && (l->tam == MAX_SIZE);
}

// Procura a posição de um item com a chave e retorna a sua respectiva posição
// Caso o item não seja encontrado, é retornado -1
int buscar(Lista *l, int chave)
{
    int i;

    if (!lista_vazia(l))
    {
        // Procurar na parte "não vazia" da lista
        for (i = 0; i < l->tam; i++)
            if (l->item[i].chave == chave)
                return i;
    }

    // Se chegou aqui é porque a lista está vazia ou o item não foi encontrado
    return -1;
}

// Um item é inserido no final da lista caso ela não estiver cheia.
// A função retorna 1 se a operação for bem sucedida ou 0, caso contrário
void inserir(Lista *l, int chave)
{
    ItemL item;

    if (!lista_cheia(l))
    {
        // atribuição no campo chave do item
        item.chave = chave;

        // O item é adicionado no final da lista
        l->item[l->tam] = item;

        // Se foi adicionado um elemento, então a lista cresceu
        l->tam++; // l->tam = l->tam + 1; ou l->tam += 1;
    }

}

// O item procurado e removido da lista caso ela. Para isso, a lista não deve estar vazia e o item
// deve existir.
// A função retorna 1 se a operação for bem sucedida ou 0, caso contrário
void remover(Lista *l, int chave)
{
    int i = buscar(l, chave);

    // A chave foi encontrada
    if (i > -1)
    {
        for (i; i < l->tam - 1; i++)
            // Deslocamento de itens da lista para ocupar o espaço do item removido
            // Se o item a ser removido for o último, o comando abaixo não é executado
            l->item[i] = l->item[i + 1];

        // Se foi removido um elemento, então a lista diminuiu
        l->tam--; // l->tam = l->tam - 1; ou l->tam -= 1;

        // Remoção bem-sucedida
    }
}

// Imprime o conteúdo da lista
void imprimir_lista(Lista *l)
{
    int i;

    if (!lista_vazia(l))
    {
        for (i = 0; i < l->tam; i++)
            printf("%d\n", l->item[i].chave);
    }
}

// Libera a lista
// Retorna 1 se a operação for bem-sucedida ou 0, caso contrário
void liberar_lista(Lista *l)
{
    if (l != NULL)
    {
        free(l);
    }
    else
        printf("sem sucesso para liberar lista\n");
}

Lista *concatenar(Lista *l1, Lista *l2)
{
    Lista *junta = malloc(sizeof(Lista));

    int i = 0;

    if ((l1->tam + l2->tam) > 100)
        return l1;

    while(l1->tam > i)
    {
        inserir(junta, l1->item[i].chave);
        i++;
    }

    i = 0;
    for (i; i < l2->tam ; i++)
    {
        inserir(junta, l2->item[i].chave);
    }

    return junta;
}