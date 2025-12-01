package monopoly;
import partida.*;

public class PistaDeporte extends Edificio {
    private static int contadorPistasDeporte = 0;

    public PistaDeporte (Jugador propietario, Casilla lugar) {
        super("pista", propietario, lugar);
    }

    /*
     * Función que comprueba si se puede crear una pista de deporte o no. Si no se puede es porque no hay un hotel ni piscina,
     * porque no hay hotel, porque no hay piscina o porque ya hay una pista(el dinero del jugador se comprueba en otra función),
     * y cada uno de esos errores tiene un código: el primero 1, el segundo 2, el tercero 3 y el último 4. La función retorna 0
     * cuando SÍ se puede construir.
     * */
    public static int puedeEdificarPista(Casilla lugar) {
        if (lugar.getNumHoteles() < 1 && lugar.getNumPiscinas() < 1) { // si no hay hotel ni piscina
            return 1;
        } // Comprobación de errores
        else if (lugar.getNumHoteles() < 1) { // si no hay hotel
            return 2;
        }
        else if (lugar.getNumPiscinas() < 1) { // si no hay piscina
            return 3;
        }
        else if (lugar.getNumPistas() == 1) { // si ya hay pista
            return 4;
        }
        else return 0;
    }

    /*
    Función que crea un id para pista de deporte casa utilizando el número de pistas
    */
    @Override
    public String generarId() {
        contadorPistasDeporte++;
        return ("pista-" + contadorPistasDeporte);
    }
}