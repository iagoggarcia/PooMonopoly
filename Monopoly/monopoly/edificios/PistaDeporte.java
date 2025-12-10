package monopoly.edificios;
import monopoly.casillas.propiedad.Solar;
import monopoly.excepciones.MaximoEdificiosException;
import partida.*;

public class PistaDeporte extends Edificio {
    private static int contadorPistasDeporte = 0;

    public PistaDeporte (Jugador propietario, Solar lugar) {
        super("pista", propietario, lugar);
    }

    /*
     * Función que comprueba si se puede crear una pista de deporte o no. Si no se puede es porque no hay un hotel ni piscina,
     * porque no hay hotel, porque no hay piscina o porque ya hay una pista(el dinero del jugador se comprueba en otra función),
     * y cada uno de esos errores tiene un código: el primero 1, el segundo 2, el tercero 3 y el último 4. La función retorna 0
     * cuando SÍ se puede construir.
     * */
    public static void  puedeEdificarPista(Solar lugar) throws MaximoEdificiosException{
        if (lugar.getNumHoteles() < 1 && lugar.getNumPiscinas() < 1) { // si no hay hotel ni piscina
            throw new MaximoEdificiosException("No puedes construir una pista de deporte: necesitas un hotel y una piscina en " + lugar);
        } // Comprobación de errores
        else if (lugar.getNumHoteles() < 1) { // si no hay hotel
            throw new MaximoEdificiosException("No puedes construir una pista de deporte: necesitas un hotel en " + lugar);
        }
        else if (lugar.getNumPiscinas() < 1) { // si no hay piscina
            throw new MaximoEdificiosException("No puedes construir una pista de deporte: necesitas una piscina en " + lugar);
        }
        else if (lugar.getNumPistas() == 1) { // si ya hay pista
            throw new MaximoEdificiosException("No puedes construir una pista de deporte: ya existe una pista de deporte en " + lugar);
        }
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