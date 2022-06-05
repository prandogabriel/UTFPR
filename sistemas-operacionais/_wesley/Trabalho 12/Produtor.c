#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <semaphore.h>
#include <mqueue.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <time.h>
#include <math.h>

#define JAVALIS_GRELHADOS 20
#define QTD_GAULESES 6

typedef struct mensagemBuff{
    int ct;
}MensagemBuff;

MensagemBuff ac, ret;
MensagemBuff *acorda = &ac, *retira = &ret;
mqd_t fd1, fd2;

int main(){
    struct mq_attr attr;

    attr.mq_maxmsg = 1;
	attr.mq_msgsize = sizeof(MensagemBuff);
	attr.mq_flags = 0;

    mq_unlink("/retira");
    mq_unlink("/acorda");
	fd2 = mq_open("/retira", O_RDWR|O_CREAT, 0666, &attr);
    fd1 = mq_open("/acorda", O_RDWR|O_CREAT, 0666, &attr);


    printf("Cozinheiro repoe javalis\n", JAVALIS_GRELHADOS);
    retira->ct = JAVALIS_GRELHADOS;
    mq_send(fd2, (void*)retira, sizeof(MensagemBuff), 0);

    while(1)
    {
        mq_receive(fd1, (void*)acorda, sizeof(MensagemBuff), 0);

        if(acorda->ct == 1)
        {
            printf("Cozinheiro repoe javalis\n", JAVALIS_GRELHADOS);
            mq_send(fd2, (void*)retira, sizeof(MensagemBuff), 0);
            acorda->ct = 0;
        }
    }
}
