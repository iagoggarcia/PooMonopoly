package monopoly;
import partida.*;

public class Casa extends Edificio {
    private static int contadorCasas = 0;

    public Casa (Jugador propietario, Casilla lugar) {
        super("casa", propietario, lugar);
    }

    /*
    * Función que comprueba si se puede crear otra casa o no. Si no se puede es porque ya hay 4 casas
    * o porque ya hay un hotel (el dinero del jugador se comprueba en otra función), y cada uno de esos errores
    * tiene un código: el primero 1 y el segundo 2. La función retorna 0 cuando SÍ se puede construir.
    * */
    public static int puedeEdificarCasa(Casilla lugar) {
        if (lugar.getNumCasas() == 4) { // si ya hay 4 casas
            return 1;
        } // Comprobación de errores
        else if (lugar.getNumHoteles() == 1) { // si ya hay un hotel
            return 2;
        }
        else return 0;
    }

    /*
    Función que crea un id para cada casa utilizando el número de casas
     */
    @Override
    public String generarId() {
        contadorCasas++;
        return ("casa-" + contadorCasas);
    }
}

