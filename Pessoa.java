import java.util.Random;

public class Pessoa {
    private int id;
    private int andarOrigem;
    private int andarDestino;
    private int prioridade;
    private int tempoEsperando;
    private static int idCounter = 1;

    public Pessoa(int id, int origem, int destino, int prioridade) {
        this.id = id;
        tempoEsperando = 0;
        this.andarOrigem = origem;
        this.andarDestino = destino;
        this.prioridade = prioridade;

    }

    //Gera uma pessoa com parametros aleatorios
    public static Pessoa gerarPessoaAleatoria(Andar andarOrigem) {
        Random random = new Random();
        int andarDestino = gerarDestinoValido(andarOrigem.getNumero());
        int prioridade = random.nextInt(3) + 1;
        return new Pessoa(idCounter++, andarOrigem.getNumero(), andarDestino, prioridade);
    }

    // Metodo para garantir que o destino não seja o mesmo que o andar de origem
    private static int gerarDestinoValido(int origem) {
        int destino;
        Random random = new Random();

        do {
            destino = random.nextInt(10); // Destino aleatório entre 0 e 9
        } while (destino == origem); // Garante que o destino não seja o andar atual (origem)

        return destino;
    }

    //Incrementa o tempo esperado na fila
    public void incrementarTempoEsperando() {
        tempoEsperando++;
    }

    //Getters
    public int getTempoEsperando() {
        return tempoEsperando;
    }

    public int getId() {
        return id;
    }

    public int getAndarDestino() {
        return andarDestino;
    }


    public int getAndarOrigem() {
        return andarOrigem;
    }

    public int getPrioridade() {
        return prioridade;
    }
}