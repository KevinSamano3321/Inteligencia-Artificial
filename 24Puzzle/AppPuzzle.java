import java.util.*;
import java.util.concurrent.*;

public class AppPuzzle {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String objetivo = "ABCDEFGHIJKLMNOPQRSXUVW T";
        String estadoInicial ="ABCDEFGHIJKLMNOPQRS UVWTX";

        int opcion;

        do {
            System.out.println("\n====== 24-PUZZLE (5x5) ======");
            System.out.println("Estado inicial:  " + estadoInicial);
            System.out.println("Estado objetivo: " + objetivo);
            System.out.println("-----------------------------");
            System.out.println("1. Búsqueda en Anchura");
            System.out.println("2. Búsqueda en Profundidad");
            System.out.println("3. Búsqueda por Costo Uniforme");
            System.out.println("4. IDA* Manhattan");
            System.out.println("5. IDA* Conflicto Lineal");
            System.out.println("6. Tabla comparativa de todos los métodos");
            System.out.println("7. Salir");
            System.out.print("Seleccione una opción: ");

            opcion = sc.nextInt();

            if (opcion >= 1 && opcion <= 5) ejecutarMetodo(opcion, estadoInicial, objetivo);
            else if (opcion == 6) tablaComparativa(estadoInicial, objetivo);
            else if (opcion == 7) System.out.println("Saliendo...");
            else System.out.println("Opción inválida.");
        } while (opcion != 7);
        sc.close();
    }

    private static void ejecutarMetodo(int opcion, String inicial, String objetivo) {
        Arbol puzzle = new Arbol(new Nodo(inicial));
        Nodo resultado = null;
        
        long inicio = System.nanoTime();

        switch (opcion) {
            case 1 -> resultado = puzzle.BusquedaAnchura(objetivo);
            case 2 -> resultado = puzzle.BusquedaProfundidad(objetivo);
            case 3 -> resultado = puzzle.BusquedaCostoUniforme(objetivo);
            case 4 -> resultado = puzzle.BusquedaIDA(objetivo, false);
            case 5 -> resultado = puzzle.BusquedaIDA(objetivo, true);
        }

        long fin = System.nanoTime();
        double tiempoMs = (fin - inicio) / 1_000_000.0;

        if (resultado != null) {
            System.out.println("\n===== SOLUCIÓN ENCONTRADA =====");
            resultado.imprimirCamino();
            System.out.println("Profundidad (nivel): " + resultado.nivel);
            System.out.println("Nodos Expandidos: " + puzzle.nodosExpandidos);
            System.out.printf("Tiempo de ejecución: %.4f ms\n", tiempoMs);
        } else {
            System.out.println("No se encontró solución.");
        }
    }

    private static void tablaComparativa(String inicial, String objetivo) {
        System.out.println("\n======================== TABLA COMPARATIVA ========================");
        System.out.printf("%-18s | %-12s | %-12s | %-12s\n", "Método", "Profundidad", "Nodos Exp.", "Tiempo (ms)");
        System.out.println("-------------------------------------------------------------------");

        String[] nombres = {"BFS", "DFS", "Costo Uniforme", "IDA* Manhattan", "IDA* C. Lineal"};

        for (int i = 1; i <= 5; i++) {
            final int metodoActual = i;
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Arbol puzzle = new Arbol(new Nodo(inicial));
            
            Callable<Nodo> tarea = () -> {
                switch (metodoActual) {
                    case 1: return puzzle.BusquedaAnchura(objetivo);
                    case 2: return puzzle.BusquedaProfundidad(objetivo);
                    case 3: return puzzle.BusquedaCostoUniforme(objetivo);
                    case 4: return puzzle.BusquedaIDA(objetivo, false);
                    case 5: return puzzle.BusquedaIDA(objetivo, true);
                    default: return null;
                }
            };

            long inicio = System.nanoTime();
            try {
                Future<Nodo> futuro = executor.submit(tarea);
                Nodo res = futuro.get(5, TimeUnit.SECONDS);
                
                long fin = System.nanoTime();
                double tiempoMs = (fin - inicio) / 1_000_000.0;

                if (res != null) {
                    System.out.printf("%-18s | %-12d | %-12d | %-12.4f\n",
                            nombres[i - 1], res.nivel, puzzle.nodosExpandidos, tiempoMs);
                } else {
                    System.out.printf("%-18s | No encontró   | %-12d | %-12.4f\n",
                            nombres[i - 1], puzzle.nodosExpandidos, tiempoMs);
                }
            } catch (TimeoutException e) {
                System.out.printf("%-18s | TIMEOUT      | > 5000ms     | INTERRUMPIDO\n", nombres[i - 1]);
            } catch (Exception | OutOfMemoryError e) {
                System.out.printf("%-18s | ERROR        | ------------ | FALLO\n", nombres[i - 1]);
                System.gc();
            } finally {
                executor.shutdownNow();
            }
        }
        System.out.println("===================================================================\n");
    }
}