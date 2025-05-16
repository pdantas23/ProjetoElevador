public class Simulador {
    private Predio predio;
    private CentralDeControle centralDeControle;
    private int tempoSimulado = 0;


    public Simulador(int totalAndares, int totalElevadores) {
        this.predio = new Predio(totalAndares, totalElevadores);
        this.centralDeControle = predio.getCentralDeControle();
    }

    public void iniciar() {

        System.out.println("Iniciando simulação...\n");
        for (tempoSimulado = 0; tempoSimulado <= 1440; tempoSimulado++) {
            System.out.println("\n--- Tempo simulado " + (tempoSimulado + 1) + " ---");

            predio.atualizar(tempoSimulado);

            try {
                int tempoPausa = centralDeControle.isHorarioPico() ? 10 : 20;
                Thread.sleep(tempoPausa);
            } catch (InterruptedException e) {
                System.out.println("Erro ao pausar o ciclo.");
            }
        }
        System.out.println("\nSimulação finalizada.");
    }
}