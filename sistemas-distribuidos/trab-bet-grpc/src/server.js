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




function DrawBet(_, callback) {
  const drawnAnimal = Math.floor(Math.random() * 25)
  const animal = animals[drawnAnimal];

  const filteredBets = bets.filter(b => b.animal === animal);

  const totalAmount =  bets.reduce((acc, cur) => acc + cur.amount, 0);
  const totalPeopleDrawn = filteredBets.length;

  console.log(totalPeopleDrawn, "apostas ganhadoras, de um total de ", bets.length, "apostas \n");

  let message = "Animal sorteado foi: " + animal + "um total de ganhadores: " + totalPeopleDrawn;


  message = totalPeopleDrawn > 0 ? message + "\nCada aposta recebe " + totalAmount / totalPeopleDrawn : message;

  return callback(null, { message });
}

const server = new grpc.Server()
server.addService(BetDefinition.BetService.service, { RegisterBet, DrawBet })

server.bind('0.0.0.0:50051', grpc.ServerCredentials.createInsecure())
server.start()
console.log('Listening')
