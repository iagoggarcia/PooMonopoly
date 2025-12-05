package monopoly.casillas.accion;

public class Caja extends Accion {
    public Caja(String nombre, int posicion) {
        super(nombre, posicion, "caja de comunidad");
    }

    // Se puede quitar porque está definida ya en el padre (Accion.java)
    //  pero así se ve más claro que hereda el método
    @Override
    public String infoCasilla() {
        return super.infoCasilla();
    }
}
