public class ListaAndares {
    private NodeAndar primeiro;
    private NodeAndar ultimo;
    private int tamanho;

    public ListaAndares() {
        primeiro = null;
        ultimo = null;
    }

    // Insere os andares no fim da lista
    public void inserirFim(Andar novo) {
        NodeAndar novoAndar = new NodeAndar(novo);
        if (primeiro == null) {
            primeiro = novoAndar;
            ultimo = novoAndar;
        } else {
            ultimo.setProximo(novoAndar);
            novoAndar.setAnterior(ultimo);
            ultimo = novoAndar;
        }
        tamanho++;
    }

    //Exibe a lista de andares
    public void exibirListaAndares() {
        NodeAndar novo = primeiro;
        while (novo != null) {
            System.out.println(novo.getValor().getNumero());
            novo = novo.getProximo();
        }
    }

    // Getters
    public NodeAndar getInicio() {
        return primeiro;
    }

    public int getTamanho() {
        return tamanho;
    }

    public NodeAndar getUltimo() {
        return ultimo;
    }
}
