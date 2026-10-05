package planificador;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LectorDatos {

    public static List<Proceso> leerFichero(String ruta) {
        List<Proceso> procesos = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            int numeroLinea = 0;

            while ((linea = br.readLine()) != null) {
                numeroLinea++;
                linea = linea.trim();

                // 1. Descartar líneas vacías y comentarios que empiecen por #
                if (linea.isEmpty() || linea.startsWith("#")) {
                    continue;
                }

                // 2. Separar por punto y coma
                String[] partes = linea.split(";");

                if (partes.length != 3) {
                    throw new IllegalArgumentException("Error en la línea " + numeroLinea + ": Formato incorrecto. Deben ser 3 campos separados por ';'");
                }

                String nombre = partes[0].trim();
                int llegada = Integer.parseInt(partes[1].trim());
                int rafaga = Integer.parseInt(partes[2].trim());

                // 3. Validaciones obligatorias de lógica
                if (llegada < 0) {
                    throw new IllegalArgumentException("Error en la línea " + numeroLinea + ": El instante de llegada del proceso " + nombre + " no puede ser negativo.");
                }
                if (rafaga <= 0) {
                    throw new IllegalArgumentException("Error en la línea " + numeroLinea + ": La ráfaga del proceso " + nombre + " debe ser estrictamente mayor que 0.");
                }

                // Si pasa todos los controles, se añade a la lista
                procesos.add(new Proceso(nombre, llegada, rafaga));
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Error de lectura: Uno de los valores numéricos no tiene el formato correcto.");
        }

        return procesos;
    }
}