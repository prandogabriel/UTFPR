typedef struct item Item;
typedef struct celula Celula;
typedef struct lista Lista;

Lista * cria_lista_vazia();
int verifica_lista_vazia(Lista *l);
void insere_inicio_lista(Lista *l, int chave);
int tamanho_lista(Lista *l);
Celula * busca_por_chave(Lista *l, int chave);
void insere_meio_lista(Lista *l, int chave, int x);
void insere_fim_lista(Lista *l, int chave);
void imprime(Lista *l);
void remove_item(Lista *l, int x);
void libera_lista(Lista *l);
