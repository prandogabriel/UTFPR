public class ContaBancaria2 {
    private String nomeCorrentista;
    private double saldo;

 public ContaBancaria2(String n, double s)
 {
  nomeCorrentista = n;
  saldo = s;
 }

    public double getSaldo() {
        return saldo;
    }

    public String getNome() {
        return nomeCorrentista;
    }

    public void deposita(double quantia) {
        saldo = saldo + quantia;
    }

    public void retira(double quantia) {
        if (quantia <= saldo)
            saldo = saldo - quantia;
    }

    // Melhorias...
    public void transfereDe(ContaBancaria2 de, double quanto) {
        deposita(quanto); // Ma ideia. Por que?
        de.retira(quanto);
    }

    public String toString() {
        return "Conta de " + nomeCorrentista + " tem saldo " + saldo;
    }
}