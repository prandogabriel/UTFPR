#include<stdio.h>
#include<stdlib.h>

#define RED   5
#define BLACK 10

typedef struct Leaf{
  int value;
  struct Leaf *left;
  struct Leaf *right;
  struct Leaf *parent;
  int color;
}leaf;

typedef struct Tree{
  leaf *root;
}tree;

void init(tree* t){
  t->root = NULL;
}

leaf* create_leaf(int value){
  leaf *new = (leaf *) malloc (sizeof(leaf));
  if(new == NULL) return NULL;
  new->value = value;
  new->color = RED;
  new->left = new->right = new->parent = NULL;
  return new;
}

void inorder(leaf *root){

    if (root != NULL){
      inorder(root->left);
      printf("%d %d\t", root->value,root->color);
      inorder(root->right);
    }
}

leaf* uncle(leaf* root){
  leaf* aux = NULL;

  if((root != NULL) && (root->parent != NULL)){
    if (root->parent->parent->left == root->parent){
      aux = root->parent->parent->right;
    }
    else{
      aux = root->parent->parent->left;
    }
  }
  return aux;
}

leaf* grandp(leaf *root){

  if((root != NULL) && (root->parent!= NULL)) return  root->parent->parent;

  return NULL;
}

leaf* rot_right(leaf *root){
  leaf *p = root->parent, *g = p->parent;

  g->left   = p->right;
  p->right  = g;
  if(g->parent != NULL){
    if(g->parent->left == g){
      g->parent->left = p;
    }
    else{
      g->parent->right = p;
    }
  }

  p->parent = g->parent;
  g->parent = p;


  root = p;
  return root;
}

leaf* rot_left(leaf *root){
  leaf *p = root->parent, *g = p->parent;

  g->right  = p->left;
  p->left   = g;

  if(g->parent != NULL){
    if(g->parent->right == g){
      g->parent->right = p;
    }
    else{
      g->parent->left = p;
    }
  }


  p->parent = g->parent;
  g->parent = p;


  root = p;
  return root;
}

leaf* rot_right_left(leaf *root){
  leaf *p = root->parent, *g = p->parent;

  p->left      = root->right;
  root->right  = p;
  if (g->left == p)
      g->left = root;
  else
  {
      g->right = root;
  }

  root->parent = p->parent;
  p->parent    = root;

  return rot_left(p);
}

leaf* rot_left_right(leaf *root){
  leaf *p = root->parent, *g = p->parent;

  p->right     = root->left;
  root->left   = p;
  if (g->left == p)
      g->left = root;
  else
  {
      g->right = root;
  }
  root->parent = p->parent;
  p->parent    = root;

  return rot_right(p);
}

leaf* balance(leaf* root){

  leaf *p, *u;

  if(root->parent == NULL){
    root->color = BLACK;
  }

  else{
    p = root->parent;

    if(p->color == RED){

      u = uncle(root);

      if((u != NULL) && (u->color == RED)){
        p->color = u->color = BLACK;
        root = grandp(root);

        if(root->parent != NULL)
          root->color = RED;
      }

      else{
        if(p->left == root){
          if(p->parent->right == p){
            root = rot_right_left(root);

          }
          else{
            root = rot_right(root);
          }
        }
        else{
          if(p->parent->left == p){
            root = rot_left_right(root);
          }
          else{
            root = rot_left(root);
          }
        }

        root->color = BLACK;
        if(root->left  != NULL) root->left->color  = RED;
        if(root->right != NULL) root->right->color = RED;
      }

    }
  }
  return root;
}

leaf* insert(leaf* root, int value){

  leaf *auxP, *aux;

  if (root == NULL){
      return create_leaf(value);
  }

  else{
    auxP = aux = root;

    while (aux != NULL){
      auxP = aux;

      if(value < aux->value) aux = aux->left;
      else aux = aux->right;
    }

    aux = create_leaf(value);
    aux->parent = auxP;

    if(aux->value < auxP->value) auxP->left = aux;
    else auxP->right = aux;

    while ((aux->parent != NULL)  && (aux->color == RED) && (aux->parent->color == RED)){
      aux = balance(aux);
    }

  }

  while (aux->parent != NULL) aux = aux->parent;
  if(aux != root) root = aux;
  return root;
}

int height_red_black(leaf* root){
  if(root == NULL) return 0;

  switch(root->color) {
    case (RED):
      return height_red_black(root->left);
    case(BLACK):
      return 1 + height_red_black(root->left);
    default:
      return 0;
  }
}

int main(void){

  int n;
  scanf(" %d",&n);

  tree *t = (tree*) malloc (sizeof(tree));
  init(t);
  int i, key;
  scanf(" %d",&key);
  t->root = insert(t->root, key);
  t->root->color = BLACK;


  for(i=1; i<n; i++){
    scanf(" %d",&key);
    t->root = insert(t->root, key);
  }

  printf("%d\n",height_red_black(t->root));

  return 0;
}
