# Criar ambiente docker para desenvolvimento no jupyter notebook

```bash
docker build -t jupyter-dev .

docker run -it -n jupyterdev -p 4000:4000 -v $PWD:/home/user jupyter-dev

# no terminal dentro do container para iniciar o jupyter
jupyter notebook --ip 0.0.0.0 --port 4000 --allow-root
```


