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

function primeFactory() {
  let prime = getRandom(3, 2048);

  while (!isPrime(prime)) {
    prime = getRandom(3, 2048);
  }
  return prime;
}

module.exports = { isPrime, primeFactory };
