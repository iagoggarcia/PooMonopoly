package monopoly;

import partida.*;
import java.util.ArrayList;

class Edificio {

    // Atributos
    private String tipo;
    private String id;
    private Jugador propietario;
    private Casilla lugar;
    private int precio;

    /* Constructor para el edificio
    * Parámetros: tipo de edificio (casa, hotel, piscina, pista de deporte), propietario del edificio, lugar en el que se ubica
    */
    public Edificio (String tipo, Jugador propietario, Casilla lugar) {
        this.tipo = tipo;
        this.propietario = propietario;
        this.lugar = jugar;
        this.precio = lugar.getValorCasa();
        this.id = generarId(); // falta por crear la función que genera los id para los edificios
    }
}