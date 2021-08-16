public class UsaContaBancaria
{
 public static void main(String[] args)
 {
  ContaBancaria fred = new ContaBancaria("Fred",1000);
  ContaBancaria richard = new ContaBancaria("Richard",2000);
  richard.retira(500);
  fred.deposita(500); // ok
  richard.saldo = 1000000; // Erro de compilação!
 }
}