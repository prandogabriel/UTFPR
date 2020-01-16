public class meuBanco{

    public static void main(String[] args) {
        
        ContaBancaria2[] nubank = new ContaBancaria2[10];

        nubank[0] = new ContaBancaria2("gabriel", 1232);
        nubank[1] = new ContaBancaria2("joao", 4100);
        nubank[2] = new ContaBancaria2("irineu", 5000);

        double total = totaliza(nubank);
        System.out.println(total);
    }
    public static double totaliza(ContaBancaria2[] x){
        double s = 0;

        for(ContaBancaria2 o: x){    
            if(o!=null){           
                s = s + o.getSaldo();
            }
        }
        return s;
    }
}