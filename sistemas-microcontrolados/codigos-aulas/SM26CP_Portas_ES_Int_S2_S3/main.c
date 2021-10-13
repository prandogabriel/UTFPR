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

    WDTCTL = WDTPW | WDTHOLD;   // stop watchdog timer

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

    /* Passo 1: Ler as flags e idendificar quem
    *          provocou a int.
    *
    *        bit            7 6 5 4   3 2 1 0
    *        P1IFG          Y X X X   Y X X X
    *        (BIT3 + BIT7)  1 0 0 0   1 0 0 0
    *            AND b-a-b  ------------------
    *                       Y 0 0 0   Y 0 0 0
    *
    *        switch(P1IFG & (BIT3 + BIT7) ){
    *
    *        }
    *
    */

    switch(P1IFG & (BIT3 + BIT7) ){
        case BIT3:  // A chave S2 foi pressionada
            // LIGA PISCA
            P1IFG &= ~BIT3;
            pisca = 1;  // Liga
            P1OUT |= LED_VM; // Acende LED VM
        break;

        case BIT7: // A chave S3 foi pressionada
            // DESLIGA PISCA
            P1IFG &= ~BIT7;
            pisca = 0;  // Desliga
            P1OUT &= ~LED_VM; // Apaga LED VM
            P1OUT &= ~LED_VD; // Apaga LED VD
        break;

        default:
            P1IFG &= ~(BIT3 + BIT7);
        break;
    }
}



void ini_P1_P2(void){

    /* Inicializacao da Porta 1
     *
     *  P1.3: S2 - entrada, resistor interno de pull-up,
     *             com interrupcao habilitada por bordad de
     *             descida.
     *
     *  P1.7: S3 - entrada, resistor interno de pull-up,
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
     *          -------> P1.7
     *         |
     *          \ S3
     *         |
     *        ---
     *         -
     *
     *
     *
     *  P1.0 - Led vermelho - saida em nivel baixo
     *  P1.6 - Led verde - saida em nivel baixo
     *  P1.X - N.C. - saidas em nivel baixo
     *
     *  P1DIR = BIT0 + BIT1 + BIT2 + BIT4 + BIT5 + BIT6;
     *  P1DIR = ~(BIT3 + BIT7);
     *
     *
     *  P1REN = BIT3 + BIT7;  // Conecta resistor em P1.3 e P1.7
     *
     *  P1OUT = BIT3 + BIT7;  // Todas as saidas em nivel baixo e
     *                        // Resistores de P1.3 eP1.7 com funcao Pull-up
     *
     *  P1IES - Seleciona a borda de interrupcao
     *             * Se for 0 -> detecta borda de subida
     *             * Se for 1 -> detecta borda de descida
     *
     *     P1IES = BIT3 + BIT7;
     *
     *  P1IFG - Flags de int. da P1
     *             * Eh necessario limpar as flags de int.
     *               antes te habilitar qualquer interrupcao.
     *
     *     P1IFG = 0;
     *
     *  P1IE - Habilita a geracao de int. pelas entradas da P1
     *
     *     P1IE = BIT3 + BIT7;
     *
     */
    P1DIR = ~(BIT3 + BIT7);


    P1REN = BIT3 + BIT7;
    P1OUT = BIT3 + BIT7;
    P1IES = BIT3 + BIT7;
    P1IFG = 0;
    P1IE = BIT3 + BIT7;

    /* Inicializacao da Porta 2
     *
     * P2.X: N.C. - saidas em nivel baixo
     * Pinos 18 e 19: N.C. - saidas em nivel baixo
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




