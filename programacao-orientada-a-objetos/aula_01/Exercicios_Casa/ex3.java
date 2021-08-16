//Leia um n ́umero do teclado e informe se este  ́e um n ́umeroperfeito. N ́umero perfeito  ́e um n ́umero inteiro cuja soma detodos os seus divisores positivos, excluindo ele pr ́oprio, ́e igualao pr ́oprio n ́umero
import java.util.Scanner;

public class ex3{
    public static void main(String[] args){
        Scanner teclado = new Scanner(System .in);

        int i,cont;
        int n = teclado.nextInt();
        teclado.close();
        if(n<=0){
            System.out.println("Numero invalido");
            
        }
        cont =0;
        for(i=1; i<=(n/2);i++){
            if((n%i)==0 ){
                cont+=i;
            }
        }
        if(cont==n){
            System.out.println(n +" é um numero perfeito");
        }
        else {
            System.out.println(n + " não é um numero perfeito ");
        }
    }
}