package monopoly.casillas;

import java.util.ArrayList;
import partida.*;
import monopoly.edificios.*;
import monopoly.*;

import javax.print.DocFlavor;

public abstract class Casilla {

    //Atributos comunes a todas las casillas:
    protected String nombre; //Nombre de la casilla
    protected int posicion; //Posición que ocupa la casilla en el tablero (entero entre 1 y 40).
    protected ArrayList<Avatar> avatares; //Avatares que están situados en la casilla.
    protected String tipo; //Tipo de casilla (Solar, Especial, Transporte, Servicios, Comunidad, Suerte y Impuesto).
    protected int contador; // indica cuantas veces ha caido un jugador en esa casilla

    //CONSTRUCTOR:
    protected Casilla(String nombre, int posicion, String tipo) {
        this.nombre = nombre;
        this.posicion = posicion;
        this.tipo = tipo;
        this.avatares = new ArrayList<>();
        this.contador = 0;
    }

    /* ---------- MÉTODOS MÍNIMOS DEL ENUNCIADO Y OTROS ---------- */

    // Función que mira si un determinado avatar está en la casilla (tiene que estar en el array avatares)
    public boolean estaAvatar(Avatar avatar) {
        if (avatar == null || avatares == null) return false;
        return avatares.contains(avatar);
    }

    // Función que aumenta el contador de visitas. Se devuelve con la funcion de debajo frecuenciaVisita()
    public void registrarVisita(Avatar avatar) {
        anhadirAvatar(avatar);
        contador++;
    }

    // Función que devuelve el número de visitas de una casilla
    public int frecuenciaVisita() {
        return contador;
    }

    /* Se puede tener un toString por clase, así que hice este para que salgan los nombres de las casillas
     * bien impresos en el array propiedades del jugador
     */
    @Override
    public String toString() {
        return this.nombre;
    }

    /*
     * Devuelve un texto con los jugadores (por nombre) que están en esta casilla. Si no hay ninguno, devuelve una cadena vacía.
     */
    protected String infoJugadoresEnEstaCasilla() {
        if (avatares == null || avatares.isEmpty()) {
            return ""; // no hay jugadores
        }

        ArrayList<String> nombres = new ArrayList<>();
        for (Avatar av : avatares) {
            if (av != null && av.getLugar() == this && av.getJugador() != null) {
                nombres.add(av.getJugador().getNombre());
            }
        }

        if (nombres.isEmpty()) {
            return "";
        }

        return "\nJugadores: " + nombres;
    }

    /*Método para evaluar qué hacer en una casilla concreta. Parámetros:
     * - Jugador cuyo avatar está en esa casilla.
     * - La banca (para ciertas comprobaciones).
     * - El valor de la tirada: para determinar impuesto a pagar en casillas de servicios.
     * Valor devuelto: true en caso de ser solvente (es decir, de cumplir las deudas), y false
     * en caso de no cumplirlas.*/
    public abstract boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada);

    /*public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        if (actual == null || banca == null)
            throw new IllegalArgumentException("Los jugadores (actual o banca) no pueden ser nulos.");

        String tipoCasilla = (this.tipo == null) ? " " : this.tipo.toLowerCase();
        String n = (this.nombre == null) ? " " : this.nombre.toLowerCase();


    /*Método para mostrar información sobre una casilla.
     * Devuelve una cadena con información específica de cada tipo de casilla.
     * Será implementado por cada subclase (solar, transporte, etc.) */
    public abstract String infoCasilla();

    /* ----------------------------------------------------------- */


    /* ---------- GESTIÓN DE AVATARES ---------- */

    //Método utilizado para añadir un avatar al array de avatares en casilla.
    public void anhadirAvatar(Avatar av) {
        if (av == null) return;
        // si la lista interna de avatares aún no existe se crea
        if (this.avatares == null) this.avatares = new ArrayList<>();

        if (!this.avatares.contains(av)) { // comprobamos que el avatar no esté ya en la lista
            this.avatares.add(av); // si no está, se añade
        }
        // aquí no se modifica el lugar, eso se hace en Avatar o Jugador
    }

    //Método utilizado para eliminar un avatar del array de avatares en casilla.
    public void eliminarAvatar(Avatar av) {
        if (av == null || this.avatares == null) return;
        this.avatares.remove(av);
    }

    /* ----------------------------------------- */

    /* ---------- GETTERS Y SETTERS ---------- */

    public String getNombre() {
        return nombre;
    }

    public int getPosicion() {
        return posicion;
    }

    public ArrayList<Avatar> getAvatares() {
        return avatares;
    }

    public String getTipo() {
        return tipo;
    }

    public int getContador() {
        return contador;
    }

    public void setContador(int contador) {
        this.contador = contador;
    }

    /* --------------------------------------- */
}