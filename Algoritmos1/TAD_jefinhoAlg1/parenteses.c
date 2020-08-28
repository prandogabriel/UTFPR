#include <math.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#define MAXTAM 100

typedef struct item {
  char nome[MAXTAM];
  // demais campos
} Item;

typedef struct pilha {
  Item item[MAXTAM];
  int topo;
}Pilha;

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

void contador (char v[], int n)
{
  int i, j, cont=0, contE=0;

        for(i=0;i<n;i++)
        {
            if(v[i]==')' && cont == 0)
              i=n;
            else if (v[i]=='(')
            {
                cont++;
                for(j=i+1;j<n;j++)
                {
                    if (v[j]==')')
                    {
                        cont--;
                        v[j] = 'a';
                    }
                }
                v[i] = 'a';

            }
            else if (v[i]==')')
            {
                contE++;
            }


        }




    if(cont != 0 || contE != 0)
    {
        printf("incorrect\n");
    }
    else
    {
        printf("correct\n");
    }
}


void criar( int n)
{
    Lista l;
    int i;
    l = (Lista) malloc (sizeof(Lista));
    l->tam = n;

    for(i=0; i<n;i++)
    {
        scanf("%s", &l->it[i].nom);
    }
    int tam;
    for (i=0;i<n;i++)
    {

        tam =  strlen(l->it[i].nom);
        contador( l->it[i].nom, tam);
    }
}


int main ()
{
  Pilha *p = cria_pilha_vazia();

    int n;
    scanf("%d", &n);
    criar(n);

}
