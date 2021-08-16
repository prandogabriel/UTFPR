#include <stdio.h>
#include <stdlib.h>

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

int main()
{
    int n, aux;
    scanf(" %d",&n);

    GrafoMA *gMA = iniciar_grafoMA(n);
    int i=0, j=0;

    while(i<n){
      j=0;
      while(j<n){
        scanf(" %d",&aux);
        if(aux==1)
          inserir_aresta(gMA, i, j);
        j++;
      }
      i++;
    }

    int aux1, aux2;

    do{
      scanf("%d",&aux1);
      scanf("%d",&aux2);
      if(aux1 == -1 || aux2 == -1)
        break;
      if(aresta_existe(gMA, aux1, aux2)==1){
        printf("sim\n");
      }
      else
      printf("nao\n");
      i++;
    }while((aux1 != -1));

    liberarGMA(gMA);
    return 0;
}
