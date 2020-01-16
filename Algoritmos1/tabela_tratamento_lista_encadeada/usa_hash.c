#include <stdio.h>
#include "hash.h"

main() {
  Hash *h = cria_tabela();
  Item *item;

  printf("Inserção...\n");
  item = cadastra_item(0);
  insere_tratamento_lista_encadeada(h, item);
  item = cadastra_item(1);
  insere_tratamento_lista_encadeada(h, item);
  item = cadastra_item(2);
  insere_tratamento_lista_encadeada(h, item);
  item = cadastra_item(3);
  insere_tratamento_lista_encadeada(h, item);
  item = cadastra_item(50);
  insere_tratamento_lista_encadeada(h, item);
  item = cadastra_item(100);
  insere_tratamento_lista_encadeada(h, item);
  item = cadastra_item(200);
  insere_tratamento_lista_encadeada(h, item);
  item = cadastra_item(52);
  insere_tratamento_lista_encadeada(h, item);
  item = cadastra_item(202);
  insere_tratamento_lista_encadeada(h, item);
  item = cadastra_item(88);
  insere_tratamento_lista_encadeada(h, item);
  imprime_tabela(h);

  printf("\nBusca...\n");
  imprime_item(busca_tratamento_lista_encadeada(h, 99));
  imprime_item(busca_tratamento_lista_encadeada(h, 200));

  printf("\nRemoção...\n");
  remove_tratamento_lista_encadeada(h, 100); // remover do meio
  remove_tratamento_lista_encadeada(h, 200); // remover do início
  remove_tratamento_lista_encadeada(h, 0); // remover do fim
  remove_tratamento_lista_encadeada(h, 1); // remover a única
  remove_tratamento_lista_encadeada(h, 500); // não existe
  imprime_tabela(h);

  libera_tabela(h);
}
