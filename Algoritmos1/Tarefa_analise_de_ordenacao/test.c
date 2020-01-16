#include<stdio.h>
#include<stdlib.h>
#include"ordenacao.h"
#include<time.h>

int main (){

    int n,i;
    double start, stop, t_bolha,t_insertion,t_selection, t_quick,t_merge;
    
    //executando o bolha pegando seu tempo de execução
    scanf("%d",&n);
    int v[n];
    for(i=0;i<n;i++){
        scanf("%d",&v[i]);
    }
    start = (double) clock () / CLOCKS_PER_SEC;
    bolha(v,n);
    stop = (double) clock () / CLOCKS_PER_SEC;
    t_bolha = stop - start;
    
    printf(" Bolha :\n");
    printf("%lf\n\n",t_bolha);

    //executando o selection pegando seu tempo de execução
     scanf("%d",&n);
    for(i=0;i<n;i++){
        scanf("%d",&v[i]);
    }
    start = (double) clock () / CLOCKS_PER_SEC;
    selecao(v,n);
    stop = (double) clock () / CLOCKS_PER_SEC;
    t_selection = stop - start;

    printf(" Selection :\n");
    printf("%lf\n\n",t_selection);

    //executando o inserção e pegando seu tempo
     scanf("%d",&n);
    for(i=0;i<n;i++){
        scanf("%d",&v[i]);
    }
    start = (double) clock () / CLOCKS_PER_SEC;
    insercao(v,n);
    stop = (double) clock () / CLOCKS_PER_SEC;
    t_insertion = stop - start;

    printf(" Insertion :\n");
    printf("%lf\n\n",t_insertion);

    //executando o merge e pegando seu tempo
     scanf("%d",&n);
    for(i=0;i<n;i++){
        scanf("%d",&v[i]);
    }
    start = (double) clock () / CLOCKS_PER_SEC;
    merge_sort(v,0,n);
    stop = (double) clock () / CLOCKS_PER_SEC;
    t_merge = stop - start;

    printf(" Merge :\n");
    printf("%lf\n\n",t_merge);

    // executando o quick e pegando seu tempo
     scanf("%d",&n);
    for(i=0;i<n;i++){
        scanf("%d",&v   [i]);
    }
    start = (double) clock () / CLOCKS_PER_SEC;
    quick_sort(v,0,n);
    stop = (double) clock () / CLOCKS_PER_SEC;
    t_quick = stop - start;

    printf(" Quick :\n");
    printf("%lf\n\n",t_quick);
    
    //imprime(v,n);
    return 0;
    
}