#define MAXTAM 5
typedef struct item Item;
typedef struct lista Lista;

Lista * cria_lista_vazia();
int verifica_lista_cheia(Lista *l);
void adiciona_item_fim_lista(Lista *l, int chave);
void imprime_lista(Lista *l);
int busca_item_por_chave(Lista *l, int chave);
int verifica_lista_vazia(Lista *l);
void remove_item(Lista *l, int chave);
void libera_lista(Lista *l);
