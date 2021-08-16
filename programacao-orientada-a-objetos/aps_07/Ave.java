public class Ave extends Animal {

    private float bico;
    private int n_patas;
    private String som;

    public Ave(String cor, String som, int idade, int n_patas, float bico) {
        super(cor, idade);
        this.n_patas = n_patas;
        this.bico = bico;
        this.som = som;

    }

    public void som() {
        System.out.println("eu faço o som " + som);
    }

    public void mamar() {
        System.out.println("Estou mamando");
    }

}