package monopoly.edificios;
import monopoly.casillas.propiedad.Solar;
import partida.*;
import monopoly.casillas.Casilla;

public class Hotel extends Edificio {
    private static int contadorHoteles = 0;

    public Hotel (Jugador propietario, Solar lugar) {
        super("hotel", propietario, lugar);
    }

    /*
     * Función que comprueba si se puede crear un hotel o no. Si no se puede es porque no hay 4 casas
     * o porque ya hay un hotel (el dinero del jugador se comprueba en otra función), y cada uno de esos errores
     * tiene un código: el primero 1 y el segundo 2. La función retorna 0 cuando SÍ se puede construir.
     * */
    public static int puedeEdificarHotel(Solar lugar) {
        if (lugar.getNumCasas() < 4) { // si no hay 4 casas
            return 1;
        } // Comprobación de errores
        else if (lugar.getNumHoteles() == 1) { // si ya hay un hotel
            return 2;
        }
        else return 0;
    }

    /*
    Función que crea un id para cada hotel utilizando el número de hoteles
     */
    @Override
    public String generarId() {
        contadorHoteles++;
        return ("hotel-" + contadorHoteles);
    }
}