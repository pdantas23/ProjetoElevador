public class NodeElevador {
    private NodeElevador proximo;
    Elevador elevador;

    public NodeElevador(Elevador elevador) {
        this.elevador = elevador;
        this.proximo = null;
    }

    //Getters e setter
    public Elevador getElevador() {
        return elevador;
    }

    public NodeElevador getProximo() {
        return proximo;
    }

    public void setProximo(NodeElevador novoNode) {
        this.proximo = novoNode;
    }
}
