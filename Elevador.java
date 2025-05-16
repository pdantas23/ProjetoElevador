public class Elevador extends EntidadeSimulavel {
    private int id;
    private boolean inicializado;
    private int andarDestino;
    private boolean subindo;
    private final int capacidadeMaxima;
    private int embarcados;
    private NodeAndar andarAtual;
    private int energiaGasta;
    private int posicaoY;
    CentralDeControle centralDeControle;
    ListaPassageiros listaPassageiros;
    ListaAndares listaAndares;
    ListaChamadas chamadasExclusivas;
    ListaChamadas chamadasGlobal;


    public Elevador(int id, ListaChamadas chamadasGlobal, ListaAndares listaAndares, CentralDeControle centralDeControle) {
        this.id = id;
        this.inicializado = false;
        this.capacidadeMaxima = 4;
        this.embarcados = 0;
        this.subindo = true;
        this.andarDestino = 0;
        this.energiaGasta = 0;
        this.centralDeControle = centralDeControle;
        this.listaAndares = listaAndares;
        this.andarAtual = listaAndares.getInicio();
        this.listaPassageiros = new ListaPassageiros();
        this.chamadasExclusivas = new ListaChamadas();
        this.chamadasGlobal = chamadasGlobal;
    }


    @Override
    public void atualizar(int minutoSimulado) {
        simular();
    }

    //Simulação do elevador
    public void simular() {
        System.out.println("--------------------------------------------");
        System.out.println("Elevador:  " + id);
        System.out.println("Andar atual:  " + andarAtual.getValor().getNumero());
        chamadasExclusivas.exibirChamadas();

        desembarcarPessoas(andarAtual.getValor());
        embarcarPessoas(this, andarAtual.getValor());

        andarDestino = encontrarDestinoMaisProximo();
        if(andarDestino != -1) {
            System.out.println("Andar destino: " + andarDestino);
        }
        if (andarDestino != -1) {
            mover();
        } else {
            System.out.println("Elevador sem destino.");
        }
        System.out.println("-----------------------------");
        System.out.println("Passageiros no elevador " + getId() + ":");
        getListaPassageiros().mostrarPassageiros();
        System.out.println("Gasto de energia total: " + energiaGasta);
    }

    //Inicializa o elevador
    public void inicializar(){
        this.inicializado = true;
        System.out.println("Elevador:  " + id + "  Inicializado");
    }

    //Move o elevador e incrementa a quantidade de energia gasta
    public void mover() {
        ajustarDirecaoSeNecessario();

        int tempoViagem = centralDeControle.isHorarioPico() ? 1 : 2;

        if (andarDestino > andarAtual.getValor().getNumero()) {
            subindo = true;
            if (andarAtual.getProximo() != null) {
                andarAtual = andarAtual.getProximo();
                System.out.println("Elevador subindo para o andar: " + andarAtual.getValor().getNumero() + " | Tempo de viagem: " + tempoViagem + " segundos");
            }
        } else if (andarDestino < andarAtual.getValor().getNumero()) {
            subindo = false;
            if (andarAtual.getAnterior() != null) {
                andarAtual = andarAtual.getAnterior();
                System.out.println("Elevador descendo para o andar: " + andarAtual.getValor().getNumero() + " | Tempo de viagem: " + tempoViagem + " segundos");
            }
        } else {
            System.out.println("Elevador sem andar de destino.");
        }

        if(centralDeControle.isHorarioPico()){
            energiaGasta += 2;
        }else{
            energiaGasta += 1;
        }
        System.out.println("Novo andar atual: " + andarAtual.getValor().getNumero());
    }

    //Encontra o destino mais proximo
    public int encontrarDestinoMaisProximo() {
        int destino = procurarDestinoPassageirosNaDirecao(subindo);

        // Se não há mais passageiros na direção, verifica chamadas externas
        if (destino == -1) {
            destino = procurarDestinoChamadasNaDirecao(subindo);
        }

        // Se não há chamdas na direção atual, inverte a direção
        if (destino == -1) {
            boolean descendo = !subindo;

            destino = procurarDestinoPassageirosNaDirecao(descendo);
            if (destino == -1) {
                destino = procurarDestinoChamadasNaDirecao(descendo);
            }
        }
        return destino;
    }

    //Verifica se o destino do passageiro está na direção
    public int procurarDestinoPassageirosNaDirecao(boolean direcao) {
        int melhorDestino = -1;
        NodePassageiro node = listaPassageiros.getInicio();

        while (node != null) {
            int destino = node.getPessoa().getAndarDestino();
            boolean naDirecao = direcao ? destino > andarAtual.getValor().getNumero() : destino < andarAtual.getValor().getNumero();

            if (naDirecao) {
                if (melhorDestino == -1 || (direcao && destino < melhorDestino) || (!direcao && destino > melhorDestino)) {
                    melhorDestino = destino;
                }
            }

            node = node.getProximo();
        }

        return melhorDestino;
    }

   //Procura um destino de acordo com a lista de chamadas
    private int procurarDestinoChamadasNaDirecao(boolean direcao) {
        int melhorDestino = -1;
        NodeChamadas node = chamadasExclusivas.getPrimeiro();

        while (node != null) {
            int andar = node.getAndarOrigem().getNumero();
            boolean naDirecao = direcao ? andar > andarAtual.getValor().getNumero() : andar < andarAtual.getValor().getNumero();

            if (naDirecao) {
                if (melhorDestino == -1 || (direcao && andar < melhorDestino) || (!direcao && andar > melhorDestino)) {
                    melhorDestino = andar;
                }
            }
            node = node.getProximo();
        }
        return melhorDestino;
    }

    //Metodo para embarcar pessoas
    public void embarcarPessoas(Elevador elevador, Andar andarAtual) {
        // Verifica se o andar atual está na lista de chamadas do elevador
        NodeChamadas nodeChamada = elevador.getListaChamadas().getPrimeiro();
        boolean chamadaEncontrada = false;

        while (nodeChamada != null) {
            if (nodeChamada.getAndarOrigem().getNumero() == andarAtual.getNumero()) {
                chamadaEncontrada = true;
                break;
            }
            nodeChamada = nodeChamada.getProximo();
        }

        if (!chamadaEncontrada) {
            return; // Se o andar atual não estiver na lista de chamadas, não embarca ninguém
        }

        FilaPessoas fila = andarAtual.getPessoasAguardando();
        NodePessoa atual = fila.getInicio();

        while (atual != null && elevador.getEmbarcados() < elevador.getCapacidadeMaxima()) {
            Pessoa pessoa = atual.getPessoa();
            NodePessoa proximo = atual.getProximo();

            boolean elevadorVazio = elevador.getListaPassageiros().isEmpty();

            if (elevadorVazio || verificarDirecao(pessoa, elevador)) {
                elevador.getListaPassageiros().adicionarNoFim(pessoa);
                fila.remover(pessoa);
                embarcados++;
            }
            atual = proximo;
        }
        andarAtual.getPainelExterno().setBotaoPressionado(false);
    }

    //Metodo para desembarcar os passageiros
    public void desembarcarPessoas(Andar andarAtual) {
        NodePassageiro atual = listaPassageiros.getInicio();
        int desembarcados = 0;

        if (atual == null) {
            return;
        }

        while (atual != null) {
            NodePassageiro proximo = atual.getProximo();
            Passageiro passageiro = atual.getPassageiro();

            if (passageiro.getAndarDestino() == andarAtual.getNumero()) {
                listaPassageiros.remover(atual);
                desembarcados++;
            }

            atual = proximo;
        }
        embarcados -= desembarcados;
        chamadasExclusivas.removerChamada(andarAtual.getNumero());
    }

    //Ajusta a direção se necessario
    private void ajustarDirecaoSeNecessario() {
        int destinoMaisProximo = encontrarDestinoMaisProximo();

        if (destinoMaisProximo == -1) {
            return;
        }
        if (destinoMaisProximo > andarAtual.getValor().getNumero()) {
            subindo = true;
        } else if (destinoMaisProximo < andarAtual.getValor().getNumero()) {
            subindo = false;
        }
    }

    //Verifica a direção
    private boolean verificarDirecao(Pessoa pessoa, Elevador elevador) {
        boolean elevadorSubindo = elevador.isSubindo();
        boolean pessoaSubindo = pessoa.getAndarOrigem() < pessoa.getAndarDestino();
        return elevadorSubindo == pessoaSubindo;
    }

    //Verifica se o elevador está parado
    public boolean isParado(){
        boolean semPassageiros = this.getListaPassageiros().isEmpty();
        boolean semChamadas = this.getListaChamadas().isEmpty();
        return semPassageiros && semChamadas;
    }

    //Getters
    public boolean isInicializado() {
        return inicializado;
    }

    public boolean isSubindo() {
        return subindo;
    }

    public void setPosicaoY(int posicaoY) {
        this.posicaoY = posicaoY;
    }

    public int getPosicaoY() {
        return posicaoY;
    }

    public int getAndarDestino(){
        return andarDestino;
    }

    public int getEmbarcados() {
        return embarcados;
    }

    public int getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    public int getEnergiaGasta(){
        return energiaGasta;
    }

    public int getId() {
        return id;
    }

    public ListaPassageiros getListaPassageiros() {
        return listaPassageiros;
    }

    public NodeAndar getAndarAtual() {
        return andarAtual;
    }

    public ListaChamadas getListaChamadas() {
        return this.chamadasExclusivas;
    }
}
