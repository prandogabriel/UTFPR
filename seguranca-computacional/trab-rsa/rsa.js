const { pow, customGcd } = require("./utils/math");


class RSA {
  #p;
  #q;

  constructor(p, q) {
    this.#p = p;
    this.#q = q;
  }

  #findD(e, phi_value) {
    let d = 0;

    while ((d * e) % phi_value != 1) {
      d += 1;
    }
    return d;
  }

  genKeyPair() {
    let phi = (this.#p - 1) * (this.#q - 1);
    let e = 2;
    for (let i = 2; i < phi; i++) {
      if (customGcd(i, phi) === 1) e = i
    }
    const d = this.#findD(e, phi);

    return { public: { key: e, n: this.#p * this.#q }, private: { key: d, n: this.#p * this.#q } };
  }


  static encrypt(publicKey, message) {
    const { key, n } = publicKey;
    const letters = message.split("").map(c => c.charCodeAt(0));
    const ciphertext = [];
    letters.forEach(letter => {
      ciphertext.push(pow(letter, key, n));
    });

    return ciphertext
  }

  static decrypt(privateKey, messageArray) {
    const { key, n } = privateKey;
    const text = [];

    messageArray.forEach(letter => {
      text.push(String.fromCharCode(pow(letter, key, n)));
    });

    return text.join("");
  }

}





module.exports = { RSA }
