#include<stdio.h>
#include<stdlib.h>

#define N 100

int pilha[N];
int ultimo, primeiro;

int desempilha(int n, int chave[n]){

  int menor = 10000, i, j;

  i = primeiro;
  while(i < ultimo && chave[i] > menor){
    i++;
  }

  int vertice;
  vertice = pilha[i];

  if(i != primeiro && i != ultimo){
    for ( j = i; j < ultimo; j++) {
      pilha[j] = pilha[j + 1];
    }
    ultimo--;
  }
  if(i == primeiro) primeiro++;
  if(i == ultimo) ultimo--;

  return vertice;
}

void prim(int n, int matriz[n][n]){

  int pai[n], chave[n], i, u;

  for (i = 0; i < n; i++){
    pai[i] = 0;
    chave[i] = 10000;
  }
  chave[0] = 0;
  primeiro = ultimo = 0;
  for ( i = 0; i < n; i++) {
    pilha[ultimo++] = i;
  }
  int j;
  while(ultimo != primeiro){
    u = desempilha(n, chave);
    for ( i = 0; i < n; i++) {
      if (matriz[u][i] != 0) {
        if(matriz[u][i] < chave[i]){
          for ( j = primeiro; j < ultimo; j++) {
            if(pilha[j] == i) {
              pai[i] = u;
              chave[i] = matriz[u][i];
            }
          }

        }
      }
    }
  }

  printf("0: -\n");
  for (i = 1; i < n; i++) {
    printf("%d: %d\n",i,pai[i]);
  }
}

int main(void) {

  int i, j, n;
  scanf(" %d",&n);
  int matriz_adja[n][n];
  for(i=0;i<n;i++){
    for(j=0;j<n;j++){
      scanf(" %d",&matriz_adja[i][j]);
    }
  }

  prim(n, matriz_adja);

  return 0;
}
