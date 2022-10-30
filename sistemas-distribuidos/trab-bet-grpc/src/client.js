const grpc = require('@grpc/grpc-js')
const protoLoader = require('@grpc/proto-loader')
const path = require('path')

const protoObject = protoLoader.loadSync(path.resolve(__dirname, '../proto/bet.proto'))
const BetDefinition = grpc.loadPackageDefinition(protoObject)

const client = new BetDefinition.BetService('localhost:50051', grpc.credentials.createInsecure());

const action = process.argv[3]
const nickname = process.argv[5]
const name = process.argv[7]
const amount = process.argv[9]
const animal = process.argv[11]

console.log({action});

// node src/client.js --action register --nickname: prando --name: Gabriel --amount: 123.3 --animal boi

if(action === "register") {
  client.registerBet({ nickname, name, amount: Number(amount), animal }, (err, response) => {
    console.log(response)
    if (err) throw err
  })
}

// node src/client.js --action draw
if(action === "draw") {
  client.drawBet({}, (err, response) => {
    console.log(response)
    if (err) throw err
  })
}


