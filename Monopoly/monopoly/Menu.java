package monopoly;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import partida.*;

public class Menu {

    //Atributos
    private ArrayList<Jugador> jugadores; //Jugadores de la partida.
    private ArrayList<Edificio> edificios; // Edificios creados en la partida
    private ArrayList<Avatar> avatares; //Avatares en la partida.
    private int turno = 0; //Índice del jugador al que le toca (0..n-1)
    private int lanzamientos; //Variable para contar el número de lanzamientos de un jugador en un turno.
    private Tablero tablero; //Tablero en el que se juega.
    private Dado dado1; //Dos dados para lanzar y avanzar casillas.
    private Dado dado2;
    private Jugador banca; //El jugador banca.
    private boolean tirado; //Booleano para comprobar si el jugador que tiene el turno ha tirado o no.
    private boolean solvente; //Booleano para comprobar si el jugador que tiene el turno es solvente, es decir, si ha pagado sus deudas.
    private boolean enCurso;   // indica si la partida está activa
    private ArrayList<Carta> cartasSuerte = new ArrayList<>();
    private ArrayList<Carta> cartasCaja = new ArrayList<>();
    private int indiceSuerte = 0;
    private int indiceCaja = 0;
    private static Menu instancia;

    public Menu() {
        instancia = this;
        this.jugadores = new ArrayList<>();
        this.edificios = new ArrayList<>();
        this.enCurso = false; //para elegir en el main si se inicializa o no con la funcion iniciarpartida
    }

    public static Menu getInstancia(){
        return instancia;
    }

    private void inicializarCartas() {
        // cartas de suerte
        cartasSuerte.add(new Carta("suerte", 1, "Decides hacer un viaje de placer. Avanza hasta Solar19. Si pasas por la casilla de Salida, cobra 2.000.000€.", "mover Solar19"));
        cartasSuerte.add(new Carta("suerte", 2, "Los acreedores te persiguen por impago. Ve a la Cárcel. Ve directamente sin pasar por la casilla de Salida y sin cobrar los 2.000.000€.", "carcel"));
        cartasSuerte.add(new Carta("suerte", 3, "¡Has ganado el bote de la lotería! Recibe 1.000.000€.", "cobrar 1000000"));
        cartasSuerte.add(new Carta("suerte", 4, "Has sido elegido presidente de la junta directiva. Paga a cada jugador 250.000€.", "pagarJugadores 250000"));
        cartasSuerte.add(new Carta("suerte", 5, "¡Hora punta de tráfico! Retrocede tres casillas.", "retroceder 3"));
        cartasSuerte.add(new Carta("suerte", 6, "Te multan por usar el móvil mientras conduces. Paga 150.000€", "pagar 150000"));
        cartasSuerte.add(new Carta("suerte", 7, "Avanza hasta la casilla de transporte más cercana. Si no tiene dueño, puedes comprarla. Si tiene dueño, paga al dueño el doble de la operación indicada.", "mover TransporteMasCercano"));

        // cartas de caja de comunidad
        cartasCaja.add(new Carta("caja", 1, "Paga 500.000€ por un fin de semana en un balneario de 5 estrellas.", "pagar 500000"));
        cartasCaja.add(new Carta("caja", 2, "Te investigan por fraude de identidad. Ve a la Cárcel. Ve directamente sin pasar por la casilla de Salida y sin cobrar los 2.000.000€.", "carcel"));
        cartasCaja.add(new Carta("caja", 3, "Colócate en la casilla de Salida. Cobra 2.000.000€.", "mover Salida"));
        cartasCaja.add(new Carta("caja", 4, "Devolución de Hacienda. Cobra 500.000€.", "cobrar 500000"));
        cartasCaja.add(new Carta("caja", 5, "Retrocede hasta Solar1 para comprar antiguedades exóticas.", "mover Solar1"));
        cartasCaja.add(new Carta("caja", 6, "Ve a Solar20 para disfrutar del San Fermín. Si pasas por la casilla de Salida, cobra 2.000.000€.", "mover Solar20"));
    }

    // Método para inciar una partida: crea los jugadores y avatares.
    public void iniciarPartida() {
        Scanner sc = new Scanner(System.in);

        // 1. Crear banca y tablero, inicializar cartas
        Jugador banca = new Jugador();
        banca.sumarFortuna(Valor.FORTUNA_BANCA);
        Tablero tablero = new Tablero(banca);
        inicializarCartas();

        // 2. Obtener la casilla "Salida" donde se colocan los avatares
        Casilla salida = tablero.getPosiciones().get(0).get(0);

        // 3. Preguntar número de jugadores
        int numJugadores = 0;
        while (true) {
            System.out.print("¿Cuántos jugadores van a jugar? (2-4): ");
            try {
                numJugadores = Integer.parseInt(sc.nextLine().trim());
                if (numJugadores >= 2 && numJugadores <= 4) break;
            } catch (NumberFormatException e) { }
            System.out.println("Número no válido, introduce 2, 3 o 4.");
        }

        // 4. Crear los jugadores
        ArrayList<Jugador> jugadores = new ArrayList<>();
        ArrayList<Avatar> avatares = new ArrayList<>();

        for (int i = 1; i <= numJugadores; i++) {
            System.out.print("Introduce el nombre del jugador " + i + ": ");
            String nombre = sc.nextLine().trim();

            String tipoAvatar;
            while (true) {
                System.out.print("Tipo de avatar (coche | esfinge | sombrero | pelota): ");
                tipoAvatar = sc.nextLine().trim().toLowerCase();
                if (tipoAvatar.equals("coche") || tipoAvatar.equals("esfinge")
                        || tipoAvatar.equals("sombrero") || tipoAvatar.equals("pelota")) break;
                System.out.println("Tipo no válido. Intenta de nuevo.");
            }

            Jugador jugador = new Jugador(nombre, tipoAvatar, salida, avatares);
            salida.setContador(salida.getContador()+1); //aumento el contador de casilla por cada jugador que se cree
            jugadores.add(jugador);

            System.out.println("Jugador creado: ");
            System.out.println("{");
            System.out.println("  nombre: " + jugador.getNombre() + ",");
            System.out.println("  avatar: " + jugador.getAvatar().getId());
            System.out.println("}\n");
        }

        // 6. Guardar la información en los atributos de Menu
        this.tablero = tablero;
        this.banca = banca;
        this.jugadores = jugadores;
        this.avatares = avatares;

        this.turno = 0;          // Jugador en índice 0 empieza
        this.solvente = true;
        this.enCurso = true;

        System.out.println("\nPartida inicializada correctamente con " + numJugadores + " jugadores.\n");

        java.util.Scanner in = new java.util.Scanner(System.in);
        while (this.enCurso) {

            // Defensa: que haya jugadores y turno válido
            if (this.jugadores == null || this.jugadores.isEmpty()) {
                System.out.println("No hay jugadores en la partida. Terminando...");
                this.enCurso = false;
                break;
            }

            Jugador actual = this.jugadores.get(this.turno);
            String cmd;

            do {
                System.out.println("\n--- Turno de " + actual.getNombre() + " ---");
                System.out.println("Comandos:");
                System.out.println(" - crear jugador <nombreJugador> <tipoAvatar>");
                System.out.println(" - ver tablero");
                System.out.println(" - lanzar dados");
                System.out.println(" - lanzar dados X+Y");
                System.out.println(" - describir jugador <nombreJugador>");
                System.out.println(" - describir <nombreCasilla>");
                //System.out.println(" - Describir avatar <id>");
                System.out.println(" - listar jugadores");
                //System.out.println(" - Listar avatares");
                System.out.println(" - listar enventa");
                System.out.println(" - listar edificios");
                System.out.println(" - listar edificios <nombreGrupo>");
                System.out.println(" - comprar <nombreCasilla>");
                System.out.println(" - vender <tipoEdificio> <nombreCasilla> <numEdificios>");
                System.out.println(" - edificar <tipoEdificio>");
                System.out.println(" - estadisticas");
                System.out.println(" - estadisticas <nombreJugador>");
                System.out.println(" - salir cárcel");
                System.out.println(" - acabar turno");
                System.out.println(" - salir (para terminar)\n");

                System.out.print("> "); // prompt
                cmd = in.nextLine().trim();
                analizarComando(cmd);

            }while (!(cmd.equalsIgnoreCase("salir") || cmd.equalsIgnoreCase("acabar turno")));

            if(cmd.equalsIgnoreCase("salir")){
                System.out.println("Fin de la partida");
                this.enCurso = false;
                return;
            }
        }
    }

    /*Método que interpreta el comando introducido y toma la accion correspondiente.
    * Parámetro: cadena de caracteres (el comando).
    */
    public void ejecutarArchivoComandos(String ruta) {
        Path path = Path.of(ruta);
        if (!Files.exists(path)) {
            System.err.println("No existe el archivo: " + ruta);
            return;
        }

        inicializarCartas();

        try (Scanner sc = new Scanner(path, StandardCharsets.UTF_8)) {
            while (sc.hasNextLine()) {
                String lineaOriginal = sc.nextLine();
                if (lineaOriginal == null) continue;

                String linea = lineaOriginal.strip();
                if (linea.isEmpty()) continue; // líneas vacías
                if (linea.startsWith("#") || linea.startsWith("//")) continue; // comentarios

                String l = linea.toLowerCase();


                if (l.startsWith("crear jugador")) {
                    String[] p = linea.split("\\s+");
                    if (p.length >= 4) {
                        String nombre = p[2];
                        String tipo   = p[3];
                        analizarComando("crear jugador " + nombre + " " + tipo);
                    } else {
                        System.err.println("Formato inválido para 'crear jugador': " + lineaOriginal);
                    }
                }
                else if(l.equals("ver tablero")){
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
                    String resto = linea.substring("describir jugador ".length()).trim();
                    if (resto.isEmpty()) {
                        analizarComando("describir jugador");
                    } else {
                        analizarComando("describir jugador " + resto);
                    }
                }
                else if (l.startsWith("describir")) {
                    String nombre = linea.substring("describir".length()).trim();
                    analizarComando("describir " + nombre);
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
                    String nombreGrupo = linea.substring("listar edificios".length()).trim();
                    analizarComando("listar edificios " + nombreGrupo);
                }
                else if (l.equals("acabar turno")) {
                    analizarComando("acabar turno");
                }
                else if(l.startsWith("salir cárcel")){
                    String resto = l.substring("salir cárcel".length()).trim();
                    analizarComando("salir cárcel " + resto);
                }

                else if (l.startsWith("comprar ")) {
                    String prop = linea.substring("comprar".length()).trim();
                    analizarComando("comprar " + prop);
                }
                else if (linea.startsWith("vender ")) {
                    String datos = linea.substring("vender".length()).trim();
                    analizarComando("vender " + datos);
                }
                else if (l.startsWith("describir avatar ")) {
                    String id = linea.substring("describir avatar".length()).trim();
                    analizarComando("describir avatar " + id);
                }
                else if (l.equals("listar avatares")) {
                    analizarComando("listar avatares");
                }
                else if (l.equals("jugador")) {
                    analizarComando("jugador");
                }
                else if (l.startsWith("hipotecar ")) {
                    String nombre = linea.substring("hipotecar".length()).trim();
                    analizarComando("hipotecar " + nombre);
                }
                else if (l.startsWith("deshipotecar ")) {
                    String nombre = linea.substring("deshipotecar".length()).trim();
                    analizarComando("deshipotecar " + nombre);
                }
                else if (l.startsWith("edificar ")) {
                    String nombre = linea.substring("edificar".length()).trim();
                    analizarComando("edificar " + nombre);
                }
                else if(l.equals("estadisticas")){
                    analizarComando("estadisticas");
                }
                else if(l.startsWith("estadisticas ")) {
                    String nombre = linea.substring("estadisticas".length()).trim();
                    analizarComando("estadisticas " + nombre);
                }
                else {
                    System.err.println("comando no reconocido en el archivo: " + lineaOriginal);
                }
            }
        } catch (IOException e) {
            System.err.println("Error leyendo " + ruta + ": " + e.getMessage());
        }
    }

    private void analizarComando(String comando) {
        if(comando.equals("jugador")){
            Jugador actual = jugadores.get(turno);//dependiendo de que turno sea (cada jugador tiene un turno asociado) nos devuelve a uno u otro
            System.out.println("{\n" + "Nombre: " + actual.getNombre() + "\n" + "Avatar: " + actual.getAvatar().getId() + "\n}");
        }

        if (comando.equals("ver tablero")) {
            imprimirTablero();
            return;
        }
        if (comando.startsWith("describir jugador")) {
            String nombreJugador = comando.substring("describir jugador ".length()).trim();
            descJugador(nombreJugador);
        }
        else if (comando.startsWith("describir avatar")) {
            String Idavatar = comando.substring("describir avatar ".length()).trim();
            descAvatar(Idavatar);
        }
        else if (comando.startsWith("describir")) {
            String nombreCasilla = comando.substring("describir".length()).trim();
            descCasilla(nombreCasilla);
        }
        else if (comando.startsWith("lanzar dados")) {
            String cadenaComprobar = comando.substring("lanzar dados".length()).trim();
            if (cadenaComprobar.isEmpty()) {
                lanzarDados();
            }
            else {
                String[] valores = cadenaComprobar.split("\\+");
                lanzarDadosValor(valores);
            }
        }
        else if (comando.startsWith("comprar")) {
            String nombrePropiedad = comando.substring("comprar".length()).trim();
            comprar(nombrePropiedad);
        }
        else if (comando.startsWith("salir cárcel")) {
            String resto = comando.substring("salir cárcel".length()).trim();
            if (resto.isEmpty()) {
                // Sólo puede salir el jugador del turno
                salirCarcel();
            } else {
                // Buscar inline por nombre y comprobar turno (sin helpers)
                int idx = -1;
                if (this.jugadores != null) {
                    for (int i = 0; i < this.jugadores.size(); i++) {
                        if (this.jugadores.get(i).getNombre().equalsIgnoreCase(resto)) { idx = i; break; }
                    }
                }
                if (idx == -1) {
                    System.out.println("No existe un jugador llamado '" + resto + "'.");
                } else if (idx != this.turno) {
                    System.out.println("No es el turno de " + this.jugadores.get(idx).getNombre() + ".");
                } else {
                    salirCarcel(); // es su turno, usamos el método estándar sin parámetros
                }
            }
        }
        else if (comando.equalsIgnoreCase("listar enventa")) {
            listarVenta();
        }
        else if (comando.equalsIgnoreCase("listar jugadores")) {
            listarJugadores();
        }
        else if (comando.equalsIgnoreCase("listar avatares")) {
            listarAvatares();
        }
        else if (comando.equalsIgnoreCase("listar edificios")) {
            listarEdificios();
        }
        else if (comando.startsWith("listar edificios")) {
            String grupo = comando.substring("listar edificios".length()).trim();
            listarEdificiosGrupo(grupo);
        }
        else if (comando.startsWith("vender")) {
            String datos = comando.substring("vender".length()).trim();
            String[] partes = datos.isEmpty() ? new String[0] : datos.split("\\s+");
            if (partes.length >= 3) {
                String tipoEdificio = partes[0];
                String casilla = partes[1];
                int numEdificios;
                numEdificios = Integer.parseInt(partes[2]); // paso el número de edificios a vender de string a int
                gestionarVentaEdificios(tipoEdificio, casilla, numEdificios);
            }
            else {
                System.err.println("Formato inválido para 'vender'. Uso: vender <tipoEdificio> <numEdificios>");
            }
        }
        else if (comando.equalsIgnoreCase("acabar turno")) {
            acabarTurno();
        }
        else if (comando.startsWith("crear jugador")) {
            String datosJugador = comando.substring("crear jugador".length()).trim();
            String[] partes = datosJugador.isEmpty() ? new String[0] : datosJugador.split("\\s+");
            if (partes.length >= 2) {
                crearJugadorArchivo(partes); // partes[0]=nombre, partes[1]=tipo
            } else {
                System.err.println("Formato inválido para 'crear jugador'. Uso: crear jugador <Nombre> <Tipo>");
            }
        }
        else if (comando.startsWith("hipotecar ")) {
            String nombreCasilla = comando.substring("hipotecar".length()).trim();
            hipotecar(nombreCasilla);
        }
        else if (comando.startsWith("deshipotecar ")) {
            String nombreCasilla = comando.substring("deshipotecar ".length()).trim();
            deshipotecar(nombreCasilla);
        }
        else if (comando.startsWith("edificar ")) {
            String tipoEdificio = comando.substring("edificar".length()).trim();
            crearEdificio(tipoEdificio);
        }
        else if(comando.equalsIgnoreCase("estadisticas")) {
            estadisticas();
        }
        /*else if(comando.startsWith("estadisticas ")){
            String nombreJugador = comando.substring("estadisticas".length()).trim();
            estadisticasjugador(nombreJugador);
        }*/
    }

    //función para crear jugador desde archivo
    private void crearJugadorArchivo(String[] partes){
        if (this.jugadores == null) {
            this.jugadores = new ArrayList<>();
        }

        if (this.jugadores.size() >= 4) {
            System.err.println("Error: ya hay 4 jugadores. No se puede añadir más.");
            return;
        }

        if(this.banca == null){
            this.banca = new Jugador(Valor.FORTUNA_BANCA);
        }
        if(this.tablero == null){
            this.tablero = new Tablero(banca);
        }

        if(this.avatares == null){
            this.avatares = new ArrayList<>();
        }
        Casilla salida = tablero.getPosiciones().get(0).get(0);
        Jugador jugador = new Jugador(partes[0], partes[1], salida, avatares);
        this.jugadores.add(jugador);
        salida.setContador(salida.getContador()+1); //si creo un jugador desde el archivo tambien aumento el contador de salida

        // Faltaba printear esto cuando se crea un jugador nuevo:
        System.out.println("Jugador creado: ");
        System.out.println("{");
        System.out.println("  nombre: " + jugador.getNombre() + ",");
        System.out.println("  avatar: " + jugador.getAvatar().getId());
        System.out.println("}\n");
    }

    //EN LAS FUNCIONES DE DESCRIBIR Y LISTAR SOLAMENTE HAY QUE HACER PRINTS, LAS HAGO DE ÚLTIMO

//helpers
    private Jugador buscarJugadorPorNombre(String nombre) {
        if (this.jugadores == null) return null;
        for (Jugador j : this.jugadores) {
            String n = j.getNombre();
            if (n != null && n.equalsIgnoreCase(nombre)) return j;
        }
        return null;
    }

    private Avatar buscarAvatarPorId(String id) {
        if (this.avatares == null) return null;
        for (Avatar a : this.avatares) {
            if (a != null && a.getId() != null && a.getId().equalsIgnoreCase(id)) return a;
        }
        return null;
    }


    private void descJugador(String nombreJugador) {
        if (nombreJugador == null || nombreJugador.isBlank()) {
            System.err.println("Uso correcto: Describir <Nombre>");
            return;
        }
        Jugador j = buscarJugadorPorNombre(nombreJugador.trim());
        if (j == null) {
            System.out.println("No existe un jugador con el nombre '" + nombreJugador + "'.");
            return;
        }

        System.out.println("===== Información del jugador =====");
        System.out.println("Nombre: " + j.getNombre());
        System.out.println("Avatar: " + j.getAvatar().getId());
        System.out.println("Fortuna: " + j.getFortuna());
        if (j.getPropiedades() != null && !j.getPropiedades().isEmpty()) {
            System.out.println("Propiedades: " + j.getPropiedades().toString());
        }

        ArrayList<Casilla> solaresHipotecados = j.getHipotecas();
        if (solaresHipotecados != null && !solaresHipotecados.isEmpty()) {
            System.out.println("Propiedades hipotecadas: " + solaresHipotecados.toString());
        }

        if (j.getEdificios() != null && !j.getEdificios().isEmpty()) {
            System.out.println("Edificios: " + j.getEdificios().toString());
        }
        System.out.println("===================================");
    }

    /*private void avatarActual(){
        Jugador actual = this.jugadores.get(this.turno);
        Avatar avatar = actual.getAvatar();
    }*/

    private void descAvatar(String id) {
        if (id == null || id.isBlank()) {
            System.err.println("Uso correcto: Describir avatar <ID>");
            return;
        }
        Avatar a = buscarAvatarPorId(id.trim());
        if (a == null) {
            System.out.println("No existe un avatar con ID '" + id + "'.");
            return;
        }

        System.out.println("---- Avatar ----");
        System.out.println("ID: " + a.getId());
        System.out.println("Tipo: " + a.getTipo());
        System.out.println("Jugador: " + (a.getJugador() != null ? a.getJugador().getNombre() : "(ninguno)"));
        if (a.getLugar() != null) {
            System.out.println("Ubicación: " + a.getLugar().getNombre());
        } else {
            System.out.println("Ubicación: (desconocida)");
        }
    }

    private void descCasilla(String nombreCasilla) {
        if (nombreCasilla == null || nombreCasilla.isBlank()) {
            System.err.println("Uso correcto: describir <Nombre>");
            return;
        }
        if (this.tablero == null) {
            System.out.println("No hay tablero inicializado.");
            return;
        }

        Casilla c = this.tablero.encontrar_casilla(nombreCasilla.trim());
        if (c == null) {
            System.out.println("No existe una casilla llamada '" + nombreCasilla + "'.");
            return;
        }

        System.out.println("---- Casilla ----");
        String info = c.infoCasilla();
        System.out.println(info);
    }


    //Método que ejecuta todas las acciones relacionadas con el comando 'lanzar dados'.

    private void lanzarDados() { //funcion para valores random de dados
        //comprobacion para no dejarle tirar normalmente si está en la cárcel
        Jugador actual = this.jugadores.get(this.turno);
        if(actual.isEnCarcel()){
            System.out.println("Actualmente el jugador solo puede lanzar dados para salir de la cárcel desde el comando salir cárcel");
            //hay que hacerlo así porque puede ser que caiga en la casilla cárcel sin ser encarcelado
        }
        else if((!tirado)||tirado && lanzamientos!=0) {//si aún no hemos tirado, o hemos tirado pero hemos sacado dobles, podemos tirar
            if (this.jugadores == null || this.jugadores.size() < 2) {
                System.err.println("Error: no hay suficientes jugadores (mínimo 2) para lanzar dados.");
                return;
            }

            if (this.dado1 == null) this.dado1 = new Dado();
            if (this.dado2 == null) this.dado2 = new Dado();

            this.enCurso = true;//por si no ha tirado nadie aun, se cambia esta variable para indicar que la partida está en juego

            int valor1 = this.dado1.hacerTirada();
            int valor2 = this.dado2.hacerTirada();

            System.out.println("Dados: " + valor1 + " y " + valor2 + " (suma = " + (valor1 + valor2) + ")");

            tirado = true;
            realizarTirada(valor1, valor2);
        }
        else{
            System.out.println("El jugador ya no tiene el derecho a tirar, escoja otro comando");
        }
    }

    private void lanzarDadosValor(String[] valores) { //funcion para forzar valor de dados
        //comprobacion para no dejarle tirar normalmente si está en la cárcel
        Jugador actual = this.jugadores.get(this.turno);
        if(actual.isEnCarcel()){
            System.out.println("Actualmente el jugador solo puede lanzar dados para salir de la cárcel desde el comando salir cárcel");
            //hay que hacerlo así porque puede ser que caiga en la casilla cárcel sin ser encarcelado
        }

        else if((!tirado)||tirado && lanzamientos!=0) {//hacemos la misma comprobación de arriba
            if (valores.length != 2) {
                System.err.println("Error: formato inválido. Uso: 'Lanzar dados <dado1>+<dado2>'");
                return;
            }

            try {
                int valor1 = Integer.parseInt(valores[0].trim());
                int valor2 = Integer.parseInt(valores[1].trim());
                realizarTirada(valor1, valor2);
            } catch (NumberFormatException e) {
                System.err.println("Error: los valores de los dados deben ser números enteros.");
            }
            tirado = true;
        }
        else{
            System.out.println("El jugador ya no tiene el derecho a tirar, escoja otro comando");
        }
    }

    // Imprime el tablero tal y como está ahora mismo
    private void imprimirTablero() {
        if (this.tablero != null) {
            System.out.println();
            System.out.println(this.tablero); // usa Tablero.toString()
            System.out.println();
        }
    }


    //  Método auxiliar con toda la lógica compartida
    private void realizarTirada(int valor1, int valor2) {//hice el proceso de la tirada fuera para no tener que copiarlo en ambas funciones de lanzar
        int suma = valor1 + valor2;
        Jugador actual = this.jugadores.get(this.turno);
        Avatar av = actual.getAvatar();
        Casilla origen = av.getLugar();

        av.moverAvatar(this.tablero.getPosiciones(), suma);
        Casilla destino = av.getLugar();
        destino.setContador(destino.getContador()+1); //aumentamos el contador de la casilla en la que caemos

        if (destino.getPosicion() < origen.getPosicion()) {//comprobacion de si pasa la casilla salida
            actual.sumarFortuna(Valor.SUMA_VUELTA);
            System.out.println(actual.getNombre() + " pasa por Salida y recibe " + Valor.SUMA_VUELTA + "€.");

            actual.setVueltas(actual.getVueltas() + 1);//suma una vuelta
        }

        this.solvente = destino.evaluarCasilla(actual, this.banca, suma);//evaluamos casilla

        if (!this.solvente) {
            System.out.println(actual.getNombre() + " ha quedado en bancarrota y queda fuera del juego.");
            acabarTurno();
            return;
        }

        // si el jugador cayó en 'Ir a la Cárcel', se aplica el encarcelamiento
        // esto pensé que iba en evaluarcasilla, pero como no tengo acceso al tablero desde casilla, tiene que ser desde aquí
        if (destino.getNombre().equalsIgnoreCase("Ir a la cárcel") ||
            destino.getNombre().equalsIgnoreCase("Ir a carcel") ||
            destino.getNombre().equalsIgnoreCase("Ircarcel")) {

            System.out.println(actual.getNombre() + " es trasladado a la Cárcel.");
            actual.encarcelar(this.tablero.getPosiciones()); // mueve al avatar y marca enCarcel=true
        }

        // si la casilla es de tipo impuestos, añadir el dinero al Parking
        if (destino.getTipo().equalsIgnoreCase("impuestos")) {
            Casilla parking = this.tablero.encontrar_casilla("Parking");
            if (parking != null) {
                parking.sumarValor(destino.getImpuesto());  // usa tu método sumarValor()
            }
        }

        // Control de dobles / turno
        if (valor1 == valor2){ 
            System.out.println("¡" + actual.getNombre() + " ha sacado dobles!");
            imprimirTablero();//se imprime el tablero en el estado actual
            lanzamientos++;

            // Voy a comentar esto que es lo que habíamos hablado de no tirar aleatoriamente nada más sacar dobles:
            
            /*if (lanzamientos < 3) {
                lanzarDados(); // repite turno (solo usa aleatorio de nuevo)
            } else {
                System.out.println(actual.getNombre() + " ha sacado tres dobles seguidos y va a la cárcel.");
                Casilla carcel = this.tablero.encontrar_casilla("Cárcel");
                av.setLugar(carcel);
            }*/
            if (lanzamientos < 3) {
                // ahora en vez de lanzar aleatoriamente como antes, dejamos un aviso de que puede hacer la acción que quiera y ya está
                // para que pueda comprar la casilla (si puede), que lance los dados con un valor específico, o que lo haga de forma aleatoria
                System.out.println("Tienes un lanzamiento extra");
                // después de este print debería salir el menú otra vez y poder hacer lo que sea
            } else {
                System.out.println(actual.getNombre() + " ha sacado tres dobles seguidos y va a la cárcel.");
                actual.encarcelar(this.tablero.getPosiciones());
                lanzamientos = 0; // reiniciamos los lanzamientos aquí también
            }
           
        }
        else{
            lanzamientos = 0;
            imprimirTablero();//se imprime el tablero en el estado actual
        }
        tirado = true;
    }


    /*Método que ejecuta todas las acciones realizadas con el comando 'comprar nombre_casilla'.
    * Parámetro: cadena de caracteres con el nombre de la casilla.
    */
    private void comprar(String nombre) {
        Casilla casilla = this.tablero.encontrar_casilla(nombre);
        
        if (casilla == null) {
            System.out.println("No existe una casilla con el nombre '" + nombre + "'.");
            return;
        }

        Jugador comprador = this.jugadores.get(this.turno); // jugador del turno
        String nombre_casilla_comprador = comprador.getAvatar().getLugar().getNombre();
        System.out.println(jugadores.get(turno).getNombre() + " intenta comprar " + nombre + "...");

        if(nombre_casilla_comprador.equalsIgnoreCase(nombre)) {
            casilla.comprarCasilla(comprador, this.banca);
        }
        else{
            System.out.println("El jugador intenta comprar una casilla en la que no está posicionado");
        }
    }

    private void hipotecar(String nombre) {
        Casilla casilla = this.tablero.encontrar_casilla(nombre);

        if (casilla == null) {
            System.out.println("No existe una casilla con el nombre '" + nombre + "'.");
            return;
        }

        Jugador actual = this.jugadores.get(this.turno);

        if (!casilla.getTipo().equalsIgnoreCase("solar")) {
            System.out.println("No puedes hipotecar una casilla de tipo '" + casilla.getTipo() + "'.");
            return;
        }

        if (casilla.getDuenho() == null || casilla.getDuenho() != actual) {
            System.out.println("No puedes hipotecar " + casilla.getNombre() + " porque no eres su propietario.");
            return;
        }

        if (casilla.isHipotecada()) {
            System.out.println("La casilla '" + casilla.getNombre() + "' ya está hipotecada.");
            return;
        }

        // añadir después: si tiene edificios, pedir que los venda y demás
        if (casilla.getEdificios() != null && !casilla.getEdificios().isEmpty()) {
            System.out.println("No puedes hipotecar " + casilla.getNombre() + " porque tiene edificios. Véndelos antes.");
            return;
        }

        float valorHipoteca = casilla.getHipoteca();
        casilla.setHipotecada(true);
        actual.sumarFortuna(valorHipoteca);

        String colorGrupo = (casilla.getGrupo() != null && casilla.getGrupo().getNombreColorGrupo() != null ) ? casilla.getGrupo().getNombreColorGrupo() : "sin grupo";
        System.out.println(actual.getNombre() + " recibe " + valorHipoteca + "€ por la hipoteca de " + casilla.getNombre() + ". No se puede recibir alquileres ni edificar en el grupo " + colorGrupo + ".");
    }

    private void deshipotecar(String nombre) {
        Casilla casilla = this.tablero.encontrar_casilla(nombre.toLowerCase());

        if (casilla == null) {
            System.out.println("No existe una casilla con el nombre '" + nombre + "'.");
            return;
        }

        Jugador actual = this.jugadores.get(this.turno);

        if (!casilla.getTipo().equalsIgnoreCase("solar")) {
            System.out.println("No puedes deshipotecar una casilla de tipo '" + casilla.getTipo() + "'.");
            return;
        }

        if (casilla.getDuenho() == null || casilla.getDuenho() != actual) {
            System.out.println("No puedes deshipotecar " + casilla.getNombre() + " porque no eres su propietario.");
            return;
        }

        if (!casilla.isHipotecada()) {
            System.out.println("La casilla '" + casilla.getNombre() + "' no está hipotecada.");
            return;
        }

        float valorHipoteca = casilla.getHipoteca();

        if (actual.getFortuna() < valorHipoteca) {
            System.out.println("La fortuna de " + actual.getNombre() + " no es suficiente para deshipotecar " + casilla.getNombre() + ".");
            return;
        }

        casilla.setHipotecada(false);
        actual.sumarFortuna(-valorHipoteca);
        actual.sumarGastos(valorHipoteca);

        String colorGrupo = (casilla.getGrupo() != null && casilla.getGrupo().getNombreColorGrupo() != null ) ? casilla.getGrupo().getNombreColorGrupo() : "sin grupo";
        System.out.println(actual.getNombre() + " paga " + valorHipoteca + "€ por deshipotecar " + casilla.getNombre() + ". Ahora puede recibir alquileres y edificar en el grupo " + colorGrupo + ".");

    }

    //Método que ejecuta todas las acciones relacionadas con el comando 'salir carcel'.
    // Solo puede ejecutarlo el jugador cuyo índice coincide con 'turno' (o si en el comando se especificó ese jugador).
    private void salirCarcel() {
        if (this.jugadores == null || this.jugadores.isEmpty()) {
            System.out.println("No hay jugadores en la partida.");
            return;
        }

        Jugador actual = this.jugadores.get(this.turno); 
        if(!actual.isEnCarcel()){
            System.out.println("El jugador no está encarcelado.");
        }
        else {

            System.out.println(actual.getNombre() + " está en la cárcel e intenta salir tirando los dados...");

            if (this.dado1 == null) this.dado1 = new Dado();
            if (this.dado2 == null) this.dado2 = new Dado();

            int valor1 = this.dado1.hacerTirada();
            int valor2 = this.dado2.hacerTirada();
            System.out.println("Dados: " + valor1 + " y " + valor2 + " (suma = " + (valor1 + valor2) + ")");

            //ESTA PARTE HAY QUE REPASARLA, LA LOGICA ESTA MAL
            // si saca dobles sale sin pagar
            if (valor1 == valor2) {
                System.out.println("¡" + actual.getNombre() + " ha sacado dobles y sale de la cárcel!");
                actual.setEnCarcel(false);
                actual.setTiradasCarcel(0);
                realizarTirada(valor1, valor2);
                return;
            }

            // no saca dobles, pierde un intento
            actual.setTiradasCarcel(actual.getTiradasCarcel() + 1);
            System.out.println("No ha sacado dobles (" + actual.getTiradasCarcel() + " intento/s).");

            // si pierde los tres turnos sin sacar dobles, tiene que pagar
            if (actual.getTiradasCarcel() >= 3) {
                if (actual.getFortuna() >= 500000) {
                    actual.sumarFortuna(-500000);
                    actual.sumarGastos(500000);
                    this.banca.sumarFortuna(500000);
                    actual.setEnCarcel(false);
                    actual.setTiradasCarcel(0);
                    System.out.println("Tras tres turnos sin sacar dobles, " + actual.getNombre() + " debe pagar 500.000€ para salir de la cárcel.");
                    realizarTirada(valor1, valor2);
                } else {
                    System.out.println(actual.getNombre() + " no puede pagar la fianza y se declara en bancarrota.");
                    // lo de bancarrota no se como implementarlo
                }
            } else { // si aun no llego al tercer intento, sigue preso
                System.out.println(actual.getNombre() + " permanece en la cárcel.");
            }
        }

    }

    private void listarVenta() {
        if (this.tablero == null) {
            System.out.println("No hay tablero inicializado.");
            return;
        }

        System.out.println("=== Propiedades en venta ===");

        for (ArrayList<Casilla> lado : this.tablero.getPosiciones()) { //recorremos el array de casillas, lado por lado
            if (lado == null) continue;
            for (Casilla c : lado) {
                if (c == null) continue;

                String info = c.casEnVenta(); //dentro ya se hace la comprobacion
                if (!info.isBlank()) {
                    System.out.println(info); //solo imprimimos si es comprable
                }
            }
        }

        System.out.println();
        System.out.println(this.tablero.toString()); // Mostrar tablero al final (lo pide en el pdf)
    }



    private void listarJugadores() {
        for(Jugador jugador : this.jugadores){
            System.out.println("{");
            System.out.println("Nombre: " + jugador.getNombre());
            System.out.println("Avatar: " + jugador.getAvatar().getId());
            System.out.println("Fortuna: " + jugador.getFortuna());
            if (jugador.getPropiedades() != null && !jugador.getPropiedades().isEmpty()) {
                System.out.println("Propiedades: " + jugador.getPropiedades().toString());
            }
            if (jugador.getEdificios() != null &&  !jugador.getEdificios().isEmpty()) {
                System.out.println("Edificios en propiedad: " + jugador.getEdificios().toString());
            }
            System.out.println("}");
            System.out.print("\n");
        }
    }

    private void listarAvatares() {
        for(Avatar avatar : this.avatares){
            System.out.println(avatar.getJugador().getNombre());
            System.out.println(avatar.getTipo());
            System.out.println(avatar.getLugar());
            System.out.println(avatar.getId());
        }
    }

    private void listarEdificios() {
        if (!this.edificios.isEmpty()) {
            for (Edificio edificio : this.edificios) {
                System.out.println("{");
                System.out.println("id: " + edificio.getId());
                System.out.println("propietario: " + edificio.getPropietario().getNombre());
                System.out.println("casilla: " + edificio.getLugar().toString());
                System.out.println("grupo: " + edificio.getLugar().getGrupo().getNombreColorGrupo());
                System.out.println("coste: " + edificio.getPrecio());
                System.out.println("}");
            }
        }
        else {
            System.err.println("No hay edificios que listar");
        }
    }

    private void listarEdificiosGrupo(String grupo) {
        if (this.tablero == null || this.tablero.getPosiciones() == null) {
            System.out.println("No hay tablero inicializado.");
            return;
        }
        if (grupo == null || grupo.isBlank()) {
            System.out.println("Uso: listar edificios <nombreGrupo>");
            return;
        }

        boolean hayAlgo = false;

        for (ArrayList<Casilla> lado : this.tablero.getPosiciones()) { // itero cada lado del tablero
            for (Casilla c : lado) { // ahora cada casilla de cada lado
                Grupo g = c.getGrupo();
                if (!"solar".equalsIgnoreCase(c.getTipo())) continue; // si no es un solar, pasa a la siguiente
                if (!g.getNombreColorGrupo().equalsIgnoreCase(grupo)) continue; // si el grupo pedido y el grupo de la casilla no coinciden, pasa al siguiente

                // Creamos un array para cada tipo de edificio donde gaurdamos los ids
                ArrayList<String> casas = new ArrayList<>();
                ArrayList<String> hoteles = new ArrayList<>();
                ArrayList<String> piscinas = new ArrayList<>();
                ArrayList<String> pistas = new ArrayList<>();

                if (c.getEdificios() != null) {
                    for (Edificio e : c.getEdificios()) { // itero los edificios que hay en la casilla
                        if (e == null || e.getTipo() == null) continue; // si hay algún error paso al siguiente
                        switch (e.getTipo().toLowerCase()) { // y voy guardando el id del edificio según su tipo
                            case "casa":    casas.add(e.getId());    break;
                            case "hotel":   hoteles.add(e.getId());  break;
                            case "piscina": piscinas.add(e.getId()); break;
                            case "pista":   pistas.add(e.getId());   break;
                        }
                    }
                }

                int alquiler = c.calcularAlquilerParaMostrar();

                System.out.println("{");
                System.out.println("propiedad: " + c.getNombre());
                System.out.println("hoteles: " + hoteles);
                System.out.println("casas: " + casas);
                System.out.println("piscinas: " + piscinas);
                System.out.println("pistas: " + pistas);
                System.out.println("alquiler: " + alquiler);
                System.out.println("}");

                if (!casas.isEmpty() || !hoteles.isEmpty() || !piscinas.isEmpty() || !pistas.isEmpty()) {
                    hayAlgo = true;
                }
            }
        }

        if (!hayAlgo) {
            System.out.println("Todavía no hay edificaciones construidas en el grupo " + grupo + ".");
        }
    }



    private void acabarTurno() {
        // aquí se podría añadir la comprobación de que hay jugadores en la partida
        
        Jugador actual = this.jugadores.get(this.turno);
        System.out.println("El jugador " + actual.getNombre() + " termina su turno.");

        this.lanzamientos = 0;//reiniciamos dobles

        this.turno = (this.turno + 1) % this.jugadores.size();//actualizamos turno
        Jugador siguiente = this.jugadores.get(this.turno);//damos turno al jugador siguiente
        tirado = false; //hay que actualizar esto también porque si no no deja tirar al siguiente jugador

        System.out.println("Le toca al jugador " + siguiente.getNombre() + ".");

        comprobarSolaresNoComprados(); // cada vez que un turno acaba
    }

    private void comprobarSolaresNoComprados() {
        // verifica si todos los jugadores han dado 4 vueltas o más, la usamos para sumar valor a los solares en sumarvalor y hay que ir actualizando
        boolean todos4vueltas = true;
        for (Jugador j : this.jugadores) {
            if (j.getVueltas() < 4) {
                todos4vueltas = false;
                break;
            }
        }

        if (todos4vueltas) {
            System.out.println("Todos los jugadores han dado al menos 4 vueltas. Se incrementa el valor de los solares sin dueño.");

            // recorremos todas las casillas del tablero
            for (ArrayList<Casilla> lado: this.tablero.getPosiciones()) {
                for (Casilla c : lado) {
                    if (c.getTipo().equalsIgnoreCase("solar") && (c.getDuenho() == null || c.getDuenho() == this.banca)) {
                        float incremento = 100000; // por poner algo pq ns cuanto es
                        c.sumarValor(incremento);
                    }
                }
            }
            // reiniciamos las vueltas para volver a contar 4 más
            for (Jugador j: this.jugadores) {
                j.setVueltas(0);
            }
        }
    }

    //imprime las casillas mas frecuentadas (en caso de que una sobresalga sobre el resto solo imprime esa)
    private void casillasfrecuentadas(){

        if(this.tablero == null){
            System.out.println("El tablero no está inicializado.");
            return;
        }
        if(this.tablero.getPosiciones() == null){
            System.out.println("No hay casillas inicializadas en el tablero.");
            return;
        }
        int maximo = 0;
        ArrayList<Casilla> provisional = new ArrayList<>();

        for (ArrayList<Casilla> lado : this.tablero.getPosiciones()) { //recorremos el array de casillas, lado por lado
            if (lado == null) continue;
            for (Casilla c : lado) {
                if (c == null) continue;

                if(maximo <= c.getContador()){
                    maximo = c.getContador();
                    provisional.add(c);
                }
            }
        }

        for(Casilla c : provisional){
            if(c.getContador() == maximo){
                System.out.println("casillaMasFrecuentada: " + c.getNombre()  + ",");
            }
        }
    }

    private void masvueltas(){
        if(this.jugadores == null || this.jugadores.isEmpty()){
            System.out.println("No hay jugadores registrados en la partida");
            return;
        }
        else{
            int maximo = 0;
            ArrayList<Jugador> provisional = new ArrayList<>();
            //ArrayList<Jugador> definitivo = new ArrayList<>(); //dejo esto comentado por si luego me piden el que mas adelantado vaya en la vuelta
            for (Jugador j : this.jugadores) {
                if (j.getVueltas() >= maximo) {
                    maximo = j.getVueltas();
                    provisional.add(j);
                }
            }
            for (Jugador j : provisional) {
                if(j.getVueltas() == maximo){
                    System.out.println("jugadorMasVueltas: " + j.getNombre() + ",");
                    //definitivo.add(j);
                }
            }
        }
    }

    //funcion que imprime el jugador con mas valor de toda la partida (dinero, edificios, casillas)
    private void encabeza(){
        if(this.jugadores == null ||  this.jugadores.isEmpty()){
            System.out.println("No hay jugadores registrados en la partida");
            return;
        }
        else{
            float maximo = 0;
            Jugador maspatrimonio = null;
            for(Jugador j: this.jugadores){
                j.getPatrimonio();
                if(j.getPatrimonio() >= maximo){
                    maximo = j.getPatrimonio();
                    maspatrimonio = j;
                }
            }
            System.out.println("jugadorEnCabeza: " + maspatrimonio.getNombre() + ",");
        }
    }


    //funcion que imprime los datos de la casilla mas rentable
    private void casillarentable() {
        if (this.jugadores == null || this.jugadores.isEmpty()) {
            System.out.println("No hay jugadores registrados en la partida");
            return;
        } else {
            ArrayList<Casilla> rentables = new ArrayList<>();
            float maximo = Float.NEGATIVE_INFINITY;
            for (Jugador j : this.jugadores) {
                for (Casilla c : j.getPropiedades()) {
                    c.getRentabilidad();//actualizo las rentabilidades
                    if (c.getRentabilidad() >= maximo) {
                        if(c.getDuenho() != this.banca && c.getDuenho() != null) {
                            maximo = c.getRentabilidad();
                            rentables.add(c);
                        }
                    }
                }
            }
            if (rentables.isEmpty()) {
                System.out.println("Ningun jugador tiene casillas en propiedad\n");
                return;
            } else {
                for (Casilla c : rentables) {
                    if (c.getRentabilidad() == maximo) {
                        System.out.println("casillaMasRentable: " + c.getNombre() + ",");
                    }
                }
            }
        }
    }


    private int grupoduenho(Grupo g){
        int tiene_duenho = 0;
        if(g == null || g.getMiembros().isEmpty()){
            System.out.println("Grupo no valido.\n");
            return tiene_duenho;
        }
        else{
            for(Casilla c: g.getMiembros()){
                if(c.getDuenho() != this.banca && c.getDuenho() != null) {
                    tiene_duenho = 1;
                    return tiene_duenho;
                }
            }
        }
        return tiene_duenho;
    }
    //funcion que hace de setter para la rentabilidad de los grupos, no uso el setter directamente, se usa esta funcion para tenerlo actualizado siempre
    private void rentabilidadgrupos(){
        if(this.tablero == null){
            System.out.println("No hay tablero inicializado\n");
            return;
        }
        if(this.tablero.getGrupos() == null || this.tablero.getGrupos().isEmpty()){
            System.out.println("No hay grupos\n");
            return;
        }
        else{
            HashMap<String,Grupo> grupos = this.tablero.getGrupos();
            for(Grupo g : grupos.values()){
                float rentacum = 0;
                for(Casilla c : g.getMiembros()){
                    if(c.getDuenho() != this.banca && c.getDuenho() != null) {
                        rentacum += c.getRentabilidad();
                    }
                }
                g.setRentabilidadgrupo(rentacum);
            }
        }
    }

    private void gruporentable(){
        if(this.tablero == null){
            System.out.println("No hay tablero registrado en la partida\n");
            return;
        }
        else{
            float maximo = Float.NEGATIVE_INFINITY;
            HashMap<String,Grupo> grupos = this.tablero.getGrupos();
            if(grupos == null || grupos.isEmpty()){
                System.out.println("No hay grupos\n");
                return;
            }
            else {
                rentabilidadgrupos(); //actualizo las rentabilidades
                for (Grupo g : grupos.values()) {
                    if(grupoduenho(g) == 1) {
                        if (g.getRentabilidadgrupo() > maximo) {
                            maximo = g.getRentabilidadgrupo();
                        }
                    }
                }
                for (Grupo g : grupos.values()) {
                    if (grupoduenho(g) == 1) {
                        if (g.getRentabilidadgrupo() == maximo) {
                            System.out.println("grupoMasRentable: " + g.getNombreColorGrupo() + ",");
                        }
                    }
                }
            }
        }
    }

    //imprime las estadisticas de la partida
    private void estadisticas(){
        System.out.println("\nEstadisticas de la partida\n");
        casillarentable();
        gruporentable();
        casillasfrecuentadas();
        masvueltas();
        encabeza();
        System.out.println("\n");
    }

    /* Función que crea el edificio si se cumplen los requisitos necesarios
     *
     */
    public void crearEdificio(String tipoEdificio) {
        Jugador actual = this.jugadores.get(this.turno); // cojo el jugador que tiene el turno
        Casilla casilla = actual.getAvatar().getLugar(); // y la casilla en la que está
        if (casilla.getTipo().equalsIgnoreCase("solar")) {
            int precio;
            switch (tipoEdificio.toLowerCase()) {
                case "casa":
                    if (casilla.getNumCasas() < 4 && casilla.getNumHoteles() < 1) { // requisito (no puede haber 4 casas ni un hotel para construir otra casa)
                        precio = casilla.getValorCasayHotel();
                        intentarConstruir(actual, casilla, tipoEdificio, precio); // esta función también comprueba si el jugador es dueño de la casilla y si tiene el dinero suficiente
                    }
                    else { // Mensajes de error
                        if (casilla.getNumCasas() == 4) { // si ya hay 4 casas
                            System.err.println(actual.getNombre() + " ya ha construido el máximo de casas permitido en " + casilla.getNombre());
                        }
                        else if (casilla.getNumHoteles() == 1) { // si ya hay un hotel
                            System.err.println(actual.getNombre() + " no puede construir más casas en " + casilla.getNombre() + ", ya ha construido un hotel");
                        }
                    }
                    break;
                case "hotel":
                    if (casilla.getNumCasas() == 4 && casilla.getNumHoteles() < 1) { // requisito (si hay 4 casas y ningún hotel)
                        casilla.quitarCuatroCasas();
                        precio = casilla.getValorCasayHotel();
                        intentarConstruir(actual, casilla, tipoEdificio, precio); // lo construye si es dueño de la casilla y si tiene el dinero suficiente
                    }
                    else { // Mensajes de error
                        if (casilla.getNumHoteles() == 1) { // si ya hay un hotel
                            System.err.println(actual.getNombre() + " ya ha construido un hotel en " + casilla.getNombre());
                        }
                        else if (casilla.getNumCasas() < 4) { // si no hay 4 casas todavía
                            System.err.println(actual.getNombre() + " no ha construido todavía el número de casas necesarias en " + casilla.getNombre());
                        }
                    }
                    break;
                case "piscina":
                    if (casilla.getNumPiscinas() == 0 && casilla.getNumHoteles() == 1) { // si hay 1 hotel pero no piscina se puede construir
                        precio = casilla.getValorPiscina();
                        intentarConstruir(actual, casilla, tipoEdificio, precio); // si hay dinero y el jugador es dueño de la casilla
                    }
                    else { // Mensajes de error
                        if (casilla.getNumPiscinas() == 1) { // si ya hay piscina
                            System.err.println(actual.getNombre() + " no puede edificar una piscina en " +  casilla.getNombre() + " porque ya ha edificado una");
                        }
                        else if (casilla.getNumHoteles() < 1) { // si no hay hotel
                            System.err.println(actual.getNombre() + " no puede edificar una piscina en " + casilla.getNombre() + " porque todavía no hay hotel");
                        }
                    }
                    break;
                case "pista":
                    if (casilla.getNumPiscinas() == 1 && casilla.getNumHoteles() == 1 && casilla.getNumPistas() < 1) { // si hay 1 piscina y 1 hotel se puede construir
                        precio = casilla.getValorPistaDeporte();
                        intentarConstruir(actual, casilla, tipoEdificio, precio); // si el jugador tiene dinero suficiente y es dueño de la casilla
                    }
                    else { // Mensajes de error
                        if (casilla.getNumPiscinas() < 1 &&  casilla.getNumHoteles() < 1) { // si no hay ni hotel ni piscina
                            System.err.println(actual.getNombre() + " no puede edificar una pista en " +  casilla.getNombre() + " porque todavía no hay hotel ni piscina");
                        }
                        else if (casilla.getNumPiscinas() < 1) { // si no hay piscina (pero sí hotel)
                            System.err.println(actual.getNombre() + " no puede edificar una pista en " + casilla.getNombre() + " porque todavía no hay piscina");
                        }
                        else if (casilla.getNumPistas() == 1) { // si ya hay pista
                            System.err.println(actual.getNombre() + " no puede edificar una pista en " + casilla.getNombre() + " porque ya hay una");
                        }
                    }
                    break;
                default:
                    System.err.println("Tipo de edificio no válido.");
            }
        }
        else {
            System.err.println("No se puede edificar en una casilla de tipo " + casilla.getTipo());
        }
    }

    /* Función que una vez pasados los requisitos principales en crearEdificio, comprueba otros
    * y si los cumple, construye el edificio para cada caso de la función anterior.
    * Se le llama en cada case del switch, por eso hice una función específica, para no repetir
    * este fragmento tantas veces
     */
    public void intentarConstruir(Jugador actual, Casilla casilla, String tipoEdificio, int precio) {
        if (actual.getFortuna() < precio ) { // de primeras compruebo si el jugador tiene el dinero suficiente
            System.err.println("La fortuna de " + actual.getNombre() + " no es suficiente para edificar un/a " + tipoEdificio + " en la casilla " + casilla.getNombre());
        }
        else { // si lo tiene
            if (casilla.getDuenho() == actual && casilla.getGrupo().esDuenhoGrupo(actual)) { // miro si es dueño de la casilla en la que está y del grupo completo
                Edificio e = new Edificio(tipoEdificio, actual, casilla); // creo el edificio
                casilla.anhadirEdificioACasilla(e); // añadimos el nuevo edificio a la casilla
                actual.anhadirEdificioAJugador(e); // añadimos el edificio también al jugador
                if (this.edificios == null) { // para crear el array edificios del menú la primera vez
                    this.edificios = new ArrayList<>();
                }
                this.edificios.add(e); // añadimos el edificio al array creado en esta clase Menu.java, sirve para luego hacer el listar edificios

                actual.sumarFortuna(-precio); // restamos lo que se acaba de gastar
                System.out.println("Se ha edificado un/a " +  tipoEdificio + " en " +  casilla.getNombre() + ". La fortuna de " + actual.getNombre() + " se reduce en " + precio + "€");
            }
            else { // si no es dueño de la casilla o del grupo
                if (casilla.getDuenho() != actual) {
                    System.err.println(actual.getNombre() + " no puede edificar en " + casilla.getNombre() + " porque no le pertenece");
                }
                else if (!casilla.getGrupo().esDuenhoGrupo(actual)) {
                    System.err.println(actual.getNombre() + " no puede edificar en " + casilla.getNombre() + " porque no es propietario del grupo " + casilla.getGrupo().getNombreColorGrupo());
                }
            }
        }
    }

    // Función para eliminar los edificios del array edificios del menú,
    // si no, al hacer listar edificios se imprimían las 4 casas a pesar
    // de haber creado un hotel. Se usa en quitarCuatroCasas()
    public void eliminarEdificioGlobal(Edificio e) {
        if (this.edificios != null) {
            this.edificios.remove(e);
        }
    }

    // Función para vender x cantidad de un edificio concreto en una casilla específica
    private void gestionarVentaEdificios(String tipoEdificio, String nombreCasilla, int numEdificios) {
        Casilla casilla = tablero.encontrar_casilla(nombreCasilla);
        if (casilla != null) {
            if (casilla.getTipo().equals("solar")) {
                requisitosVenta(tipoEdificio, casilla, numEdificios);
            }
            else {
                System.err.println("No se pueden vender edificios en casillas de tipo " + casilla.getTipo() + " , porque no se puede edificar en ellas");
            }
        }
        else {
            System.out.println("No existe la casilla " +  nombreCasilla);
        }
    }

    private void requisitosVenta(String tipoEdificio, Casilla casilla, int numEdificios) {
        Jugador actual = this.jugadores.get(this.turno); // guardo el jugador que ejecuta el comando
        switch (tipoEdificio) {
            case "casas":
                if (casilla.getDuenho() != null && casilla.getDuenho().equals(actual)) {
                    if (casilla.getEdificios() != null && !casilla.getEdificios().isEmpty()) {
                        casilla.venderCasas(numEdificios, actual); // y esta función ya comprueba si es el dueño
                    }
                } else {
                    System.err.println("No se pueden vender casas en " + casilla.getNombre() + ". Esta propiedad no pertenece a " + actual.getNombre() + ".");
                }
                break;
            case "hoteles":
                if (casilla.getDuenho() != null && casilla.getDuenho().equals(actual)) {
                    if (casilla.getEdificios() != null && !casilla.getEdificios().isEmpty()) {
                        casilla.venderHoteles(numEdificios, actual); // y esta función ya comprueba si es el dueño
                    }
                } else {
                    System.err.println("No se pueden vender hoteles en " + casilla.getNombre() + ". Esta propiedad no pertenece a " + actual.getNombre() + ".");
                }
                break;
            case "piscinas":
                if (casilla.getDuenho() != null && casilla.getDuenho().equals(actual)) {
                    if (casilla.getEdificios() != null && !casilla.getEdificios().isEmpty()) {
                        casilla.venderPiscinas(numEdificios, actual); // y esta función ya comprueba si es el dueño
                    }
                } else {
                    System.err.println("No se pueden vender piscinas en " + casilla.getNombre() + ". Esta propiedad no pertenece a " + actual.getNombre() + ".");
                }
                break;
            case "pista":
                if (casilla.getDuenho() != null && casilla.getDuenho().equals(actual)) {
                    if (casilla.getEdificios() != null && !casilla.getEdificios().isEmpty()) {
                        casilla.venderPistas(numEdificios, actual); // y esta función ya comprueba si es el dueño
                    }
                } else {
                    System.err.println("No se pueden vender pistas de deporte en " + casilla.getNombre() + ". Esta propiedad no pertenece a " + actual.getNombre() + ".");
                }
                break;
        }
    }

    // funcion para manejar todas las cartas
    public void ejecutarCartas(String tipo, Jugador jugador, Jugador banca, Casilla casillaActual) {
        ArrayList<Carta> cartas = tipo.equals("suerte") ? this.cartasSuerte : this.cartasCaja;
        int indice = tipo.equals("suerte") ? this.indiceSuerte : indiceCaja;
        
        Carta carta = cartas.get(indice);
        System.out.println(jugador.getNombre() + ", elige una carta: " + carta.getId());
        System.out.println("Acción: " + carta.getDescripcion());

        if (tipo.equals("suerte")) {
            this.indiceSuerte = (indice + 1) % cartas.size();
        } else {
            this.indiceCaja = (indice + 1) % cartas.size();
        }

        // ejecutamos la acción de la carta
        String accion = carta.getAccion();
        String[] partes = accion.split(" ");
        String tipoAccion = partes[0];

        switch (tipoAccion) {
            case "mover":
                String destino = partes[1];

                if (destino.equalsIgnoreCase("TransporteMasCercano")) {
                    moverTransporteMasCercano(jugador, banca);
                    break;
                } 
                
                // busca la casilla destino por nombre 
                Casilla casilla = this.tablero.encontrar_casilla(destino);

                if (casilla == null) {
                    System.out.println("No se ha encontrado la casilla destino: " + destino);
                    break;
                } else {
                    jugador.getAvatar().getLugar().eliminarAvatar(jugador.getAvatar());
                }

                int posInicial = casillaActual.getPosicion();
                int posDestino = casilla.getPosicion();

                // mover el avatar
                casilla.anhadirAvatar(jugador.getAvatar());
                jugador.getAvatar().setLugar(casilla);
                
                if (destino.equalsIgnoreCase("Solar19") || destino.equalsIgnoreCase("Solar20") || destino.equalsIgnoreCase("Salida")) {
                    System.out.println(jugador.getNombre() + " avanza hasta " + casilla.getNombre() + ".");
                } else {
                     System.out.println(jugador.getNombre() + " retrocede hasta " + casilla.getNombre() + ".");                   
                }

                if ((destino.equalsIgnoreCase("Salida") || destino.equalsIgnoreCase("Solar19") || destino.equalsIgnoreCase("Solar20")) && posDestino < posInicial) {
                    jugador.sumarFortuna(Valor.SUMA_VUELTA);
                    System.out.println(jugador.getNombre() + " cobra 2.000.000€ por pasar por la casilla de Salida.");
                }

                // evaluar la casilla de destino
                casilla.evaluarCasilla(jugador, banca, 0);
                break;

            case "carcel":
                jugador.encarcelar(this.tablero.getPosiciones());
                break;

            case "cobrar":
                float cantidadCobrar = Float.parseFloat(partes[1]);
                jugador.sumarFortuna(cantidadCobrar);
                System.out.println(jugador.getNombre() + " cobra " + (int) cantidadCobrar + "€.");
                break;

            case "pagar": // pagar a la banca
                float cantidadPagar = Float.parseFloat(partes[1]);

                if (jugador.getFortuna() < cantidadPagar) {
                    // comprobamos si puede hipotecar alguna propiedad
                    boolean puedeHipotecar = jugador.getPropiedades() != null && !jugador.getPropiedades().isEmpty() && jugador.getHipotecas().size() < jugador.getPropiedades().size();

                    if (puedeHipotecar) {
                        System.out.println(jugador.getNombre() + " no tiene suficiente dinero para pagar " + (int) cantidadPagar + "€. Debe hipotecar alguna propiedad o se declarará en bancarrota.");
                    }
                } else {
                    jugador.sumarFortuna(-cantidadPagar);
                    jugador.sumarGastos(cantidadPagar);
                    banca.sumarFortuna(cantidadPagar);
                    System.out.println(jugador.getNombre() + " paga " + (int) cantidadPagar + "€ a la banca.");
                }
                break;

            case "pagarJugadores":
                float cantidad = Float.parseFloat(partes[1]);
                int numJugadores = this.jugadores.size() - 1; // sin contar al propio jugador
                float total = cantidad * numJugadores;

                // comprobamos  si puede pagar a todos los jugadores
                if (jugador.getFortuna() < total) {
                    boolean puedeHipotecar = jugador.getPropiedades() != null && !jugador.getPropiedades().isEmpty() && jugador.getHipotecas().size() < jugador.getPropiedades().size();

                    if (puedeHipotecar) {
                        System.out.println(jugador.getNombre() + " no tiene suficiente dinero para pagar " + (int) cantidad + "€ a todos los jugadores. Debe hipotecar alguna propiedad o se declarará en bancarrota.");
                    } else {
                        System.out.println(jugador.getNombre() + " no puede pagar ni hipotecar propiedades. Se declara en bancarrota.");
                        for (Casilla c : jugador.getPropiedades()) {
                            c.setDuenho(banca);
                        }
                        jugador.getPropiedades().clear();
                        this.solvente = false;
                    } 
                } else {
                    // paga a todos los jugadores
                    for (Jugador j : this.jugadores) {
                        if (j != jugador) {
                            jugador.sumarFortuna(-cantidad);
                            jugador.sumarGastos(cantidad);
                            j.sumarFortuna(cantidad);
                        }
                    }
                    System.out.println(jugador.getNombre() + " paga " + (int) cantidad + "€ a cada jugador");
                }
                break;

            case "retroceder":
                int n = Integer.parseInt(partes[1]);
                int posActual = casillaActual.getPosicion();
                int nuevaPos = posActual - n;
                if (nuevaPos < 1) nuevaPos += 40; // por si cruza el inicio del tablero

                Casilla retroceso = null;
                for (ArrayList<Casilla> lado : this.tablero.getPosiciones()) {
                    for (Casilla c2 : lado) {
                        if (c2.getPosicion() == nuevaPos) {
                            retroceso = c2;
                            break;
                        }
                    }
                    if (retroceso != null) break;
                }

                if (retroceso != null) {
                    jugador.getAvatar().setLugar(retroceso);
                    System.out.println(jugador.getNombre() + " retrocede " + n + " casillas hasta " + retroceso.getNombre() + ".");
                    retroceso.evaluarCasilla(jugador, banca, 0);
                }
                break;
        }
    }

    public void moverTransporteMasCercano(Jugador jugador, Jugador banca) {
        if (jugador == null || this.tablero == null)  return;

        // casilla actual del jugador
        Casilla origen = jugador.getAvatar().getLugar();
        if (origen == null) return;

        // buscar el transporte más cercano hacia delante
        ArrayList<ArrayList<Casilla>> lados = this.tablero.getPosiciones();
        Casilla destino = null;
        int posActual = origen.getPosicion();
        int minDistancia = 41; // suponemos que la distancia minima  sera como máximo una vuelta al tablero

        for (ArrayList<Casilla> lado : lados) {
            for (Casilla c : lado) {
                if (c.getTipo().equalsIgnoreCase("transporte")) {
                    int distancia = (c.getPosicion() - posActual + 40) % 40;
                    if (distancia > 0 && distancia < minDistancia) {
                        minDistancia = distancia;
                        destino = c;
                    }
                }
            }
        }

        if (destino == null) {
            System.out.println("No se encontró ninguna casilla de transporte en el tablero.");
            return;
        }

        // mover el avatar hasta la casilla de transporte
        jugador.getAvatar().moverAvatar(this.tablero.getPosiciones(), minDistancia);
        System.out.println(jugador.getNombre() + " avanza hasta la casilla " + destino.getNombre() + ".");

        // si pasa por la salida, cobra 2.000.000€
        if (destino.getPosicion() < posActual) {
            jugador.sumarFortuna(Valor.SUMA_VUELTA);
            System.out.println(jugador.getNombre() + " pasa por la Salida y cobra " + Valor.SUMA_VUELTA + "€.");
        }

        // evaluar la casilla del destino, como es un caso diferente a caer de fomra normal en una casilla de transporte, hay que hacerlo aquí
        if (destino.getDuenho() == banca || destino.getDuenho() == null) {
            System.out.println("[" + destino.getNombre() + "] Propiedad libre por " + destino.getValor() + "€. Usa el comando 'comprar' para adquirirla.");
        } else if (destino.getDuenho() == jugador) {
            System.out.println("[" + destino.getNombre() + "] Ya posees esta propiedad.");
        } else {
            // si pertenece a otro juugador, paga el doble de alquiler
            float alquiler = destino.getImpuesto() * 2;
            System.out.println("La casilla pertenece a " + destino.getDuenho().getNombre() + ". Debes pagar el doble de alquiler (" + (int) alquiler + "€).");

            // si no puede pagar, comprobar si puede hipotecar o sino bancarrota
            if (jugador.getFortuna() < alquiler) {
                boolean puedeHipotecar = jugador.getPropiedades() != null && !jugador.getPropiedades().isEmpty() && jugador.getHipotecas().size() < jugador.getPropiedades().size();

                if (puedeHipotecar) {
                    System.out.println(jugador.getNombre() + " no tiene suficiente dinero y debe hipotecar alguna propiedad para pagar.");
                } else {
                    System.out.println(jugador.getNombre() + " no puede pagar y se declara en bancarrota.");
                    for (Casilla c : jugador.getPropiedades()) {
                        c.setDuenho(destino.getDuenho());
                        destino.getDuenho().anhadirPropiedad(c);
                    }
                    jugador.getPropiedades().clear();
                    this.solvente = false;
                    return;
                }
            } else {
                jugador.sumarFortuna(-alquiler);
                jugador.sumarGastos(alquiler);
                destino.getDuenho().sumarFortuna(alquiler);
                System.out.println(jugador.getNombre() + " paga " + (int) alquiler + "€ a " + destino.getDuenho().getNombre() + ".");
            }
        }
    }

    public boolean isSolvente() {
        return this.solvente;
    }

    public void setSolvente(boolean valor) {
        this.solvente = valor;
    }
}