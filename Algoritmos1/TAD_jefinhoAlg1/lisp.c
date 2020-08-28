#include <stdio.h>
#include "pilha.h"

int main() {
    int quantidade, posicao, nivel;

    printf("Quantidade de caracteres:\n");
    scanf("%d", &quantidade);

    char frase[quantidade];
    printf("Frase:\n");
    scanf("%s", frase);

    printf("Verificar caracter na posicao?\n");
    scanf("%d", &posicao);
    char procurado = frase[posicao - 1];

    int abortado = 0;
    Pilha *lisp = create();

    for (int i = 0; i < quantidade; i++) {
        if (frase[i] == '(') {
            push(lisp, frase[i]);
        }
        if (frase[i] == ')') {
            if (isEmpty(lisp)) {
                abortado = 1;
                break;
            } else {
                pop(lisp);
            }
        }
        if (i == posicao - 1) {
            nivel = size(lisp);
        }
    }
    if (!abortado && isEmpty(lisp)) {
        printf("A lista esta balanceada! %c está no nivel %d", procurado, nivel);
    } else {
        printf("A lista nao esta balanceada");
    }
}
