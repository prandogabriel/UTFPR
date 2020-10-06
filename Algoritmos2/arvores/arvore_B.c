#include<stdio.h>
#include<stdlib.h>
#include <math.h>

#define N 5

typedef struct Leaf{
  int n_keys;
  int keys[N-1];
  struct Leaf *children[N];
  int is_leaf;
}leaf;

leaf* create_leaf(){
  leaf *new = (leaf *) malloc (sizeof(leaf));

  if(new == NULL) {
    return NULL;
  }

  int i;
  new->is_leaf = 1;
  new->n_keys = 0;

  for (i = 0; i < N; i++)
    new->children[i] = NULL;

  return new;
}

int binarySearch(leaf* leaf, int key){
  int b = 0, m, e = leaf->n_keys - 1;

  while(b <= e){
    m = (b + e)/ 2;
    if(leaf->keys[m] == key) return m;
    if(leaf->keys[m] >= key) e = m - 1;
    if(leaf->keys[m] < key) b = m + 1;
  }
  return b;
}

leaf * split_page(leaf* leaf, int p){

  int i;
  struct Leaf *left, *right;
  left = leaf->children[p];
  right = create_leaf();
  right->is_leaf = left->is_leaf;

  int nk = round((N-1) / 2);
  right->n_keys = nk;

  for (i = 0; i < nk; i++){
    right->keys[i] = left->keys[i + nk];
  }

  if (!left->is_leaf)
  {
    for (i = 0; i < nk; i++){
      right->children[i] = left->children[i + nk];
    }
  }

  left->n_keys = (N - 1) / 2;
  for(i = leaf->n_keys; i > p; i--){
    leaf->children[i + 1] = leaf->children[i];
  }

  leaf->children[p + 1] = right;
  leaf->children[p] = left;
  leaf->keys[p] = right->keys[0];
  leaf->n_keys++;

  for (i = 0; i < nk; i++) {
    right->keys[i] = right->keys[i + 1];
  }
  right->n_keys--;

  return leaf;

}

leaf* insertNFP(leaf* leaf, int key){

   int i, p = binarySearch(leaf,key);

   if(leaf->is_leaf){

     for(i = leaf->n_keys; i > p; i--){
       leaf->keys[i] = leaf->keys[i - 1];
     }
     leaf->keys[i] = key;
     leaf->n_keys++;
   }
   else{
     if(leaf->children[p]->n_keys == N - 1){
       leaf = split_page(leaf, p);
       if(key > leaf->keys[p]) p++;
     }

     leaf = insertNFP(leaf->children[p], key);
   }

   return leaf;

}

leaf* insert(leaf* leaf, int key){
  struct Leaf *aux;
  struct Leaf *new;
  aux = leaf;


  if(aux->n_keys == N - 1){
    new  = create_leaf();
    new->is_leaf = 0;
    new->children[0] = aux;
    leaf = split_page(new, 0);
    new  = insertNFP(new,key);
  }
  else  aux = insertNFP(aux,key);

  return leaf;
}

int num_pages(leaf *root){
  int sum = 0, i;

  if(root != NULL){
    for (i = 0; i <= root->n_keys; i++)
    {
      if (root->children[i] != NULL)
        sum =  sum + 1 + num_pages(root->children[i]);
    }
  }

  return sum;
}

int main(void){

  int n, i, key;
  scanf(" %d", &n);
  leaf *root = create_leaf();
  for(i = 0; i < n; i++){
    scanf(" %d",&key);
    root = insert(root, key);
  }

  printf("%d\n", 1 + num_pages(root));

  return 0;
}
