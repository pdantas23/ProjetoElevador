import java.util.Scanner;

public class Main{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Quantidade de andares: ");
        int quantidadeAndares = scanner.nextInt();

        System.out.println("Quantidade de elevadores: ");
        int quantidadeElevadores = scanner.nextInt();

        Simulador simulador= new Simulador(quantidadeAndares, quantidadeElevadores);
        simulador.iniciar();
    }
}