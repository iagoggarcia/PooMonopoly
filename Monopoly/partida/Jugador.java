package partida;

import java.util.ArrayList;
import monopoly.*;


public class Jugador {

    //Atributos:
    private String nombre; //Nombre del jugador
    private Avatar avatar; //Avatar que tiene en la partida.
    private float fortuna; //Dinero que posee.
    private float gastos; //Gastos realizados a lo largo del juego.
    private boolean enCarcel; //Será true si el jugador está en la carcel
    private int tiradasCarcel; //Cuando está en la carcel, contará las tiradas sin éxito que ha hecho allí para intentar salir (se usa para limitar el numero de intentos).
    private int vueltas; //Cuenta las vueltas dadas al tablero.
    private ArrayList<Casilla> propiedades; //Propiedades que posee el jugador.

    //Constructor vacío. Se usará para crear la banca.
    // estaba mal, como pone en el esqueleto es VACÍO, no hay que completar nada
    // en el menu luego se haria antes de crear el tablero algo como Jugador banca = new Jugador(); Tablero tablero = new Tablero(banca);
    public Jugador() {
    }

    /*Constructor principal. Requiere parámetros:
    * Nombre del jugador, tipo del avatar que tendrá, casilla en la que empezará y ArrayList de
    * avatares creados (usado para dos propósitos: evitar que dos jugadores tengan el mismo nombre y
    * que dos avatares tengan mismo ID). Desde este constructor también se crea el avatar.
     */

    public Jugador(String nombre, String tipoAvatar, Casilla inicio, ArrayList<Avatar> avCreados) {
        this.nombre = nombre;
        this.avatar = new Avatar(tipoAvatar, this, inicio, avCreados); // para el avatar hay que crearlo y por defecto le puse que lo cree en la casilla inicial
        this.fortuna = 15000000; // puse la fortuna inicial
        this.gastos = 0; // antes = gastos, incorrecto porque está sin inicializar, no se pasa como parámetro, daría error
        this.enCarcel = false; // antes = enCarcel, lo mismo que arriba
        this.tiradasCarcel = 0; // antes = tiradasCarcel
        this.vueltas = 0; 
        this.propiedades = new ArrayList<>(); // solo hay que inicializar propiedades como  una lista vaćía
    }

    public Jugador(float valor){
        this.fortuna = valor;
    }

    // ----- Getters mínimos que necesita el menú ----- //los añado porque me hacen falta en menu
    public String getNombre() {
        return nombre;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public float getFortuna() {
        return fortuna;
    }

    public boolean isEnCarcel() {
        return enCarcel;
    }

    public ArrayList<Casilla> getPropiedades() {
        return propiedades;
    }

    //Otros métodos:
    //Método para añadir una propiedad al jugador (cuando la compra). Como parámetro, la casilla a añadir.
    public void anhadirPropiedad(Casilla casilla) {
        if (casilla == null) {
            System.out.println("No se puede añadir una propiedad nula.");
            return;
        }
        if (!propiedades.contains(casilla)) { // comprobamos que la casilla no está ya en la lista de propiedades del jugador
            propiedades.add(casilla); // si no está, la añadimos
            System.out.println(nombre + " ha adquirido la propiedad " + casilla.getNombre() + ".");
            // aqui creo que se añadiria casilla.setPropietario(this), pero en esta entrega creo que aun no se pide
        } else {
            System.out.println(nombre + " ya posee la propiedad " + casilla.getNombre() + ".");            
        }
    }

    //Método para eliminar una propiedad del arraylist de propiedades de jugador (venta, hipoteca o eliminación).
    public void eliminarPropiedad(Casilla casilla) {
        if (casilla == null) {
            System.out.println("No se puede eliminar una propiedad nula.");
            return;
        }
        if (propiedades.contains(casilla)) { // comprobamos que la  casilla está en la lista de propiedades del jugador
            propiedades.remove(casilla); // si está, la eliminamos
            System.out.println(nombre + " ha perdido la propiedad " + casilla.getNombre() + ".");
            // aqui creo que se añadiria casilla.setPropietario(null), pero en esta entrega creo que aun no se pide
        } else {
            System.out.println(nombre + " no posee la propiedad " + casilla.getNombre() + ".");
        }
    }

    //Método para añadir fortuna a un jugador
    //Como parámetro se pide el valor a añadir. Si hay que restar fortuna, se pasaría un valor negativo.
    public void sumarFortuna(float valor) { 
        this.fortuna += valor; 
    }

    //Método para sumar gastos a un jugador.
    //Parámetro: valor a añadir a los gastos del jugador (será el precio de un solar, impuestos pagados...).
    public void sumarGastos(float valor) { // sumarGastos no modifica nunca fortuna, solo es para contabilizar los gastos
        // hay que asegurarse de que lo que se acumula es no negativo (siempre se meten valor positivos a gastos)
        if (valor < 0) valor = -valor;
        this.gastos += valor;
    }

    /*Método para establecer al jugador en la cárcel. 
    * Se requiere disponer de las casillas del tablero para ello (por eso se pasan como parámetro).*/
    
    private static final int NUM_CASILLAS = 40;
    private static final int IDX_CARCEL   = 10; // posición fija de "Cárcel"


    // ddevuelve  la casilla con indice lineal en el orden el que fue construido
    private Casilla casillaPorIndice(ArrayList<ArrayList<Casilla>> pos, int indice) {
        if (pos == null || indice < 0 || indice >= NUM_CASILLAS) return null;

        int k = 0; // k ira contando para cada casilla visitada
        for (ArrayList<Casilla> lado : pos) {
            for (Casilla c : lado) {
                if (k == indice) return c; // encontramos la casilla en la posicion buscada
                k++; // avanzamosa a la siguiente
            }
        }
        return null; 
    }
    
    // por ahora solo mueve fisicamente el avatar a la carcel y marca el estado en enCarcel y modifica tiradasCarcel
    // las otras "reglas" (cobrar o pagar dinero, comprobar si se puede salir, etc) se gestionan desde otro sitio, por ejemplo el menu
    public void encarcelar(ArrayList<ArrayList<Casilla>> pos) {
        if (pos == null || pos.isEmpty()) {
            throw new IllegalArgumentException("El tablero (pos) no puede ser nulo ni vacío");
        }
        if (this.avatar == null) {
            throw new IllegalStateException("El jugador no tiene avatar asociado");
        }

        // localizamos la casilla cárcel por índice fijo
        Casilla carcel = casillaPorIndice(pos, IDX_CARCEL);
        if (carcel == null) {
            throw new IllegalStateException("No se encontró la casilla de Cárcel (índice 10)");
        }
        // quitamos al avatar de su casilla actual y lo colocamos en carcel
        Casilla actual = this.avatar.getLugar();
        if (actual != null) actual.eliminarAvatar(this.avatar);  // cuando esté implementado en casilla
        carcel.anhadirAvatar(this.avatar);                       
        this.avatar.setLugar(carcel);                            // actualiza el puntero del avatar

        this.enCarcel = true;
        this.tiradasCarcel = 0;
    }

}
