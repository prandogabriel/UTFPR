#include<stdio.h>
#include"lista.h"

 int main(){
  Lista *l = cria_lista_vazia();
  float lim_peso, peso_mala, aux;
  int n_itens, i, codigo, indice;

  scanf("%f",&lim_peso);
  scanf("%f",&peso_mala);
  scanf("%d",&n_itens);

  for(i=0;i<n_itens;i++){
      scanf("%d %f",&codigo,&aux);
      adiciona_item_fim_lista(l,codigo,aux);
  }
  while(peso_total(l , peso_mala) > lim_peso){
      indice = item_mais_pesado(l);
      printa_mais_pesado(l, indice);
      remove_mais_pesado(l);
  }
  libera_lista(l);
    return 0;
}
