package monopoly.edificios;
import monopoly.casillas.propiedad.Solar;
import monopoly.excepciones.MaximoEdificiosException;
import partida.*;

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
    public static void puedeEdificarHotel(Solar lugar) throws MaximoEdificiosException {
        if (lugar.getNumCasas() < 4) { // si no hay 4 casas
            throw new MaximoEdificiosException("No puedes construir unn hotel: necesitas 4 casas en " + lugar);
        } // Comprobación de errores
        else if (lugar.getNumHoteles() == 1) { // si ya hay un hotel
            throw new MaximoEdificiosException("No puedes construir un hotel: ya existe un hotel en " + lugar);
        }
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