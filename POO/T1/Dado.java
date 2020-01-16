import java.io.Serializable;
// classe Random para rolar os dados de forma aleatória
import java.util.Random;

public class Dado implements Serializable {

    private int sideUp;

    public Dado() {
        Random x = new Random();
        int aux = 1 + x.nextInt(6);
        sideUp = aux;
    }

    /**
     * 
     * @return face superior do dado
     */
    public int getSideUp() {
        return sideUp;
    }

    /**
     * @return rola o dado e retorna seu valor
     */
    public void roll() {
        // obtendo numeros pseudo-aleatorios entre 1 a 6
        Random x = new Random();
        int aux = 1 + x.nextInt(6);
        sideUp = aux;
    }

    public String toString() {
        return "valor do dado " + sideUp;
    }
}