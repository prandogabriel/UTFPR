#include <stdio.h>
#include <stdlib.h>

int main(void){
  int n, m, i, j;

  scanf("%d",&n);

  int *vetor = malloc(n * sizeof(int));

  for(i=0; i<n;i++){
    scanf("%d",&vetor[i]);
  }

  scanf("%d",&m);

  vetor = (int *)realloc(vetor, (m+n) * sizeof(int));

  for(i=n; i<m+n;i++){
    scanf("%d",&vetor[i]);
  }

  int *vetor_invertido = malloc((n+m) * sizeof(int));

  j= m+n-1;
  for (i = 0; i <(m+n); i++){
    vetor_invertido[j] = vetor[i];
    j--;
  }

  for (i = 0; i <n; i++){
      printf("%d\t", vetor[i]);
  }

  printf("\nvetor normal \n ");

  for (i = 0; i <(m+n); i++){
      printf("%d ", vetor[i]);
  }
  printf("\nvetor invertido \n ");

  for (i = 0; i <(m+n); i++){
      printf("%d ", vetor_invertido[i]);
  }

  printf("\n");


  free(vetor);

  return 0;
}
