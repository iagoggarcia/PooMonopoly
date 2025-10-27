package monopoly;

import partida.*;
import java.util.ArrayList;

public class Edificio {

    // Atributos
    private String tipo;
    private String id;
    private Jugador propietario;
    private Casilla lugar;
    private int precio;

    // Contadores que uso para generar el id del edificio, con formato -> "tipo-contadorTipo"
    // Son static para que sean comunes a todos los edificios, si no una vez creado un edificio,
    // el siguiente volvía a empezar con id "tipo-1" en vez de "tipo-2"
    private static int contadorCasas = 0;
    private static int contadorHoteles = 0;
    private static int contadorPiscinas = 0;
    private static int contadorPistasDeporte = 0;

    /* Constructor para el edificio
    * Parámetros: tipo de edificio (casa, hotel, piscina, pista de deporte), propietario del edificio, lugar en el que se ubica
    */
    public Edificio (String tipo, Jugador propietario, Casilla lugar) {
        this.tipo = tipo;
        this.propietario = propietario;
        this.lugar = lugar;
        this.precio = lugar.getValorCasayHotel();
        this.id = generarId(); // falta por crear la función que genera los id para los edificios
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Jugador getPropietario() {
        return propietario;
    }

    public void setPropietario(Jugador propietario) {
        this.propietario = propietario;
    }

    public Casilla getLugar() {
        return lugar;
    }

    public void setLugar(Casilla lugar) {
        this.lugar = lugar;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public String generarId() {
        int numId = 0;
        char numIdChar;
        String id = "";

        switch (tipo) {
            case "casa":
                contadorCasas += 1;
                numId = contadorCasas;
                break;
            case "hotel":
                contadorHoteles += 1;
                numId = contadorHoteles;
                break;
            case "piscina":
                contadorPiscinas += 1;
                numId = contadorPiscinas;
                break;
            case "pista":
                contadorPistasDeporte += 1;
                numId = contadorPistasDeporte;
                break;
            default:
                throw new IllegalArgumentException("Tipo desconocido: " + tipo);
        }

        numIdChar = (char) numId;
        id = tipo + '-' + numId;

        return id;
    }

    /** Se puede tener un toString por clase, así que hice este
     * para que salgan los nombres de las casillas bien printeados
     * en el array propiedades del jugador
     * */
    @Override
    public String toString() {
        return this.id;
    }
}