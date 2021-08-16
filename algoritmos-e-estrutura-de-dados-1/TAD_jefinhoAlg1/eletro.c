#include <stdio.h>
#include <string.h>
#include <math.h>
#include <stdlib.h>

typedef struct
{
    char nome[15];
    float potencia;
    float tempo;

} Consumo;

int main(){

    int qtd;
    printf("Informe a quantidade de eletrodomesticos: ");
    scanf("%d", &qtd);
    Consumo eletrodomesticos[qtd];
    int i=0;
    float consumototal= 0;
    float porcentagem;
    int dias;



    printf("Insira o nome do eletrodomestico, potencia(em kW), tempo ativo por dia(em hrs):\n\n");
    for(i=0;i<qtd;i++)
    {
      scanf("%s %f %f", &eletrodomesticos[i].nome, &eletrodomesticos[i].potencia, &eletrodomesticos[i].tempo);
    }

    printf("Informe o numero de dias: ");
    scanf("%d", &dias);

    i=0;
    for(i=0;i<qtd;i++)
    {
        consumototal+=eletrodomesticos[i].potencia*eletrodomesticos[i].tempo*dias;
    }

    printf("Consumo total= %.2f\n", consumototal);

    // for(i=0;i<qtd;i++){
    //  porcentagem=consumo/consumototal;

    // }
    //printf("%s %f",  , (porcentagem*100));
    //printf("Porcentagem (2): %.2f porcento\n", (1-porcentagem)*100);

    return 0;
}
