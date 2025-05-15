import javafx.application.Platform;
import javafx.stage.Stage;

public class Simulador {

    private Predio predio;
    private InterfaceGrafica interfaceGrafica;
    private CentralDeControle centralDeControle;

    private boolean pausado = false;
    private boolean encerrado = false;
    private int tempoSimulado = 0;
    private Thread threadSimulacao;

    public Simulador(int totalAndares, int totalElevadores, Stage stage) {
        this.predio = new Predio(totalAndares, totalElevadores);
        this.centralDeControle = predio.getCentralDeControle();
        this.interfaceGrafica = new InterfaceGrafica(stage, predio);
    }

    public void iniciar() {
        if (threadSimulacao != null && threadSimulacao.isAlive()) return;

        encerrado = false;
        pausado = false;
        tempoSimulado = 0;

        threadSimulacao = new Thread(() -> {
            System.out.println("Iniciando simulação...\n");

            while (tempoSimulado < 100 && !encerrado) {
                if (!pausado) {
                    int minutoAtual = ++tempoSimulado;
                    System.out.println("\n--- Tempo simulado " + minutoAtual + " ---");

                    predio.atualizar(minutoAtual);

                    Platform.runLater(() -> interfaceGrafica.atualizar(minutoAtual));

                    try {
                        int tempoPausa = centralDeControle.isHorarioPico() ? 1000 : 2000;
                        Thread.sleep(tempoPausa);
                    } catch (InterruptedException e) {
                        System.out.println("Simulação interrompida.");
                        return;
                    }
                } else {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }

            System.out.println("\nSimulação finalizada.");
        });

        threadSimulacao.setDaemon(true);
        threadSimulacao.start();
    }

    public void pausar() {
        System.out.println("Simulação pausada.");
        this.pausado = true;
    }

    public void retomar() {
        System.out.println("Simulação retomada.");
        this.pausado = false;
    }

    public void resetar() {
        System.out.println("Simulação resetada.");
        this.encerrado = true;
        this.pausado = false;

        tempoSimulado = 0;
        predio = new Predio(predio.getQuantidadeAndares(), predio.getCentralDeControle().getElevadores().getTamanho());
        centralDeControle = predio.getCentralDeControle();

        Platform.runLater(() -> interfaceGrafica.reinicializar(predio));
    }
}