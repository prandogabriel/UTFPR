#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <semaphore.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <time.h>
#include <math.h>

#define JAVALIS_GRELHADOS 20
#define QTD_GAULESES 7

typedef struct _P1
{
  int javalis;

} P1;

typedef struct _P2
{
  char pipe;

} P2;

char nome[QTD_GAULESES] = "GABRIEL";
