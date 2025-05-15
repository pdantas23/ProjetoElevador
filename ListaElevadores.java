public class ListaElevadores {
    private NodeElevador primeiro;
    private NodeElevador ultimo;
    private int tamanho;

    public ListaElevadores() {
        primeiro = null;
        tamanho = 0;
    }

    //Adiciona um elevador a lista
    public void inserir(Elevador elevador) {
        NodeElevador novoNo = new NodeElevador(elevador);

        if (primeiro == null) {
            primeiro = novoNo;
        } else {
            NodeElevador atual = primeiro;
            while (atual.getProximo() != null) {
                atual = atual.getProximo();
            }
            atual.setProximo(novoNo);
        }
        tamanho++;
    }

    //Getters
    public NodeElevador getInicio() {
        return primeiro;
    }

    public int getTamanho() {
        return tamanho;
    }
}

