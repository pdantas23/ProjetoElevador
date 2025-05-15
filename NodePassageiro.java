public class NodePassageiro {
    public Passageiro passageiro;
    private NodePassageiro anterior;
    private NodePassageiro proximo;

    public NodePassageiro(Passageiro passageiro) {
        this.passageiro = passageiro;
        this.proximo = null;
        this.anterior = null;
    }

    //Getters e setters
    public Passageiro getPassageiro() {
        return passageiro;
    }

    public Pessoa getPessoa() {
        return passageiro;
    }

    public void setProximo(NodePassageiro proximo) {
        this.proximo = proximo;
    }

    public NodePassageiro getProximo() {
        return proximo;
    }

    public NodePassageiro getAnterior() {
        return anterior;
    }

    public void setAnterior(NodePassageiro anterior) {
        this.anterior = anterior;
    }
}
