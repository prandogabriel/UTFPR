//Preencha um vetor com 10 n ́umeros pseudo-aleat ́orios eimprima este vetor de forma ordenada. Fa ̧ca uso doalgoritmo de ordena ̧c ̃ao bolha.1
import java.util.Random;

public class ex5{
    public static void main(String[] args){
        Random x = new Random( ) ;
        int i, aux, j;
        int[] vet = new int[10];
        System.out.println("vetor desordenado");
        for(i=0;i<10;i++){
            vet[i]=x.nextInt(100);
            System.out.println(vet[i]);
        }
        System.out.println("vetor ordenado");
        for(i = 0; i<10; i++){
            for(j = 0; j<10; j++){
            if(vet[j] > vet[j + 1]){
                aux = vet[j];
                vet[j] = vet[j+1];
                vet[j+1] = aux;
            }
        }
    }
    for(i = 0; i<10; i++){
        System.out.println(" "+vet[i]);
    }

    }
}