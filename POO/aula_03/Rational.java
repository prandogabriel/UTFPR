public class Rational{
    
    private int numerador;
    private int denominador;

    public Rational(){

    }

    /**
     * Construtor que recebe os valores iniciais para um numero fracionário, e o reduz, testando se o resto de suas divisões por 2,3,5,7 são igual a 0
     * @param nume numerador da fração
     * @param deno denominador da fração
     */
    public Rational(int nume,int deno ){
        while(nume%2==0 && deno%2==0 || nume%3==0 && deno%3==0 ||nume%5==0 && deno%5==0 || nume%7==0 && deno%7==0){
                
            if(nume%2==0 && deno%2==0){
                numerador=nume/2;
                denominador=deno/2;
                nume=nume/2;
                deno=deno/2;
            }
            if(nume%3==0 && deno%3==0){
                numerador=nume/3;
                denominador=deno/3;
                nume=nume/3;
                deno=deno/3;
            }
            if(nume%5==0 && deno%5==0){
                numerador=nume/5;
                denominador=deno/5;
                nume=nume/5;
                deno=deno/5;
            }
            if(nume%7==0 && deno%7==0){
                numerador=nume/7;
                denominador=deno/7;
                nume=nume/7;
                deno=deno/7;
            }
        }
    }
    // public Rational(int n, int d){
    //     denominador=d;
    //     numerador=n;
    // }

    /**
     * se o numerador e o denominador forem iguais, quer dizer que o numero é igual a 1, todos os metodos tem esta condição
     * @param x recebe um numero racional para mostrálo
     */
    public void mostrar(Rational x){
        if(x.denominador==x.numerador) System.out.println(1);
        else System.out.println(x.numerador+"/"+x.denominador);
        
    }
    
    public void somar(Rational x, Rational y){
        int auxNume;
        int auxDeno;
        auxDeno = x.denominador*y.denominador;
        auxNume = ((x.denominador*y.numerador)+(x.numerador*y.denominador));
        if(x.denominador==x.numerador) System.out.println(1);
        else System.out.println(auxNume+"/"+auxDeno);
    }

    public void subtrair(Rational x, Rational y){
        int auxNume;
        int auxDeno;
        auxDeno = x.denominador*y.denominador;
        auxNume = ((x.denominador*y.numerador)-(x.numerador*y.denominador));
        if(x.denominador==x.numerador) System.out.println(1);
        else System.out.println(auxNume+"/"+auxDeno);
    }

    public void multiplicar(Rational x, Rational y){
        if(x.denominador==x.numerador) System.out.println(1);
        else System.out.println(x.numerador*y.numerador+"/"+x.denominador*y.denominador);
    }

    public void dividir(Rational x, Rational y){
        if(x.denominador==x.numerador) System.out.println(1);
        else System.out.println(x.numerador*y.denominador+"/"+x.denominador*y.numerador);
    }
    public void mostrarFloat(int num){
        float aux = (float)numerador/(float)denominador;
        System.out.printf("%."+num+"f",aux);
    }

}