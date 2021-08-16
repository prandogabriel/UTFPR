import java.util.Scanner;
import java.util.Random;
import java.io.Console;

public class Segundo{
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        //Console console = System.console();

        int i = teclado.nextInt();//lendo inteiro
        double r = teclado.nextDouble();// lendo real
        String s = teclado.nextLine(); // lendo cadeia de caracteres
        s = teclado.nextLine();
        System.out.println("inteiro:" + i + ", real: " + r);
        System.out.println("frase : " + s);

        // obtendo numeros pseudo-aleatorios de 0 a 9
        Random x = new Random( ) ;
        int j = x.nextInt (10) ;
	    System.out.println("\nUm numero inteiro pseudo-aleatoria: " + j);
        teclado.close();
	}
    }
