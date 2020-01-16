typedef struct item Item;
typedef struct celula Celula;
typedef struct fila Fila;

Fila * cria_fila_vazia();
int verifica_fila_vazia(Fila *f);
void enfileira(Fila *f, int chave);
void imprime(Fila *f);
void desenfileira(Fila *f);
void libera_fila(Fila *f);
