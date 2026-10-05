package planificador;

import java.util.List;
import java.util.Queue;

public abstract class Planificador {

    protected List<Proceso> procesos;
    protected Queue<Proceso> colaListos;
    protected Proceso procesoEnCPU;
    protected int instante;
    protected int procesosTerminados;
    protected boolean traza;

    public Planificador(List<Proceso> procesos, boolean traza) {
        this.procesos = procesos;
        this.traza = traza;
        this.instante = 0;
        this.procesosTerminados = 0;
        this.procesoEnCPU = null;
        this.colaListos = inicializarCola(); // Cada algoritmo (hijo) creará su propia cola
    }

    // Método abstracto que obliga a los hijos a decidir qué tipo de cola usan
    protected abstract Queue<Proceso> inicializarCola();

    // El bucle de simulación único para todos los algoritmos
    public void simular() {
        System.out.println("\n--- Iniciando simulación ---");

        while (procesosTerminados < procesos.size()) {
            // 1. Llegadas en t
            llegadas();

            // 2. Fin / Desalojo (Depende de cada algoritmo, por eso es abstracto)
            comprobarFinODesalojo();

            // 3. Selección
            seleccionarProceso();

            // 4. Ejecutar
            ejecutar();
        }
    }

    protected void llegadas() {
        // Recorremos los procesos para ver si alguno llega en este instante exacto
        for (Proceso p : procesos) {
            if (p.getLlegada() == instante) {
                p.setEstado(EstadoProceso.LISTO);
                colaListos.add(p);
                if (traza) System.out.println("[t=" + instante + "] " + p.getNombre() + " llega a la cola.");
            }
        }
    }

    // Cada algoritmo tiene sus reglas para desalojar o terminar
    protected abstract void comprobarFinODesalojo();

    protected void seleccionarProceso() {
        if (procesoEnCPU == null && !colaListos.isEmpty()) {
            procesoEnCPU = colaListos.poll(); // Extrae el primero según las reglas de la cola
            procesoEnCPU.setEstado(EstadoProceso.EJECUCION);

            // Si es la primera vez que entra a la CPU, registramos el instante (para la métrica de Respuesta)
            if (procesoEnCPU.getPrimeraVezCpu() == -1) {
                procesoEnCPU.setPrimeraVezCpu(instante);
            }

            if (traza) System.out.println("[t=" + instante + "] " + procesoEnCPU.getNombre() + " entra en la CPU.");
        }
    }

    protected void ejecutar() {
        if (procesoEnCPU != null) {
            procesoEnCPU.setTiempoRestante(procesoEnCPU.getTiempoRestante() - 1);
        }
        instante++; // Avanza a t+1
    }
}