#include "pilha.h"
#include <stdlib.h>

Pilha *create() {
    Pilha *p = (Pilha *) malloc(sizeof(Pilha));
    p->topo = -1;
    for (int i = 0; i < MAX; i++) {
        p->elementos[i] = 0;
    }
    return p;
}

int isFull(Pilha *p) {
    return p->topo == MAX - 1;
}

char pop(Pilha *p) {
    char c = p->elementos[p->topo];
    p->topo--;
    return c;
}

int push(Pilha *p, char c) {
    if (isFull(p)) {
        return 0;
    }
    p->topo++;
    p->elementos[p->topo] = c;
    return 1;
}

int isEmpty(Pilha *p) {
    return p->topo == -1;
}

int size(Pilha *p) {
    return p->topo + 1;
}
