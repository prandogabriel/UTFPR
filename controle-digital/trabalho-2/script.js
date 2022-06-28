const newEmptyArray = (numberOfElements = 100) => [...Array(numberOfElements)].map((_, i) => i);

const y = new Array(100);
const x = new Array(100);
const points = newEmptyArray();

let value = 5;
// Gerar x[n] = onda quadrada com p erío do de 40 p ontos e amplitude A=5
for (let i = 0; i < x.length; i++) {
  x[i] = value;
  if ((i + 1) % 20 === 0) value *= -1;
}

console.log(x);

// original 10 * y[n + 2] - 8 * y[n + 1] + 3 * y[n] = 4 * x[n + 1] + x[n];
//aplicar (-2n) para deixar dependendo de n ou n do passado -> 10 * y[n] - 8 * y[n - 1] + 3 * y[n - 2] = 4 * x[n - 1] + x[n - 2];
//isolar y[n]  = ((4 * x[n - 1]) + (x[n - 2]) + (8 * y[n - 1]) - (3 * y[n - 2])) / 10;


// // y[n]  = ((4 * x[n - 1]) + (x[n - 2]) + (8 * y[n - 1]) - (3 * y[n - 2])) / 10;
y[0] = x[0];
y[1] = x[1];


for (let n = 2; n < y.length; n++) {
  y[n] = ((4 * x[n - 1]) + (x[n - 2]) + (8 * y[n - 1]) - (3 * y[n - 2])) / 10;
}

console.log("y ->>> ", y);



// // // ------------------------------- graph -------------------------------
const dataX = [
  {
    x: points,
    y: x,
    type: 'bar'
  },
];

let dataY = [
  {
    x: points,
    y,
    type: 'bar'
  },
];

let layoutX = {
  title: "Sinal de entrada"
};
let layoutY = {
  title: "Sinal de saída com dados dado"
};

Plotly.newPlot('x-id', dataX, layoutX);
Plotly.newPlot('y-id', dataY, layout);


// calculando novamente para novos coeficientes aleatórios
for (let n = 2; n < y.length; n++) {
  y[n] = ((3 * x[n - 1]) + (x[n - 2]) + (7 * y[n - 1]) - (2 * y[n - 2])) / 10;
}

layoutY.title = "Sinal de saída com coeficientes modificados"

dataY = [
  {
    x: points,
    y,
    type: 'bar'
  },
];

Plotly.newPlot('x2-id', dataX, layoutX);
Plotly.newPlot('y2-id', dataY, layoutY);
