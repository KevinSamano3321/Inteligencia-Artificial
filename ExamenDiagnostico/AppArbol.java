public class AppArbol {
    public static void main(String[] args) {

        Arbol miArbol = new Arbol();

        System.out.println("El arbol esta vacio?: " + miArbol.vacio());

        miArbol.insertar("Kevin"); 
        miArbol.insertar("Zulema");
        miArbol.insertar("Alexis");
        miArbol.insertar("Bruno");
        miArbol.insertar("Mario");

        
        System.out.print("Contenido del arbol: ");
        miArbol.imprimirArbol();

        String nombreABuscar = "Kevin";
        Nodo resultado = miArbol.buscarNodo(nombreABuscar);
        
        if (resultado != null) {
            System.out.println("\nNodo encontrado: " + resultado.nombre);
        } else {
            System.out.println("\nEl nombre '" + nombreABuscar + "' no existe en el arbol.");
        }

        nombreABuscar = "Jasiel";
        resultado = miArbol.buscarNodo(nombreABuscar);

         if (resultado != null) {
            System.out.println("\nNodo encontrado: " + resultado.nombre);
        } else {
            System.out.println("\nEl nombre '" + nombreABuscar + "' no existe en el arbol.");
        }

        System.out.println("El arbol esta vacio ahora?: " + miArbol.vacio());
    }
}