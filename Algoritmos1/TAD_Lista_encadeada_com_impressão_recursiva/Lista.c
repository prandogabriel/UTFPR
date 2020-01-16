#include <stdio.h>
#include <stdlib.h>
#include "Lista.h"

// item que vai ser guardado na lista
struct item {
  int chave;
  // demais campos;
};

// a célula contém um item e um ponteiro para a próxima célula da lista
struct celula {
  Item item;
  struct celula *prox;
};

/* a struct lista gurada o endereço da primeira célula de lista. A partir da
primeira é possível acessar todas as outras */
struct lista {
  Celula *primeira;
};

Lista * cria_lista_vazia() {
  Lista *l = malloc(sizeof(Lista));
  /* NULL é um ponteiro nulo, pode ser usado para inicializar um ponteiro
  que ainda não está associado a um endereço de memória */
  l->primeira = NULL;
  return l;
}

int verifica_lista_vazia(Lista *l) {
  // se a primeira ainda aponta para NULL, então está vazia
  return l->primeira == NULL;
}

void insere_inicio_lista(Lista *l, int chave) {
  // cria novo item que vai ser guardado na lista
  Item novo;
  novo.chave = chave;
  // cria nova célula que vai guardar o item
  Celula *nova = malloc(sizeof(Celula));
  nova->item = novo;
  // estamos inserindo no início, então a próxima célula é aquela que era a 1a
  nova->prox = l->primeira;
  // a primeira agora é a nova célula
  l->primeira = nova;
}

int tamanho_lista(Lista *l) {
   Celula *aux = l->primeira;
   int i = 0;
   while(aux != NULL) {
      aux = aux->prox;
      i++;
   }
   return i;
}

/* retorna a célula contendo o item correspondente a chave informada
ou NULL se não encontrou */
Celula * busca_por_chave(Lista *l, int chave) {
  int achou = 0;
  Celula *aux = l->primeira;
  while(achou == 0 && aux != NULL) {
    if(aux->item.chave == chave)
      achou = 1;
    else
      aux = aux->prox;
  }
  return aux;
}

void insere_meio_lista(Lista *l, int chave, int x) {
  // cria novo item que vai ser guardado na lista
  Item novo;
  novo.chave = chave;
  // cria nova célula que vai guardar o item
  Celula *nova = malloc(sizeof(Celula));
  nova->item = novo;
  // acha a célula após a qual será feita a inserção
  Celula *aux = busca_por_chave(l, x);
  if(aux != NULL) {
    /* a nova célula vai ser inserida depois de aux, então sua próxima célula
    será aquela que era a próxima de aux */
    nova->prox = aux->prox;
    /* a nova célula vai ser inserida depois de aux, então a próxima célula
    de aux é a nova célula */
    aux->prox = nova;
  }
  else {
    printf("O item informado não existe.\n");
  }
}

void insere_fim_lista(Lista *l, int chave) {
  // cria novo item que vai ser guardado na lista
  Item novo;
  novo.chave = chave;
  // cria nova célula que vai guardar o item
  Celula *nova = malloc(sizeof(Celula));
  nova->item = novo;
  // a nova célula vai ser a última, então após ela tem NULL
  nova->prox = NULL;

  if(verifica_lista_vazia(l)) // se a lista está vazia, quem vai apontar para a nova é a primeira
    l->primeira = nova;
  else { // se não está vazia, quem vai apontar para a nova é a que era a última
    /* variável auxiliar que vai guardar o endereço da última célula
    para inserir a nova depois da última */
    Celula *ultima;
    // partindo da primeira célula, percorrer a lista até achar a última
    ultima = l->primeira;
    while(ultima->prox != NULL) {
      ultima = ultima->prox;
    }
    // após a última, inserir a nova célula
    ultima->prox = nova;
  }
}

void imprime(Lista *l) {
    Celula *aux;
    // partindo da primeira célula, percorrer a lista até achar a última
    for(aux = l->primeira; aux != NULL; aux = aux->prox)
        printf("chave = %d\n", aux->item.chave);
}

Celula * primeira(Lista *l) {
	return l->primeira;
}

void imprime_rec(Celula *aux) {  
    if (aux == NULL)  
    	return;  
  
    imprime_rec(aux->prox);  
    printf("chave = %d\n", aux->item.chave); 
}

// remove item após determinada posição
void remove_item(Lista *l, int x) {
  Celula *anterior = busca_por_chave(l, x);
  if(verifica_lista_vazia(l) || anterior == NULL) {
    printf("Erro: a lista está vazia ou o item não existe.\n");
    return;
  }
  // será removida aquela que vem depois da célula com o item da chave buscada
  Celula *remover = anterior->prox;
  // liga a anterior com a próxima da que vai ser removida
  anterior->prox = remover->prox;
  // libera memória
  free(remover);
}

void libera_lista(Lista *l) {
  Celula *aux = l->primeira;
  Celula *liberar;
  while(aux != NULL) {
    liberar = aux;
    aux = aux->prox;
    free(liberar);
  }
  free(l);
}
