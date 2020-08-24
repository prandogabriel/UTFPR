#include <stdio.h>
#include <stdlib.h>

int main()
{
  int quantity_activities;
  int i = 0;
  int j = 0;

  scanf("%d", &quantity_activities);

  int start_time[quantity_activities];
  int end_time[quantity_activities];

  int aux;

  while (i<quantity_activities)
  {
    scanf(" %d %d", &start_time[i], &end_time[i]);
    i++;
  }



  for(i = 0; i < quantity_activities; i++)
  {
    for(j = i+1; j < quantity_activities; j++)
    {
      if(end_time[j] < end_time[i])
      {
        aux = end_time[i];
        end_time[i] = end_time[j];
        end_time[j] = aux;
        aux = start_time[i];
        start_time[i] = start_time[j];
        start_time[j] = aux;
      }
    }
  }

  i = 0;

  printf("Aulas alocadas: ");

  j = 0;
  printf("%d ", j);

  for(i = 0; i < quantity_activities; i++)
  {
    if(start_time[i] > end_time[j])
    {
      j = i;
      printf("%d ", i);
    }
  }

  printf("\n");
  return 0;
  }
