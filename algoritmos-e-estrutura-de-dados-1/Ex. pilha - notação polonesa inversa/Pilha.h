#define MAXTAM 100

typedef struct item Item;
typedef struct pilha Pilha;

Pilha * cria_pilha_vazia();
int verifica_pilha_cheia(Pilha *p);
void empilha(Pilha *p, int numero);
void imprime_pilha(Pilha *p);
int verifica_pilha_vazia(Pilha *p);
Item * desempilha(Pilha *p);
void libera_pilha(Pilha *p);
void imprime_item(Item *i);
int soma(Item *penultimo, Item *ultimo);
int subtrai(Item *penultimo, Item *ultimo);
int multiplica(Item *penultimo, Item *ultimo);
int divide(Item *penultimo, Item *ultimo);
