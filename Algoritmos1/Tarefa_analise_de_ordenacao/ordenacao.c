#include<stdio.h>
#include<stdlib.h>
#include"ordenacao.h"
#include<time.h>

int main (){
    FILE *fp;
    char nome_arquivo[] = "comparacao.csv" ;

    fp = fopen(nome_arquivo,"w+");
    fprintf (fp, "Itens,Bolha,Insercao,Selecao,Quick,Merge\n");
    
    int n,i,j;
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
    t_bolha = (stop - start) * 1000;

    //executando o selection pegando seu tempo de execução
     scanf("%d",&n);
    for(i=0;i<n;i++){
        scanf("%d",&v[i]);
    }
    start = (double) clock () / CLOCKS_PER_SEC;
    selecao(v,n);
    stop = (double) clock () / CLOCKS_PER_SEC;
    t_selection = (stop - start) * 1000;

    //executando o inserção e pegando seu tempo
     scanf("%d",&n);
    for(i=0;i<n;i++){
        scanf("%d",&v[i]);
    }
    start = (double) clock () / CLOCKS_PER_SEC;
    insercao(v,n);
    stop = (double) clock () / CLOCKS_PER_SEC;
    t_insertion = (stop - start) *1000;

    //executando o merge e pegando seu tempo
     scanf("%d",&n);
    for(i=0;i<n;i++){
        scanf("%d",&v[i]);
    }
    start = (double) clock () / CLOCKS_PER_SEC;
    merge_sort(v,0,n);
    stop = (double) clock () / CLOCKS_PER_SEC;
    t_merge = (stop - start) * 1000;

    // executando o quick e pegando seu tempo
     scanf("%d",&n);
    for(i=0;i<n;i++){
        scanf("%d",&v[i]);
    }
    start = (double) clock () / CLOCKS_PER_SEC;
    quick_sort(v,0,n);
    stop = (double) clock () / CLOCKS_PER_SEC;
    t_quick = (stop - start) * 1000;

    imprime(v,n);
    fprintf(fp, "%d,%lf,%lf,%lf,%lf,%lf\n",n,t_bolha,t_insertion,t_selection,t_quick,t_merge);
    fclose(fp);
    
    return 0;
    
}