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
        
        generarId(avCreados); // pone el this.id, no añadeel avatar a avCreados, solo genera el id

        // colocamos el avatar en la casilla que se pasa como argumento
        if (this.lugar != null) {
            try { this.lugar.anhadirAvatar(this); } // coloca el avatar en la lista de avatares de la casilla, da error porque aun no esta hecho anhadirAvatar
            catch (UnsupportedOperationException e) { } // esto es para que como aun no esta todo en casilla no de error
            catch (RuntimeException e) { throw  e;} // esto es para que si da error por otra cosa verlo
        }

        if (!avCreados.contains(this)) { // registramos el avatar en la lista de avatares
            avCreados.add(this);
        }
    }

    //A continuación, tenemos otros métodos útiles para el desarrollo del juego.
    /*Método que permite mover a un avatar a una casilla concreta. Parámetros:
    * - Un array con las casillas del tablero. Se trata de un arrayList de arrayList de casillas (uno por lado).
    * - Un entero que indica el numero de casillas a moverse (será el valor sacado en la tirada de los dados).
    * EN ESTA VERSIÓN SUPONEMOS QUE valorTirada siemrpe es positivo.
     */
    public void moverAvatar(ArrayList<ArrayList<Casilla>> casillas, int valorTirada) {
    }

    /*Método que permite generar un ID para un avatar. Sólo lo usamos en esta clase (por ello es privado).
    * El ID generado será una letra mayúscula. Parámetros:
    * - Un arraylist de los avatares ya creados, con el objetivo de evitar que se generen dos ID iguales.
     */
    private void generarId(ArrayList<Avatar> avCreados) {
        for (char c = 'A'; c <= 'Z'; c++0) {
            boolean libre = true;
            for (Avatar a : avCreados) {
                if (a != null && a.id != null && a.id.equals(String.valueOf(c))) {
                    libre = false;
                    break;
                }
            }
            if (libre)
        }
    }
}
