import java.util.Random;

public class Usa{
    public static void main(String[] args) {
        
        Mamifero m[10] ;

        Random x = new Random( ) ;
        for(int i=0;i<10;i++){
            
            if(x.nextInt(4) == 0)
                m[i]= new Mamifero("preto","auauau" , 2, 4, 8);

        }


        // Mamifero m = new Mamifero("marrom","auauau", 10, 4, 8);
        // Ave a = new Ave("preto","quack quack", 1, 2, 30);
        // Peixe p = new Peixe("branco", 3, 4);

    }
}