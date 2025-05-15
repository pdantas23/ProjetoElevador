public class NodePessoa {
    private Pessoa pessoa;
    private NodePessoa proximo;
    private NodePessoa anterior;

    public NodePessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
        this.proximo = null;
        this.anterior = null;
    }

    //Getters e setters
    public Pessoa getPessoa() {
        return pessoa;
    }

    public NodePessoa getProximo() {
        return proximo;
    }

    public void setProximo(NodePessoa proximo) {
        this.proximo = proximo;
    }
}