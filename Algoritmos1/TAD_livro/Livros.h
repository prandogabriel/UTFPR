typedef struct livro Livro;

Livro * cadastra_livro(char titulo[], char genero[], int ano, float valor);
void imprime_livro(Livro *l);
Livro * retorna_mais_caro(Livro **l, int n);
Livro * retorna_mais_barato(Livro **l, int n);
// mostra livros de um determinado genero
// mostra livros que são lançamento
// libera livros
