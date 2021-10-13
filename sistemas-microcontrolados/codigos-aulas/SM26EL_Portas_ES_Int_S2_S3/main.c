/* Uso de Interrupcao das Portas
 *
 *    -> A chave S2 (P1.3) ao ser pressionada provocara
 *    interrupcao pela Porta 1.
 *        - Ligar Pisca LED
 *    -> A chave S3 (P1.7) ao ser...:
 *        - Desligar Pisca LED
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

    WDTCTL = WDTPW | WDTHOLD;   // stop watchdog timer

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

    // 1 - Ler e Limpar flags de int. de P1.3 e P1.7

    switch( P1IFG & (BIT3 + BIT7) ){
        case BIT3: // Significa que foi a S2
            P1IFG &= ~BIT3;
            ctrl_pisca_led = 1;
            P1OUT |= LED_VM;
        break;
        case BIT7: // Significa que foi a S3
            P1IFG &= ~BIT7;
            ctrl_pisca_led = 0;
            P1OUT &= ~LED_VM;
            P1OUT &= ~LED_VD;
        break;

        default:
            P1IFG &= ~(BIT3 + BIT7);
        break;
    }

}




void ini_P1_P2(void){

    /* Inicializacao da P1
     *
     *    P1.0 - LED VM - saida em nivel baixo
     *    P1.6 - LED VD - saida em nivel baixo
     *    P1.3 - chave S2 - entrada com resistor de pull-up
     *    P1.7 - chave S3 - entrada com resistor de pull-up
     *    P1.X - N.C. - saidas em nivel baixo
     *
     *
     *          -------> P1.3/P1.7
     *         |
     *          \ S2/S3
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
     *         P1DIR desejado       0 1 1 1    0 1 1 1
     *                           ---------------------------
     *                   HEX   0x     7          7
     *
     *         P1DIR = 0x77;
     *         P1DIR = ~(BIT3 + BIT7);
     *         P1DIR = BIT0 + BIT1 + BIT2 + BIT4 + BIT5 + BIT6;
     *
     *
     *    2 - P1REN - Conecta resistor na entrada
     *
     *                  bit         7 6 5 4    3 2 1 0
     *         P1REN inicial        0 0 0 0    0 0 0 0
     *         P1REN desejado       1 0 0 0    1 0 0 0
     *                           ---------------------------
     *                   HEX   0x     8            8
     *
     *         P1REN = 0x88;
     *         P1REN = BIT3 + BIT7;
     *
     *
     *    3 - P1OUT - Define o estado das saidas/Funcao resistor
     *
     *                               --------------> Funcao resistor
     *                              |           |
     *                  bit         7 6 5 4    3 2 1 0
     *         P1OUT inicial        0 0 0 0    0 0 0 0
     *         P1OUT desejado       1 0 0 0    1 0 0 0
     *                           ---------------------------
     *                   HEX   0x     8            8
     *
     *         P1OUT = 0x88;
     *         P1OUT = BIT3 + BIT7;
     *
     *    4 - P1IES - Seleciona borda de interrupcao
     *           * Se o bit for setado (1) -> detecta borda de descida;
     *           * Se o bit for limpo  (0) -> detecta borda de subida
     *
     *                  bit         7 6 5 4    3 2 1 0
     *         P1IES inicial        0 0 0 0    0 0 0 0
     *         P1IES desejado       1 0 0 0    1 0 0 0
     *                           ---------------------------
     *                   HEX   0x     8            8
     *
     *         P1IES = 0x88;
     *         P1IES = BIT3 + BIT7;
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
     *         P1IE  desejado       1 0 0 0    1 0 0 0
     *                           ---------------------------
     *                   HEX   0x     8            8
     *
     *         P1IE  = 0x88;
     *         P1IE  = BIT3 + BIT7;
     *
     */
    P1DIR = BIT0 + BIT1 + BIT2 + BIT4 + BIT5 + BIT6;
    P1REN = BIT3 + BIT7;
    P1OUT = BIT3 + BIT7;
    P1IES = BIT3 + BIT7;
    P1IFG = 0;
    P1IE  = BIT3 + BIT7;

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












