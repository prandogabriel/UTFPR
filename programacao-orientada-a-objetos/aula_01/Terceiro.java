public class Terceiro{

    public static void main(String[] args){
        String texto = JOptionPane.showInputDialog("Entre com um numero");
        int numero = Integer.parseInt(texto); //convertendo String para int
        JOptionPane.showMessageDialog(null, "O numero digitado foi: \n"+ numero,"Este eh o titulo",0);
    }
}