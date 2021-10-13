#include <msp430.h> 


#define LED_VM      BIT0
#define LED_VD      BIT6


void ini_P1_P2(void);

char pisca = 0;

/**
 * main.c
 */
void main(void)
{
    unsigned long i = 0;

	WDTCTL = WDTPW | WDTHOLD;	// stop watchdog timer
	
	__enable_interrupt(); // Faz com que CPU aceite requisicoes de INT

	ini_P1_P2();


	do{ // Loop infinito
	    if(pisca > 0){
	        P1OUT ^= LED_VD;
	        for(i=0;i<100000;i++);
	    }
	}while(1);

}
//-------------------------------------------------


#pragma vector=PORT1_VECTOR
__interrupt void RTI_da_Porta_1(void){

    // Passo 1: Limpar a flag de int de P1.3
    P1IFG &= ~BIT3;

    if(pisca > 0){
        pisca = 0;  // Desliga
        P1OUT &= ~LED_VM; // Apaga LED VM
        P1OUT &= ~LED_VD; // Apaga LED VD
    }else{
        pisca = 1;  // Liga
        P1OUT |= LED_VM; // Acende LED VM
    }
}



void ini_P1_P2(void){

    /* Inicializacao da Porta 1
     *
     *  P1.3: S2 - entrada, resistor interno de pull-up,
     *             com interrupcao habilitada por bordad de
     *             descida.
     *
     *      Vcc
     *      ---
     *       |
     *       -
     *      | | Res. Pull-up
     *      | |
     *       -
     *       |
     *     --*----> P1.3
     *    |  |
     *    _  |
     *  C _   \ S2
     *    |  |
     *    |  |
     *     |
     *    ---
     *     -
     *
     *  P1.0 - Led vermelho - saida em nivel baixo
     *  P1.6 - Led verde - saida em nivel baixo
     *  P1.X - N.C. - saidas em nivel baixo
     *
     *  P1DIR = BIT0 + BIT1 + BIT2 + BIT4 + BIT5 + BIT6 + BIT7;
     *  P1DIR = ~BIT3;
     *
     *
     *  P1REN = BIT3;  // Conecta resistor em P1.3
     *
     *  P1OUT = BIT3;  // Todas as saidas em nivel baixo e
     *                 // Resistor de P1.3 com funcao Pull-up
     *
     *  P1IES - Seleciona a borda de interrupcao
     *             * Se for 0 -> detecta borda de subida
     *             * Se for 1 -> detecta borda de descida
     *
     *     P1IES = BIT3;
     *
     *  P1IFG - Flags de int. da P1
     *             * Eh necessario limpar as flags de int.
     *               antes te habilitar qualquer interrupcao.
     *
     *     P1IFG = 0;
     *
     *  P1IE - Habilita a geracao de int. pelas entradas da P1
     *
     *     P1IE = BIT3;
     *
     */
    P1DIR = ~BIT3;
    P1REN = BIT3;
    P1OUT = BIT3;
    P1IES = BIT3;
    P1IFG = 0;
    P1IE = BIT3;

    /* Inicializacao da Porta 2
     *
     * P2.X: N.C. - saidas em nivel baixo
     * Pinos 18 e 19: N.C. - saida em nivel baixo
     *
     *  - Para mudar dos pinos 18 e 19
     *
     *       P2SEL &= ~(BIT6 + BIT7);
     *       ou P2SEL &= ~BIT6;
     *       ou P2SEL = 0;
     *
     *  P2DIR = 0xFF;
     *  P2OUT = 0;
     */
    P2SEL = 0;
    P2DIR = 0xFF;
    P2OUT = 0;
}




