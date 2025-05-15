import javafx.application.Application;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.util.Optional;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        int andares = solicitarValor("Numero de andares: ");
        int elevadores = solicitarValor("Numero de elevadores: ");

        Simulador simulador = new Simulador(andares, elevadores, stage);
        simulador.iniciar();
    }

    private int solicitarValor(String mensagem) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Configuração da Simulação");
        dialog.setHeaderText(mensagem);

        Optional<String> resultado = dialog.showAndWait();
        while (resultado.isEmpty() || !resultado.get().matches("\\d+") || Integer.parseInt(resultado.get()) <= 0) {
            dialog.setContentText("Por favor, insira um número inteiro positivo.");
            resultado = dialog.showAndWait();
        }

        return Integer.parseInt(resultado.get());
    }

    public static void main(String[] args) {
        launch(args);
    }
}