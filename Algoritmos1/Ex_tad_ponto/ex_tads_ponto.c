#include <stdio.h>
#include "Ponto.h"

main() {
    Ponto *p1, *p2;
    float d;

    p1 = cria_ponto(10, 20);
    p2 = cria_ponto(10, 30);
    imprime_ponto(p1);
    imprime_ponto(p2);

    d = distancia(p1, p2);
    printf("%.2f\n", d);

    libera_ponto(p1);
    libera_ponto(p2);
}
