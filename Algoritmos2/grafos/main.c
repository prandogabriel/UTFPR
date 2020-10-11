#include <stdio.h>
#include <stdlib.h>
#include "lista.h"
#include "GrafoMA.h"
#include "GrafoLA.h"


int main()
{
    Lista *l = criar_lista();
    GrafoMA *gMA = iniciar_grafoMA(7);
    GrafoLA *gLA = iniciar_grafoLA(7);

    inserir_na_lista(1, l);
    inserir_na_lista(0, l);
    inserir_na_lista(2, l);
    inserir_na_lista(3, l);
    inserir_na_lista(4, l);
    inserir_na_lista(5, l);
    inserir_na_lista(6, l);
    remover_na_lista(0, l);
    remover_na_lista(6, l);
    remover_na_lista(3, l);

    imprimir(l);

    liberar_lista(l);

    printf("\n\n\n");

    inserir_aresta(gMA, 0, 1);
    inserir_aresta(gMA, 0, 2);
    inserir_aresta(gMA, 0, 3);
    inserir_aresta(gMA, 0, 4);
    inserir_aresta(gMA, 1, 2);
    inserir_aresta(gMA, 1, 5);
    inserir_aresta(gMA, 1, 6);
    inserir_aresta(gMA, 2, 4);

    imprimir_arestas(gMA);

    printf("\n\n\nAresta 2 e 1 existem? Resp: %d\n", aresta_existe(gMA, 2, 1));

    printf("Aresta 1 e 3 existem? Resp: %d\n\n", aresta_existe(gMA, 1, 3));

    remover_aresta(gMA, 2, 0);

    imprimir_arestas(gMA);

    liberarGMA(gMA);

    printf("\n\n\n");

    inserir_arestaLA(gLA, 0, 1);
    inserir_arestaLA(gLA, 0, 2);
    inserir_arestaLA(gLA, 0, 3);
    inserir_arestaLA(gLA, 0, 4);
    inserir_arestaLA(gLA, 1, 2);
    inserir_arestaLA(gLA, 1, 5);
    inserir_arestaLA(gLA, 1, 6);
    inserir_arestaLA(gLA, 2, 4);

    imprimir_arestasLA(gLA);

    printf("\n\n\nAresta 2 e 1 existem? Resp: %d\n", aresta_existeLA(gLA, 2, 1));

    printf("Aresta 1 e 3 existem? Resp: %d\n\n", aresta_existeLA(gLA, 1, 3));

    remover_arestaLA(gLA, 2, 0);

    imprimir_arestasLA(gLA);

    liberarGLA(gLA);

    return 0;
}
