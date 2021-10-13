#include <msp430.h> 



void ini_P1_P2(void);


void main(void)
{
    unsigned long  i = 0;

	WDTCTL = WDTPW | WDTHOLD;	// stop watchdog timer
	
	ini_P1_P2();

	do{
	    // Loop infinito
	    for(i=0;i<100000;i++){  // delay por SW

	    }

	    P1OUT ^= BIT0;

	}while(1);
}
//--------------------------------------------------------------


void ini_P1_P2(void){
    /* Funcao de inicializacao das Portas 1 e 2 */

    /* Inicializacao da PORTA 1
     *      - P1.0 - LED VM: saida em nivel baixo
     *      - P1.1-P1.7: N.C. - saidas em nivel baixo
     *
     * Configuracao de REGISTRADORES
     *      - P1DIR - config. os pinos como E/S
     *
     *          bit             7  6  5  4   3  2  1  0
     *          P1DIR ini.      0  0  0  0   0  0  0  0
     *          P1DIR desejado  1  1  1  1   1  1  1  1
     *                        ---------------------------
     *               Decimal              255
     *               Hex       0x    F            F
     *
     *               BIT0      0 0 0 0   0 0 0 1
     *               BIT1      0 0 0 0   0 0 1 0
     *               BIT2      0 0 0 0   0 1 0 0
     *               BIT3      0 0 0 0   1 0 0 0
     *               BIT4      0 0 0 1   0 0 0 0
     *               BIT5      0 0 1 0   0 0 0 0
     *               BIT6      0 1 0 0   0 0 0 0
     *               BIT7      1 0 0 0   0 0 0 0
     *                        -------------------
     *   BIT0|BIT1|...BIT7 ->  1 1 1 1   1 1 1 1  -> BIT0 + BIT1 +...
     *
     *
     *
     *          P1DIR = 255;  ou P1DIR = 0xFF; ou
     *          P1DIR = BIT0 + BIT1 + BIT2 + BIT3 + BIT4 + BIT5 + BIT6 + BIT7;
     *
     *
     *      - P1OUT - config. o estado das saidas
     *
     *          bit             7  6  5  4   3  2  1  0
     *          P1OUT ini.      0  0  0  0   0  0  0  0
     *          P1OUT desejado  0  0  0  0   0  0  0  0
     *                        ---------------------------
     *               Decimal               0
     *               Hex       0x    0            0
     *
     *          P1OUT = 0;  ou  P1OUT = 0x00;
     *
     *
     */
    P1DIR = BIT0 + BIT1 + BIT2 + BIT3 + BIT4 + BIT5 + BIT6 + BIT7;
    P1OUT = 0;


    /* Inicializacao da PORTA 2
     *      - P2.0-P2.5: N.C. - saidas em nivel baixo
     *      - Pinos 18 e 19: Mudar funcao de XIN/XOUT para P2.6 e P2.7,
     *                       como nao serao usado, entao serao saidas em
     *                       nivel baixo.
     *
     * Configuracao de REGISTRADORES
     *      - P2DIR - config. os pinos como E/S
     *
     *          bit             7  6  5  4   3  2  1  0
     *          P2DIR ini.      0  0  0  0   0  0  0  0
     *          P2DIR desejado  1  1  1  1   1  1  1  1
     *                        ---------------------------
     *               Decimal              255
     *               Hex       0x    F            F
     *
     *          P2DIR = 255;  ou P2DIR = 0xFF; ou
     *          P2DIR = BIT0 + BIT1 + BIT2 + BIT3 + BIT4 + BIT5 + BIT6 + BIT7;
     *
     *
     *      - P2OUT - config. o estado das saidas
     *
     *          bit             7  6  5  4   3  2  1  0
     *          P2OUT ini.      0  0  0  0   0  0  0  0
     *          P2OUT desejado  0  0  0  0   0  0  0  0
     *                        ---------------------------
     *               Decimal               0
     *               Hex       0x    0            0
     *
     *          P2OUT = 0;  ou  P2OUT = 0x00;
     *
     *      - P2SEL -> Mudar a funcao dos pinos 18 e 19
     *
     *          bit             7  6  5  4   3  2  1  0
     *          P2SEL ini.      1  1  0  0   0  0  0  0
     *          P2SEL desejado  0  0  0  0   0  0  0  0
     *                        ---------------------------
     *               Decimal               0
     *               Hex       0x    0            0
     *
     *          P2SEL = 0;  ou  P2SEL = 0x00;
     *          P2SEL &= ~(BIT6 + BIT7);
     *
     */
    P2DIR = BIT0 + BIT1 + BIT2 + BIT3 + BIT4 + BIT5 + BIT6 + BIT7;
    P2OUT = 0;
    P2SEL &= ~(BIT6 + BIT7);
}

