#include <stdio.h>
#include "tree.h"

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



  nodes_leaves(no);

  printf("\n");
  return 0;
}
