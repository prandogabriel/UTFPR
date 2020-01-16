typedef struct item Item;
typedef struct hash Hash;

#define TABLESIZE 50

Hash * cria_tabela();
void libera_tabela(Hash *h);
Item * cadastra_item(int chave);
void imprime_item(Item *item);
int verifica_tabela_cheia(Hash *h);
int hashing_divisao(int chave);
int sondagem_linear(int posicao, int i);
int sondagem_quadratica(int posicao, int i);
int duplo_hash(int chave, int antiga, int i);
void insere_enderecamento_aberto(Hash *h, Item *item);
Item * busca_enderecamento_aberto(Hash *h, int chave);
void imprime_tabela(Hash *h);
