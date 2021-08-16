#include <stdio.h>
#include <stdlib.h>

int nun_atividades(int ini[],int fim[],int N){
    int i=0,aux=-1,tot=0;

    if(N > 0){
        aux=fim[i];
        tot++;
        for(i=1;i<N;i++){
            if(ini[i]>aux){
                aux=fim[i];
                tot++;
            }
        }
        return tot;
    }
    if(N==0)
        return 0;
    else
    {
            return -1;
    }
    
}

main(){
    int ini[] = {1, 3, 0, 5, 3, 5, 6, 8, 8, 2, 12};
    int fim[] = {4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};

    printf(" %d", nun_atividades(ini, fim, 11));

    return 0;
}