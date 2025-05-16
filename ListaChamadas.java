public class ListaChamadas {
    private NodeChamadas primeiro;
    private int tamanho;

    public ListaChamadas() {
        primeiro = null;
        tamanho = 0;
    }

    //Adiciona uma chamada
    public void adicionarChamada(Andar andar, int andarDestino) {
        if (tamanho >= 4) {
            return;
        }

        NodeChamadas novaChamada = new NodeChamadas(andar, andarDestino);

        if (primeiro == null) {
            primeiro = novaChamada;
        } else {
            NodeChamadas atual = primeiro;
            while (atual.getProximo() != null) {
                atual = atual.getProximo();
            }
            atual.setProximo(novaChamada);
        }
        tamanho++;
    }


    // Remove uma chamada específica da lista
    public void removerChamada(int andar) {
        NodeChamadas atual = primeiro;
        NodeChamadas anterior = null;

        while (atual != null) {
            if (atual.getAndarOrigem().getNumero() == andar) {
                if (anterior == null) {
                    primeiro = atual.getProximo();
                } else {
                    anterior.setProximo(atual.getProximo());
                }
                tamanho--;
                return;
            }
            anterior = atual;
            atual = atual.getProximo();
        }
    }

    //Exibe a lista de chamadas
    public void exibirChamadas() {
        if (primeiro == null) {
            System.out.println("Nenhuma chamada registrada.");
            return;
        }

        NodeChamadas atual = primeiro;
        System.out.println("Chamadas Ativas:");
        while (atual != null) {
            Andar andar = atual.getAndarOrigem();

            System.out.println("Andar: " + andar.getNumero());
            atual = atual.getProximo();
        }
    }

    public boolean contemChamada(int andar) {
        NodeChamadas atual = primeiro;
        while (atual != null) {
            if (atual.getAndarOrigem().getNumero() == andar) {
                return true;
            }
            atual = atual.getProximo();
        }
        return false;
    }

    //Getters
    public boolean isEmpty() { return tamanho == 0; }

    public NodeChamadas getPrimeiro() {
        return primeiro;
    }

    public int getTamanho() {
        return tamanho;
    }
}
