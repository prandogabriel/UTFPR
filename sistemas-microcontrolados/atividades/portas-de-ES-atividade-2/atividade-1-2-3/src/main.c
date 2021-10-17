// Em um sistema microcontrolado (MSP430G2553) as Portas 1 e 2 são utilizadas,
// conforme o circuito representado abaixo. Considere que as entradas
// correspondentes a S1, S3, S4 e S6 precisam ter interrupção habilitada e que
//  os LEDs devem ficar inicialmente apagados. Mudar a função digital
// dos pinos 11, 18 e 19.

// S1 -> P1 BIT 0
// S3 -> P1 BIT 2
// S4 -> P2 BIT 0
// S6 -> P2 BIT 2

#include <msp430.h>

void init_ports(void);

void main(void)
{
  init_ports();
}
//-----------------------------------------------

void init_ports(void)
{

  // --------------
  P1DIR = BIT3 + BIT4 + BIT5 + BIT6 + BIT7; // DEFINE LED 1 E 2 COMO SAIDA DIGITAL
  P1OUT = 0;                                // VALOR INICIAL
  P1REN = BIT0 + BIT1 + BIT2;               // HABILITAR RESISTOR PARA S1, S2, S3
  P1IE = BIT0 + BIT2;                       // INTERUPÇÃO HABILIDATA PARA S1, S3.
  P1IES = 0;
  P1IFG = 0;

  //  ----------
  P2DIR = BIT1 + BIT4 + BIT6 + BIT7; // SETA LED 3 E 4 COMO SAIDA
  P2OUT = 0;
  P2REN = BIT0 + BIT2 + BIT5; // HABILITAR RESISTOR PARA S4, S5, S6
  P2IE = BIT0 + BIT5;         // HABILITAR INTERRUPÇÕES PARA S4 E S6;
  P2SEL = BIT3;
  P2SEL2 = 0;
  P2IES = BIT0 + BIT2;

  //  ----------
  P1OUT |= BIT3 + BIT7;
  P2OUT |= BIT1;
  P2OUT &= ~BIT6;
}
