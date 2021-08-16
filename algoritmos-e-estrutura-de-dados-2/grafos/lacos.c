#include <stdio.h>
#include <stdlib.h>

int main()
{
    int n, aux, auxNull;
    scanf(" %d",&n);

    int i=0, j=0;

    printf("Laco no vertice\n");
    while(i<n){
      j=0;
      while(j<n){
        scanf(" %d",&aux);
        if(i==0 && j==0)
          auxNull=aux;
        if(aux==1 ==1 && i==j)
          printf("%d\n", i);
        j++;
      }
      i++;
    }

    if(auxNull==0)
      printf("-");

    return 0;
}
