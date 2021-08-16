% Método da Bissecção para encontrar Zero de Funções

%receber a função
disp('Informe a função');
f = input('','s');

%informe os intervalos 
disp('Informe o intervalo inferior');
intInfer = input('');
disp('Informe o intervalo Superior');
intSuperior = input('');

%informe o valor de Epsolon
disp('Informe o E');
erro = input('');

%informe o Máximo de interações
disp('Máximo de interações');
maxi = input('');

%processamento 

aux = 1;

while (aux < maxi)
    x = (intInfer+IntSuperior)/2;

    if (subs(f,x)==0 || intSuperior - intInfer < erro)
        fprintf('A raiz é: %d',x);
        break
        
    end
    aux++;
    if sign(subs(f,x)) == sign(subs(f,intInfer))
        intInfer = x;
    else 
        intSuperior = x;
    end
end

