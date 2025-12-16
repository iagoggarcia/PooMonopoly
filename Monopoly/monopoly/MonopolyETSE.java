package monopoly;

import monopoly.excepciones.MonopolyException;

public class MonopolyETSE {

    public static void main(String[] args) throws MonopolyException {

        Menu menu = new Menu();

        if (args.length == 0) {
            menu.iniciarJuego();
        }
        else {
            String ruta = args[0];
            menu.ejecutarArchivoComandos(ruta);
        }
    }
}
