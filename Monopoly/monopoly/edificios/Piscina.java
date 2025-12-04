package monopoly.edificios;
import monopoly.casillas.propiedad.Solar;
import partida.*;
import monopoly.casillas.Casilla;

public class Piscina extends Edificio {
    private static int contadorPiscinas = 0;

    public Piscina (Jugador propietario, Solar lugar) {
        super("piscina", propietario, lugar);
    }

    /*
     * Función que comprueba si se puede crear una piscina o no. Si no se puede es porque no hay un hotel
     * o porque ya hay una piscina (el dinero del jugador se comprueba en otra función), y cada uno de esos errores
     * tiene un código: el primero 1 y el segundo 2. La función retorna 0 cuando SÍ se puede construir.
     * */
    public static int puedeEdificarPiscina(Solar lugar) {
        if (lugar.getNumHoteles() < 1) { // si no hay hotel
            return 1;
        } // Comprobación de errores
        else if (lugar.getNumPiscinas() == 1) { // si ya hay piscina
            return 2;
        }
        else return 0;
    }

    /*
    Función que crea un id para cada piscina utilizando el número de piscinas
    */
    @Override
    public String generarId() {
        contadorPiscinas++;
        return ("piscina-" + contadorPiscinas);
    }
}
