#include<stdlib.h>
#include<stdio.h>
#include"fila.h"

int main (){
    int n, chave,i;
    Fila *f = cria_fila_vazia();

    scanf("%d",&n);     //saber quantos itens tera na fila
    for(i=0;i<n;i++){
        scanf("%d",&chave);
        enfileira(f,chave);
    }
    remove_compara(f);
    return 0;
}