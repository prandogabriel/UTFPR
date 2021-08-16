#include "hash.h"

main() {
  Hash *h = cria_tabela();
  Item *item;

  item = cadastra_item(92);
  insere_sem_tratamento(h, item);
  item = cadastra_item(10);
  insere_sem_tratamento(h, item);
  item = cadastra_item(2);
  insere_sem_tratamento(h, item);
  item = cadastra_item(60);
  insere_sem_tratamento(h, item);

  imprime_tabela(h);

  imprime_item(busca_sem_tratamento(h, 1));
  imprime_item(busca_sem_tratamento(h, 110));

  libera_tabela(h);
}
