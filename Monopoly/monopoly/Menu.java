package monopoly;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import partida.*;

import static monopoly.Valor.FORTUNA_BANCA;

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
        Casilla salida = tablero.getPosiciones().get(1).get(1);

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

                // === mapear formato del PDF -> formato que espera tu analizarComando ===
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
                    analizarComando("Lanzar dados");
                }
                else if (l.startsWith("lanzar dados ")) {
                    String resto = linea.substring(linea.toLowerCase().indexOf("lanzar dados") + "lanzar dados".length()).trim();
                    analizarComando("Lanzar dados " + resto);
                }
                else if (l.startsWith("describir jugador")) {
                    String resto = linea.substring("describir jugador".length()).trim();
                    if (resto.isEmpty()) {
                        analizarComando("Describir jugador");
                    } else {
                        analizarComando("Describir jugador " + resto);
                    }
                }
                else if (l.startsWith("describir ")) {
                    String nombre = linea.substring("describir".length()).trim();
                    analizarComando("Describir casilla " + nombre);
                }
                else if (l.equals("listar jugadores")) {
                    analizarComando("Listar jugadores");
                }
                else if (l.equals("listar enventa") || l.equals("listar propiedades en venta")) {
                    analizarComando("Listar propiedades en venta");
                }
                else if (l.equals("acabar turno")) {
                    analizarComando("Acabar turno");
                }
                else if (l.equals("salir carcel") || l.equals("salir cárcel")
                        || l.equals("salir de la carcel") || l.equals("salir de la cárcel")
                        || l.startsWith("salir carcel ") || l.startsWith("salir cárcel ")
                        || l.startsWith("salir de la carcel ") || l.startsWith("salir de la cárcel ")) {
                    // Normalizamos a "Salir de la cárcel ..."
                    String canon = "Salir de la cárcel" + linea.substring(linea.indexOf("salir")).substring("salir".length()).replaceFirst("(?i)\\s*de\\s*la\\s*c(a|á)rcel", "").trim();
                    // canon ahora es "Salir de la cárcel" + (opcional nombre)
                    String resto = linea.toLowerCase().startsWith("salir") ? linea.substring(linea.indexOf(' ')).trim() : "";
                    // más simple: llamamos directamente con el formato estándar
                    String nombrePosible = linea.substring(linea.indexOf("salir") + "salir".length()).replaceAll("(?i)\\s*de\\s*la\\s*c[aá]rcel", "").trim();
                    if (nombrePosible.isEmpty()) {
                        analizarComando("Salir de la cárcel");
                    } else {
                        analizarComando("Salir de la cárcel " + nombrePosible);
                    }
                }
                else if (l.startsWith("comprar ")) {
                    String prop = linea.substring("comprar".length()).trim();
                    analizarComando("Comprar una propiedad " + prop);
                }
                else if (l.startsWith("describir avatar ")) {
                    String id = linea.substring("describir avatar".length()).trim();
                    analizarComando("Describir avatar " + id);
                }
                else if (l.equals("listar avatares")) {
                    analizarComando("Listar avatares");
                }
                else {
                    System.err.println("Comando no reconocido en el archivo: " + lineaOriginal);
                }
            }
        } catch (IOException e) {
            System.err.println("Error leyendo " + ruta + ": " + e.getMessage());
        }
    }

    private void analizarComando(String comando) {
        if (comando.startsWith("Describir jugador")) {
            String partes[] = comando.split(" ");
            descJugador(partes);
        }
        else if (comando.startsWith("Describir avatar")) {
            String Idavatar = comando.substring("Describir avatar ".length()).trim();
            descAvatar(Idavatar);
        }
        else if (comando.startsWith("Describir casilla")) {
            String nombreCasilla = comando.substring("Describir casilla".length()).trim();
            descCasilla(nombreCasilla);
        }
        else if (comando.startsWith("Lanzar dados")) {
            String cadenaComprobar = comando.substring("Lanzar dados".length()).trim();
            if (cadenaComprobar.isEmpty()) {
                lanzarDados();
            }
            else {
                String[] valores = cadenaComprobar.split("\\+");
                lanzarDadosValor(valores);
            }
        }
        else if (comando.startsWith("Comprar una propiedad")) {
            String nombrePropiedad = comando.substring("Comprar una propiedad".length()).trim();
            comprar(nombrePropiedad);
        }
        else if (comando.startsWith("Salir de la cárcel")) {
            // Puede venir "Salir de la cárcel" o "Salir de la cárcel <Nombre>"
            String resto = comando.substring("Salir de la cárcel".length()).trim();
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
        else if (comando.equals("Listar propiedades en venta")) {
            listarVenta();
        }
        else if (comando.equals("Listar jugadores")) {
            listarJugadores();
        }
        else if (comando.equals("Listar avatares")) {
            listarAvatares();
        }
        else if (comando.equals("Acabar turno")) {
            acabarTurno();
        }
        else if (comando.startsWith("Crear jugador")) {
            String datosJugador = comando.substring("Crear jugador".length()).trim();
            String[] partes = datosJugador.isEmpty() ? new String[0] : datosJugador.split("\\s+");
            if (partes.length >= 2) {
                crearJugadorArchivo(partes); // partes[0]=nombre, partes[1]=tipo
            } else {
                System.err.println("Formato inválido para 'Crear jugador'. Uso: Crear jugador <Nombre> <Tipo>");
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
        if(this.jugadores == null){
            this.jugadores = new ArrayList<>();
        }
        if(this.avatares == null){
            this.avatares = new ArrayList<>();
        }
        Casilla salida = tablero.getPosiciones().get(1).get(0);
        Jugador jugador = new Jugador(partes[0], partes[1], salida, avatares);
        this.jugadores.add(jugador);
    }

    //EN LAS FUNCIONES DE DESCRIBIR Y LISTAR SOLAMENTE HAY QUE HACER PRINTS, LAS HAGO DE ÚLTIMO

    private void descJugador(String[] partes) {
    }

    private void descAvatar(String ID) {
    }

    private void descCasilla(String nombre) {
    }

    //Método que ejecuta todas las acciones relacionadas con el comando 'lanzar dados'.
    private int dobles=0;

    private void lanzarDados() {

        //mínimo 2 jugadores
        if (this.jugadores == null || this.jugadores.size() < 2) {
            System.err.println("Error: no hay suficientes jugadores (mínimo 2) para lanzar dados.");
            return;
        }

        if (this.dado1 == null){
            this.dado1 = new Dado();
        }
        if (this.dado2 == null){
            this.dado2 = new Dado();
        }
        this.enCurso = true;

        int valor1 = this.dado1.hacerTirada();
        int valor2 = this.dado2.hacerTirada();
        int suma = valor1 + valor2;

        Jugador actual = this.jugadores.get(this.turno);
        Avatar av = actual.getAvatar();
        Casilla origen = av.getLugar();

        av.moverAvatar(this.tablero.getPosiciones(), suma);

        Casilla destino = av.getLugar();

        evaluarCasilla(destino);

        // Control de dobles / turno (cambios mínimos, inline)
        if (suma == 12) { // tu criterio de "dobles"
            dobles++;
            if (dobles < 3) {
                lanzarDados(); // repite turno
            } else {
                Casilla carcel = this.tablero.getPosiciones().get(1).get(11);
                av.setLugar(carcel);
                dobles = 0;
                // pasa al siguiente jugador (ciclo 0..n-1)
                this.turno = (this.turno + 1) % this.jugadores.size();
            }
        } else {
            dobles = 0;
            acabarTurno();
        }
    }

    private void lanzarDadosValor(String[] valores) {
        // pendiente
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
        casilla.comprarCasilla(comprador,this.banca);
    }

    //Método que ejecuta todas las acciones relacionadas con el comando 'salir carcel'.
    // Solo puede ejecutarlo el jugador cuyo índice coincide con 'turno' (o si en el comando se especificó ese jugador).
    private void salirCarcel() {
        if (this.jugadores == null || this.jugadores.isEmpty()) {
            System.out.println("No hay jugadores.");
            return;
        }
        Jugador actual = this.jugadores.get(this.turno);
        Casilla casillaActual = actual.getAvatar().getLugar();
        Casilla carcel = this.tablero.getPosiciones().get(1).get(11);
        if(casillaActual == carcel){
            actual.sumarFortuna(-500000);
            this.banca.sumarFortuna(500000);
            // según tu enunciado, puede lanzar los dados:
            lanzarDados();
        }
        else{
            System.out.println("El jugador " + actual.getNombre() + "no está en la cárcel");
        }
    }

    private void listarVenta() {
    }

    private void listarJugadores() {
    }

    private void listarAvatares() {
    }

    private void acabarTurno() {
    Jugador actual = this.jugadores.get(this.turno);
    System.out.println("El jugador " + actual.getNombre() + " termina su turno.");

    this.turno = (this.turno + 1) % this.jugadores.size();

    Jugador siguiente = this.jugadores.get(this.turno);
    System.out.println("Le toca al jugador " + siguiente.getNombre() + ".");

    }


}


