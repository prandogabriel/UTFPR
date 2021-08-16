typedef struct matriz Matriz;
typedef struct celula Celula;

Matriz * inicializa_matriz(int lin, int col);
void libera_matriz(Matriz *m);
void adiciona_celula(Matriz *m, int lin, int col, float valor);
void imprime_matriz(Matriz *m);
float busca_valor(Matriz *m, int lin, int col);
