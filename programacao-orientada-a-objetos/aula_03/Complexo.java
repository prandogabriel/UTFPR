public class Complexo{
    private double real;
    private double imaginaria;

    public Complexo(){

    }
    public Complexo(double x, double y){
        real = x;
        imaginaria = y;
    }
    public void inicializaNumero(double r, double i){
        real = r;
        imaginaria = i;
    }
    public void imprimeNumero(){
        System.out.println("("+  real + ")" + "+" +"("+imaginaria +")"+"i" );
    }
    public boolean eIgual(Complexo x){
        if( (real == x.real) &&(imaginaria == x.imaginaria)  ){
            return true;
        }
        return false;
    }
    public void somaNumeros(Complexo a, Complexo b){
        double aux1, aux2;
        aux1 = a.real+b.real;
        aux2 = a.imaginaria+b.imaginaria; 
        System.out.println("("+  aux1 + ")" + "+" +"("+aux2 +")"+"i" );
    }
    public void subtraiNumeros(Complexo a, Complexo b){
        double aux1, aux2;
        aux1 = a.real-b.real;
        aux2 = a.imaginaria-b.imaginaria; 
        System.out.println("("+  aux1 + ")" + "+" +"("+aux2 +")"+"i" );
    }   
    //(a+bi)∗(c+di)=(ac−bd)+(ad+bc)i;
    public void multiplica(Complexo a, Complexo b){
        double aux1 = (a.real*b.real)-(a.imaginaria*b.imaginaria);
        double aux2 = (a.real*b.imaginaria)+(a.imaginaria*b.real);
        System.out.println("("+  aux1 + ")" + "+" +"("+aux2 +")"+"i" );
    }
    public void divide(Complexo a, Complexo b){
        double aux1 = ((a.real*b.real)+(a.imaginaria*b.imaginaria))/((b.real*b.real)+(b.imaginaria*b.imaginaria));
        double aux2 = (((a.imaginaria*b.real)-(a.real*b.imaginaria))/((b.real*b.real)+(b.imaginaria*b.imaginaria)));
        System.out.println("("+  aux1 + ")" + "+" +"("+aux2 +")"+"i" );

    } 
}