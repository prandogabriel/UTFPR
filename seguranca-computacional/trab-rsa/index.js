const { primeFactory } = require("./utils/prime");
const { RSA } = require("./rsa");

const MESSAGE = process.env.MESSAGE || "Mensagem a ser criptografada com RSA";

console.log("gerando números primos aleatórios")

const p = primeFactory();
const q = primeFactory();

console.log("primos gerados ", { p, q });

console.log("instanciando classe RSA");
const rsa = new RSA(p, q)


console.log("gerando chaves");

const { public, private } = rsa.genKeyPair();

console.log("chaves geradas ", {public, private} );



console.log("Encriptando ");
const ciphertext = RSA.encrypt(public, MESSAGE);
console.log("Mensagem criptografada -> ", ciphertext);

const originalMessage = RSA.decrypt(private, ciphertext);
console.log("Mensagem Descriptografada ", RSA.decrypt(private, ciphertext));

