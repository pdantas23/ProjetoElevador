import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class InterfacePredio extends Pane implements Simulavel {
    private static final int MARGEM_SUPERIOR = 10;
    private static final int MARGEM_INFERIOR = 10;
    private static final int MARGEM_LATERAL = 20;
    private static final int ESPACO_MINIMO_POR_ELEVADOR = 50;

    private final Predio predio;
    private final Canvas canvas;

    public InterfacePredio(Predio predio) {
        this.predio = predio;
        this.canvas = new Canvas(400, 600);
        this.getChildren().add(canvas);
        desenhar();
    }

    @Override
    public void atualizar(int tempoSimulado) {
        desenhar();
    }

    private void desenhar() {
        GraphicsContext g2 = canvas.getGraphicsContext2D();
        g2.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        g2.setLineWidth(2);

        int larguraPainel = (int) canvas.getWidth();
        int alturaPainel = (int) canvas.getHeight();

        int larguraUtil = larguraPainel - 2 * MARGEM_LATERAL;
        int alturaUtil = alturaPainel - MARGEM_SUPERIOR - MARGEM_INFERIOR;

        int qtdAndares = predio.getQuantidadeAndares();
        int alturaAndar = alturaUtil / qtdAndares;

        int qtdElevadores = predio.getCentralDeControle().getElevadores().getTamanho();
        int larguraPorElevador = Math.max(ESPACO_MINIMO_POR_ELEVADOR, larguraUtil / (qtdElevadores + 2));
        int larguraElevadoresTotal = larguraPorElevador * qtdElevadores;

        int predioX = MARGEM_LATERAL;
        int predioY = MARGEM_SUPERIOR;

        // Contorno do prédio
        g2.strokeRect(predioX, predioY, larguraUtil, alturaUtil);

        // Linhas dos andares e elevadores
        desenharBaias(g2, predioX, predioY, alturaUtil, qtdElevadores, larguraPorElevador, larguraElevadoresTotal);
        desenharAndares(g2, alturaPainel, larguraUtil, predioX, predioY);

        // Desenhar pessoas do lado direito
        desenharPessoasNosAndares(g2, predio.getListaAndares().getInicio(), alturaAndar, alturaPainel, larguraUtil, predioX);

        // Desenhar elevadores
        desenharElevadores(g2);
    }

    private void desenharBaias(GraphicsContext g2, int predioX, int predioY, int alturaUtil, int qtdElevadores, int larguraBaia, int larguraElevadoresTotal) {
        for (int i = 1; i <= qtdElevadores; i++) {
            int linhaX = predioX + i * larguraBaia;
            g2.strokeLine(linhaX, predioY, linhaX, predioY + alturaUtil);
        }
        g2.strokeLine(predioX + larguraElevadoresTotal, predioY, predioX + larguraElevadoresTotal, predioY + alturaUtil);
    }

    private void desenharAndares(GraphicsContext g2, int alturaPainel, int larguraUtil, int predioX, int predioY) {
        int numeroAndares = predio.getQuantidadeAndares();
        int alturaUtilAndares = alturaPainel - MARGEM_SUPERIOR - MARGEM_INFERIOR;
        int alturaAndar = alturaUtilAndares / numeroAndares;

        int linhaInferior = MARGEM_SUPERIOR + (alturaAndar * numeroAndares);

        int y = MARGEM_SUPERIOR;

        for (int i = 0; i <= numeroAndares; i++) {
            g2.strokeLine(predioX, y, predioX + larguraUtil, y);
            y += alturaAndar;
        }
    }

    private void desenharPessoasNosAndares(GraphicsContext gc, NodeAndar atual, int alturaAndar, int alturaPainel, int larguraUtil, int predioX) {
        int numeroAndares = predio.getQuantidadeAndares();
        int alturaUtilAndares = alturaPainel - MARGEM_SUPERIOR - MARGEM_INFERIOR;
        int y = MARGEM_SUPERIOR;

        for (int i = 0; i < numeroAndares; i++) {
            if (atual != null && atual.getValor().getPessoasAguardando() != null) {
                NodePessoa pessoa = atual.getValor().getPessoasAguardando().getInicio();
                double filaX = predioX + larguraUtil + 10; // lado direito do prédio
                desenharFilaPessoas(gc, pessoa, filaX, y + alturaAndar, alturaAndar);
                atual = atual.getProximo();
            }
            y += alturaAndar;
        }
    }

    private void desenharFilaPessoas(GraphicsContext gc, NodePessoa pessoa, double pessoaX, double linhaY, double alturaAndar) {
        double alturaStickman = Math.min(alturaAndar - 10, 40);
        double larguraStickman = 12;
        double pessoaY = linhaY - alturaStickman;

        while (pessoa != null) {
            desenharStickman(gc, pessoaX, pessoaY, larguraStickman, alturaStickman);
            pessoaX += larguraStickman + 8;
            pessoa = pessoa.getProximo();
        }
    }

    private void desenharStickman(GraphicsContext gc, double x, double y, double w, double h) {
        double cabeca = h / 4;
        double centro = x + w / 2;

        gc.strokeOval(centro - cabeca / 2, y, cabeca, cabeca);
        gc.strokeLine(centro, y + cabeca, centro, y + cabeca + h / 3);
        gc.strokeLine(centro, y + cabeca + h / 6, centro - w / 2, y + cabeca + h / 5);
        gc.strokeLine(centro, y + cabeca + h / 6, centro + w / 2, y + cabeca + h / 5);
        gc.strokeLine(centro, y + cabeca + h / 3, centro - w / 3, y + h);
        gc.strokeLine(centro, y + cabeca + h / 3, centro + w / 3, y + h);
    }

    private void desenharElevadores(GraphicsContext gc) {
        int qtdElevadores = predio.getCentralDeControle().getElevadores().getTamanho();
        int larguraUtil = (int) canvas.getWidth() - 2 * MARGEM_LATERAL;
        int alturaUtil = (int) canvas.getHeight() - MARGEM_SUPERIOR - MARGEM_INFERIOR;
        int qtdAndares = predio.getQuantidadeAndares();
        int alturaAndar = alturaUtil / qtdAndares;
        int larguraPorElevador = Math.max(ESPACO_MINIMO_POR_ELEVADOR, larguraUtil / (qtdElevadores + 2));
        int predioX = MARGEM_LATERAL;

        NodeElevador nodeElevador = predio.getCentralDeControle().getElevadores().getInicio();

        int posElevador = 0;

        while (nodeElevador != null) {
            Elevador elevador = nodeElevador.getElevador();
            int andar = elevador.getAndarAtual().getValor().getNumero();


            int elevadorY = MARGEM_SUPERIOR + (qtdAndares - andar) * alturaAndar + 10;
            int elevadorX = predioX + larguraPorElevador * posElevador + (larguraPorElevador - 30) / 2;

            elevador.setPosicaoY(elevadorY);

            gc.setFill(Color.LIGHTGRAY);
            gc.fillRect(elevadorX, elevadorY, 30, 40);
            gc.setStroke(Color.BLACK);
            gc.strokeRect(elevadorX, elevadorY, 30, 40);

            posElevador++;
            nodeElevador = nodeElevador.getProximo();
        }
    }

    @Override
    public void simular() {
    }
}