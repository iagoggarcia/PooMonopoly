package monopoly;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Menu {

    private Juego juego;

    public Menu() {
        this.juego = Juego.getInstancia();
    }

    public void iniciarJuego() {

        Juego.consola.imprimir("Bienvenido a Monopoly.\n");

        juego.iniciarPartida();
        juego.mostrarComandos();   // Mostrar menú una sola vez

        bucleComandos();
    }

    private void bucleComandos() {

        while (true) {

            Juego.consola.imprimir("> ");
            String comando = Juego.consola.leer();

            if (comando == null) continue;
            comando = comando.trim();

            if (comando.equalsIgnoreCase("salir")) {
                Juego.consola.imprimir("Saliendo del Juego...");
                break;
            }

            analizarComando(comando);
        }
    }

    private void analizarComando(String comando) {

        Juego.consola.imprimir(comando + "\n");

        if (!juego.comandoPermitido(comando)) {
            Juego.consola.imprimir("No puedes ejecutar este comando ahora.");
            juego.mostrarComandos();
            return;
        }

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
            return;
        }

        if (comando.startsWith("lanzar dados ")) {
            String valores = comando.substring("lanzar dados ".length()).trim();
            String[] nums = valores.split("\\+");

            if (nums.length != 2) {
                Juego.consola.imprimir("Formato inválido. Usa: lanzar dados X+Y");
                return;
            }

            try {
                int d1 = Integer.parseInt(nums[0]);
                int d2 = Integer.parseInt(nums[1]);
                juego.lanzarDadosValor(d1, d2);
            } catch (NumberFormatException e) {
                Juego.consola.imprimir("Formato inválido. Usa: lanzar dados X+Y");
            }
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
            String grupo = comando.substring("listar edificios ".length()).trim();
            juego.listarEdificiosGrupo(grupo);
            return;
        }

        if (comando.startsWith("vender ")) {

            String args = comando.substring("vender".length()).trim();
            String[] partes = args.split("\\s+");

            if (partes.length != 3) {
                Juego.consola.imprimir("Uso: vender <tipoEdificio> <nombreCasilla> <cantidad>");
                return;
            }

            try {
                String tipo = partes[0];
                String nombreCasilla = partes[1];
                int cantidad = Integer.parseInt(partes[2]);
                juego.gestionarVentaEdificios(tipo, nombreCasilla, cantidad);
            } catch (Exception e) {
                Juego.consola.imprimir("Error en la cantidad. Debe ser un número.");
            }
            return;
        }

        if (comando.equalsIgnoreCase("acabar turno")) {
            juego.acabarTurno();
            return;
        }

        if (comando.startsWith("crear jugador ")) {

            String datos = comando.substring("crear jugador ".length()).trim();
            String[] partes = datos.split("\\s+");

            if (partes.length != 2) {
                Juego.consola.imprimir("Uso: crear jugador <nombre> <tipoAvatar>");
                return;
            }

            juego.crearJugadorArchivo(partes[0], partes[1]);
            return;
        }

        if (comando.startsWith("hipotecar ")) {
            String casilla = comando.substring("hipotecar ".length()).trim();
            juego.hipotecar(casilla);
            return;
        }

        if (comando.startsWith("deshipotecar ")) {
            String casilla = comando.substring("deshipotecar ".length()).trim();
            juego.deshipotecar(casilla);
            return;
        }

        if (comando.startsWith("edificar ")) {
            String tipo = comando.substring("edificar ".length()).trim();
            juego.edificarJuego(tipo);
            return;
        }

        if (comando.equalsIgnoreCase("estadisticas")) {
            juego.estadisticas();
            return;
        }

        if (comando.startsWith("estadisticas ")) {
            String nombre = comando.substring("estadisticas ".length()).trim();
            juego.estadisticasjugador(nombre);
            return;
        }

        if (comando.equalsIgnoreCase("bancarrota")) {
            juego.bancarrota();
            return;
        }

        Juego.consola.imprimir("Comando no reconocido.");
    }

    public void ejecutarArchivoComandos(String ruta) {

        Path path = Path.of(ruta);
        if (!Files.exists(path)) {
            System.err.println("No existe el archivo: " + ruta);
            return;
        }

        juego.inicializarCartas();

        try (Scanner sc = new Scanner(path, StandardCharsets.UTF_8)) {

            while (sc.hasNextLine()) {

                String lineaOriginal = sc.nextLine();
                if (lineaOriginal == null) continue;

                String linea = lineaOriginal.strip();
                if (linea.isEmpty()) continue;
                if (linea.startsWith("#") || linea.startsWith("//")) continue;

                String l = linea.toLowerCase();

                if (l.startsWith("crear jugador")) {
                    String[] p = linea.split("\\s+");
                    if (p.length >= 4) {
                        String nombre = p[2];
                        String tipo = p[3];
                        analizarComando("crear jugador " + nombre + " " + tipo);
                    } else {
                        System.err.println("Formato inválido para 'crear jugador': " + lineaOriginal);
                    }
                }
                else if (l.equals("ver tablero")) {
                    analizarComando("ver tablero");
                }
                else if (l.equals("lanzar dados")) {
                    analizarComando("lanzar dados");
                }
                else if (l.startsWith("lanzar dados ")) {
                    String resto = linea.substring(linea.toLowerCase().indexOf("lanzar dados") + "lanzar dados".length()).trim();
                    analizarComando("lanzar dados " + resto);
                }
                else if (l.startsWith("describir jugador")) {
                    String resto = linea.substring("describir jugador".length()).trim();
                    analizarComando("describir jugador " + resto);
                }
                else if (l.startsWith("describir avatar")) {
                    String resto = linea.substring("describir avatar".length()).trim();
                    analizarComando("describir avatar " + resto);
                }
                else if (l.startsWith("describir")) {
                    String resto = linea.substring("describir".length()).trim();
                    analizarComando("describir " + resto);
                }
                else if (l.equals("listar jugadores")) {
                    analizarComando("listar jugadores");
                }
                else if (l.equals("listar enventa")) {
                    analizarComando("listar enventa");
                }
                else if (l.equals("listar edificios")) {
                    analizarComando("listar edificios");
                }
                else if (l.startsWith("listar edificios ")) {
                    String resto = linea.substring("listar edificios".length()).trim();
                    analizarComando("listar edificios " + resto);
                }
                else if (l.equals("acabar turno")) {
                    analizarComando("acabar turno");
                }
                else if (l.startsWith("salir cárcel")) {
                    analizarComando("salir cárcel");
                }
                else if (l.startsWith("comprar ")) {
                    String prop = linea.substring("comprar".length()).trim();
                    analizarComando("comprar " + prop);
                }
                else if (l.startsWith("vender ")) {
                    String datos = linea.substring("vender".length()).trim();
                    analizarComando("vender " + datos);
                }
                else if (l.equals("listar avatares")) {
                    analizarComando("listar avatares");
                }
                else if (l.startsWith("hipotecar ")) {
                    String resto = linea.substring("hipotecar".length()).trim();
                    analizarComando("hipotecar " + resto);
                }
                else if (l.startsWith("deshipotecar ")) {
                    String resto = linea.substring("deshipotecar".length()).trim();
                    analizarComando("deshipotecar " + resto);
                }
                else if (l.startsWith("edificar ")) {
                    String resto = linea.substring("edificar".length()).trim();
                    analizarComando("edificar " + resto);
                }
                else if (l.equals("estadisticas")) {
                    analizarComando("estadisticas");
                }
                else if (l.startsWith("estadisticas ")) {
                    String resto = linea.substring("estadisticas".length()).trim();
                    analizarComando("estadisticas " + resto);
                }
                else {
                    System.err.println("comando no reconocido en el archivo: " + lineaOriginal);
                }
            }
        }
        catch (IOException e) {
            System.err.println("Error leyendo " + ruta + ": " + e.getMessage());
        }
        //para seguir jugando depsues
        Juego.consola.imprimir("\nArchivo finalizado. Puedes continuar jugando.\n");
        juego.mostrarComandos();
        bucleComandos();
    }
}
