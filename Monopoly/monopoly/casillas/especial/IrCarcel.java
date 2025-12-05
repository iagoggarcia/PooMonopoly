package monopoly.casillas.especial;

import partida.Jugador;

public class IrCarcel extends Especial {

    public IrCarcel(String nombre, int posicion) {
        super(nombre, posicion);
    }

    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        // el menu es el que tiene que mover a la carcel al jugador
        // si caes aquí tienes que moverte a la carcel como preso inmediatamente
        System.out.println(actual.getNombre() + " ha caído en 'Ir a la Cárcel'. Será trasladado al finalizar la tirada.");
        return true;
    }

    @Override
    public String infoCasilla() {
        StringBuilder informacion = new StringBuilder();
        informacion.append(this.getNombre() + " no tiene información que mostrar\n");
        return informacion.toString();
    }

}
