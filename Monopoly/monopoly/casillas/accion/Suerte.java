package monopoly.casillas.accion;

public class Suerte extends Accion{
    public Suerte(String nombre, int posicion) {
        super(nombre, posicion, "suerte");
    }

    // Se puede quitar porque está definida ya en el padre
    //  pero así se ve más claro que hereda el método
    @Override
    public String infoCasilla() {
        return super.infoCasilla();
    }
}