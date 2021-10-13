#include <msp430.h> 


void ini_P1_P2(void);



void main(void)
{
    unsigned long i = 0;

	WDTCTL = WDTPW | WDTHOLD;	// stop watchdog timer
	
	ini_P1_P2();

	do{
	    // Loop infinito

	    for(i=0;i<100000;i++);
	    P1OUT ^= BIT0;   // P1OUT = P1OUT ^ BIT0;

	}while(1);
}
//-----------------------------------------------



void ini_P1_P2(void){
    /* --> Funcao de inicializacao das Portas 1 e 2
     *
     * INI. DA PORTA 1
     *      - P1.0 - LED VM - Saida em nivel baixo(led apagado)
     *      - P1.1 - P1.7: N.C. - saidas em nivel baixo
     *
     *  1 - Reg. P1DIR - Direcao dos pinos (entrada/saida)
     *
     *        bit          7  6  5  4  3  2  1  0
     *        P1DIR ini.   0  0  0  0  0  0  0  0 -> entradas dig.
     *        P1DIR desej. 1  1  1  1  1  1  1  1
     *                   |-----------|-------------|
     *           HEX  0x       F            F
     *           Decimal           255
     *
     *        P1DIR = 0xFF; ou P1DIR = 255; ou P1DIR = 0b11111111;
     *        P1DIR = BIT0 + BIT1 + BIT2 + BIT3 + BIT4 + BIT5 + BIT6 + BIT7;
     *
     *  2 - P1OUT - Estado das saidas da P1
     *
     *        bit          7  6  5  4  3  2  1  0
     *        P1OUT ini.   0  0  0  0  0  0  0  0 -> nivel baixo
     *        P1OUT desej. 0  0  0  0  0  0  0  0
     *                   |-----------|-------------|
     *           HEX  0x       0            0
     *           Decimal           0
     *
     *        P1OUT = 0x00;  ou P1OUT = 0;
     *
     */
    P1DIR = 0xFF;
    P1OUT = 0;

     /* INI. DA PORTA 2
     *      - P2.0 - P2.5: N.C. - saidas em nivel baixo
     *      - Pinos 18 e 19: mudar funcao de XIN/XOUT para P2.6/P2.7
     *
    *  1 - Reg. P2DIR - Direcao dos pinos (entrada/saida)
     *
     *        bit          7  6  5  4  3  2  1  0
     *        P2DIR ini.   0  0  0  0  0  0  0  0 -> entradas dig.
     *        P2DIR desej. 1  1  1  1  1  1  1  1
     *                   |-----------|-------------|
     *           HEX  0x       F            F
     *           Decimal           255
     *
     *        P2DIR = 0xFF; ou P2DIR = 255; ou P2DIR = 0b11111111;
     *        P2DIR = BIT0 + BIT1 + BIT2 + BIT3 + BIT4 + BIT5 + BIT6 + BIT7;
     *
     *  2 - P2OUT - Estado das saidas da P2
     *
     *        bit          7  6  5  4  3  2  1  0
     *        P2OUT ini.   0  0  0  0  0  0  0  0 -> nivel baixo
     *        P2OUT desej. 0  0  0  0  0  0  0  0
     *                   |-----------|-------------|
     *           HEX  0x       0            0
     *           Decimal           0
     *
     *        P2OUT = 0x00;  ou P2OUT = 0;
     *
     *  3 - Mudar funcao dos pinos 18 e 19
     *
     *        bit          7  6  5  4  3  2  1  0
     *        P2SEL ini.   1  1  0  0  0  0  0  0 -> nivel baixo
     *        P2SEL desej. 0  0  0  0  0  0  0  0
     *                   |-----------|-------------|
     *           HEX  0x       0            0
     *           Decimal           0
     *
     *        P2SEL = 0x00;  ou P2SEL = 0;
     *        P2SEL &= ~(BIT6 + BIT7);
     *
     */
    P2DIR = 0xFF;
    P2OUT = 0;
    P2SEL &= ~(BIT6 + BIT7);

}








