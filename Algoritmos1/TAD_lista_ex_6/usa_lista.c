#include <stdio.h>
#include "Lista.h"

main() {
  Lista *l = cria_lista_vazia();
  printf("%d", verifica_lista_vazia(l));
  printf("%d", verifica_lista_cheia(l));
  adiciona_item_fim_lista(l, 2, 1.5000);
  adiciona_item_fim_lista(l, 5, 2.5000);
  imprime_lista(l);
  libera_lista(l);
}
