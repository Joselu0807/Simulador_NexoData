package planificador;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class RoundRobin extends Planificador {

    private int quantum;
    private int quantumConsumido;

    public RoundRobin(List<Proceso> procesos, boolean traza, int quantum) {
        super(procesos, traza);
        this.quantum = quantum;
        this.quantumConsumido = 0;
    }

    @Override
    protected Queue<Proceso> inicializarCola() {
        // Round Robin usa una cola normal FIFO
        return new LinkedList<>();
    }

    @Override
    protected void comprobarFinODesalojo() {
        if (procesoEnCPU != null) {
            // 1. Fin de ráfaga
            if (procesoEnCPU.getTiempoRestante() == 0) {
                procesoEnCPU.setEstado(EstadoProceso.TERMINADO);
                procesoEnCPU.setFin(instante);
                if (traza) System.out.println("[t=" + instante + "] " + procesoEnCPU.getNombre() + " TERMINA.");

                procesoEnCPU = null;
                quantumConsumido = 0; // Reiniciamos el contador para el siguiente
                procesosTerminados++;

                // 2. Agotamiento de quantum
            } else if (quantumConsumido == quantum) {
                if (colaListos.isEmpty()) {
                    // Regla 4: si la cola está vacía, se queda en CPU y renueva quantum
                    if (traza) System.out.println("[t=" + instante + "] " + procesoEnCPU.getNombre() + " agota quantum, pero renueva porque la cola está vacía.");
                    quantumConsumido = 0;
                } else {
                    // Desalojo normal
                    procesoEnCPU.setEstado(EstadoProceso.LISTO);
                    colaListos.add(procesoEnCPU); // Se va al final de la cola
                    if (traza) System.out.println("[t=" + instante + "] " + procesoEnCPU.getNombre() + " es DESALOJADO por fin de quantum.");

                    procesoEnCPU = null;
                    quantumConsumido = 0;
                }
            }
        }
    }

    @Override
    protected void ejecutar() {
        super.ejecutar(); // Hace que el proceso reste 1 a su tiempo restante y el instante avance
        if (procesoEnCPU != null) {
            quantumConsumido++; // Sumamos 1 unidad al turno actual
        }
    }
}
