import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class InterfaceGrafica implements Simulavel {

    private Predio predio;
    private final BorderPane root;
    private InterfacePredio interfacePredio;

    public InterfaceGrafica(Stage stage, Predio predio) {
        this.predio = predio;
        this.root = new BorderPane();
        this.interfacePredio = new InterfacePredio(predio);

        initUI(stage);
    }

    private void initUI(Stage stage) {
        root.setCenter(interfacePredio);

        Scene scene = new Scene(root, 400, 600);
        stage.setTitle("Simulação de Elevador");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void atualizar(int minutoSimulado) {
        interfacePredio.atualizar(minutoSimulado); // atualiza visual sem recriar toda a interface
    }

    @Override
    public void simular() {
        // Sem lógica aqui, pois Simulador controla o loop
    }

    public void reinicializar(Predio novoPredio) {
        this.predio = novoPredio;
        this.interfacePredio = new InterfacePredio(novoPredio);
        root.setCenter(interfacePredio);
    }
}
