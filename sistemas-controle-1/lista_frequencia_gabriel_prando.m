% 1.A
s = tf('s');
G = 1/(s*(s+2)*(s+4));
figure;
bode(G);
grid on;

% 1.B
G = ((s+5)/((s+2)*(s+4)));
figure;
bode(G);
grid on;

% 1.C
G = ((s+3) *(s+5))/(s*(s+2) *(s+4));
figure;
bode(G) ;
grid on;

% 2.A
G = 50/(s*(s+3) *(s+6));
GMF = feedback(G,1);
figure;
nyquist (GMF);

% 2.B
G = 50/(s^2*(s+1));
Gmf = feedback(G,1/s);
figure;
nyquist (Gmf) ;

% 2.C
G = 20/(s*(s+1));
H = (s+3)/(s+4);
Gmf = feedback(G,H);
figure;
nyquist (Gmf) ;

% 2.D
G = 100*(s+5) /(s*(s+3) *(s^2+4));
Gmf = feedback(G,1);

figure;

nyquist(Gmf);

% 3.A
Ka = 1000;
G1 = 50/(s*(s+3) *(s+6));
Gmfa = feedback(Ka*G1,1);

G2 = 50/(s^2*(s+1));

Gmfb = feedback(Ka*G2,1/s);

G3 = 20/(s*(s+1));

H = (5+3)/(s+4);

Gmfc = feedback(Ka*G3,H);

G4 = 100*(s+5) /(s*(s+3) * (s^2+4));
Gmfd = feedback(Ka*G4,1);

figure;

bode (Gmfa) ;

legend('Sistema a');

grid on;

figure;

bode (Gmfb) ;

legend('Sistema b');

grid on;

figure;

bode (Gmfc) ;

legend('Sistema c');

grid on;

figure;

bode (Gmfd) ;

legend('Sistema d');

grid on;


% 3.B
Kb = 100;

G1 = 50/(s*(s+3) *(s+6));

Gmfa = feedback(Kb*G1,1);

G2 = 50/(s^2*(s+1));

Gmfb = feedback(Kb*G2,1/s);

G3 = 20/(s*(s+1));

H = (s+3)/(s+4);

Gmfc = feedback(Kb*G3,H);

G4 = 100*(s+5) /(s*(s+3) * (s^2+4));

Gmfd = feedback(Kb*G4,1);

figure;

bode (Gmfa) ;

legend('Sistema a');

grid on;

figure;

bode (Gmfb) ;

legend('Sistema b');

grid on;

figure;

bode (Gmfc) ;

legend('Sistema c');

grid on;

figure;

bode (Gmfd) ;
legend('Sistema d');

grid on;


% 3.C
Kc = 0.1;
G1 = 50/(s*(s+3) *(s+6));
Gmfa = feedback(Kc*G1,1);

G2 = 50/(s^2*(s+1));

Gmfb = feedback(Kc*G2,1/s);

G3 = 20/(s*(s+1));

H = (s+3)/(s+4);

Gmfc = feedback(Kc*G3,H);

G4 = 100*(s+5) /(s* (s+3) *(s^2+4));

Gmfd = feedback(Kc*G4,1);

figure;

bode (Gmfa) ;

legend('Sistema a')

grid on;

figure;

bode (Gmfb) ;

legend('Sistema b')

grid on;

figure;

bode (Gmfc) ;

legend('Sistema c')

grid on;

figure;

bode (Gmfd) ;

legend('Sistema d');

grid on;


% 5

%5.A
k=14.313;

G = ((s+5)/(s*(s+3) *(s^2+4*s+16)));
Gmf = feedback(k*G,1);
figure;

bode (G) ;
legend('G(s)');

grid on;

figure;

bode (Gmf) ;
legend('GM£ComGanho')
grid on;


s = tf('s');

k=14.313;

G= ((s+5)/(s*(s+3) *(s^2+4*s+16)));
Gmf = feedback(G,1);
figure;

step (Gmf) ;
legend('GmfSemGanho');
GmfG = feedback(k*G,1);
figure;

step (GmfG) ;
legend('GmfComGanho');

