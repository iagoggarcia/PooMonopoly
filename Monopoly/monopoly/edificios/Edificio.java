package monopoly.edificios;

import partida.*;
import monopoly.casillas.propiedad.*;

public abstract class Edificio {

    // Atributos
    private String tipo;
    private String id;
    private Jugador propietario;
    private Solar lugar;
    private int precio;

    /* Constructor para el edificio
    * Parámetros: tipo de edificio (casa, hotel, piscina, pista de deporte), propietario del edificio, lugar en el que se ubica
    */
    public Edificio (String tipo, Jugador propietario, Solar lugar) {
        this.tipo = tipo.toLowerCase();
        this.propietario = propietario;
        this.lugar = lugar;
        // El switch es para asignarle un valor concreto que tiene ese tipo de edificio en la casilla en la que se quiere construir
        switch (this.tipo) {
            case "casa":
            case "hotel":
                precio = lugar.getValorCasayHotel();
                break;
            case "piscina":
                precio = lugar.getValorPiscina();
                break;
            case "pista":
                precio = lugar.getValorPistaDeporte();
        }
        this.id = generarId(); // falta por crear la función que genera los id para los edificios
    }

    public String getTipo() {
        return tipo;
    }

    public String getId() {
        return id;
    }


    public Jugador getPropietario() {
        return propietario;
    }

    public void setPropietario(Jugador propietario) {
        this.propietario = propietario;
    }

    public Solar getLugar() {
        return lugar;
    }


    public int getPrecio() {
        return precio;
    }


    // se escribe aquí como abstract para que las subclases (casa, hotel, piscina, pista) tengan que implementarlo sí o sí.
    // Se implementa con @Override porque tienen el mismo nombre
    protected abstract String generarId();

    /** Se puede tener un toString por clase, así que hice este
     * para que salgan los nombres de las casillas bien printeados
     * en el array propiedades del jugador
     * */
    @Override
    public String toString() {
        return this.id;
    }
}