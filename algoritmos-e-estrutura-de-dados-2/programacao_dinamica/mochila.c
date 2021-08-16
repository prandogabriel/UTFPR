#include <stdio.h>
#include <stdlib.h>

int max(int capacity, int weights[], int values[], int number){
  int custos[capacity+1][number+2];
  int value_stored;
  int best_0;
  int best_1;
  int i, j;


  for(i = 0; i <= capacity; i++){
    custos[i][0] = i;
    custos[i][1] = 0;
  }

  for(j = 2; j <= number+1;j++){
    for(i = 0; i <= capacity; i++){

      best_0 = custos[i][j-1];

      if(i >= weights[j-2]){
        best_1 = values[j-2]+custos[i-weights[j-2]][j-1];

        if(best_0 > best_1){
          value_stored = best_0;
        } else {
          value_stored = best_1;
        }
      } else {
        value_stored = best_0;
      }
      custos[i][j] = value_stored;
    }
  }

  return custos[capacity][number+1];
}

int main() {
  int capacity;
  int number_items;

  scanf("%d", &capacity);

  scanf("%d", &number_items);

  int weights[number_items];
  int values[number_items];

  int i = 0;

  while (i<number_items)
  {
    scanf("%d", &weights[i]);
    scanf("%d", &values[i]);
    i++;
  }

  printf("%d\n", max(capacity, weights, values, number_items));

  return 0;
}
