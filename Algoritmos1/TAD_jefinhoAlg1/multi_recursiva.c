#include <stdio.h>
#include <string.h>
#include <math.h>
#include <stdlib.h>

int multiplica(int x, int y) {
  if(x > 0 && y > 1){
    return x += multiplica(x, y-1);
  }else if(y==0){
    return 0;
  }else {
    return x;
  }
}

int main(){
  printf("%d\n",multiplica(5,3));
  return 0;
}
