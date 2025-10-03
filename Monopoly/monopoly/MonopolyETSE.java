package monopoly;
import java.util.ArrayList; // faltaba ponerlo para usar arrays

import partida.Jugador;
import partida.Avatar; // también faltaba y por eso daba error al crear un avatar
public class MonopolyETSE {

    public static void main(String[] args) {
        new Menu();
        ArrayList<Avatar> avCreados = new ArrayList<>();
        Jugador banca = new Jugador();
        Tablero tablero = new Tablero(banca); // se le pasa solo la banca al iniciarlo, lo pone en tablero.java

        /*
        * PRUEBAS
        * Solo creo una casilla con nombre salida para ver si en tablero.java está bien guardado todo en cada lado del tablero,
        * hago get(0).get(0) porque como "posiciones" es un array de arrays, accedo al lado sur con el primer get(0) y a la casilla 0,
        * la salida, con el segundo get(0). Es como si estuviera haciendo posiciones[0][0] y eso corresponde a la casilla de salida.
        *
        * */
        Casilla salida = tablero.getPosiciones().get(0).get(0);
        System.out.println(salida.getNombre()); // debería imprimirse el nombre de la salida
        System.out.println(salida.getTipo()); // y el tipo de casilla que le puse en Tablero.java
    }
}