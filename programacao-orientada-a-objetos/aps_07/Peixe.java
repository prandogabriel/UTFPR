public class Peixe extends Animal {

    private int n_nadadeiras;

    public Peixe(String cor, int idade, int n_nadadeiras) {
        super(cor, idade);
        this.n_nadadeiras = n_nadadeiras;
    }

    public void som() {
        System.out.println("eu não faço som ");
    }

    public void nadar() {
        System.out.println("Estou nadando");
    }

}