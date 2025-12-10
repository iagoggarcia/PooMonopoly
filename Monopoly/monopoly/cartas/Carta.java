package monopoly.cartas;

import monopoly.Juego;
import monopoly.casillas.accion.Accion;
import monopoly.excepciones.CasillaInexistenteException;
import monopoly.excepciones.JugadorBancarrotaException;
import monopoly.excepciones.JugadorNoExisteException;
import partida.Jugador;

public abstract class Carta {
    protected  int id; // número de carta del 1 al 6
    protected  String descripcion; 
    
    // constructor
    public Carta(int id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    // Método asbtracto que deben implementar todas las cartas. 
    // Aquí va a ir la acción concreta de la carta (mover, cobrar, pagar, etc.)
    // No se implementa en Carta, solo en las subclases.
    public abstract void accion(Jugador jugador, Jugador banca, Accion casilla, Juego juego) throws JugadorBancarrotaException, CasillaInexistenteException, JugadorNoExisteException;
}

