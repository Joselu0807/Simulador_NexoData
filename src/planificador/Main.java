package planificador;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Uso: java planificador.Main <fichero.csv> <fcfs|sjf|rr|todos> [quantum] [--traza]");
            System.exit(1);
        }
        Path fichero = Path.of(args[0]);
        String algoritmo = args[1].toLowerCase();
        boolean traza = List.of(args).contains("--traza");
        int quantum = 2;
        if (args.length >= 3 && !args[2].startsWith("--")) {
            try {
                quantum = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                System.err.println("El quantum debe ser un número entero: " + args[2]);
                System.exit(1);
            }
        }
        if (!Files.exists(fichero)) {
            System.err.println("No encuentro el fichero " + fichero.toAbsolutePath()
                    + "\nComprueba el «Working directory» de la configuración de ejecución.");
            System.exit(1);
        }
        if (!List.of("fcfs", "sjf", "rr", "todos").contains(algoritmo)) {
            System.err.println("Algoritmo desconocido: " + algoritmo + " (usa fcfs, sjf, rr o todos)");
            System.exit(1);
        }

        try {
            List<Proceso> listaOriginal = LectorDatos.leerFichero(fichero.toString());

            switch (algoritmo) {
                case "fcfs":
                    List<Proceso> pFcfs = clonarLista(listaOriginal);
                    FCFS fcfs = new FCFS(pFcfs, traza);
                    fcfs.simular();
                    imprimirMetricas(pFcfs, "FCFS");
                    break;

                case "sjf":
                    List<Proceso> pSjf = clonarLista(listaOriginal);
                    SJF sjf = new SJF(pSjf, traza);
                    sjf.simular();
                    imprimirMetricas(pSjf, "SJF (Sin desalojo)");
                    break;

                case "rr":
                    List<Proceso> pRr = clonarLista(listaOriginal);
                    RoundRobin rr = new RoundRobin(pRr, traza, quantum);
                    rr.simular();
                    imprimirMetricas(pRr, "Round Robin (q=" + quantum + ")");
                    break;

                case "todos":
                    System.out.println("=== EJECUTANDO TODOS LOS ALGORITMOS ===");

                    List<Proceso> t1 = clonarLista(listaOriginal);
                    new FCFS(t1, traza).simular();
                    imprimirMetricas(t1, "FCFS");

                    List<Proceso> t2 = clonarLista(listaOriginal);
                    new SJF(t2, traza).simular();
                    imprimirMetricas(t2, "SJF");

                    List<Proceso> t3 = clonarLista(listaOriginal);
                    new RoundRobin(t3, traza, quantum).simular();
                    imprimirMetricas(t3, "Round Robin (q=" + quantum + ")");
                    break;
            }

        } catch (Exception e) {
            System.err.println("Error durante la ejecución: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static List<Proceso> clonarLista(List<Proceso> original) {
        List<Proceso> copia = new ArrayList<>();
        for (Proceso p : original) {
            copia.add(new Proceso(p));
        }
        return copia;
    }

    private static void imprimirMetricas(List<Proceso> procesos, String titulo) {
        System.out.println("\n--- MÉTRICAS: " + titulo + " ---");

        int totalRetorno = 0;
        int totalEspera = 0;
        int totalRespuesta = 0;

        for (Proceso p : procesos) {
            int retorno = p.getFin() - p.getLlegada();
            int espera = retorno - p.getRafaga();
            int respuesta = p.getPrimeraVezCpu() - p.getLlegada();

            totalRetorno += retorno;
            totalEspera += espera;
            totalRespuesta += respuesta;

            System.out.println("Proceso " + p.getNombre() +
                    " -> Fin: " + p.getFin() +
                    ", Retorno: " + retorno +
                    ", Espera: " + espera +
                    ", Respuesta: " + respuesta);
        }

        int n = procesos.size();
        System.out.println("Medias -> Retorno: " + (totalRetorno / (double)n) +
                " | Espera: " + (totalEspera / (double)n) +
                " | Respuesta: " + (totalRespuesta / (double)n) + "\n");
    }
}