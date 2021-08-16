typedef struct ponto Ponto;

Ponto * cria_ponto(float x, float y);
void imprime_ponto(Ponto *p);
float distancia(Ponto *p1, Ponto *p2);
void libera_ponto(Ponto *p);
