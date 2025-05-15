import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class InterfaceElevador extends Canvas {
    private final Predio predio;
    private boolean inicializado = false;

    private int margemSuperior = 30;
    private int margemInferior = 30;

    public InterfaceElevador(Predio predio) {
        this.predio = predio;

        // Redesenhar sempre que o tamanho do Canvas mudar
        widthProperty().addListener(evt -> draw());
        heightProperty().addListener(evt -> draw());
    }

    public void draw() {
        double largura = getWidth();
        double altura = getHeight();
        GraphicsContext gc = getGraphicsContext2D();

        gc.clearRect(0, 0, largura, altura);

        int qtdAndares = predio.getQuantidadeAndares();
        int alturaUtil = (int) altura - margemSuperior - margemInferior;
        int alturaAndar = alturaUtil / qtdAndares;
        int larguraPorElevador = (int) (largura / predio.getCentralDeControle().getElevadores().getTamanho());

        int predioX = 50;
        int predioY = 0;

        simular(gc, (int) altura, margemSuperior, margemInferior, predioX, alturaAndar, larguraPorElevador, predioY, qtdAndares);
    }

    private void simular(GraphicsContext gc, int alturaPainel, int margemSuperior, int margemInferior, int predioX, int alturaAndar, int larguraPorElevador, int predioY, int qtdAndares) {
        inicializarElevadores(alturaPainel, margemSuperior, margemInferior, qtdAndares);
        atualizarPosicoes(alturaPainel, margemSuperior, margemInferior, qtdAndares);
        desenharElevadores(gc, predioX, alturaAndar, larguraPorElevador, predioY);
    }

    private void inicializarElevadores(int alturaPainel, int margemSuperior, int margemInferior, int qtdAndares) {
        if (!inicializado) {
            int alturaUtil = alturaPainel - margemSuperior - margemInferior;
            int alturaAndar = alturaUtil / qtdAndares;
            int linhaInferiorTerreo = margemSuperior + (alturaAndar * qtdAndares);

            NodeElevador elevadorNode = predio.getCentralDeControle().getElevadores().getInicio();
            while (elevadorNode != null) {
                Elevador elevador = elevadorNode.getElevador();
                elevador.setPosicaoY(linhaInferiorTerreo - 5);
                elevadorNode = elevadorNode.getProximo();
            }
            inicializado = true;
        }
    }

    private void atualizarPosicoes(int alturaPainel, int margemSuperior, int margemInferior, int qtdAndares) {
        int alturaUtil = alturaPainel - margemSuperior - margemInferior;
        int alturaAndar = alturaUtil / qtdAndares;
        int linhaInferiorTerreo = margemSuperior + (alturaAndar * qtdAndares);

        NodeElevador elevadorNode = predio.getCentralDeControle().getElevadores().getInicio();
        while (elevadorNode != null) {
            Elevador elevador = elevadorNode.getElevador();
            int andarAtual = elevador.getAndarAtual().getValor().getNumero();
            if (andarAtual != -1) {
                int novaPosicaoY = linhaInferiorTerreo - (alturaAndar * andarAtual);
                elevador.setPosicaoY(novaPosicaoY);
            }
            elevadorNode = elevadorNode.getProximo();
        }
    }

    private void desenharElevadores(GraphicsContext gc, int predioX, int alturaAndar, int larguraPorElevador, int predioY) {
        NodeElevador elevadorNode = predio.getCentralDeControle().getElevadores().getInicio();
        int posicaoVisual = 0;

        while (elevadorNode != null) {
            Elevador elevador = elevadorNode.getElevador();
            desenharElevador(gc, elevador, posicaoVisual, predioX, alturaAndar, larguraPorElevador, predioY);
            posicaoVisual++;
            elevadorNode = elevadorNode.getProximo();
        }
    }

    private void desenharElevador(GraphicsContext gc, Elevador elevador, int posicaoVisual, int predioX, int alturaAndar, int larguraPorElevador, int predioY) {
        int largura = (int) (larguraPorElevador * 0.7);
        int altura = (int) (alturaAndar * 0.7);
        int elevadorX = predioX + (posicaoVisual * larguraPorElevador) + (larguraPorElevador - largura) / 2;
        int elevadorY = elevador.getPosicaoY();

        gc.setFill(Color.WHITE);
        gc.fillRect(elevadorX, elevadorY, largura, altura);

        gc.setStroke(Color.BLACK);
        gc.strokeRect(elevadorX, elevadorY, largura, altura);
    }
}