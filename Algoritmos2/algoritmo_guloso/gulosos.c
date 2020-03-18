float mochila_fracionaria(int p[], int v[], int n, int c)
{
    float total = 0;
    int i = 0;

    while ((i < n) && (p[i] <= c))
    {
        total += v[i];
        c -= p[i];
        i++;
    }

    if ((i < n) && (c > 0))
        total += ((float)c / p[i]) * v[i];

    return total;
}

float mochila_binaria(int p[], int v[], int n, int c)
{
    int total = 0;
    int i = 0;

    while ((i < n) && (p[i] <= c))
    {
        total += v[i];
        c -= p[i];
        i++;
    }
    return total;
}

int tot_atividades(int ini[], int fim[], int N)
{
    int i = 0, aux = -1, tot = 0;

    if (N > 0)
    {
        aux = fim[i];
        tot++;
        for (i = 1; i < N; i++)
        {
            if (ini[i] > aux)
            {
                aux = fim[i];
                tot++;
            }
        }
        return tot;
    }
    if (N == 0)
        return 0;
    else
    {
        return -1;
    }
}

int qtd_moedas(int v[], int troco)
{
    int qtd = 0;
    int i;

    for (i = 0; (troco > 0); i++)
    {
        qtd += troco / v[i];
        troco = troco % v[i];
    }

    if (troco == 0)
        return qtd;
    else
        return -1;
}
