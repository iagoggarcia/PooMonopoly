package monopoly;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
import partida.*;

public class Menu {

    //Atributos
    private ArrayList<Jugador> jugadores; //Jugadores de la partida.
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

    public Menu() {
        this.enCurso = false; //para elegir en el main si se inicializa o no con la funcion iniciarpartida
    }

    // Método para inciar una partida: crea los jugadores y avatares.
    public void iniciarPartida() {
        Scanner sc = new Scanner(System.in);

        // 1. Crear banca y tablero
        Jugador banca = new Jugador();
        banca.sumarFortuna(Valor.FORTUNA_BANCA);
        Tablero tablero = new Tablero(banca);

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
            jugadores.add(jugador);

            System.out.println("{");
            System.out.println("  nombre: " + nombre + ",");
            System.out.println("  avatar: " + jugador.getAvatar().getId());
            System.out.println("}");
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

            System.out.println("\n--- Turno de " + actual.getNombre() + " ---");
            System.out.println("Comandos:");
            //System.out.println(" - Crear jugador <nombre> <tipo>");
            System.out.println(" - ver tablero");
            System.out.println(" - lanzar dados");
            System.out.println(" - lanzar dados X+Y");
            System.out.println(" - describir jugador nombre");
            System.out.println(" - describir nombre");
            //System.out.println(" - Describir avatar <id>");
            System.out.println(" - listar jugadores");
            //System.out.println(" - Listar avatares");
            System.out.println(" - listar enventa");
            System.out.println(" - comprar nombre");
            System.out.println(" - salir cárcel");
            System.out.println(" - acabar turno");
            System.out.println(" - salir (para terminar)\n");

            System.out.print("> "); // prompt
            String cmd = in.nextLine().trim();
            if (cmd.equalsIgnoreCase("salir")) {
                System.out.println("Fin de la partida.");
                this.enCurso = false;
                break;
            }
            if (cmd.isEmpty()) continue;

            analizarComando(cmd);
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
                        analizarComando("Crear jugador " + nombre + " " + tipo);
                    } else {
                        System.err.println("Formato inválido para 'crear jugador': " + lineaOriginal);
                    }
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
                else if (l.equals("listar enventa") || l.equals("listar propiedades en venta")) {
                    analizarComando("listar propiedades en venta");
                }
                else if (l.equals("acabar turno")) {
                    analizarComando("acabar turno");
                }
                else if (l.equals("salir carcel") || l.equals("salir cárcel")
                        || l.equals("salir de la carcel") || l.equals("salir de la cárcel")
                        || l.startsWith("salir carcel ") || l.startsWith("salir cárcel ")
                        || l.startsWith("salir de la carcel ") || l.startsWith("salir de la cárcel ")) {
                    // Normalizamos a "Salir de la cárcel ..."
                    String canon = "salir de la cárcel" + linea.substring(linea.indexOf("salir")).substring("salir".length()).replaceFirst("(?i)\\s*de\\s*la\\s*c(a|á)rcel", "").trim();
                    // canon ahora es "Salir de la cárcel" + (opcional nombre)
                    String resto = linea.toLowerCase().startsWith("salir") ? linea.substring(linea.indexOf(' ')).trim() : "";
                    // más simple: llamamos directamente con el formato estándar
                    String nombrePosible = linea.substring(linea.indexOf("salir") + "salir".length()).replaceAll("(?i)\\s*de\\s*la\\s*c[aá]rcel", "").trim();
                    if (nombrePosible.isEmpty()) {
                        analizarComando("salir de la cárcel");
                    } else {
                        analizarComando("salir de la cárcel " + nombrePosible);
                    }
                }
                else if (l.startsWith("comprar ")) {
                    String prop = linea.substring("comprar".length()).trim();
                    analizarComando("comprar " + prop);
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
        Casilla salida = tablero.getPosiciones().get(1).get(0);
        Jugador jugador = new Jugador(partes[0], partes[1], salida, avatares);
        this.jugadores.add(jugador);
    }

    //EN LAS FUNCIONES DE DESCRIBIR Y LISTAR SOLAMENTE HAY QUE HACER PRINTS, LAS HAGO DE ÚLTIMO

    // --- helpers opcionales para no repetir búsquedas ---
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

    // --- Describir jugador: delega en avatar y casilla ---
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
        System.out.println("===================================");
    }

    // --- Describir avatar por ID ---
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

    // --- Describir casilla por nombre (usa Tablero.encontrar_casilla) ---
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
        System.out.println("Nombre: " + c.getNombre());
        System.out.println("Tipo: " + c.getTipo());
        System.out.println("Posición: " + c.getPosicion());
        System.out.println("Valor: " + c.getValor());
        System.out.println("Dueño: " + (c.getDuenho() != null ? c.getDuenho().getNombre() : "(sin dueño)"));
        // Si quieres, también: avatares presentes
        if (c.getAvatares() != null && !c.getAvatares().isEmpty()) {
            System.out.print("Avatares en la casilla: ");
            boolean primero = true;
            for (Avatar av : c.getAvatares()) {
                if (!primero) System.out.print(", ");
                System.out.print(av.getId());
                primero = false;
            }
            System.out.println();
        }
    }


    //Método que ejecuta todas las acciones relacionadas con el comando 'lanzar dados'.
    private int dobles=0;

    private void lanzarDados() { //funcion para valores random de dados
        if (this.jugadores == null || this.jugadores.size() < 2) {
            System.err.println("Error: no hay suficientes jugadores (mínimo 2) para lanzar dados.");
            return;
        }

        if (this.dado1 == null) this.dado1 = new Dado();
        if (this.dado2 == null) this.dado2 = new Dado();

        this.enCurso = true;

        int valor1 = this.dado1.hacerTirada();
        int valor2 = this.dado2.hacerTirada();

        System.out.println("Dados: " + valor1 + " y " + valor2 + " (suma = " + (valor1 + valor2) + ")");

        realizarTirada(valor1, valor2);
    }

    private void lanzarDadosValor(String[] valores) { //funcion para forzar valor de dados
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

        if (destino.getPosicion() < origen.getPosicion()) {
            actual.sumarFortuna(Valor.SUMA_VUELTA);
            System.out.println(actual.getNombre() + " pasa por Salida y recibe " + Valor.SUMA_VUELTA + "€.");

            actual.setVueltas(actual.getVueltas() + 1);
        }

        destino.evaluarCasilla(actual, this.banca, suma);

        imprimirTablero();//se imprime el tablero en el estado actual

        // si el jugador cayó en 'Ir a la Cárcel', se aplica el encarcelamiento
        // esto pensé que iba en evaluarcasilla, pero como no tengo acceso al tablero desde casilla, tiene que ser desde aquí
        if (destino.getNombre().equalsIgnoreCase("Ir a la cárcel") ||
            destino.getNombre().equalsIgnoreCase("Ir a carcel") ||
            destino.getNombre().equalsIgnoreCase("Ircarcel")) {

            System.out.println(actual.getNombre() + " es trasladado a la Cárcel.");
            actual.encarcelar(this.tablero.getPosiciones()); // mueve al avatar y marca enCarcel=true
            acabarTurno();
            return; // el turno termina inmediatamente tras ser encarcelado
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
            dobles++;

            if (dobles < 3) {
                lanzarDados(); // repite turno (solo usa aleatorio de nuevo)
            } else {
                System.out.println(actual.getNombre() + " ha sacado tres dobles seguidos y va a la cárcel.");
                Casilla carcel = this.tablero.encontrar_casilla("Cárcel");
                av.setLugar(carcel);
                // estas dos líneas de aquí se pueden cambiar por acabarTUrno()
                // dobles = 0;
                // this.turno = (this.turno + 1) % this.jugadores.size();
                acabarTurno();
            }
        } else {
            acabarTurno();
        }
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
        System.out.println(jugadores.get(turno).getNombre() + " intenta comprar " + nombre + "...");
        casilla.comprarCasilla(comprador,this.banca);
    }

    //Método que ejecuta todas las acciones relacionadas con el comando 'salir carcel'.
    // Solo puede ejecutarlo el jugador cuyo índice coincide con 'turno' (o si en el comando se especificó ese jugador).
    private void salirCarcel() {
        if (this.jugadores == null || this.jugadores.isEmpty()) {
            System.out.println("No hay jugadores en la partida.");
            return;
        }

        Jugador actual = this.jugadores.get(this.turno); 
        Casilla casillaActual = actual.getAvatar().getLugar();
        Casilla carcel = this.tablero.encontrar_casilla("Cárcel"); 

        // comprobamos si el jugador está realmente en la carcel
        if (casillaActual != carcel) {
            System.out.println("El jugador " + actual.getNombre() + " no está en la cárcel.");
            return;
        }

        System.out.println(actual.getNombre() + " está en la cárcel e intenta salir tirando los dados...");

        // tirada de dados para intentar sacar dobles
        int valor1 = this.dado1.hacerTirada();
        int valor2 = this.dado2.hacerTirada();
        System.out.println("Dados: " + valor1 + " y " + valor2 + " (suma = " + (valor1 + valor2) + ")");

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
            System.out.println("Tras tres turnos sin sacar dobles, " + actual.getNombre() + " debe pagar 500.000€ para salir de la cárcel.");
            
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
            acabarTurno();
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
            System.out.println("Propiedades: " + jugador.getPropiedades());
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

    private void acabarTurno() {
        // aquí se podría añadir la comprobación de que hay jugadores en la partida
        
        Jugador actual = this.jugadores.get(this.turno);
        System.out.println("El jugador " + actual.getNombre() + " termina su turno.");

        // si reinicias el contador de dobles aqui no tienes que acordarte de hacerlo cada vez que uses el método
        this.dobles = 0;
        // se puede poner aqui o al final, es indiferente

        this.turno = (this.turno + 1) % this.jugadores.size();
        Jugador siguiente = this.jugadores.get(this.turno);

        System.out.println("Le toca al jugador " + siguiente.getNombre() + ".");

        comprobarSolaresNoComprados(); // cada vez que un turno acaba
    }

    private void comprobarSolaresNoComprados() {
        // verifica si todos los jugadores han dado 4 vueltas o más
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
}


