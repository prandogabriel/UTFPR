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

function findD(e, phi_value) {
  let d = 0;

  while ((d * e) % phi_value != 1) {
    d += 1;
  }

  return d;
}

function genKeyPair(p, q) {
  let phi = (p - 1) * (q - 1);
  let e = 2;
  for (let i = 2; i < phi; i++) {
    if (gcd(i, phi) === 1) e = i
  }
  d = findD(e, phi);

  return { public: { key: e, n: p * q }, private: { key: d, n: p * q } };
}


function encrypt(public, message) {
  const { key, n } = public;
  const letters = message.split("").map(c => c.charCodeAt(0));
  const ciphertext = [];
  letters.forEach(letter => {
    ciphertext.push(pow(letter, key, n));
  });

  return ciphertext
}

function decrypt(private, messageArray) {
  const { key, n } = private;
  const text = [];

  messageArray.forEach(letter => {
    text.push(String.fromCharCode(pow(letter, key, n)));
  });

  return text.join("");
}

module.exports = { genKeyPair, encrypt, decrypt }
