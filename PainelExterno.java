public class PainelExterno {
    private Andar andarOrigem;
    private int andarDestino;
    private boolean botaoPressionado;
    private ListaChamadas listaChamadasGlobal;

    public PainelExterno(Andar andarOrigem, ListaChamadas listaChamadasGlobal) {
        this.andarOrigem = andarOrigem;
        this.botaoPressionado = false;
        this.listaChamadasGlobal = listaChamadasGlobal;
    }

    //Passa o andar diretamente pra central de controle
    public void digitarAndar(int andarDestino, Pessoa pessoa) {
        if (!botaoPressionado) {
            this.andarDestino = andarDestino;
            botaoPressionado = true;
            chamarElevador(pessoa);
        }
    }

    //Chama o elevador adicionando uma chamada global
    private void chamarElevador(Pessoa pessoa) {
        listaChamadasGlobal.adicionarChamada(andarOrigem, pessoa.getAndarDestino());
    }

    //Getters e setters
    public void setBotaoPressionado(boolean botaoPressionado) {
        this.botaoPressionado = botaoPressionado;
    }

    public boolean isBotaoPressionado() {
        return botaoPressionado;
    }
}
