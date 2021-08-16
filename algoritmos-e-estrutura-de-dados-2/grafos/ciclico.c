#include<stdio.h>
#include<stdlib.h>

int busca(int v, int n, int matriz_adj[n][n], int cor[n], int tempo, int descoberta[n]){
  int j;
  tempo = tempo + 1;
  descoberta[v] = tempo;
  for(j = 0; j < n; j++){
    if(matriz_adj[v][j]==1){
      if(cor[j] == 0) continue;
      if(cor[j] == 1 || (descoberta[v]  - descoberta[j]) > 2) return 1;
      descoberta[j] = tempo;
      cor[j] = 1;
      busca(j, n, matriz_adj, cor, tempo, descoberta);
    }
  }

  cor[v] = 0;
  return 0;

}

void propriedade(int n, int matriz_adj[n][n]){
  // 0=preto, 1=cinza, 2=branco
  int i, cor[n],  descoberta[n], tempo=0;
  for(i=0;i<n;i++){
    cor[i] = 2;
    descoberta[i] = -1;
  }

  for(i=0;i<n;i++){

    if(cor[i]==0) {
      printf("aciclico\n");
      return;
    }
    cor[i] = 1;
    if (busca(i, n, matriz_adj, cor, tempo, descoberta) == 1){
      printf("ciclico\n");
      return;
    }
  }
  printf("aciclico\n");
  return;
}

int main(void) {
  int i=0, j, n;
  scanf(" %d",&n);

  int matriz_adj[n][n];

  while(i<n){
    j=0;
    while(j<n){
      scanf("%d",&matriz_adj[i][j]);
      j++;
    }
    i++;
  }
  propriedade(n ,matriz_adj);

  return 0;
}
