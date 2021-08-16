#include<stdlib.h>
#include<stdio.h>

int backtracking_bag(int capacity, int weights[], int values[], int index)
{
  int test1, test2;

  if(index<0)
    return 0;
  else if(weights[index]>capacity)
    return backtracking_bag(capacity, weights, values, index-1);
  else {
    test1 = backtracking_bag(capacity, weights, values, index-1);
    test2 = values[index] + backtracking_bag(capacity - weights[index], weights, values, index-1);

    if(test1>test2)
      return test1;

    return test2;
  }

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

  printf("%d\n", backtracking_bag(capacity, weights, values, number_items - 1));

  return 0;
}

// gcc -o teste mochila.c  && ./teste < mochila.txt
