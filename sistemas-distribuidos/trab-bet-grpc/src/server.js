const grpc = require('grpc')
const protoLoader = require('@grpc/proto-loader')
const path = require('path')

const protoObject = protoLoader.loadSync(path.resolve(__dirname, '../proto/bet.proto'))
const BetDefinition = grpc.loadPackageDefinition(protoObject)

const animals = ['Avestruz', 'Águia', 'Burro', 'Borboleta', 'Cachorro', 'Cabra', 'Carneiro', 'Camelo', 'Cobra', 'Coelho', 'Cavalo', 'Elefante', 'Galo', 'Gato', 'Jacaré', 'Leão', 'Macaco', 'Porco', 'Pavão', 'Peru', 'Touro', 'Tigre', 'Urso', 'Veado', 'Vaca'];

const bets = [
  // { nickname: 'prando', name: 'Gabriel Prando', amount: 123.3, animal: 'boi' },
]

function RegisterBet({ request }, callback) {
  bets.push(request);
  console.log("request", request);
  console.log("bets", bets);
  return callback(null, { message: "Aposta cadastrada com sucesso" });
}




function RaffleBet(_, callback) {
  const raffleAnimal = Math.floor(Math.random() * 25)
  const animal = animals[raffleAnimal];

  const filteredBets = bets.filter(b => b.animal === animal);

  const totalAmount =  bets.reduce((acc, cur) => acc + cur.amount, 0);
  const totalPeopleRaffle = filteredBets.length;

  console.log(totalPeopleRaffle, "apostas ganhadoras, de um total de ", bets.length, "apostas \n");

  let message = "Animal sorteado foi: " + animal + "um total de ganhadores: " + totalPeopleRaffle;


  message = totalPeopleRaffle > 0 ? message + "\nCada aposta recebe " + totalAmount / totalPeopleRaffle : message;

  return callback(null, { message });
}

const server = new grpc.Server()
server.addService(BetDefinition.BetService.service, { RegisterBet, RaffleBet })

server.bind('0.0.0.0:50051', grpc.ServerCredentials.createInsecure())
server.start()
console.log('Listening')
