const { getRandom } = require("./random");

function isPrime(n) {
  if (n < 2 || n % 2 === 0) {
    return false;
  }
  if (n == 2) {
    return true;
  }

  for (let i = 3; i < n; i++) {
    if (n % i == 0) {
      return false;
    }
  }
  return true;
}

function generatePrime() {
  let p =  getRandom(3, 256);
  let q = getRandom(3, 256);

  while (!isPrime(p)) {
    p = getRandom(3, 256);
  }
  while (!isPrime(q)) {
    q = getRandom(3, 256);
  }
  return { p, q };
}

module.exports = { isPrime, generatePrime };
