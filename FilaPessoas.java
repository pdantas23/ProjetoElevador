public class FilaPessoas {
    private NodePessoa inicio;
    private NodePessoa fim;
    private int tamanho;

    public FilaPessoas() {
        this.inicio = null;
        this.fim = null;
        this.tamanho = 0;
    }

    //Adiciona uma nova pessoa a fila
    public void adicionarPessoa(Pessoa pessoa) {
        NodePessoa novo = new NodePessoa(pessoa);

        if (inicio == null) {
            inicio = novo;
            fim = novo;
        } else {
            NodePessoa atual = inicio;
            NodePessoa anterior = null;

            while (atual != null && atual.getPessoa().getPrioridade() >= pessoa.getPrioridade()) {
                anterior = atual;
                atual = atual.getProximo();
            }

            if (anterior == null) {
                novo.setProximo(inicio);
                inicio = novo;
            } else {
                anterior.setProximo(novo);
                novo.setProximo(atual);

                if (atual == null) {
                    fim = novo;
                }
            }
        }
        tamanho++;

    }

    //Remove a pessoa da fila
    public void remover(Pessoa pessoa) {
        if (inicio == null) {
            return;
        }

        NodePessoa atual = inicio;
        NodePessoa anterior = null;

        while (atual != null) {
            if (atual.getPessoa().equals(pessoa)) {
                if (anterior == null) {
                    inicio = atual.getProximo();
                    if (inicio == null) {
                        fim = null;
                    }
                } else {
                    anterior.setProximo(atual.getProximo());
                    if (atual == fim) {
                        fim = anterior;
                    }
                }
                tamanho--;
                return;
            }
            anterior = atual;
            atual = atual.getProximo();
        }
    }

    //Exibe a fila
    public void exibirFila(){
        if (inicio == null) {
            System.out.println("A fila está vazia.");
            return;
        }

        NodePessoa atual = inicio;
        while (atual != null) {
            System.out.println("Pessoa ID: " + atual.getPessoa().getId() + " | Destino: " + atual.getPessoa().getAndarDestino() + " | Prioridade: " + atual.getPessoa().getPrioridade());
            atual = atual.getProximo();
        }
    }

    //Getters
    public boolean isEmpty() {
        return inicio == null;
    }

    public NodePessoa getInicio() {
        return inicio;
    }

    public int getTamanho() {
        return tamanho;
    }
}
