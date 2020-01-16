public class Animal {

    private String cor;
    private int idade;

    public Animal(String cor, int idade) {
        this.cor = cor;
        this.idade = idade;
    }

    public void comer() {
        System.out.println("Estou comendo");
    }

    public String toString() {
        return "sou um animal da  " + cor + " e idade " + idade;
    }
}