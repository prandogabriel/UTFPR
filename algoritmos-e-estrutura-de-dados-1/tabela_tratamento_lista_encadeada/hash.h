typedef struct item Item;
typedef struct celula Celula;
typedef struct hash Hash;

#define TABLESIZE 50

Hash * cria_tabela();
void libera_tabela(Hash *h);
Item * cadastra_item(int chave);
void imprime_item(Item *item);
int verifica_tabela_cheia(Hash *h);
int hashing_divisao(int chave);
void imprime_tabela(Hash *h);
void insere_tratamento_lista_encadeada(Hash *h, Item *item);
Item * busca_tratamento_lista_encadeada(Hash *h, int chave);
void remove_tratamento_lista_encadeada(Hash *h, int chave);
Item * cadastra_item(int chave);
void imprime_item(Item *item);
