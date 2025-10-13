package monopoly;
import java.util.ArrayList; // faltaba ponerlo para usar arrays

import partida.Jugador;
import partida.Avatar; // también faltaba y por eso daba error al crear un avatar

import monopoly.Menu;
public class MonopolyETSE {
    public static void main(String[] args) {
        Menu menu = new Menu();
        Jugador banca = new Jugador();
        Tablero tablero = new Tablero(banca);

        System.out.println(tablero);
    }
}