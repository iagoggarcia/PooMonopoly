package monopoly;
import java.util.ArrayList; // faltaba ponerlo para usar arrays

import partida.Jugador;
import partida.Avatar; // también faltaba y por eso daba error al crear un avatar

import monopoly.Menu;

public class MonopolyETSE {
    public static void main(String[] args) {
        Menu menu = new Menu();

        if (args.length == 0) {
            menu.iniciarPartida();//si no le pasamos un archivo por comandos, iniciamos la partida de 0 de manera normal
        }
        else {
            String ruta = args[0];
            menu.ejecutarArchivoComandos(ruta);//si nos llega un archivo por linea de comandos, lo leemos y analizamos que comandos debemos usar, e iniciamos la partida de una forma distinta
        }
    }
}
