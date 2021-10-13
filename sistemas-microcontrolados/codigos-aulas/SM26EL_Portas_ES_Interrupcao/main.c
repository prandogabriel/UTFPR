/* Uso de Interrupcao das Portas
 *
 *    -> A chave S2 (P1.3) ao ser pressionada provocara
 *    interrupcao pela Porta 1.
 *        - Quando pressionada, liga led VM (P1.0) e faz
 *        led VD (P1.6) piscar. Se pressionar novamente,
 *        apaga led VM e VD para de piscar.
 *
 *
 */
#include <msp430.h> 

#define     LED_VM      BIT0
#define     LED_VD      BIT6

void ini_P1_P2(void);


char ctrl_pisca_led = 0;



void main(void)
{
    unsigned long i = 0;

	WDTCTL = WDTPW | WDTHOLD;	// stop watchdog timer
	
	__enable_interrupt(); // Habilita a CPU a aceitar req. de int.


	ini_P1_P2();

	do{  // Neste loop sera colocada uma rotina para o LED piscar

	    if(ctrl_pisca_led == 1){
	        P1OUT ^= LED_VD; // = BIT6;
	        for(i=0; i<100000;i++){
	            // Delay de aprox. 1s
	        }
	    }

	}while(1);

}
//---------------------------------------------------------------


// RTI da Porta 1
#pragma vector=PORT1_VECTOR
__interrupt void RTI_da_Porta_1(void){

    // 1 - Limpar flag de int. de P1.3

    P1IFG &= ~BIT3;

    // 2 - Realizar a acao de S2: liga/desliga pisca led

    if(ctrl_pisca_led == 1){
        ctrl_pisca_led = 0;
        P1OUT &= ~LED_VM;
        P1OUT &= ~LED_VD;
    }else{
        ctrl_pisca_led = 1;
        P1OUT |= LED_VM;
    }

}




void ini_P1_P2(void){

    /* Inicializacao da P1
     *
     *    P1.0 - LED VM - saida em nivel baixo
     *    P1.6 - LED VD - saida em nivel baixo
     *    P1.3 - chave S2 - entrada com resistor de pull-up
     *    P1.X - N.C. - saidas em nivel baixo
     *
     *
     *          -------> P1.3
     *         |
     *          \ S2
     *         |
     *        ---
     *         -
     *
     *
     *
     *    1 - P1DIR - define se os pinos serao entradas ou saidas
     *
     *                  bit         7 6 5 4    3 2 1 0
     *         P1DIR inicial        0 0 0 0    0 0 0 0
     *         P1DIR desejado       1 1 1 1    0 1 1 1
     *                           ---------------------------
     *                   HEX   0x     F          7
     *
     *         P1DIR = 0xF7;
     *         P1DIR = ~BIT3;
     *         P1DIR = BIT0 + BIT1 + BIT2 + BIT4 + BIT5 + BIT6 + BIT7;
     *
     *
     *    2 - P1REN - Conecta resistor na entrada
     *
     *                  bit         7 6 5 4    3 2 1 0
     *         P1REN inicial        0 0 0 0    0 0 0 0
     *         P1REN desejado       0 0 0 0    1 0 0 0
     *                           ---------------------------
     *                   HEX   0x     0            8
     *
     *         P1REN = 0x08;
     *         P1REN = BIT3;
     *
     *
     *    3 - P1OUT - Define o estado das saidas/Funcao resistor
     *
     *                                          ---> Funcao resistor
     *                                         |
     *                  bit         7 6 5 4    3 2 1 0
     *         P1OUT inicial        0 0 0 0    0 0 0 0
     *         P1OUT desejado       0 0 0 0    1 0 0 0
     *                           ---------------------------
     *                   HEX   0x     0            8
     *
     *         P1OUT = 0x08;
     *         P1OUT = BIT3;
     *
     *    4 - P1IES - Seleciona borda de interrupcao
     *           * Se o bit for setado (1) -> detecta borda de descida;
     *           * Se o bit for limpo  (0) -> detecta borda de subida
     *
     *                  bit         7 6 5 4    3 2 1 0
     *         P1IES inicial        0 0 0 0    0 0 0 0
     *         P1IES desejado       0 0 0 0    1 0 0 0
     *                           ---------------------------
     *                   HEX   0x     0            8
     *
     *         P1IES = 0x08;
     *         P1IES = BIT3;
     *
     *    5 - P1IFG - Flags de Int. da P1
     *          * Antes de habilitar a Int. deve-se limpar as flags
     *
     *         P1IFG = 0;
     *
     *
     *    6 - P1IE - Habilita interrupcao da P1
     *
     *                  bit         7 6 5 4    3 2 1 0
     *         P1IE  inicial        0 0 0 0    0 0 0 0
     *         P1IE  desejado       0 0 0 0    1 0 0 0
     *                           ---------------------------
     *                   HEX   0x     0            8
     *
     *         P1IE  = 0x08;
     *         P1IE  = BIT3;
     *
     */
    P1DIR = ~BIT3;
    P1REN = BIT3;
    P1OUT = BIT3;
    P1IES = BIT3;
    P1IFG = 0;
    P1IE  = BIT3;

    /* Inicializacao do Porta 2
     *
     *    P2.X - N.C. - Todos como saida em nivel baixo
     *
     *    P2DIR = 0xFF;
     *    P2OUT = 0;
     *    P2SEL &= ~(BIT6 + BIT7); // Muda funcao de pinos 18 e 19
     *
     *
     *
     */
    P2DIR = 0xFF;
    P2OUT = 0;
    P2SEL &= ~(BIT6 + BIT7); // Muda funcao de pinos 18 e 19

}












