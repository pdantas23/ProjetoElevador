public class NodeAndar {
    private Andar valor;
    private NodeAndar proximo;
    private NodeAndar anterior;

    public NodeAndar(Andar valor) {
        this.valor = valor;
        this.proximo = null;
        anterior = null;
    }

    //Getters
    public Andar getValor() {
        return valor;
    }

    public NodeAndar getProximo() {
        return proximo;
    }

    public NodeAndar getAnterior() {
        return anterior;
    }

    public void setProximo(NodeAndar proximo) {
        this.proximo = proximo;
    }

    public void setAnterior(NodeAndar anterior) {
        this.anterior = anterior;
    }
}