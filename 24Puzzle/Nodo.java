import java.util.*;

public class Nodo implements Comparable<Nodo> {
    String estado;
    int nivel; 
    int costo;   
    Nodo padre;
    private static final int LADO = 5;

    public Nodo(String estado) {
        this.estado = estado;
        this.nivel = 0;
        this.costo = 0;
        this.padre = null;
    }

    public Nodo(String estado, int nivel, Nodo padre) {
        this.estado = estado;
        this.nivel = nivel;
        this.costo = 0;
        this.padre = padre;
    }

    @Override
    public int compareTo(Nodo otro) {
        return Integer.compare(this.costo, otro.costo);
    }

    public LinkedList<Nodo> generarSucesores() {
        LinkedList<Nodo> sucesores = new LinkedList<>();
        int indice = this.estado.indexOf(' ');
        int fila = indice / LADO;
        int col = indice % LADO;

        int[][] movimientos = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] m : movimientos) {
            int nFila = fila + m[0];
            int nCol = col + m[1];

            if (nFila >= 0 && nFila < LADO && nCol >= 0 && nCol < LADO) {
                int nIndice = nFila * LADO + nCol;
                sucesores.add(new Nodo(intercambiar(indice, nIndice), this.nivel + 1, this));
            }
        }
        return sucesores;
    }

    private String intercambiar(int i, int j) {
        char[] caracteres = this.estado.toCharArray();
        char temp = caracteres[i];
        caracteres[i] = caracteres[j];
        caracteres[j] = temp;
        return new String(caracteres);
    }

    public int heuristicaManhattan(String objetivo) {
        int h = 0;
        for (int i = 0; i < estado.length(); i++) {
            char c = estado.charAt(i);
            if (c != ' ') {
                int posObj = objetivo.indexOf(c);
                h += Math.abs(i / LADO - posObj / LADO) + Math.abs(i % LADO - posObj % LADO);
            }
        }
        return h;
    }

    public int heuristicaConflictoLineal(String objetivo) {
        int h = heuristicaManhattan(objetivo);
        int conflicto = 0;
        for (int f = 0; f < LADO; f++) {
            for (int i = 0; i < LADO; i++) {
                for (int j = i + 1; j < LADO; j++) {
                    char c1 = estado.charAt(f * LADO + i);
                    char c2 = estado.charAt(f * LADO + j);
                    if (c1 != ' ' && c2 != ' ') {
                        int o1 = objetivo.indexOf(c1);
                        int o2 = objetivo.indexOf(c2);
                        if (o1 / LADO == f && o2 / LADO == f && o1 > o2){
                            conflicto += 2;
                        } 
                    }
                }
            }
        }
        return h + conflicto;
    }

    public void imprimirCamino() {
        ArrayList<Nodo> camino = new ArrayList<>();
        Nodo actual = this;
        while (actual != null) {
            camino.add(actual);
            actual = actual.padre;
        }
        Collections.reverse(camino);
        for (Nodo n : camino) {
            for (int i = 0; i < n.estado.length(); i++) {
                System.out.printf("[%c] ", n.estado.charAt(i));
                if ((i + 1) % LADO == 0) System.out.println();
            }
            System.out.println("Nivel: " + n.nivel + " | f(n): " + n.costo);
            System.out.println("-------------------------");
        }
    }
}