package monopoly.casillas.especial;

import partida.Jugador;

public class Carcel extends Especial {

    public Carcel(String nombre, int posicion) {
        super(nombre, posicion);
    }

    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        // de visita o preso lo gestiona el menu
        // caes por movimiento "normal", o ya estabas en la cárcel preso
        System.out.println(actual.getNombre() + " está visitando la Cárcel. No está arrestado.");
        System.out.println(actual.getNombre() + " está en la Cárcel (visita o preso).");
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
