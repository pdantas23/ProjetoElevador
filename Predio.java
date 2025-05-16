public class Predio extends EntidadeSimulavel {
    private int quantidadeAndares;
    private int quantidadeElevadores;
    private CentralDeControle centralDeControle;
    private ListaAndares listaAndares;
    private ListaChamadas listaChamadasGlobal;

    public Predio(int quantidadeAndares, int quantidadeElevadores) {
        this.quantidadeAndares = quantidadeAndares;
        this.quantidadeElevadores = quantidadeElevadores;
        this.listaAndares = new ListaAndares();
        this.listaChamadasGlobal = new ListaChamadas();
        criarAndares(quantidadeAndares);  // Criando andares
        this.centralDeControle = new CentralDeControle(this, listaAndares);
        criarElevadores();
    }

    @Override
    public void atualizar(int minutoSimulado) {
        exbirHoraDoDia(minutoSimulado);
        atualizarAndares(minutoSimulado);
        centralDeControle.atualizar(minutoSimulado);
    }

    //Atualiza todos os andares em cada iteração
    private void atualizarAndares(int minutoSimulado) {
        NodeAndar atual = listaAndares.getInicio();

        while (atual != null) {
            atual.getValor().atualizar(minutoSimulado);
            atual = atual.getProximo();
        }
    }

    //Cria os andares ao iniciar o predio
    private void criarAndares(int quantidadeAndares) {
        for (int i = 0; i < quantidadeAndares; i++) {
            listaAndares.inserirFim(new Andar(i, getListaChamadasGlobal()));
        }
    }

    //Cria os elevadores ao iniciar o predio
    private void criarElevadores() {
        for (int i = 0; i < quantidadeElevadores; i++) {
            Elevador elevador = new Elevador(i + 1, getListaChamadasGlobal(), listaAndares, centralDeControle);
            centralDeControle.adicionarElevador(elevador);
        }

    }

    private void exbirHoraDoDia(int minutoSimulado){
        int horaSimulada = minutoSimulado / 60;
        System.out.println("Hora do dia: " + horaSimulada);
    }

    //Getters
    public ListaChamadas getListaChamadasGlobal() {
        return listaChamadasGlobal;
    }

    public CentralDeControle getCentralDeControle() {
        return centralDeControle;
    }

    public int getQuantidadeAndares() {
        return quantidadeAndares;
    }

    public ListaAndares getListaAndares() {
        return listaAndares;
    }

}