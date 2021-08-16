//Crie uma classe Die com uma variável de instância inteira sideUp. Forneça a ela um construtor, um método getSideUp() que retorne o valor de sideUp e um método void roll() que altere o valor de sideUp para um valor aleatório de 1 a 6. Em seguida, crie uma classe DieDemo com um método principal que gere dois objetos Die, jogue-os e exiba a soma dos dois lados superiores.
import java.util.Random;
public class Die{
    private int sideUp;

    public Die(){
        sideUp = 6;
    }
    public Die(int x){
        sideUp = x;
    }
    public int getSideUp(){
         return sideUp;
    }
    public void roll(){
     // obtendo numeros pseudo-aleatorios de 1 a 6
        Random x = new Random( ) ;
        int aux=1+x.nextInt (6) ;
        sideUp=aux;
    }
    public String toString(){
        return "valor do dado " + sideUp;
    }
}