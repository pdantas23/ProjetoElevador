public class ListaPassageiros {
    private NodePassageiro inicio;
    private NodePassageiro fim;
    private int tamanho;

    public ListaPassageiros() {
        this.inicio = null;
        this.fim = null;
        this.tamanho = 0;
    }

    //Remove um passageiro da lista
    public void remover(NodePassageiro atual) {
        if (atual == null || inicio == null) return;

        if (inicio == fim && atual == inicio) {
            inicio = fim = null;
            tamanho--;
            return;
        }

        if (atual == inicio) {
            inicio = inicio.getProximo();
            inicio.setAnterior(null);
        }

        else if (atual == fim) {
            fim = fim.getAnterior();
            fim.setProximo(null);
        } else {
            NodePassageiro anterior = atual.getAnterior();
            NodePassageiro proximo = atual.getProximo();

            if (anterior != null) anterior.setProximo(proximo);
            if (proximo != null) proximo.setAnterior(anterior);
        }
        tamanho--;
    }

    // Metodo para adicionar passageiros à lista
    public void adicionarNoFim(Pessoa pessoa) {
        if (pessoa == null) return;

        Passageiro passageiro = new Passageiro(pessoa);
        NodePassageiro novo = new NodePassageiro(passageiro);

        System.out.println("Adicionando Passageiro ID: " + pessoa.getId() + " à lista.");


        if (inicio == null) {
            inicio = fim = novo;
        } else {
            fim.setProximo(novo);
            novo.setAnterior(fim);
            fim = novo;
        }

        tamanho++;
    }

    // Metodo para mostrar os passageiros embarcados
    public void mostrarPassageiros() {
        if (inicio == null) {
            System.out.println("Nenhum passageiro no elevador.");
            return;
        }

        NodePassageiro atual = inicio;

        while (atual != null) {
            System.out.println("ID: " + atual.getPassageiro().getId() + " | Andar de destino: " + atual.getPassageiro().getAndarDestino());
            atual = atual.getProximo();
        }
    }

    //Getters
    public boolean isEmpty() {
        return inicio == null;
    }

    public NodePassageiro getInicio() {
        return inicio;
    }
}
