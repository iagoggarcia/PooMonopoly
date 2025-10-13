package partida;

import monopoly.*;

import java.util.ArrayList;


public class Avatar {

    //Atributos
    private String id; //Identificador: una letra generada aleatoriamente.
    private String tipo; //Sombrero, Esfinge, Pelota, Coche
    private Jugador jugador; //Un jugador al que pertenece ese avatar.
    private Casilla lugar; //Los avatares se sitúan en casillas del tablero.

    //Constructor vacío
    public Avatar() {
    }


    /*Constructor principal. Requiere éstos parámetros:
    * Tipo del avatar, jugador al que pertenece, lugar en el que estará ubicado, y un arraylist con los
    * avatares creados (usado para crear un ID distinto del de los demás avatares).
     */
    public Avatar(String tipo, Jugador jugador, Casilla lugar, ArrayList<Avatar> avCreados) {
        this.tipo = tipo; // cadena con el tipo de avatar
        this.jugador = jugador; // el dueño del avatar (quien lo va a mover)
        this.lugar = lugar; // la casilla  inicial donde aparecerá el avatar (salida normalmente)
        
        generarId(avCreados); // pone el this.id, no añade el avatar a avCreados, solo genera el id

        // colocamos el avatar en la casilla que se pasa como argumento
        if (this.lugar != null) {
            try { this.lugar.anhadirAvatar(this); } // coloca el avatar en la lista de avatares de la casilla
        }

        if (!avCreados.contains(this)) { // registramos el avatar en la lista de avatares
            avCreados.add(this);
        }
    }

    // ----- Getters / setters mínimos ----- //los añado porque hacen falta para menu
    public String getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public Casilla getLugar() {
        return lugar;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setLugar(Casilla lugar) {
        this.lugar = lugar;
    }

    //A continuación, tenemos otros métodos útiles para el desarrollo del juego.
    /*Método que permite mover a un avatar a una casilla concreta. Parámetros:
    * - Un array con las casillas del tablero. Se trata de un arrayList de arrayList de casillas (uno por lado).
    * - Un entero que indica el numero de casillas a moverse (será el valor sacado en la tirada de los dados).
    * EN ESTA VERSIÓN SUPONEMOS QUE valorTirada siemrpe es positivo.
     */
    private static final int NUM_CASILLAS = 40;

    // devuelve casilla por indice lineal
    private Casilla casillaPorIndice(ArrayList<ArrayList<Casilla>> pos, int indice) {
        if (pos == null || indice < 0 || indice >= NUM_CASILLAS) return null;
        int k = 0;
        for (ArrayList<Casilla> lado : pos) {
            if (lado == null) continue;
            for (Casilla c : lado) {
                if (c == null) continue;
                if (k == indice) return c;
                k++;
            }
        }
        return null; // con tablero fijo bien formado, no deberíamos llegar aquí
    }

    // indice lineal de una casilla concreta
    private int indiceDeCasilla(ArrayList<ArrayList<Casilla>> pos, Casilla objetivo) {
        if (pos == null || objetivo == null) return -1;
        int k = 0;
        for (ArrayList<Casilla> lado : pos) {
            if (lado == null) continue;
            for (Casilla c : lado) {
                if (c == objetivo) return k; // comparación por identidad (misma instancia)
                // si no la encuentra por identidad, la busca por nombre
                try {
                    if (c != null && c.getNombre() != null && c.getNombre().equals(objetivo.getNombre())) {
                        return k;
                    }
                } catch (Exception ignored) {}
                k++;
            }
        }
        return -1;
    }

    // mover avatar SIN REGLAS
    public void moverAvatar(ArrayList<ArrayList<Casilla>> casillas, int valorTirada) {

        if (casillas == null || casillas.isEmpty())
            throw new IllegalArgumentException("El tablero no puede ser nulo ni vacío");
        if (this.lugar == null)
            throw new IllegalStateException("El avatar no tiene casilla actual (lugar == null)");
        if (valorTirada < 0)
            throw new IllegalArgumentException("valorTirada debe ser >= 0");

        // indice actual del avatar
        int indiceActual = indiceDeCasilla(casillas, this.lugar);
        if (indiceActual == -1)
            throw new IllegalStateException("No se encuentra la casilla actual del avatar en el tablero");

        // indice destino (modulo fijo es el num de casillas = 40)
        int indiceNuevo = (indiceActual + valorTirada) % NUM_CASILLAS;

        // obtener casilla destino
        Casilla destino = casillaPorIndice(casillas, indiceNuevo);
        if (destino == null)
            throw new IllegalStateException("No se pudo localizar la casilla destino (índice " + indiceNuevo + ")");

        // quitar de origen y poner en destino (solo movimiento físico)
        try {
            if (this.lugar != null) this.lugar.eliminarAvatar(this);
        } catch (UnsupportedOperationException ignored) {} // por si aún es stub
        try {
            destino.anhadirAvatar(this);
        } catch (UnsupportedOperationException ignored) {}

        // actualizar referencia interna
        this.lugar = destino;
    }

    /*Método que permite generar un ID para un avatar. Sólo lo usamos en esta clase (por ello es privado).
    * El ID generado será una letra mayúscula. Parámetros:
    * - Un arraylist de los avatares ya creados, con el objetivo de evitar que se generen dos ID iguales.
     */
    private void generarId(ArrayList<Avatar> avCreados) {
        for (char c = 'A'; c <= 'Z'; c++) { // recorre todas las letras para buscar una letra libre
            boolean libre = true; // asumimos que la letra si esta libre
            for (Avatar a : avCreados) { // comprobamos que el avatar a y su id no son nulos
                if (a != null && a.id != null && a.id.equals(String.valueOf(c))) { // convierte char c a string y comprueba si esta libre o no
                    libre = false; // si no lo esta libre es false
                    break;
                }
            }
            if (libre) { // si despues de revisar toda la lista libre es true, nadie esta usando esa letra
                this.id = String.valueOf(c); // asignamos esa letra como id del avatar
                return;
            }
        }
        throw new IllegalStateException("No quedan IDs libres entre A y Z para avatares");
    }
}
