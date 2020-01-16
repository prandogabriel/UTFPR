#define MAXTAM 100
typedef struct item Item;
typedef struct lista Lista;

Lista * cria_lista_vazia();
int verifica_lista_vazia(Lista *l);
int verifica_lista_cheia(Lista *l);
void adiciona_item_fim_lista(Lista *l, int codigo, float peso);
void imprime_lista(Lista *l);
void libera_lista(Lista *l);
