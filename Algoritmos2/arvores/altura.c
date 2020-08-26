#include <stdio.h>
#include <stdlib.h>
typedef struct Node Node;

typedef struct Node {
  int key;
  Node *right;
  Node *left;
}Node;

void nodes_leaves(Node *tree) {
  if(tree != NULL && tree->left==NULL && tree->right==NULL)
    printf(" %d", tree->key);
  else if (tree != NULL){
    nodes_leaves(tree->left);
    nodes_leaves(tree->right);
  }
}

int height_tree(Node *tree) {
  int left;
  int right;

  if (tree != NULL){
    left = 1 + height_tree(tree->left);
    right = 1 + height_tree(tree->right);

    if(left > right)
      return left;
    else
      return right;
  }

  return 0;
}

Node * create(int key){
  Node *no = (Node*) malloc(sizeof(Node));
  no->key = key;
  no->left = NULL;
  no->right = NULL;
  return no;
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



int main() {
  int i = 0;
  int quantity_nodes;
  int aux;
  scanf("%d",&quantity_nodes);

  scanf("%d",&aux);
  Node *no = create(aux);

  while (i < quantity_nodes-1)
  {
    scanf("%d",&aux);
    insert(no, aux);
    i++;
  }

  printf("%d\n", height_tree(no)-1);

  return 0;
}
