//Leia um numero do teclado e informe se este numero e primoou nao
import java.util.Scanner;

public class ex2 {
    public static void main(String[] args){
        Scanner teclado = new Scanner(System .in);

        int i,cont;
        int n = teclado.nextInt();
        teclado.close();
        if(n<=0){
            System.out.println("Numero invalido");
            
        }
        cont =0;
        for(i=2; i<=(n/2);i++){
            if((n%i)==0 ){
                cont++;
            }
        }
        if(cont==0){
            System.out.println(n +" é um numero primo");
        }
        else {
            System.out.println(n + " não é um numero primo ");
        }
    }
}