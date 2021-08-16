#include "hash.h"

main() {
  Hash *h = cria_tabela();
  Item *item;

  item = cadastra_item(0);
  insere_enderecamento_aberto(h, item);
  item = cadastra_item(1);
  insere_enderecamento_aberto(h, item);
  item = cadastra_item(2);
  insere_enderecamento_aberto(h, item);
  item = cadastra_item(3);
  insere_enderecamento_aberto(h, item);
  item = cadastra_item(50); // sem tratamento, cairia na posição 0
  insere_enderecamento_aberto(h, item);
  item = cadastra_item(101); // sem tratamento, cairia na posição 1
  insere_enderecamento_aberto(h, item);
  item = cadastra_item(102); // sem tratamento, cairia na posição 2
  insere_enderecamento_aberto(h, item);
  item = cadastra_item(103); // sem tratamento, cairia na posição 3
  insere_enderecamento_aberto(h, item);

  imprime_tabela(h);

  imprime_item(busca_enderecamento_aberto(h, 0));
  imprime_item(busca_enderecamento_aberto(h, 1));
  imprime_item(busca_enderecamento_aberto(h, 2));
  imprime_item(busca_enderecamento_aberto(h, 3));
  imprime_item(busca_enderecamento_aberto(h, 50));
  imprime_item(busca_enderecamento_aberto(h, 101));
  imprime_item(busca_enderecamento_aberto(h, 102));
  imprime_item(busca_enderecamento_aberto(h, 103));

  libera_tabela(h);
}
