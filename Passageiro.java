public class Passageiro extends Pessoa {
    private int tempoEsperando;

    public Passageiro(Pessoa pessoa) {
        super(pessoa.getId(), pessoa.getAndarOrigem(), pessoa.getAndarDestino(), pessoa.getPrioridade());
        this.tempoEsperando = pessoa.getTempoEsperando();
    }

    //Exibe as informações do passageiro
    @Override
    public String toString() {
        return "Passageiro ID: " + getId() +
                ", Origem: " + getAndarOrigem() +
                ", Destino: " + getAndarDestino() +
                ", Prioridade: " + getPrioridade();
    }

    //Getter
    public int getTempoEsperando() {
        return tempoEsperando;
    }
}
