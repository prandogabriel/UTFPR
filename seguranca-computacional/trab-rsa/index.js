const { generatePrime } = require("./utils/prime");
const { decrypt, encrypt, genKeyPair } = require("./rsa");

const {p, q} =  generatePrime();

console.log({p, q});

console.log("Generating your public/private key-pairs now . . .");

const {public, private} = genKeyPair(p, q); //# ((133,253), (177, 253))

console.log("Your public key is ", public, " and your private key is ", private);

const message = "Meu";// # input("Enter a message to encrypt with your public key: ")
const encryptedMsg = encrypt(public, message);

console.log("Your encrypted message is: ", encryptedMsg); //# ''.join(map(lambda x: str(x), encrypted_msg)))
console.log("Decrypting message with private key ", private, " . . .");
console.log("Your message is: ", decrypt(private, encryptedMsg));

console.log("======================== END =================================");
console.log("==============================================================");
