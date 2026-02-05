public class Arbol {

    private Nodo raiz;

    public Arbol() {
        this.raiz = null;
    }

    public boolean vacio() {
        return raiz == null;
    }

    public Nodo buscarNodo(String nombre) {
        return buscarRecursivo(raiz, nombre);
    }

    private Nodo buscarRecursivo(Nodo actual, String nombre) {

        if (actual == null || actual.nombre.equals(nombre)) {
            return actual;
        }
        
        if (nombre.compareTo(actual.nombre) < 0) {
            return buscarRecursivo(actual.izquierdo, nombre);
        }
        return buscarRecursivo(actual.derecho, nombre);
    }

    public void insertar(String nombre) {
        raiz = insertarRecursivo(raiz, nombre);
    }

    private Nodo insertarRecursivo(Nodo actual, String nombre) {

        if (actual == null) {
            return new Nodo(nombre);
        }

        if (nombre.compareTo(actual.nombre) < 0) {
            actual.izquierdo = insertarRecursivo(actual.izquierdo, nombre);
        } else if (nombre.compareTo(actual.nombre) > 0) {
            actual.derecho = insertarRecursivo(actual.derecho, nombre);
        }

        return actual;
    }

    public void imprimirArbol() {
        if (vacio()) {
            System.out.println("El árbol está vacío.");
        } else {
            imprimirRecursivo(raiz);
            System.out.println();
        }
    }

    private void imprimirRecursivo(Nodo actual) {
        if (actual != null) {
            imprimirRecursivo(actual.izquierdo);
            System.out.print("[" + actual.nombre + "] ");
            imprimirRecursivo(actual.derecho);
        }
    }
}