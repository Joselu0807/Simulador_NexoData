package planificador;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class FCFS extends Planificador {

    public FCFS(List<Proceso> procesos, boolean traza) {
        super(procesos, traza);
    }

    @Override
    protected Queue<Proceso> inicializarCola() {
        // FCFS usa una cola normal (FIFO: el primero en entrar es el primero en salir)
        return new LinkedList<>();
    }

    @Override
    protected void comprobarFinODesalojo() {
        // FCFS NO desaloja a la fuerza. Un proceso no suelta la CPU hasta que termina.
        if (procesoEnCPU != null && procesoEnCPU.getTiempoRestante() == 0) {
            procesoEnCPU.setEstado(EstadoProceso.TERMINADO);

            // Guardamos el instante de fin para las métricas de la Tarea 4
            procesoEnCPU.setFin(instante);

            if (traza) {
                System.out.println("[t=" + instante + "] " + procesoEnCPU.getNombre() + " TERMINA.");
            }

            procesoEnCPU = null; // Liberamos la CPU para el siguiente
            procesosTerminados++;
        }
    }
}