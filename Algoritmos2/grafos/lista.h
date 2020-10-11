typedef struct Cell Cell;

struct Cell{
    int key;
    Cell *next;
};


typedef struct{
    Cell *head;
}Lista;


Lista* criar_lista();

Cell* criar_celula(int key);

int lista_vazia(Lista *l);

int procurar(int key, Lista *l);

int remover_na_lista(int key, Lista *l);

void inserir_na_lista(int key, Lista *l);

void imprimir(Lista *l);

int liberar_lista(Lista *l);
