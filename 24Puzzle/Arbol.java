import java.util.*;

public class Arbol {
    Nodo raiz;
    public int nodosExpandidos;

    public Arbol(Nodo raiz) {
        this.raiz = raiz;
    }

    public Nodo BusquedaAnchura(String objetivo) {
        nodosExpandidos = 0;
        Queue<Nodo> cola = new LinkedList<>();
        HashSet<String> visitados = new HashSet<>();
        cola.add(raiz);
        visitados.add(raiz.estado);

        while (!cola.isEmpty()) {
            Nodo actual = cola.poll();
            if (actual.estado.equals(objetivo)) return actual;
            nodosExpandidos++;
            for (Nodo hijo : actual.generarSucesores()) {
                if (!visitados.contains(hijo.estado)) {
                    visitados.add(hijo.estado);
                    cola.add(hijo);
                }
            }
        }
        return null;
    }

    public Nodo BusquedaProfundidad(String objetivo) {
        nodosExpandidos = 0;
        Stack<Nodo> pila = new Stack<>();
        HashSet<String> visitados = new HashSet<>();
        pila.push(raiz);

        while (!pila.isEmpty()) {
            Nodo actual = pila.pop();
            if (actual.estado.equals(objetivo)) return actual;
            if (!visitados.contains(actual.estado)) {
                visitados.add(actual.estado);
                nodosExpandidos++;
                for (Nodo hijo : actual.generarSucesores()) {
                    if (!visitados.contains(hijo.estado)) pila.push(hijo);
                }
            }
        }
        return null;
    }

    public Nodo BusquedaCostoUniforme(String objetivo) {
        nodosExpandidos = 0;
        PriorityQueue<Nodo> frontera = new PriorityQueue<>();
        HashSet<String> visitados = new HashSet<>();
        raiz.costo = raiz.nivel;
        frontera.add(raiz);

        while (!frontera.isEmpty()) {
            Nodo actual = frontera.poll();
            if (actual.estado.equals(objetivo)) return actual;
            if (!visitados.contains(actual.estado)) {
                visitados.add(actual.estado);
                nodosExpandidos++;
                for (Nodo hijo : actual.generarSucesores()) {
                    hijo.costo = hijo.nivel;
                    frontera.add(hijo);
                }
            }
        }
        return null;
    }

    public Nodo BusquedaIDA(String objetivo, boolean usarConflicto) {
        nodosExpandidos = 0;
        int limite = usarConflicto ? raiz.heuristicaConflictoLineal(objetivo) : raiz.heuristicaManhattan(objetivo);
        while (true) {
            Object[] res = buscarRecursivo(raiz, 0, limite, objetivo, usarConflicto);
            if (res[0] instanceof Nodo){
                return (Nodo) res[0];
            } 
            if ((int) res[1] == Integer.MAX_VALUE) {
                return null;
            }
            limite = (int) res[1];
        }
    }

    private Object[] buscarRecursivo(Nodo n, int g, int limite, String obj, boolean hType) {
        int f = g + (hType ? n.heuristicaConflictoLineal(obj) : n.heuristicaManhattan(obj));
        if (f > limite) return new Object[]{null, f};
        if (n.estado.equals(obj)) return new Object[]{n, f};

        int min = Integer.MAX_VALUE;
        nodosExpandidos++;
        for (Nodo hijo : n.generarSucesores()) {
            if (n.padre != null && hijo.estado.equals(n.padre.estado)) continue;
            Object[] res = buscarRecursivo(hijo, g + 1, limite, obj, hType);
            if (res[0] instanceof Nodo) return res;
            if ((int) res[1] < min) min = (int) res[1];
        }
        return new Object[]{null, min};
    }
}