package planificador;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        // Probamos con el archivo de ejemplo que ya tienes en la carpeta datos
        String fichero = "datos/ejemplo_clase.csv";

        try {
            System.out.println("Leyendo el fichero: " + fichero);
            List<Proceso> listaProcesos = LectorDatos.leerFichero(fichero);

            System.out.println("Se han leído " + listaProcesos.size() + " procesos correctamente.");

            // Imprimimos la lista para comprobar visualmente que están bien cargados
            for (Proceso p : listaProcesos) {
                System.out.println("- Proceso: " + p.getNombre() + " | Llegada: " + p.getLlegada() + " | Ráfaga: " + p.getRafaga());
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}