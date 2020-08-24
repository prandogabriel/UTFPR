#include <stdio.h>
#include <stdlib.h>

int main()
{
  int i = 0;
  int j = 0;
  int troco;
  int qtd_moedas;
  int auxiliar;

  scanf("%d",&troco);
  scanf("%d",&qtd_moedas);

  int moedas[qtd_moedas];

  while (i<qtd_moedas)
  {
    scanf("%d", &moedas[i]);
    i++;
  }

  while (troco != 0)
  {
    auxiliar = troco + 1;
    for (i = 0; i < qtd_moedas; i++)
    {
      if(troco/moedas[i]<= auxiliar && troco/moedas[i]!=0)
      {
        auxiliar = troco/moedas[i];
        j=i;
      }
    }
    for (i = 0; i < auxiliar; i++)
    {
      printf("%d\n",moedas[j]);
    }
    troco-=moedas[j]*auxiliar;
  }
  return 0;
  }
