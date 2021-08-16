typedef struct item Item;
typedef struct pilha Pilha;

#define MAXTAM 5

Pilha * cria_pilha_vazia();
int verifica_pilha_cheia(Pilha *p);
void empilha(Pilha *p, int chave);
void imprime_pilha(Pilha *p);
int verifica_pilha_vazia(Pilha *p);
void desempilha(Pilha *p);
void libera_pilha(Pilha *p);
