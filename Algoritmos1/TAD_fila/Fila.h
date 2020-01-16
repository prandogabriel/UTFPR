typedef struct item Item;
typedef struct fila Fila;

#define MAXTAM 5

Fila * cria_fila_vazia();
int verifica_fila_vazia(Fila *f);
int verifica_fila_cheia(Fila *f);
void enfileira(Fila* f, int chave);
void imprime(Fila* f);
void desenfileira(Fila* f);
int tamanho(Fila *f);
void libera(Fila *f);
