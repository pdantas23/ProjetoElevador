import java.io.Serializable;

public interface Simulavel extends Serializable {
    void simular();
    void atualizar(int minutoSimulado);
}
