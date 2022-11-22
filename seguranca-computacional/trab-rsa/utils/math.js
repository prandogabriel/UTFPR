const { gcd } = require("mathjs");

function pow(base, exp, mod) {
  if (exp == 0) return 1;
  if (exp % 2 == 0) {
    return Math.pow(pow(base, (exp / 2), mod), 2) % mod;
  }
  else {
    return (base * pow(base, (exp - 1), mod)) % mod;
  }
}

const customGcd = gcd;

module.exports = { pow, customGcd };
