package planificador;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class SJF extends Planificador {

    public SJF(List<Proceso> procesos, boolean traza) {
        super(procesos, traza);
    }

    @Override
    protected Queue<Proceso> inicializarCola() {
        // SJF usa una Cola de Prioridad con las 3 reglas del módulo:
        Comparator<Proceso> comparadorSJF = Comparator
                .comparingInt(Proceso::getRafaga)           // 1º: Menor ráfaga
                .thenComparingInt(Proceso::getLlegada)      // 2º: Si empatan, menor llegada
                .thenComparingInt(Proceso::getOrdenFichero);// 3º: Si empatan en todo, primero del CSV

        return new PriorityQueue<>(comparadorSJF);
    }

    @Override
    protected void comprobarFinODesalojo() {
        // En SJF SIN desalojo, la condición es idéntica a FCFS:
        if (procesoEnCPU != null && procesoEnCPU.getTiempoRestante() == 0) {
            procesoEnCPU.setEstado(EstadoProceso.TERMINADO);
            procesoEnCPU.setFin(instante);

            if (traza) {
                System.out.println("[t=" + instante + "] " + procesoEnCPU.getNombre() + " TERMINA.");
            }

            procesoEnCPU = null;
            procesosTerminados++;
        }
    }
}