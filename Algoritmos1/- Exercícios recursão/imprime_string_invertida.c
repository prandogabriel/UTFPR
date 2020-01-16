#include <stdio.h>

void imprime_rec(char *s) {
  if(*s == '\0')
    return;
  else
    imprime_rec(s + 1); // o ponteiro vai sendo movido até o final
    printf("%c", *s);
}

main() {
  imprime_rec("bom dia");
  printf("\n");
}
