package monopoly.casillas.especial;

import monopoly.casillas.*;

public abstract class Especial extends Casilla {
    public Especial(String nombre, int posicion) {
        super(nombre, posicion, "especial");
    }

    // Cada subclase (salida, parking, ...) implementará su propio evaluarCasilla con @Override,
    // por eso no está creado aquí

}
