public class ContaBancaria
{
	 private String nomeCorrentista;
	 private double saldo;
	 
	 public ContaBancaria(String n, double s)
	 {
		  nomeCorrentista = n;
		  saldo = s;
	 }
	 public double getSaldo() { return saldo; }
	 public String getNome() { return nomeCorrentista; }
	 public void deposita(double quantia)
	 {
		saldo = saldo + quantia;
	 }
	 public void retira(double quantia)
	 {
	  if (quantia <= saldo) saldo = saldo - quantia;
	 }
}