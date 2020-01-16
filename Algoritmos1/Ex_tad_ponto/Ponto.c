#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#include "Ponto.h"

struct ponto {
    float x;
    float y;
};

Ponto * cria_ponto(float x, float y) {
    Ponto *p = malloc(sizeof(Ponto));
    p->x = x;
    p->y = y;
    return p;
}

void imprime_ponto(Ponto *p){
    printf("%.2f, %.2f\n", p->x, p->y);
}

float distancia(Ponto *p1, Ponto *p2) {
    float dx, dy, dt;
    dx = p1->x - p2->x;
    dy = p1->y - p2->y;
    dt = sqrt(pow(dx, 2) + pow(dy, 2));
    return dt;
}

void libera_ponto(Ponto *p) {
  free(p);
}
