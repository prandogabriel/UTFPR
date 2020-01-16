public class demoComplexo{
    public static void main (String [] args){
        Complexo a = new Complexo(2,4);
        Complexo b = new Complexo(5,7);
        
        a.imprimeNumero();
        b.imprimeNumero();
        a.eIgual(a);
        a.somaNumeros(a,b);
        a.subtraiNumeros(a,b);
        a.multiplica(a,b);
        a.divide(a,b);
    }
}