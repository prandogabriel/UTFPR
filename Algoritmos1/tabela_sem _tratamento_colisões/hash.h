typedef struct item Item;
typedef struct hash Hash;

#define TABLESIZE 50

Hash * cria_tabela();
void libera_tabela(Hash *h);
Item * cadastra_item(int chave);
void imprime_item(Item *item);
int verifica_tabela_cheia(Hash *h);
void insere_sem_tratamento(Hash *h, Item *item);
void imprime_tabela(Hash *h);
Item * busca_sem_tratamento(Hash *h, int chave);
