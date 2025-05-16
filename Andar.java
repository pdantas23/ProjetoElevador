import java.util.Random;

public class Andar extends EntidadeSimulavel {
    private int numero;
    private Andar proximo;
    private FilaPessoas pessoasAguardando;
    private PainelExterno painel;

    public Andar(int numero, ListaChamadas listaChamadasGlobal) {
        this.numero = numero;
        this.pessoasAguardando = new FilaPessoas();
        this.painel = new PainelExterno(this, listaChamadasGlobal);
        this.proximo = null;
    }

    @Override
    public void atualizar(int minutoSimulado) {
        gerarPessoaPeriodicamente(minutoSimulado);
        verificarChamadaNecessaria();
        incrementarTempoFila();
        System.out.println("Fila do andar: " + numero);
        pessoasAguardando.exibirFila();
    }

    //Gera pessoas periodicamente no andar
    public void gerarPessoaPeriodicamente(int minutoSimulado) {
        Random random = new Random();

        int horaSimulada = minutoSimulado / 60;
        double chanceGerar;

        if (horaSimulada >= 9 && horaSimulada < 17) {
            // Horário comercial
            chanceGerar = 0.50;
        } else if ((horaSimulada >= 6 && horaSimulada < 9) || (horaSimulada >= 17 && horaSimulada < 20)) {
            // Início da manhã ou fim da tarde/começo da noite
            chanceGerar = 0.30;
        } else {
            // Noite e madrugada
            chanceGerar = 0.10;
        }

        double sorteio = random.nextDouble(); // gera valor entre 0.0 e 1.0

        if (sorteio < chanceGerar && pessoasAguardando.getTamanho() < 10) {
            Pessoa pessoa = Pessoa.gerarPessoaAleatoria(this);
            pessoasAguardando.adicionarPessoa(pessoa);
        }
    }

    //Verifica se há pessoas esperando no andar
    public void verificarChamadaNecessaria() {
        if (!pessoasAguardando.isEmpty() && !painel.isBotaoPressionado()) {
            Pessoa pessoa = pessoasAguardando.getInicio().getPessoa();
            painel.digitarAndar(pessoa.getAndarDestino(), pessoa);
            }
        }

    //Incrementa o tempo esperado na fila por cada pessoa
    public void incrementarTempoFila() {
        NodePessoa atual = pessoasAguardando.getInicio();
        while (atual != null) {
            atual.getPessoa().incrementarTempoEsperando();
            atual = atual.getProximo();
        }
    }

    //Getters
    public FilaPessoas getFilaPessoas() {
        return pessoasAguardando;
    }

    public int getNumero() {
        return numero;
    }

    public FilaPessoas getPessoasAguardando() {
        return pessoasAguardando;
    }


    public Andar getProximo() {
        return proximo;
    }

    public PainelExterno getPainelExterno() {
        return painel;
    }
}

