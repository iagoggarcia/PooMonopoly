package monopoly;

public class Menu {

    private Juego juego;

    public Menu() {
        this.juego = Juego.getInstancia();
    }

    public void iniciarJuego() {

        juego.consola.imprimir("Bienvenido a Monopoly.\n");

        juego.iniciarPartida();

        juego.consola.imprimir("Introduce un comando (escribe 'salir' para terminar):");

        while (true) {

            juego.consola.imprimir("> ");
            String comando = juego.consola.leer();

            if (comando == null) continue;

            comando = comando.trim();

            if (comando.equalsIgnoreCase("salir")) {
                juego.consola.imprimir("Saliendo del juego...");
                break;
            }

            analizarComando(comando);
        }
    }

    private void analizarComando(String comando) {

        if (!juego.comandoPermitido(comando)) {//comprobamos si el comando elegido puede ejecutarse según la situacion economica del jugador
            juego.consola.imprimir("No puedes ejecutar este comando ahora.");
            juego.mostrarComandos();  // muestra el submenú o menú normal
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
                juego.consola.imprimir("Formato inválido. Usa: lanzar dados X+Y");
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
            String grupo = comando.substring("listar edificios ".length()).trim();
            juego.listarEdificiosGrupo(grupo);
            return;
        }

        if (comando.startsWith("vender ")) {

            String args = comando.substring("vender".length()).trim();
            String[] partes = args.split("\\s+");

            if (partes.length != 3) {
                juego.consola.imprimir("Uso: vender <tipoEdificio> <nombreCasilla> <cantidad>");
                return;
            }

            String tipo = partes[0];
            String nombreCasilla = partes[1];
            int cantidad = Integer.parseInt(partes[2]);

            juego.gestionarVentaEdificios(tipo, nombreCasilla, cantidad);
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
                juego.consola.imprimir("Uso: crear jugador <nombre> <tipoAvatar>");
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

        juego.consola.imprimir("Comando no reconocido.");
    }
}
