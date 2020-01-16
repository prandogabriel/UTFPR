public class Mamifero extends Animal {

    private int n_tetas;
    private String som;
    private int n_patas;

    public Mamifero(String cor, String som, int idade, int n_patas, int n_tetas) {
        super(cor, idade);
        this.n_patas = n_patas;
        this.n_tetas = n_tetas;
        this.som = som;

    }

    public void som() {
        System.out.println("eu faço o som " + som);
    }

    public void mamar() {
        System.out.println("Estou mamando");
    }

}