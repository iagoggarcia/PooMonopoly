package monopoly.casillas.accion;

import monopoly.*;
import monopoly.cartas.*;
import monopoly.casillas.*;
import partida.Jugador;

public abstract class Accion extends Casilla {

    protected Accion (String nombre, int posicion, String tipo) {
        super(nombre, posicion, tipo);
    }

    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {

        Juego juego = Juego.getInstancia();

        // 1. Obtener la siguiente carta del mazo correspondiente
        Carta carta = juego.obtenerCarta(this.tipo);

        // 2. Ejecutar la acción de la carta
        carta.accion(actual, banca, this, juego);

        // 3. Avanzar el índice del mazo
        juego.avanzarIndice(this.tipo);

        // 4. Comprobar solvencia
        if (!juego.isSolvente()) {
            System.out.println(actual.getNombre() + " ha quedado insolvente tras ejecutar la carta.");
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
