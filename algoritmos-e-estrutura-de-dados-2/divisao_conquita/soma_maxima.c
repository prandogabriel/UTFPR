#include <stdio.h>
#include <stdlib.h>

int max_sum(int array[], int start, int end){
    int center;

    if(start < 0|| start > end){
        return 0;
    } else if(start == end){
        if(array[start] >= 0){
            return array[start];
        }
    } else {
        center = (int)(start+end) / 2;

        if(array[center] >= 0){
            return array[center] + max_sum(array, start, center-1) + max_sum(array, center + 1, end);
        } else {
            return max_sum(array, start, center-1) + max_sum(array, center + 1, end);
        }
    }

    return 0;
}

int main()
{
  int quantity_elements;
  int i = 0;

  scanf("%d", &quantity_elements);

  int elements[quantity_elements];

  while (i<quantity_elements)
  {
    scanf(" %d", &elements[i]);
    i++;
  }

  printf("%d\n", max_sum(elements, 0, quantity_elements-1));

  return 0;
}
