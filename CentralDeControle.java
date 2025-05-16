public class CentralDeControle extends EntidadeSimulavel {
    private ListaElevadores listaElevadores;
    private ListaAndares listaAndares;
    private Predio predio;
    private boolean horarioPico;

    // Construtor
    public CentralDeControle(Predio predio, ListaAndares listaAndares) {
        this.listaElevadores = new ListaElevadores();
        this.listaAndares = listaAndares;
        this.predio = predio;
        horarioPico = false;
    }

    @Override
    public void atualizar(int minutoSimulado) {
        inicializarElevadores();
        predio.getListaChamadasGlobal().exibirChamadas();
        distribuirChamadas(predio.getListaChamadasGlobal());
        verificarHorarioPico();

        NodeElevador atual = listaElevadores.getInicio();
        while (atual != null) {
            Elevador elevador = atual.getElevador();

            if (!elevador.isInicializado()) {
                elevador.atualizar(minutoSimulado);
            }
            elevador.simular();
            atual = atual.getProximo();
        }
    }

    // Metodo para inicializar os elevadores
    private void inicializarElevadores() {
        NodeElevador atual = listaElevadores.getInicio();

        while (atual != null) {
            Elevador elevador = atual.getElevador();

            if (!elevador.isInicializado()) {
                elevador.inicializar();
            }
            atual = atual.getProximo();
        }
    }

    //Verifica se é horario de pico baseado na quantidade de pessoas esperando em todo o predio
    public void verificarHorarioPico() {
        int totalPessoas = 0;

        NodeAndar atual = listaAndares.getInicio();
        while (atual != null) {
            totalPessoas += atual.getValor().getFilaPessoas().getTamanho();
            atual = atual.getProximo();
        }

        if (totalPessoas > 20) {
            horarioPico = true;
        } else {
            horarioPico = false;
        }
    }

    //Distribui as chamadas para os elevadores
    public void distribuirChamadas(ListaChamadas chamadasGlobais) {
        NodeChamadas chamadaAtual = chamadasGlobais.getPrimeiro();

        while (chamadaAtual != null) {
            Andar andarChamada = chamadaAtual.getAndarOrigem();
            NodeElevador melhorElevadorNode = null;
            int menorPontuacao = Integer.MAX_VALUE;

            NodeElevador elevadorAtual = listaElevadores.getInicio();

            while (elevadorAtual != null) {
                Elevador elevador = elevadorAtual.getElevador();

                // Ignora elevadores cheios ou com muitas chamadas
                if (elevador.getEmbarcados() >= elevador.getCapacidadeMaxima() || elevador.getListaChamadas().getTamanho() >= 4) {
                    elevadorAtual = elevadorAtual.getProximo();
                    continue;
                }

                int andarElevador = elevador.getAndarAtual().getValor().getNumero();
                int andarChamadaNum = andarChamada.getNumero();
                int distancia = Math.abs(andarElevador - andarChamadaNum);

                boolean mesmaDirecao = (elevador.isSubindo() && andarChamadaNum > andarElevador) || (!elevador.isSubindo() && andarChamadaNum < andarElevador);

                boolean elevadorParado = elevador.isParado();
                boolean temPassageiros = elevador.getEmbarcados() > 0;

                // Pontuação baseada em critérios
                int pontuacao = distancia;
                pontuacao += temPassageiros ? 10 : 0;
                pontuacao += elevadorParado ? -10 : 20;
                pontuacao += mesmaDirecao ? 0 : 30;

                if (pontuacao < menorPontuacao) {
                    menorPontuacao = pontuacao;
                    melhorElevadorNode = elevadorAtual;
                }

                elevadorAtual = elevadorAtual.getProximo();
            }

            // Atribui a chamada ao melhor elevador encontrado
            if (melhorElevadorNode != null) {
                Elevador escolhido = melhorElevadorNode.getElevador();

                // Verifica se a chamada já não está presente
                if (!escolhido.getListaChamadas().contemChamada(andarChamada.getNumero())) {
                    escolhido.getListaChamadas().adicionarChamada(andarChamada, andarChamada.getNumero());
                    chamadasGlobais.removerChamada(andarChamada.getNumero());
                    chamadaAtual = chamadasGlobais.getPrimeiro(); // Reinicia, pois a lista mudou
                    continue;
                }
            }

            chamadaAtual = chamadaAtual.getProximo();
        }

        // Segunda fase: ainda há chamadas não atribuídas
        chamadaAtual = chamadasGlobais.getPrimeiro();
        while (chamadaAtual != null) {
            Andar andarChamada = chamadaAtual.getAndarOrigem();
            boolean atribuida = false;

            // Prioriza elevadores parados
            NodeElevador elevadorNode = listaElevadores.getInicio();
            while (elevadorNode != null) {
                Elevador elevador = elevadorNode.getElevador();

                if (elevador.isParado() && elevador.getListaChamadas().getTamanho() < 4 && elevador.getEmbarcados() < elevador.getCapacidadeMaxima() && !elevador.getListaChamadas().contemChamada(andarChamada.getNumero())) {

                    elevador.getListaChamadas().adicionarChamada(andarChamada, andarChamada.getNumero());
                    chamadasGlobais.removerChamada(andarChamada.getNumero());
                    atribuida = true;
                    break;
                }

                elevadorNode = elevadorNode.getProximo();
            }

            // Se nenhum parado pôde atender, tenta qualquer disponível
            if (!atribuida) {
                elevadorNode = listaElevadores.getInicio();
                while (elevadorNode != null) {
                    Elevador elevador = elevadorNode.getElevador();

                    if (elevador.getListaChamadas().getTamanho() < 4 && elevador.getEmbarcados() < elevador.getCapacidadeMaxima() && !elevador.getListaChamadas().contemChamada(andarChamada.getNumero())) {

                        elevador.getListaChamadas().adicionarChamada(andarChamada, andarChamada.getNumero());
                        chamadasGlobais.removerChamada(andarChamada.getNumero());
                        break;
                    }

                    elevadorNode = elevadorNode.getProximo();
                }
            }

            chamadaAtual = chamadaAtual.getProximo();
        }
    }

    //Adiciona o elevador a lista de elevadores
    public void adicionarElevador(Elevador elevador) {
        listaElevadores.inserir(elevador);
    }

    //Getters
    public boolean isHorarioPico() {
        return horarioPico;
    }
    public ListaElevadores getElevadores() {
        return listaElevadores;
    }
}
