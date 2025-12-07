//aqui tengo que crear una funcion que sea iniciar juego y llame a juego.iniciarPartida
//tambien tiene que mostrarsele el menu de comandos al usuario desde aqui y no puede inicializarse nada desde este .java
package monopoly;
import java.util.Scanner;

public class Menu{

    private Juego juego;
    private Consola consola;
    private Scanner input;
    private void analizarComando(String comando) {

        consola.imprimir("\n> " + comando);

        // Esto lo miro luego
        /*if (juego.estaEnSubmenuBancarrota()) {
            if (!comando.startsWith("hipotecar") && !comando.equalsIgnoreCase("bancarrota")) {
                consola.imprimir("Debe hipotecar propiedades o declararse en bancarrota.");
                consola.imprimir(" - hipotecar <casilla>");
                consola.imprimir(" - bancarrota");
                return;
            }
        }*/


        if (comando.equalsIgnoreCase("ver tablero")) {
            juego.imprimirTablero();
            return;
        }

        if (comando.startsWith("describir jugador")) {
            String nombre = comando.substring("describir jugador".length()).trim();
            juego.descJugador(nombre);
            return;
        }

        if (comando.startsWith("describir avatar")) {
            String id = comando.substring("describir avatar".length()).trim();
            juego.descAvatar(id);
            return;
        }

        if (comando.startsWith("describir")) {
            String casilla = comando.substring("describir".length()).trim();
            juego.descCasilla(casilla);
            return;
        }

        if (comando.equalsIgnoreCase("lanzar dados")) {
            juego.lanzarDados();
        }
        //tuve que cambiar esta función, al menos la forma de recoger los datos porque ahora la recgida se hace fuera de juego, y es mejor pasarle solo 2 enteros
        if (comando.startsWith("lanzar dados ")) {
            String valores = comando.substring("lanzar dados ".length()).trim(); // "5+1"
            String[] nums = valores.split("\\+");

            if (nums.length != 2) {
                consola.imprimir("Formato inválido. Usa: lanzar dados X+Y");
                return;
            }

            int d1 = Integer.parseInt(nums[0]);
            int d2 = Integer.parseInt(nums[1]);

            juego.lanzarDadosValor(d1, d2);
            return;
        }


        if (comando.startsWith("comprar")) {
            String casilla = comando.substring("comprar".length()).trim();
            juego.comprar(casilla);
            return;
        }

        if (comando.startsWith("salir cárcel")) {
            juego.salirCarcel();
            return;
        }

        if (comando.equalsIgnoreCase("listar jugadores")) {
            juego.listarJugadores();
            return;
        }

        if (comando.equalsIgnoreCase("listar enventa")) {
            juego.listarVenta();
            return;
        }

        if (comando.equalsIgnoreCase("listar avatares")) {
            juego.listarAvatares();
            return;
        }

        if (comando.equalsIgnoreCase("listar edificios")) {
            juego.listarEdificios();
            return;
        }

        if (comando.startsWith("listar edificios ")) {
            String grupo = comando.substring("listar edificios".length()).trim();
            juego.listarEdificiosGrupo(grupo);
            return;
        }

        if (comando.startsWith("vender ")) {

            String args = comando.substring("vender".length()).trim();
            // args = "casas Solar3 2"

            String[] partes = args.split("\\s+");

            if (partes.length != 3) {
                consola.imprimir("Uso: vender <tipoEdificio> <nombreCasilla> <cantidad>");
                return;
            }

            String tipo = partes[0];          // casas / hoteles / piscina / pista
            String nombreCasilla = partes[1]; // Ej: Solar3
            int cantidad;

            try {
                cantidad = Integer.parseInt(partes[2]);
            } catch (NumberFormatException e) {
                consola.imprimir("La cantidad debe ser un número entero.");
                return;
            }

            juego.gestionarVentaEdificios(tipo, nombreCasilla, cantidad);
            return;
        }


        if (comando.equalsIgnoreCase("acabar turno")) {
            juego.acabarTurno();
            return;
        }

        if (comando.startsWith("crear jugador ")) {

            String datos = comando.substring("crear jugador".length()).trim();
            String[] partes = datos.split("\\s+");

            if (partes.length != 2) {
                consola.imprimir("Uso: crear jugador <nombre> <tipoAvatar>");
                return;
            }

            String nombre = partes[0];
            String tipo = partes[1];

            juego.crearJugadorArchivo(nombre, tipo);
            return;
        }


        if (comando.startsWith("hipotecar")) {
            String casilla = comando.substring("hipotecar".length()).trim();
            juego.hipotecar(casilla);
            return;
        }

        if (comando.startsWith("deshipotecar")) {
            String casilla = comando.substring("deshipotecar".length()).trim();
            juego.deshipotecar(casilla);
            return;
        }

        /*if (comando.startsWith("edificar")) {
            String tipo = comando.substring("edificar".length()).trim();
            juego.edificar(tipo); //lo dejo para la tarde
            return;
        }*/

        if (comando.equalsIgnoreCase("estadisticas")) {
            juego.estadisticas();
            return;
        }

        if (comando.startsWith("estadisticas ")) {
            String nombre = comando.substring("estadisticas".length()).trim();
            juego.estadisticasjugador(nombre);
            return;
        }

        if (comando.equalsIgnoreCase("bancarrota")) {
            juego.declararBancarrota();
            return;
        }

        consola.imprimir("Comando no reconocido.");
    }
}

