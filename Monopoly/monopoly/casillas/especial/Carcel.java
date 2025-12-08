package monopoly.casillas.especial;

import monopoly.Juego;
import partida.Jugador;

public class Carcel extends Especial {

    public Carcel(String nombre, int posicion) {
        super(nombre, posicion);
    }

    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        // de visita o preso lo gestiona el menu
        // caes por movimiento "normal", o ya estabas en la cárcel preso
        Juego.consola.imprimir(actual.getNombre() + " está visitando la Cárcel. No está arrestado.");
        Juego.consola.imprimir(actual.getNombre() + " está en la Cárcel (visita o preso).");
        return true;
    }

    @Override
    public String infoCasilla() {
        StringBuilder informacion = new StringBuilder();

        informacion.append("{");
        informacion.append("\nSalir: 500000");
        informacion.append(infoJugadoresEnEstaCasilla());
        informacion.append("\n}");

        return informacion.toString();
    }

}
