#include <stdio.h>
#include "Lista.h"

main() {
  Lista *l;
  Celula *p;

  l = cria_lista_vazia();

  printf("\nVazia: %d\n", verifica_lista_vazia(l));

  printf("\nInserindo itens no início:\n");
  insere_inicio_lista(l, 10);
  insere_inicio_lista(l, 20);
  insere_inicio_lista(l, 30);
  insere_inicio_lista(l, 40);
  imprime(l);

  printf("\nVazia: %d\n", verifica_lista_vazia(l));

  printf("\nInserindo item 50 após o 20:\n");
  insere_meio_lista(l, 50, 20);
  imprime(l);

  printf("\nTetando inserir item 100 após o 18:\n");
  insere_meio_lista(l, 50, 18);
  imprime(l);

  printf("\nInserindo itens no fim:\n");
  insere_fim_lista(l, 60);
  insere_fim_lista(l, 70);
  insere_fim_lista(l, 80);
  insere_fim_lista(l, 90);
  imprime(l);

  printf("\nRemove item após o 20:\n");
  remove_item(l, 20);
  imprime(l);

  printf("\nTenta remover item após o 15:\n");
  remove_item(l, 15);
  imprime(l);

  printf("\nTamanho da lista: %d\n", tamanho_lista(l));

  printf("\nImprimindo a lista recursivamente:\n");
  p = primeira(l);
  imprime_rec(p);

  libera_lista(l);
}
