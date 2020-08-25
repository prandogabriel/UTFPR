#include <stdio.h>
#include <stdlib.h>
#include "tree.h"

struct node {
  int key;
  Node *right;
  Node *left;
};

// criar nó
Node * create(int key){
  Node *no = (Node*) malloc(sizeof(Node));
  no->key = key;
  no->left = NULL;
  no->right = NULL;
  return no;
}

// liberar nó
int release(Node *no){
  if (no != NULL){
    free(no);
    return 1;
  }
  return 0;
}

Node * search(Node *tree, int value) {
  if (tree != NULL)
    if (tree->key == value)
      return tree;
    else if (tree->key < value)
      return search(tree->left, value);
    else
      return search(tree->right, value);
  else
    return NULL;
}


Node * insert(Node *tree, int value) {
  if (tree == NULL)
    tree = create(value);
  else if (value < tree->key)
    tree->left = insert(tree->left, value);
  else
    tree->right = insert(tree->right, value);
  return tree;
}


int delete(Node *tree, int value) {
  Node *aux, *auxP, *auxF;
  if (tree != NULL) {
    if (value < tree->key)
      delete(tree->left, value);
    else if (value > tree->key)
      delete(tree->right, value);
    else{
      aux = tree;
      if (aux->left == NULL) // Caso aux tenha apenas um ou nenhum descendente
        tree = tree->right;
      else if (aux->right == NULL) // aux tem apenas um descendente (esquerdo)
        tree = tree->left;
      else{ // o nó a ser excluído possui dois descendentes
        auxP = aux->right; // raiz da sub-árvore esquerda de aux
        auxF = auxP; // auxiliar para procurar o menor descendente de auxP

        // Procurar o menor descendente da sub-árvore direita de aux

        while (auxF->left != NULL){
          auxP = auxF; // A partir daqui, auxP é o nó pai de auxF
          auxF = auxF->left;
        }

        auxP->left = auxF->right; // o nó procurado pode ter um descendente
        auxF->left = aux->left; // primeira parte da substituição de aux
        auxF->right = aux->right; // segunda parte da substituição de aux
        tree = auxF; // atualização do nó raiz
      }

      free(aux); // liberação segura do nó procurado para a exclusão
      return 1; // exclusão bem sucedida
    }
    return 0;
  }

  return 0; // o nó com o valor procurado não foi encontrado
}


void prefix(Node *tree) {
  if (tree != NULL){
    printf("%d ", tree->key);
    prefix(tree->left);
    prefix(tree->right);
  }
}

void posfix(Node *tree) {
  if (tree != NULL){
    posfix(tree->left);
    posfix(tree->right);
    printf("%d ", tree->key);
  }
}

void nodes_leaves(Node *tree) {
  if(tree != NULL && tree->left==NULL && tree->right==NULL)
    printf("%d ", tree->key);
  else if (tree != NULL){
    nodes_leaves(tree->left);
    nodes_leaves(tree->right);
  }
}

