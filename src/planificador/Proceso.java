package planificador;

public class Proceso {

    // 1. Datos de entrada
    private String nombre;
    private int llegada;
    private int rafaga;
    private int ordenFichero;

    // 2. Estado en la simulación
    private int tiempoRestante;
    private EstadoProceso estado;

    // 3. Datos para las métricas
    private int primeraVezCpu;
    private int fin;

    // --- CONSTRUCTORES ---

    // 1. Constructor principal
    public Proceso(String nombre, int llegada, int rafaga , int ordenFichero) {
        this.nombre = nombre;
        this.llegada = llegada;
        this.rafaga = rafaga;

        // Valores iniciales por defecto cuando se crea el proceso:
        this.tiempoRestante = rafaga; // Al nacer, le falta toda la ráfaga por ejecutar
        this.estado = EstadoProceso.NUEVO;
        this.primeraVezCpu = -1; // -1 significa que aún no ha tocado la CPU
        this.ordenFichero = ordenFichero;
    }

    // 2. Constructor de copia
    public Proceso(Proceso original) {
        this.nombre = original.nombre;
        this.llegada = original.llegada;
        this.rafaga = original.rafaga;
        this.tiempoRestante = original.rafaga; // ¡Ojo! Se reinicia la ráfaga
        this.estado = EstadoProceso.NUEVO;  // Se reinicia el estado
        this.primeraVezCpu = -1;   // Se reinicia la métrica
        this.ordenFichero = original.ordenFichero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getLlegada() {
        return llegada;
    }

    public void setLlegada(int llegada) {
        this.llegada = llegada;
    }

    public int getRafaga() {
        return rafaga;
    }

    public void setRafaga(int rafaga) {
        this.rafaga = rafaga;
    }

    public int getTiempoRestante() {
        return tiempoRestante;
    }

    public void setTiempoRestante(int tiempoRestante) {
        this.tiempoRestante = tiempoRestante;
    }

    public EstadoProceso getEstado() {
        return estado;
    }

    public void setEstado(EstadoProceso estado) {
        this.estado = estado;
    }

    public int getPrimeraVezCpu() {
        return primeraVezCpu;
    }

    public void setPrimeraVezCpu(int primeraVezCpu) {
        this.primeraVezCpu = primeraVezCpu;
    }

    public int getFin() {
        return fin;
    }

    public void setFin(int fin) {
        this.fin = fin;
    }

    public int getOrdenFichero() {
        return ordenFichero;
    }
}