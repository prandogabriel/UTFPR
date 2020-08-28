
#define PILHA_H_

#define MAX 100

typedef struct pilha {
    char elementos[MAX];
    int topo;
} Pilha;

Pilha *create(); //cria pilha
char pop(Pilha *p); //desempilha
int push(Pilha *p, char c); //empilha
int isEmpty(Pilha *p); //verifica pilha vazia
int isFull(Pilha *p); //verifica pilha cheia
int size(Pilha *p); //verifica tamanho da pilha
