//Leia um n ́umero do teclado e informe todos os n ́umerosprimos entre 0 e este n ́umero;
import java.util.Scanner;

public class ex4{
    public static void main(String[] args){
        Scanner teclado = new Scanner(System.in);

        int i,j,cont;
        int n = teclado.nextInt();
        teclado.close();
        if (n<=2){
            System.out.println("número Inválido");
        }
        for(i=2;i<=n;i++){
            cont= 0;
            for(j=2;j<=n;j++){
                if(i%j==0){
                    cont++;
                }
            }

            if(cont <= 1){
                System.out.println(i + " é um número primo");
            }
            //else if(cont >1){
            //    System.out.println(i + " não é um numero primo");
            //}
        }
    }
}