//Leia um numero do teclado e imprima todos os numeros ımpares de 0 ate o numero lido
import java.util.Scanner;

public class ex1{
    public static void main(String[] args){
        Scanner teclado = new Scanner(System .in);

        int i;
        int n = teclado.nextInt();
        teclado.close();
        if(n<=0){
            System.out.println("Numero invalido");
            
        }
        for(i=0; i<n;i++){
            if((i%2)!=0 ){
                System.out.println(i);
            }
        }

    }
}