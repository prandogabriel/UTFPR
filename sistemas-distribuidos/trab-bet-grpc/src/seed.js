const grpc = require('@grpc/grpc-js')
const protoLoader = require('@grpc/proto-loader')
const path = require('path')
const { faker } = require('@faker-js/faker');

const protoObject = protoLoader.loadSync(path.resolve(__dirname, '../proto/bet.proto'))
const BetDefinition = grpc.loadPackageDefinition(protoObject)

const client = new BetDefinition.BetService('localhost:50051', grpc.credentials.createInsecure());

// import { faker } from '@faker-js/faker/locale/de';

const animals = ['Avestruz', 'Águia', 'Burro', 'Borboleta', 'Cachorro', 'Cabra', 'Carneiro', 'Camelo', 'Cobra', 'Coelho', 'Cavalo', 'Elefante', 'Galo', 'Gato', 'Jacaré', 'Leão', 'Macaco', 'Porco', 'Pavão', 'Peru', 'Touro', 'Tigre', 'Urso', 'Veado', 'Vaca'];

function createBetData() {
  const random = Math.floor(Math.random() * 25)
  const animal = animals[random];
  return {
    nickname: faker.name.firstName(),
    name: faker.name.firstName(),
    amount: faker.finance.amount(1, 100) ,
    animal
  };
}


(async () => {

  for (let index = 0; index < 100; index++) {
    client.registerBet(createBetData(), (err, response) => {
      console.log(response)
      if (err) throw err
    })

  }
})()


