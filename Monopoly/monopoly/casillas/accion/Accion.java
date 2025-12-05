package monopoly.casillas.accion;

import monopoly.casillas.*;
import partida.Jugador;
import monopoly.*;

public abstract class Accion extends Casilla {

    protected Accion (String nombre, int posicion, String tipo) {
        super(nombre, posicion, tipo);
    }

    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        System.out.println(actual.getNombre() + " ha caído en una casilla de " + this.tipo + ".");

        // ejecutamos la carta correspondiente
        Juego.getInstancia().ejecutarCartas(this.tipo, actual, banca, this);
        if (!Juego.getInstancia().isSolvente()) {
            System.out.println(actual.getNombre() + " ha quedado insolvente tras ejecutar la carta y se declara en bancarrota.");
            return false;
        }
        return true;
    }

    @Override
    public String infoCasilla() {
        StringBuilder sb = new StringBuilder();

        sb.append("{");
        sb.append("\nTipo: ").append(tipo);
        sb.append(infoJugadoresEnEstaCasilla());
        sb.append("\n}");

        return sb.toString();
    }

}
