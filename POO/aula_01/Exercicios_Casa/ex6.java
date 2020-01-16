import java.util.Scanner;

public class ex6 {
    public static void main(String[] args){
        Scanner teclado = new Scanner(System .in);

        int n;
        
        do{
            n = teclado.nextInt();
            teclado.close();
            if(n<2 ^ n%2==0){
                System.out.println("número invalido");
            }
        }while(n<2 ^ n%2==0);
        
        int m = (n*2)-1;
        int [][] matriz = new int [n][m]; 
        for(int i=0;i<n;i++){
            for(int j=0;j<m;j++){
                matriz[i][j] = 0;
                //System.out.print(matriz[i][j]+" ");

            }
            //System.out.println();
        }
        int aux=0;
        for(int i=0;i<n;i++){
            for(int j=aux;j<m;j++){
                matriz[i][j] = 1;
                //System.out.print(matriz[i][j]+" ");                
            }
            aux++;
            m--;
            System.out.println();
        }
        m = (n*2)-1;
        for(int i=0;i<n;i++){
            for(int j=0;j<m;j++){
                System.out.print(matriz[i][j]+" ");

            }
            System.out.println();
        }

    }
}