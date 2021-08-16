#include <stdio.h>
#include <stdlib.h>

#define MAXTAM 100

typedef struct GrafoMA{
   int V; // número de vértices
   int A; // número de arestas
   int **mat;
} GrafoMA;

int** iniciar_MA(int n){
    int i, j;
    int **mat = (int**) malloc(n * sizeof(int*));

    for (i = 0; i < n; i++)
        mat[i] = (int*) malloc(n * sizeof(int));

    for (i = 0; i < n; i++)
        for (j = 0; j < n; j++)
            mat[i][j] = 0;

    return mat;
}

GrafoMA* iniciar_grafoMA(int v){
    GrafoMA* G = (GrafoMA*) malloc(sizeof(GrafoMA));

    G->V = v;
    G->A = 0;
    G->mat = iniciar_MA(G->V);

    return G;
}

int valida_vertice(GrafoMA* G, int v){
    return (v >= 0) && (v < G->V);
}

int aresta_existe(GrafoMA* G, int v1, int v2){
    return (G != NULL) && valida_vertice(G, v1) && valida_vertice(G, v2) && (G->mat[v1][v2] == 1);
}

void inserir_aresta(GrafoMA* G, int v1, int v2){
    if ((G != NULL) && (valida_vertice(G, v1)) && (valida_vertice(G, v2)) && (!aresta_existe(G, v1, v2))){
        G->mat[v1][v2] = G->mat[v2][v1] = 1;
        G->A++;
    }
}

void liberarGMA(GrafoMA* G){
    if (G != NULL){
        free(G->mat);
        free(G);
    }
}

typedef struct Celula {
  int lista[100];
  int ultimo;
  // demais campos desejados
}Celula;

typedef struct Lista {
  Celula item[MAXTAM];
  int ultimo;
}Lista;

// criar uma lista vazia
Lista * cria_lista_vazia() {
  Lista *l = malloc(sizeof(Lista));
  l->ultimo = -1;
  l->item->ultimo= -1;
  return l;
}

// retorna 1 se a lista está cheia ou 0 se não está cheia
int verifica_lista_cheia(Lista *l) {
  return l->ultimo == MAXTAM - 1;
}

// adiciona um elemento no fim da lista
void adiciona_lista(Lista *l, int chave, int index) {
  if(verifica_lista_cheia(l)){
    printf("Erro: a lista está cheia.\n");
    return;
  }
  else {
    if(l->ultimo<index){
      Celula novo_item;
      novo_item.lista[0] = chave;
      l->ultimo++;
      l->item[l->ultimo] = novo_item;
    }
    else{
      l->item->lista[l->item->ultimo] = chave;
      l->item->ultimo++;
    }
  }
}

void imprimir_arestas(GrafoMA* G){
  int i, j;

  for (i = 1; i < G->V; i++)
    for (j = 0; j < i; j++)
      if (G->mat[i][j] == 1)
        printf("(%d, %d)\n", i, j);
}

int main()
{
    Lista *l;

    l = cria_lista_vazia();

    int n;
    char auxChar;
    int i=-1, j=0, auxNumber;

    scanf(" %d",&n);
    GrafoMA *gMA = iniciar_grafoMA(n);

    i=0;
    while (i<n)
    {
      j=0;
      while (j<n)
      {
        gMA->mat[i][j]=0;
        j++;
      }
      i++;
    }

    i=-1;
    while(i<n){
      while (scanf("%c",&auxChar)==1 && auxChar!='\n'){

        if(auxChar!=' '){
          auxNumber = auxChar - '0';
          if(auxNumber!=-1){
            gMA->mat[i][auxNumber]=1;
            inserir_aresta(gMA, i, auxNumber);
          }
        }
      }
      i++;
    }

    i=0;
    while (i<n)
    {
      j=0;
      while (j<n)
      {
        printf("%d ", gMA->mat[i][j]);
        j++;
      }
      printf("\n");

      i++;
    }

    liberarGMA(gMA);
    return 0;
}
