public class CarroNaoIdeal{

// atributo s
public float velocidade ;
// metodos

public void definirVelocidade (float v ){
if (v <= 200)
{ velocidade = v ;}
else velocidade = 0;
}
public void acelerar (float v ){
	
// o car ro so ’ pode a t i n g i r 200km/h
if ( ( velocidade + v ) <= 200){ velocidade += v ;}
else velocidade = 200;
}
}