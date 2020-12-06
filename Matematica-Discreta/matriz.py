def ler_matriz(dimensao):
  matriz = []
  for i in range(dimensao):
    linha = []
    for j in range(dimensao):
      linha.append(input())

    matriz.append(linha)

  return matriz

def print_matriz(matriz, dimensao):
  for i in range(dimensao):
    print(matriz[i])

def simetrica(matriz, dimensao):
  for i in range(dimensao):
    j = i+1
    while(j < int(dimensao)):
      if(matriz[i][j] != matriz[j][i]):
        print('Não é simétrica')
        return
      j+=j

  print('Simétrica')
  return

def reflexiva(matriz, dimensao):
  for i in range(dimensao):
    if(int(matriz[i][i]) != 1):
      print('Não é reflexiva')
      return
  print('Reflexiva')
  return

def transitiva(matriz, dimensao):
  for k in range(dimensao):
    for i in range(dimensao):
      for j in range(dimensao):
        matriz[i][j]= matriz[i][j] or (matriz[i][k] and matriz[k][j])
  print('fechos transitivos')
  for i in range(dimensao):
    print(matriz[i])
  return

# uma matriz nada mais é do que um vetor de vetores
# [
#   [ 0,  0,  0],
#   [ 0,  1,  1],
#   [ 0,  1,  1],
# ]

dimensao = int(input())

matriz = ler_matriz(dimensao)

print_matriz(matriz, dimensao)

simetrica(matriz, dimensao)
reflexiva(matriz, dimensao)
transitiva(matriz, dimensao)
