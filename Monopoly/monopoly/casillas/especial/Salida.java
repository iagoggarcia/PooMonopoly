package monopoly.casillas.especial;

import monopoly.Juego;
import partida.*;

public class Salida extends Especial {

    public Salida(String nombre, int posicion) {
        super(nombre, posicion);
    }

    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        Juego.consola.imprimir(actual.getNombre() + " está en la casilla de Salida. ¡Buen viaje!");
        return true;
    }

    @Override
    public String infoCasilla() {
        StringBuilder informacion = new StringBuilder();

        informacion.append("{");
        informacion.append(infoJugadoresEnEstaCasilla());
        informacion.append("\n}");

        return informacion.toString();
    }

}
