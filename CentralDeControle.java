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

        if (totalPessoas > 150) {
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

                // Verifica se o elevador está cheio ou se sua lista de chamadas está no limite
                if (elevador.getEmbarcados() >= elevador.getCapacidadeMaxima() ||
                        elevador.getListaChamadas().getTamanho() >= 4) {
                    elevadorAtual = elevadorAtual.getProximo();
                    continue;
                }

                int andarElevador = elevador.getAndarAtual().getValor().getNumero();
                int andarChamadaNum = andarChamada.getNumero();

                // Calcula distância entre elevador e chamada
                int distancia = Math.abs(andarElevador - andarChamadaNum);

                boolean mesmaDirecao = (elevador.isSubindo() && andarChamadaNum > andarElevador) || (!elevador.isSubindo() && andarChamadaNum < andarElevador);

                boolean elevadorParado = elevador.isParado();
                boolean temPassageirosEmbarcados = elevador.getEmbarcados() > 0;

                //Sistema de pontos pra determinar o elevador escolhido
                int pontuacao = 0;
                pontuacao += temPassageirosEmbarcados ? 0 : 50;
                pontuacao += elevadorParado ? 0 : 20;
                pontuacao += mesmaDirecao ? 0 : 30;
                pontuacao += distancia;

                if (melhorElevadorNode == null || pontuacao < menorPontuacao) {
                    menorPontuacao = pontuacao;
                    melhorElevadorNode = elevadorAtual;
                }
                elevadorAtual = elevadorAtual.getProximo();
            }

            if (melhorElevadorNode != null) {
                Elevador escolhido = melhorElevadorNode.getElevador();
                // Garante que o elevador ainda tenha espaço nas chamadas
                if (escolhido.getListaChamadas().getTamanho() < 4) {
                    escolhido.getListaChamadas().adicionarChamada(andarChamada, andarChamada.getNumero());
                    chamadasGlobais.removerChamada(andarChamada.getNumero());
                }
            }
            chamadaAtual = chamadaAtual.getProximo();
        }

        // Verifica se ainda há chamadas não atribuídas e força distribuição para elevadores parados com espaço
        chamadaAtual = chamadasGlobais.getPrimeiro();
        while (chamadaAtual != null) {
            Andar andarChamada = chamadaAtual.getAndarOrigem();

            // Procura elevador parado com menos de 4 chamadas
            NodeElevador elevadorNode = listaElevadores.getInicio();
            boolean atribuida = false;

            while (elevadorNode != null) {
                Elevador elevador = elevadorNode.getElevador();

                if (elevador.isParado() && elevador.getListaChamadas().getTamanho() < 4 && elevador.getEmbarcados() < elevador.getCapacidadeMaxima()) {
                    elevador.getListaChamadas().adicionarChamada(andarChamada, andarChamada.getNumero());
                    chamadasGlobais.removerChamada(andarChamada.getNumero());
                    atribuida = true;
                    break;
                }

                elevadorNode = elevadorNode.getProximo();
            }

            // Se não conseguiu atribuir a nenhum parado, tenta atribuir a qualquer um com espaço
            if (!atribuida) {
                elevadorNode = listaElevadores.getInicio();
                while (elevadorNode != null) {
                    Elevador elevador = elevadorNode.getElevador();

                    if (elevador.getListaChamadas().getTamanho() < 4 &&
                            elevador.getEmbarcados() < elevador.getCapacidadeMaxima()) {

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
