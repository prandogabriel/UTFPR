#include <stdio.h>
#include "Lista.h"

int main() {
  Lista *l = cria_lista_vazia();

  printf("Vazia: %d\n", verifica_lista_vazia(l));

  printf("\nInserindo no inicio\n");
  inserir_inicio(l, 10);
  inserir_inicio(l, 20);
  inserir_inicio(l, 30);
  imprime_esq_dir(l);
  printf("\n");
  imprime_dir_esq(l);

  printf("\nInserindo no final\n");
  insere_final(l, 5);
  insere_final(l, 15);
  imprime_esq_dir(l);
  printf("\n");
  imprime_dir_esq(l);

  printf("\nInserindo 90 após o 20\n");
  insere_meio(l, 20, 90);
  imprime_esq_dir(l);
  printf("\n");
  imprime_dir_esq(l);

  printf("\nInserindo 1 após o 15\n");
  insere_meio(l, 15, 1);
  imprime_esq_dir(l);
  printf("\n");
  imprime_dir_esq(l);

  printf("\nRemovendo do inicio\n");
  remove_inicio(l);
  imprime_esq_dir(l);
  printf("\n");
  imprime_dir_esq(l);

  printf("\nRemovendo do final\n");
  remove_final(l);
  imprime_esq_dir(l);
  printf("\n");
  imprime_dir_esq(l);

  printf("\nRemovendo 5\n");
  remove_meio(l, 5);
  imprime_esq_dir(l);
  printf("\n");
  imprime_dir_esq(l);

  printf("\nRemovendo 45 (item que não existe)\n");
  remove_meio(l, 45);
  imprime_esq_dir(l);
  printf("\n");
  imprime_dir_esq(l);

  printf("\nRemovendo 20 (primeiro)\n");
  remove_meio(l, 20);
  imprime_esq_dir(l);
  printf("\n");
  imprime_dir_esq(l);

  printf("\nRemovendo o 15 (ultimo)\n");
  remove_meio(l, 15);
  imprime_esq_dir(l);
  printf("\n");
  imprime_dir_esq(l);

  printf("\nRemovendo 90\n");
  remove_meio(l, 90);
  imprime_esq_dir(l);
  printf("\n");
  imprime_dir_esq(l);

  printf("\nRemovendo 10 (item restante)\n");
  remove_meio(l, 10);
  imprime_esq_dir(l);
  printf("\n");
  imprime_dir_esq(l);

  libera_lista(l);
}
