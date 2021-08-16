
typedef struct item Item;
typedef struct fila Fila;

#define MAXTAM 20

Fila * cria_fila_vazia();
int verifica_fila_vazia(Fila *f);
int verifica_fila_cheia(Fila *f);
void enfileira(Fila* f, int chave);
void imprime(Fila* f);
void libera(Fila *f);
int desenfilera(Fila *f);
void remove_compara(Fila* f) ;