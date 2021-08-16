#include <stdio.h>
#include <stdlib.h>

int main() {
  int n, i, j, aux1, aux2;
  scanf(" %d",&n);
  int graph[n][n], vetAux[100];

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
  do{
    scanf("%d",&aux1);
    scanf("%d",&aux2);

    if(graph[aux1][aux2]==1){
      vetAux[i]=1;
    }
    else
      vetAux[i]=0;
    i++;
  }while((aux1 != -1));

  j=0;
  while (j<i-1)
  {
    if(vetAux[j]==1)
      printf("sim\n");
    else
      printf("nao\n");
    j++;
  }

  return 0;
}
