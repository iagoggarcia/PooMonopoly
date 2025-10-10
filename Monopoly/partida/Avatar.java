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
            try { this.lugar.anhadirAvatar(this); } // coloca el avatar en la lista de avatares de la casilla, da error porque aun no esta hecho anhadirAvatar
            catch (UnsupportedOperationException e) { } // esto es para que como aun no esta todo en casilla no de error
            catch (RuntimeException e) { throw  e;} // esto es para que si da error por otra cosa verlo
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
    // sin reglas por ahora, solo para poder mover el avatar
    public void moverAvatar(ArrayList<ArrayList<Casilla>> casillas, int valorTirada) {
        // comprobaciones básicas d si está vacío las hago después

        // convertimos la lista de listas en una lista lineal de casillas
        ArrayList<Casilla> recorrido = new ArrayList<>();
        for (ArrayList<Casilla> lado : casillas) {
            if (lado == null) continue;
            for (Casilla c : lado){
                if (c != null) recorrido.add(c);
            }
        }

        // econtramos la posición actual del avatar dentro de ese recorrido
        int indiceActual = recorrido.indexOf(this.lugar);
        if (indiceActual == -1) {
            for (int i = 0; i < recorrido.size(); i++){
                Casilla c = recorrido.get(i);
                try {
                    if (c != null && c.getNombre().equals(this.lugar.getNombre())) {
                        indiceActual = i;
                        break;
                    }
                } catch (Exception ignored) {}
            }
            if (indiceActual == -1) {
                throw new IllegalStateException("No se encuentra la casilla actual en el tablero");
            }
        }
        
        // calculamos el nuevo índice (módulo número total de casillas)
        int totalCasillas = recorrido.size();      // normalmente 40
        int indiceNuevo = (indiceActual + valorTirada) % totalCasillas;

        // quitamos el avatar de la casilla actual y lo añadimos en la nueva
        Casilla origen  = recorrido.get(indiceActual);
        Casilla destino = recorrido.get(indiceNuevo);
        try { if (origen  != null) origen.eliminarAvatar(this); } catch (UnsupportedOperationException e) { }
        try { if (destino != null) destino.anhadirAvatar(this); } catch (UnsupportedOperationException e) { } // no esta hecho 

        // actualizamos su posición interna
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
        throw new IllegalStateException("No quedan IDs libres estre A y Z para avatares");
    }
}
