#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <semaphore.h>
#include <fcntl.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <time.h>
#include <math.h>

#define JAVALIS_GRELHADOS 20
#define QTD_GAULESES 7
#define SHARED_M "/shm_open"

typedef struct _Shared_Memory
{
  int javalis;
  sem_t sem_1, sem_2, sem_3, sem_4, sem_5;
} Shared_Memory;

char nome[QTD_GAULESES] = "GABRIEL";
