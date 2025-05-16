public class NodeChamadas {
    private Andar andarOrigem;
    private int andarDestino;
    private NodeChamadas proximo;

    public NodeChamadas(Andar andarOrigem, int andarDestino) {
        this.andarOrigem = andarOrigem;
        this.andarDestino = andarDestino;
        this.proximo = null;
    }

    //Getters e setters
    public Andar getAndarOrigem() {
        return andarOrigem;
    }

    public int getAndarDestino() {
        return andarDestino;
    }

    public NodeChamadas getProximo() {
        return proximo;
    }

    public void setProximo(NodeChamadas proximo) {
        this.proximo = proximo;
    }
}

