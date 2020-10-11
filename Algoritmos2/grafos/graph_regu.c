#include <stdio.h>
#include <stdlib.h>

int main() {
  int n, i, j, count = 0, aux = 0;
  scanf(" %d",&n);
  int graph[n][n], vetAux[n];

  i=0;
  while(i<n){
    j=0;
    while(j<n){
      scanf(" %d",&graph[i][j]);
      j++;
    }
    i++;
  }

  i=0;
  while(i<n){
    j=0;
    aux=0;
    while(j<n){
      if(graph[i][j]==1){
        aux++;
      }
      if (j==n-1)
      {
        vetAux[i]=aux;
      }
      j++;
    }
    i++;
  }

  i=0;

  while(i<n-1){
    if(vetAux[i] != vetAux[i+1]){
      printf("nao\n");
      return 0;
    }
    i++;
  }
  printf("sim\n");

  return 0;
}
