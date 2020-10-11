#include <stdio.h>
#include <stdlib.h>

int main() {
  int n, i, j;
  scanf(" %d",&n);
  int graph[n][n];


  i=0;
  while(i<n){
    j=0;
    while(j<n){
      scanf(" %d",&graph[i][j]);
      if(graph[i][j]==1 && graph[j][i]==0){
        graph[j][i]=1;
      }
      j++;
    }
    i++;
  }

  i=0;
  while(i<n){
    j=0;
    while(j<n){
      if(graph[i][j]==1 && graph[j][i]==0){
        graph[j][i]=1;
      }
      j++;
    }
    i++;
  }

  i=0;

  while(i<n){
    j=0;
    while(j<n){
      printf("%d ",graph[i][j]);
      j++;
    }
    printf("\n");
    i++;
  }
  return 0;
}
