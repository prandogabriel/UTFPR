#include <stdio.h>
#include <stdlib.h>

int qtd_moedas(int v[], int troco){
    int qtd = 0;
    int i;

    for (i = 0; (troco > 0); i++){
        qtd += troco / v[i];
        troco = troco % v[i];
    }


    if (troco == 0)
        return qtd;
    else
        return -1;
}

int main(){
    int v[] = {100, 50, 25, 10, 5, 1};
    int troco;
    scanf("%d",&troco);
    printf("%d",qtd_moedas(v, troco));

    return 0;
}