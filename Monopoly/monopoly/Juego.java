package monopoly;

import java.util.ArrayList;
import java.util.HashMap;
import monopoly.cartas.*;
import monopoly.casillas.*;
import monopoly.casillas.especial.*;
import monopoly.casillas.propiedad.*;
import monopoly.edificios.*;
import partida.*;


public class Juego implements Comando{

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
    private static Juego instancia;
    private boolean enSubmenuBancarrota = false;
    private Jugador jugadorDeudor = null;
    private Jugador jugadorAcreedor = null;
    private float deudaPendiente = 0;
    private ArrayList<Carta> cartasSuerte;
    private ArrayList<Carta> cartasComunidad;
    private int indiceSuerte = 0;
    private int indiceComunidad = 0;

    public static Consola consola = new Consolanormal();

    public Juego() {
        instancia = this;
        this.jugadores = new ArrayList<>();
        this.edificios = new ArrayList<>();
        this.enCurso = false; //para elegir en el main si se inicializa o no con la funcion iniciarpartida
    }

    public static Juego getInstancia(){
        return instancia;
    }

    public void inicializarCartas() {

    this.cartasSuerte = new ArrayList<>();
    this.cartasComunidad = new ArrayList<>();

        // ======== CARTAS DE SUERTE ========
        cartasSuerte.add(new SuerteCarta(1, "Decides hacer un viaje de placer. Avanza hasta Solar19. Si pasas por la casilla de Salida, cobra 2.000.000€."));
        cartasSuerte.add(new SuerteCarta(2, "Los acreedores te persiguen. Ve a la cárcel."));
        cartasSuerte.add(new SuerteCarta(3, "¡Has ganado la lotería! Recibe 1.000.000€."));
        cartasSuerte.add(new SuerteCarta(4, "Paga a cada jugador 250.000€."));
        cartasSuerte.add(new SuerteCarta(5, "Hora punta: retrocede 3 casillas."));
        cartasSuerte.add(new SuerteCarta(6, "Te multan por usar el móvil. Paga 150.000€."));
        cartasSuerte.add(new SuerteCarta(7, "Avanza al transporte más cercano."));

        // ======== CARTAS DE CAJA DE COMUNIDAD ========
        cartasComunidad.add(new CajaComunidadCarta(1, "Paga 500.000€ por un fin de semana en un balneario de 5 estrellas."));
        cartasComunidad.add(new CajaComunidadCarta(2, "Te investigan. Ve a la cárcel."));
        cartasComunidad.add(new CajaComunidadCarta(3, "Colócate en la casilla de Salida."));
        cartasComunidad.add(new CajaComunidadCarta(4, "Cobra 500.000€ de Hacienda."));
        cartasComunidad.add(new CajaComunidadCarta(5, "Retrocede hasta Solar1."));
        cartasComunidad.add(new CajaComunidadCarta(6, "Ve a Solar20 para disfrutar de San Fermín."));
    }


    public Carta sacarCarta(String tipo) {

        Carta carta = null;

        if (tipo.equalsIgnoreCase("suerte")) {
            carta = cartasSuerte.get(indiceSuerte);
            indiceSuerte = (indiceSuerte + 1) % cartasSuerte.size();
        }
        else if (tipo.equalsIgnoreCase("caja") ||
                tipo.equalsIgnoreCase("comunidad") ||
                tipo.equalsIgnoreCase("caja de comunidad")) {

            carta = cartasComunidad.get(indiceComunidad);
            indiceComunidad = (indiceComunidad + 1) % cartasComunidad.size();
        }

        return carta;
    }

    //AUN TENGO QUE COMPROBAR TODAS LAS PETICIONES DE DATOS DESDE ESTE ARCHIVO, PORQUE AUNQUE LO HAGA CON CONSOLA NO DEBERIA HACERLO DESDE AQUI, SI NO DESDE MENU

    // Método para inciar una partida: crea los jugadores y avatares.
    public void iniciarPartida() { //esta funcion se queda en juego, pero todos los system in y system out van en menu, y el menu de comandos tambien va en menu

        // 1. Crear banca y tablero, inicializar cartas
        this.banca = new Jugador();
        banca.sumarFortuna(Valor.FORTUNA_BANCA);
        this.tablero = new Tablero(banca);
        inicializarCartas();

        // 2. Obtener la casilla "Salida" donde se colocan los avatares
        Casilla salida = tablero.getPosiciones().get(0).get(0);

        // 3. Preguntar número de jugadores
        int numJugadores = 0;
        while (true) {
            consola.imprimir("¿Cuántos jugadores van a jugar? (2-4): ");
            try {
                numJugadores = Integer.parseInt(consola.leer().trim());
                if (numJugadores >= 2 && numJugadores <= 4) break;
            } catch (NumberFormatException e) { }
            consola.imprimir("Número no válido, introduce 2, 3 o 4.");
        }

        // 4. Crear los jugadores
        ArrayList<Jugador> jugadores = new ArrayList<>();
        ArrayList<Avatar> avatares = new ArrayList<>();

        for (int i = 1; i <= numJugadores; i++) {
            consola.imprimir("Introduce el nombre del jugador " + i + ": ");
            String nombre = consola.leer().trim();

            String tipoAvatar;
            while (true) {
                consola.imprimir("Tipo de avatar (coche | esfinge | sombrero | pelota): ");
                tipoAvatar = consola.leer().trim().toLowerCase();
                if (tipoAvatar.equals("coche") || tipoAvatar.equals("esfinge")
                        || tipoAvatar.equals("sombrero") || tipoAvatar.equals("pelota")) break;
                consola.imprimir("Tipo no válido. Intenta de nuevo.");
            }

            Jugador jugador = new Jugador(nombre, tipoAvatar, salida, avatares);
            salida.setContador(salida.getContador()+1); //aumento el contador de casilla por cada jugador que se cree
            jugadores.add(jugador);

            consola.imprimir("Jugador creado: ");
            consola.imprimir("{");
            consola.imprimir("  nombre: " + jugador.getNombre() + ",");
            consola.imprimir("  avatar: " + jugador.getAvatar().getId());
            consola.imprimir("}\n");
        }

        // 6. Guardar la información en los atributos de Menu
        this.jugadores = jugadores;
        this.avatares = avatares;

        this.turno = 0;          // Jugador en índice 0 empieza
        this.solvente = true;
        this.enCurso = true;

        consola.imprimir("\nPartida inicializada correctamente con " + numJugadores + " jugadores.\n");
    }

    /*
    * Función que saca el bucle que teníamos antes en iniciarPartida() de los comandos para seguir jugando que usamos
    * tanto en esa función, como ahora en ejecutarArchivoComandos. Así al acabar de leer el archivo, en vez de terminar
    * permite seguir jugando como nos pedían.
    */
    //funcion correcta para imprimir comandos
    public void mostrarComandos() {

        if (enSubmenuBancarrota) {
            consola.imprimir("Estás en bancarrota. Solo puedes ejecutar:");
            consola.imprimir(" - hipotecar <casilla>");
            consola.imprimir(" - bancarrota");
            return;
        }

        consola.imprimir("Comandos disponibles:");
        consola.imprimir(" - ver tablero");
        consola.imprimir(" - describir <jugador|avatar|casilla>");
        consola.imprimir(" - lanzar dados [X+Y]");
        consola.imprimir(" - comprar <casilla>");
        consola.imprimir(" - vender <tipo> <casilla> <num>");
        consola.imprimir(" - hipotecar <casilla>");
        consola.imprimir(" - deshipotecar <casilla>");
        consola.imprimir(" - edificar <tipo>");
        consola.imprimir(" - crear jugador <nombre> <tipoAvatar>");
        consola.imprimir(" - listar jugadores|avatares|edificios|enventa");
        consola.imprimir(" - estadisticas [jugador]");
        consola.imprimir(" - acabar turno");
        consola.imprimir(" - bancarrota");
    }

    //funcion para bloquear el resto de comandos si el jugador esta endeudado
    public boolean comandoPermitido(String comando) {
        if (!enSubmenuBancarrota) return true;

        if (comando.startsWith("hipotecar")) return true;
        if (comando.equalsIgnoreCase("bancarrota")) return true;

        return false;
    }


    //función para crear jugador desde archivo
    @Override
    public void crearJugadorArchivo(String nombre, String tipo) {

        if (jugadores == null) {
            jugadores = new ArrayList<>();
        }

        if (jugadores.size() >= 4) {
            consola.imprimir("Error: ya hay 4 jugadores. No se puede añadir más.");
            return;
        }

        if (banca == null) {
            banca = new Jugador(Valor.FORTUNA_BANCA);
        }

        if (tablero == null) {
            tablero = new Tablero(banca);
        }

        if (avatares == null) {
            avatares = new ArrayList<>();
        }

        Casilla salida = tablero.getPosiciones().get(0).get(0);

        Jugador jugador = new Jugador(nombre, tipo, salida, avatares);
        jugadores.add(jugador);

        salida.setContador(salida.getContador() + 1);

        consola.imprimir("Jugador creado:");
        consola.imprimir("{");
        consola.imprimir("  nombre: " + jugador.getNombre());
        consola.imprimir("  avatar: " + jugador.getAvatar().getId());
        consola.imprimir("}");
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

    @Override
    public void descJugador(String nombreJugador) {
        if (nombreJugador == null || nombreJugador.isBlank()) {
            consola.imprimir("Uso correcto: Describir <Nombre>");
            return;
        }
        Jugador j = buscarJugadorPorNombre(nombreJugador.trim());
        if (j == null) {
            consola.imprimir("No existe un jugador con el nombre '" + nombreJugador + "'.");
            return;
        }

        consola.imprimir("===== Información del jugador =====");
        consola.imprimir("Nombre: " + j.getNombre());
        consola.imprimir("Avatar: " + j.getAvatar().getId());
        consola.imprimir("Fortuna: " + j.getFortuna());
        if (j.getPropiedades() != null && !j.getPropiedades().isEmpty()) {
            consola.imprimir("Propiedades: " + j.getPropiedades().toString());
        }

        ArrayList<Casilla> solaresHipotecados = j.getHipotecas();
        if (solaresHipotecados != null && !solaresHipotecados.isEmpty()) {
            consola.imprimir("Propiedades hipotecadas: " + solaresHipotecados.toString());
        }

        if (j.getEdificios() != null && !j.getEdificios().isEmpty()) {
            consola.imprimir("Edificios: " + j.getEdificios().toString());
        }
        consola.imprimir("===================================");
    }

    @Override
    public void descAvatar(String id) {
        if (id == null || id.isBlank()) {
            consola.imprimir("Uso correcto: Describir avatar <ID>");
            return;
        }
        Avatar a = buscarAvatarPorId(id.trim());
        if (a == null) {
            consola.imprimir("No existe un avatar con ID '" + id + "'.");
            return;
        }

        consola.imprimir("---- Avatar ----");
        consola.imprimir("ID: " + a.getId());
        consola.imprimir("Tipo: " + a.getTipo());
        consola.imprimir("Jugador: " + (a.getJugador() != null ? a.getJugador().getNombre() : "(ninguno)"));
        if (a.getLugar() != null) {
            consola.imprimir("Ubicación: " + a.getLugar().getNombre());
        } else {
            consola.imprimir("Ubicación: (desconocida)");
        }
    }

    @Override
    public void descCasilla(String nombreCasilla) {
        if (nombreCasilla == null || nombreCasilla.isBlank()) {
            consola.imprimir("Uso correcto: describir <Nombre>");
            return;
        }
        if (this.tablero == null) {
            consola.imprimir("No hay tablero inicializado.");
            return;
        }

        Casilla c = this.tablero.encontrar_casilla(nombreCasilla.trim());
        if (c == null) {
            consola.imprimir("No existe una casilla llamada '" + nombreCasilla + "'.");
            return;
        }

        consola.imprimir("---- Casilla ----");
        String info = c.infoCasilla();
        consola.imprimir(info);
    }


    //Método que ejecuta todas las acciones relacionadas con el comando 'lanzar dados'.

    @Override
    public void lanzarDados() { //funcion para valores random de dados
        //comprobacion para no dejarle tirar normalmente si está en la cárcel
        Jugador actual = this.jugadores.get(this.turno);
        if(actual.isEnCarcel()){
            consola.imprimir("Actualmente el jugador solo puede lanzar dados para salir de la cárcel desde el comando salir cárcel");
            //hay que hacerlo así porque puede ser que caiga en la casilla cárcel sin ser encarcelado
        }
        else if((!tirado)||tirado && lanzamientos!=0) {//si aún no hemos tirado, o hemos tirado pero hemos sacado dobles, podemos tirar
            if (this.jugadores == null || this.jugadores.size() < 2) {
                consola.imprimir("Error: no hay suficientes jugadores (mínimo 2) para lanzar dados.");
                return;
            }

            if (this.dado1 == null) this.dado1 = new Dado();
            if (this.dado2 == null) this.dado2 = new Dado();

            this.enCurso = true;//por si no ha tirado nadie aun, se cambia esta variable para indicar que la partida está en juego

            int valor1 = this.dado1.hacerTirada();
            int valor2 = this.dado2.hacerTirada();

            consola.imprimir("Dados: " + valor1 + " y " + valor2 + " (suma = " + (valor1 + valor2) + ")");

            tirado = true;
            realizarTirada(valor1, valor2);
        }
        else{
            consola.imprimir("El jugador ya no tiene el derecho a tirar, escoja otro comando");
        }
    }

    @Override
    public void lanzarDadosValor(int d1, int d2) {
        if (jugadores.get(turno).isEnCarcel()) {
            consola.imprimir("Actualmente el jugador solo puede lanzar dados para salir de la cárcel desde el comando salir cárcel");
            return;
        }

        if ((!tirado) || (tirado && lanzamientos != 0)) {

            // Aquí NO parseamos nada, ya vienen los ints listos
            realizarTirada(d1, d2);

            tirado = true;

        } else {
            consola.imprimir("El jugador ya no tiene el derecho a tirar, escoja otro comando");
        }
    }


    // Imprime el tablero tal y como está ahora mismo
    @Override
    public void imprimirTablero() {
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
            consola.imprimir(actual.getNombre() + " pasa por Salida y recibe " + Valor.SUMA_VUELTA + "€.");

            actual.setVueltas(actual.getVueltas() + 1);//suma una vuelta
        }

        this.solvente = destino.evaluarCasilla(actual, this.banca, suma);//evaluamos casilla

        if (!this.solvente) {
            consola.imprimir(actual.getNombre() + " ha quedado en bancarrota y queda fuera del juego.");
            acabarTurno();
            return;
        }

        // si el jugador cayó en 'Ir a la Cárcel', se aplica el encarcelamiento
        // esto pensé que iba en evaluarcasilla, pero como no tengo acceso al tablero desde casilla, tiene que ser desde aquí
        if (destino.getNombre().equalsIgnoreCase("Ir a la cárcel") ||
            destino.getNombre().equalsIgnoreCase("Ir a carcel") ||
            destino.getNombre().equalsIgnoreCase("Ircarcel")) {

            consola.imprimir(actual.getNombre() + " es trasladado a la Cárcel.");
            actual.encarcelar(this.tablero.getPosiciones()); // mueve al avatar y marca enCarcel=true
        }

        // si la casilla es de tipo impuestos, añadir el dinero al Parking
        if (destino instanceof Impuesto imp) {
            Casilla cParking = tablero.encontrar_casilla("Parking");
            if (cParking instanceof Parking parking) {
                parking.sumarBote(imp.getImpuesto());
            }
        }

        // Control de dobles / turno
        if (valor1 == valor2){
            consola.imprimir("¡" + actual.getNombre() + " ha sacado dobles!");
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
                consola.imprimir("Tienes un lanzamiento extra");
                // después de este print debería salir el menú otra vez y poder hacer lo que sea
            } else {
                consola.imprimir(actual.getNombre() + " ha sacado tres dobles seguidos y va a la cárcel.");
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
    @Override
    public void comprar(String nombre) {
        Casilla casilla = this.tablero.encontrar_casilla(nombre);
        
        if (casilla == null) {
            consola.imprimir("No existe una casilla con el nombre '" + nombre + "'.");
            return;
        }

        Jugador comprador = this.jugadores.get(this.turno); // jugador del turno
        String nombre_casilla_comprador = comprador.getAvatar().getLugar().getNombre();
        consola.imprimir(jugadores.get(turno).getNombre() + " intenta comprar " + nombre + "...");

        if (casilla instanceof Propiedad p) {
            if(nombre_casilla_comprador.equalsIgnoreCase(nombre)) {
                p.comprar(comprador, this.banca);
            }
            else{
                consola.imprimir("El jugador intenta comprar una casilla en la que no está posicionado");
            }
        }
    }

    @Override
    public void hipotecar(String nombre) {
        Casilla casilla = this.tablero.encontrar_casilla(nombre);

        if (casilla == null) {
            consola.imprimir("No existe una casilla con el nombre '" + nombre + "'.");
            return;
        }

        if (!(casilla instanceof Solar solar)) { // solo se puede hipotecar si es un solar
            consola.imprimir("No puedes hipotecar una casilla de tipo '" + casilla.getTipo() + "'.");
            return;
        }

        Jugador actual = this.jugadores.get(this.turno);

        // delegamos la lógica en el Solar
        solar.hipotecar(actual);
    }


    @Override
    public void deshipotecar(String nombre) {
        Casilla casilla = this.tablero.encontrar_casilla(nombre);

        if (casilla == null) {
            consola.imprimir("No existe una casilla con el nombre '" + nombre + "'.");
            return;
        }

        Jugador actual = this.jugadores.get(this.turno);
        if (!(casilla instanceof Solar solar)) { // solo se puede hipotecar si es un solar
            consola.imprimir("No puedes deshipotecar una casilla de tipo '" + casilla.getTipo() + "'.");
            return;
        }

        solar.deshipotecar(actual);

        // si hay deudas, tras deshipotecar, comprobar si ya puede pagar
        if (jugadorDeudor != null && jugadorDeudor.getFortuna() >= deudaPendiente) {  
            salirDeSubmenuBancarrota();
        }
    }

    //funcion para acceder a bancarrota desde menu
   @Override 
    public void bancarrota(){
        if(this.enSubmenuBancarrota){
            declararBancarrota(jugadorDeudor);
        }
        else{
            consola.imprimir("el jugador está en una situación económica viable");
        }
    }

    public void declararBancarrota(Jugador deudor) {
        consola.imprimir(deudor.getNombre() + " no puede pagar y se declara en bancarrota.");

        // si no hay acreedor, el acreedor es la banca
        Jugador receptor = (this.jugadorAcreedor != null) ? this.jugadorAcreedor : this.banca;

        if (deudor.getPropiedades() != null && !deudor.getPropiedades().isEmpty()) {
            for (Casilla c : deudor.getPropiedades()) {
                if (c instanceof Propiedad p) {
                    p.setDuenho(receptor); // las pasa al propietario de la deuda
                    receptor.anhadirPropiedad(p);
                }
            }
            deudor.getPropiedades().clear();
        }

        if (deudor.getEdificios() != null && !deudor.getEdificios().isEmpty()) {
            for (Edificio e : deudor.getEdificios()) {
                e.setPropietario(receptor);
                receptor.anhadirEdificioAJugador(e);
            }
            deudor.getEdificios().clear();
        }

        this.enSubmenuBancarrota = false;
        this.jugadorDeudor = null;
        this.deudaPendiente = 0;
        this.jugadorAcreedor = null;
    }

    public void activarSubmenuBancarrota(Jugador deudor, float deuda, Jugador acreedor) {
        this.enSubmenuBancarrota = true;
        this.jugadorDeudor = deudor;
        this.jugadorAcreedor = acreedor;
        this.deudaPendiente = deuda;

        consola.imprimir("\nEl jugador " + deudor.getNombre() + " no puede pagar.");
        consola.imprimir("Debe hipotecar alguna propiedad para pagar o declararse en bancarrota.");
        consola.imprimir("Opciones:");
        consola.imprimir(" - hipotecar <casilla>");
        consola.imprimir(" - bancarrota\n");
    }

    public void salirDeSubmenuBancarrota() {
        this.enSubmenuBancarrota = false;
        this.jugadorDeudor = null;
        this.jugadorAcreedor = null;
        this.deudaPendiente = 0;

        consola.imprimir("\nLa deuda ha sido saldada. Continúa el turno normalmente.\n");
    }

    // La puse aquí para no repetir código en las funciones del submenú de la cárcel
    private void intentarSalirTirandoDados(Jugador actual) {
        consola.imprimir(actual.getNombre() + " está en la cárcel e intenta salir tirando los dados...");

        if (this.dado1 == null) this.dado1 = new Dado();
        if (this.dado2 == null) this.dado2 = new Dado();

        int valor1 = this.dado1.hacerTirada();
        int valor2 = this.dado2.hacerTirada();
        consola.imprimir("Dados: " + valor1 + " y " + valor2 + " (suma = " + (valor1 + valor2) + ")");

        // si saca dobles sale sin pagar
        if (valor1 == valor2) {
            consola.imprimir("¡" + actual.getNombre() + " ha sacado dobles y sale de la cárcel!");
            actual.setEnCarcel(false);
            actual.setTiradasCarcel(0);
            realizarTirada(valor1, valor2);
            return;
        }

        // no saca dobles, pierde un intento
        actual.setTiradasCarcel(actual.getTiradasCarcel() + 1);
        consola.imprimir("No ha sacado dobles (" + actual.getTiradasCarcel() + " intento/s).");

        // si pierde los tres turnos sin sacar dobles, tiene que pagar
        if (actual.getTiradasCarcel() >= 3) {
            if (actual.getFortuna() >= 500000) {
                actual.sumarFortuna(-500000);
                actual.sumarGastos(500000);
                this.banca.sumarFortuna(500000);
                actual.setEnCarcel(false);
                actual.setTiradasCarcel(0);
                consola.imprimir("Tras tres turnos sin sacar dobles, " + actual.getNombre() + " debe pagar 500.000€ para salir de la cárcel.");
                realizarTirada(valor1, valor2);
            } else {
                consola.imprimir(actual.getNombre() + " no puede pagar la fianza.");
                // comprobar si tiene algún solar sin hipotecar
                boolean puedeHipotecar = actual.getPropiedades() != null && !actual.getPropiedades().isEmpty() && actual.getHipotecas().size() < actual.getPropiedades().size();

                if (puedeHipotecar) {
                    activarSubmenuBancarrota(actual, 500000, null);
                } else {
                    declararBancarrota(actual);
                    this.solvente = false;
                }
            }
        } else { // si aun no llego al tercer intento, sigue preso
            consola.imprimir(actual.getNombre() + " permanece en la cárcel.");
        }
    }

    /*
    * Función que se usa en salirCarcel para saber si el jugador quiere salir pagando (1), utilizar una carta de suerte (2)
    * o tirar los dados (3).
     */
    private void submenuCarcel(Jugador actual) {
        consola.imprimir(actual.getNombre() + ", ¿quieres salir pagando (1), utilizar una carta de suerte (2) o tirar los dados (3)?");
        int opcion = Integer.parseInt(consola.leer().trim());
        switch (opcion) {
            case 1:
                if (actual.getFortuna() < 500000) {
                    consola.imprimir(actual.getNombre() + " no puede salir pagando, no tiene dinero suficiente. Se intentará salir tirando los dados");
                    intentarSalirTirandoDados(actual);
                }
                else {
                    actual.setEnCarcel(false); // sale de la cárcel
                    actual.setTiradasCarcel(0);
                    actual.sumarFortuna(-500000); // se le resta lo que paga a la fortuna
                    actual.sumarGastos(500000); // para las estadísticas del jugador
                    this.banca.sumarFortuna(500000); // se le paga a la banca

                    consola.imprimir(actual.getNombre() + " ha pagado 500.000€ y sale de la cárcel. Ahora tira los dados para moverse.");

                    if (this.dado1 == null) this.dado1 = new Dado();
                    if (this.dado2 == null) this.dado2 = new Dado();

                    int valor1 = this.dado1.hacerTirada();
                    int valor2 = this.dado2.hacerTirada();
                    consola.imprimir("Dados: " + valor1 + " y " + valor2 + " (suma = " + (valor1 + valor2) + ")");

                    realizarTirada(valor1, valor2);
                }
                break;
            case 2:
                // ni idea de a qué se referían con esto en el guion
                break;
            case 3:
                intentarSalirTirandoDados(actual);
                break;
            default:
                consola.imprimir("Opción no válida. Debes elegir 1, 2 o 3.");
                break;
        }
    }

    //Método que ejecuta todas las acciones relacionadas con el comando 'salir carcel'.
    // Solo puede ejecutarlo el jugador cuyo índice coincide con 'turno' (o si en el comando se especificó ese jugador).
    @Override
    public void salirCarcel() {
        if (this.jugadores == null || this.jugadores.isEmpty()) {
            consola.imprimir("No hay jugadores en la partida.");
            return;
        }

        Jugador actual = this.jugadores.get(this.turno); 
        if(!actual.isEnCarcel()){
            consola.imprimir("El jugador no está encarcelado.");
            return;
        }
        // Si está en el submenú de bancarrota, NO puede usar este comando
        if (this.enSubmenuBancarrota && this.jugadorDeudor == actual) {
            consola.imprimir("No puedes intentar salir de la cárcel mientras tienes deudas pendientes.");
            consola.imprimir("Debes hipotecar propiedades o declararte en bancarrota.");
            return;
        }

        submenuCarcel(actual);
    }

    @Override
    public void listarVenta() {
        if (this.tablero == null) {
            consola.imprimir("No hay tablero inicializado.");
            return;
        }

        consola.imprimir("=== Propiedades en venta ===");

        for (ArrayList<Casilla> lado : this.tablero.getPosiciones()) { //recorremos el array de casillas, lado por lado
            if (lado == null) continue;
            for (Casilla c : lado) {
                if (c == null) continue;

                if (!(c instanceof Propiedad p)) { // si no es una casilla de la que se puede ser propietario pasamos a la siguiente
                    continue;
                }
                String info = p.casEnVenta(); //dentro ya se hace la comprobacion
                if (!info.isBlank()) {
                    consola.imprimir(info); //solo imprimimos si es comprable
                }
            }
        }

        consola.imprimir("");
        consola.imprimir(this.tablero.toString()); // Mostrar tablero al final (lo pide en el pdf)
    }



    @Override
    public void listarJugadores() {
        for(Jugador jugador : this.jugadores){
            consola.imprimir("{");
            consola.imprimir("Nombre: " + jugador.getNombre());
            consola.imprimir("Avatar: " + jugador.getAvatar().getId());
            consola.imprimir("Fortuna: " + jugador.getFortuna());
            if (jugador.getPropiedades() != null && !jugador.getPropiedades().isEmpty()) {
                consola.imprimir("Propiedades: " + jugador.getPropiedades().toString());
            }
            if (jugador.getEdificios() != null &&  !jugador.getEdificios().isEmpty()) {
                consola.imprimir("Edificios en propiedad: " + jugador.getEdificios().toString());
            }
            consola.imprimir("}");
            consola.imprimir("\n");
        }
    }

    @Override
    public void listarAvatares() {
        for(Avatar avatar : this.avatares){
            consola.imprimir(avatar.getJugador().getNombre());
            consola.imprimir(avatar.getTipo());
            consola.imprimir(avatar.getLugar().getNombre());
            consola.imprimir(avatar.getId());
        }
    }

    @Override
    public void listarEdificios() {
        if (!this.edificios.isEmpty()) {
            for (Edificio edificio : this.edificios) {
                consola.imprimir("{");
                consola.imprimir("id: " + edificio.getId());
                consola.imprimir("propietario: " + edificio.getPropietario().getNombre());
                consola.imprimir("casilla: " + edificio.getLugar().toString());
                if (edificio.getLugar() instanceof  Propiedad p) {
                    consola.imprimir("grupo: " + p.getGrupo().getNombreColorGrupo());
                }
                consola.imprimir("coste: " + edificio.getPrecio());
                consola.imprimir("}");
            }
        }
        else {
            System.err.println("No hay edificios que listar");
        }
    }

    @Override
    public void listarEdificiosGrupo(String grupo) {
        if (this.tablero == null || this.tablero.getPosiciones() == null) {
            consola.imprimir("No hay tablero inicializado.");
            return;
        }
        if (grupo == null || grupo.isBlank()) {
            consola.imprimir("Uso: listar edificios <nombreGrupo>");
            return;
        }

        boolean hayAlgo = false;

        for (ArrayList<Casilla> lado : this.tablero.getPosiciones()) { // itero cada lado del tablero
            for (Casilla c : lado) { // ahora cada casilla de cada lado

                if (!(c instanceof Solar solar)) { // si no es un solar pasamos a la siguiente
                    continue;
                }

                Grupo g = solar.getGrupo();
                if (g == null || g.getNombreColorGrupo() == null) { // por si hay algún error con el grupo
                    continue;
                }
                if (!g.getNombreColorGrupo().equalsIgnoreCase(grupo)) continue; // si el grupo pedido y el grupo de la casilla no coinciden, pasa al siguiente

                // Creamos un array para cada tipo de edificio donde gaurdamos los ids
                ArrayList<String> casas = new ArrayList<>();
                ArrayList<String> hoteles = new ArrayList<>();
                ArrayList<String> piscinas = new ArrayList<>();
                ArrayList<String> pistas = new ArrayList<>();

                if (solar.getEdificios() != null) {
                    for (Edificio e : solar.getEdificios()) { // itero los edificios que hay en la casilla
                        if (e == null || e.getTipo() == null) continue; // si hay algún error paso al siguiente
                        switch (e.getTipo().toLowerCase()) { // y voy guardando el id del edificio según su tipo
                            case "casa":    casas.add(e.getId());    break;
                            case "hotel":   hoteles.add(e.getId());  break;
                            case "piscina": piscinas.add(e.getId()); break;
                            case "pista":   pistas.add(e.getId());   break;
                        }
                    }
                }

                int alquiler = solar.calcularAlquilerParaMostrar();

                consola.imprimir("{");
                consola.imprimir("propiedad: " + c.getNombre());
                consola.imprimir("hoteles: " + hoteles);
                consola.imprimir("casas: " + casas);
                consola.imprimir("piscinas: " + piscinas);
                consola.imprimir("pistas: " + pistas);
                consola.imprimir("alquiler: " + alquiler);
                consola.imprimir("}");

                if (!casas.isEmpty() || !hoteles.isEmpty() || !piscinas.isEmpty() || !pistas.isEmpty()) {
                    hayAlgo = true;
                }
            }
        }

        if (!hayAlgo) {
            consola.imprimir("Todavía no hay edificaciones construidas en el grupo " + grupo + ".");
        }
    }


    @Override
    public void acabarTurno() {
        // aquí se podría añadir la comprobación de que hay jugadores en la partida
        
        Jugador actual = this.jugadores.get(this.turno);
        consola.imprimir("El jugador " + actual.getNombre() + " termina su turno.");

        this.lanzamientos = 0;//reiniciamos dobles

        this.turno = (this.turno + 1) % this.jugadores.size();//actualizamos turno
        Jugador siguiente = this.jugadores.get(this.turno);//damos turno al jugador siguiente
        tirado = false; //hay que actualizar esto también porque si no no deja tirar al siguiente jugador

        consola.imprimir("Le toca al jugador " + siguiente.getNombre() + ".");

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
            consola.imprimir("Todos los jugadores han dado al menos 4 vueltas. Se incrementa el valor de los solares sin dueño.");

            // recorremos todas las casillas del tablero
            for (ArrayList<Casilla> lado: this.tablero.getPosiciones()) {
                for (Casilla c : lado) {
                    if (c instanceof Propiedad p) {
                        if (p.getTipo().equalsIgnoreCase("solar") && (p.getDuenho() == null || p.getDuenho() == this.banca)) {
                            float incremento = 100000; // por poner algo pq ns cuanto es
                            p.sumarValor(incremento);
                        }
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
            consola.imprimir("El tablero no está inicializado.");
            return;
        }
        if(this.tablero.getPosiciones() == null){
            consola.imprimir("No hay casillas inicializadas en el tablero.");
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
                consola.imprimir("casillaMasFrecuentada: " + c.getNombre()  + ",");
            }
        }
    }

    private void masvueltas(){
        if(this.jugadores == null || this.jugadores.isEmpty()){
            consola.imprimir("No hay jugadores registrados en la partida");
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
                    consola.imprimir("jugadorMasVueltas: " + j.getNombre() + ",");
                    //definitivo.add(j);
                }
            }
        }
    }

    //funcion que imprime el jugador con mas valor de toda la partida (dinero, edificios, casillas)
    private void encabeza(){
        if(this.jugadores == null ||  this.jugadores.isEmpty()){
            consola.imprimir("No hay jugadores registrados en la partida");
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
            consola.imprimir("jugadorEnCabeza: " + maspatrimonio.getNombre());
        }
    }


    //funcion que imprime los datos de la casilla mas rentable
    private void casillarentable() {
        if (this.jugadores == null || this.jugadores.isEmpty()) {
            consola.imprimir("No hay jugadores registrados en la partida");
            return;
        } else {
            ArrayList<Casilla> rentables = new ArrayList<>();
            float maximo = Float.NEGATIVE_INFINITY;
            for (Jugador j : this.jugadores) {
                for (Casilla c : j.getPropiedades()) {
                    if (c instanceof  Propiedad p) {
                        p.getRentabilidad();//actualizo las rentabilidades
                        if (p.getRentabilidad() >= maximo) {
                            if(p.getDuenho() != this.banca && p.getDuenho() != null) {
                                maximo = p.getRentabilidad();
                                rentables.add(c);
                            }
                        }
                    }
                }
            }
            if (rentables.isEmpty()) {
                consola.imprimir("Ningún jugador tiene casillas en propiedad\n");
                return;
            } else {
                for (Casilla c : rentables) {
                    if (c instanceof Propiedad p) {
                        if (p.getRentabilidad() == maximo) {
                            consola.imprimir("casillaMasRentable: " + c.getNombre() + ",");
                        }
                    }
                }
            }
        }
    }


    private int grupoduenho(Grupo g){
        int tiene_duenho = 0;
        if(g == null || g.getMiembros().isEmpty()){
            consola.imprimir("Grupo no valido.\n");
            return tiene_duenho;
        }
        else{
            for(Casilla c: g.getMiembros()){
                if (c instanceof Propiedad p) {
                    if(p.getDuenho() != this.banca && p.getDuenho() != null) {
                        tiene_duenho = 1;
                        return tiene_duenho;
                    }
                }
            }
        }
        return tiene_duenho;
    }
    //funcion que hace de setter para la rentabilidad de los grupos, no uso el setter directamente, se usa esta funcion para tenerlo actualizado siempre
    private void rentabilidadgrupos(){
        if(this.tablero == null){
            consola.imprimir("No hay tablero inicializado\n");
            return;
        }
        if(this.tablero.getGrupos() == null || this.tablero.getGrupos().isEmpty()){
            consola.imprimir("No hay grupos\n");
            return;
        }
        else{
            HashMap<String,Grupo> grupos = this.tablero.getGrupos();
            for(Grupo g : grupos.values()){
                float rentacum = 0;
                for(Casilla c : g.getMiembros()){
                    if (c instanceof Propiedad p) {
                        if(p.getDuenho() != this.banca && p.getDuenho() != null) {
                            rentacum += p.getRentabilidad();
                        }
                    }
                }
                g.setRentabilidadgrupo(rentacum);
            }
        }
    }

    private void gruporentable(){
        if(this.tablero == null){
            consola.imprimir("No hay tablero registrado en la partida\n");
            return;
        }
        else{
            float maximo = Float.NEGATIVE_INFINITY;
            HashMap<String,Grupo> grupos = this.tablero.getGrupos();
            if(grupos == null || grupos.isEmpty()){
                consola.imprimir("No hay grupos\n");
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
                            consola.imprimir("grupoMasRentable: " + g.getNombreColorGrupo() + ",");
                        }
                    }
                }
            }
        }
    }

    //imprime las estadisticas de la partida
    @Override
    public void estadisticas(){
        consola.imprimir("{");
        casillarentable();
        gruporentable();
        casillasfrecuentadas();
        masvueltas();
        encabeza();
        consola.imprimir("}");
    }

    @Override
    public void estadisticasjugador(String nombrejugador){

        Jugador j = buscarJugadorPorNombre(nombrejugador);
        if(j == null){
            consola.imprimir("Jugador no encontrado.\n");
            return;
        }
        consola.imprimir("{");
        consola.imprimir("dineroInvertido: " + j.getInversiones() + ","); //aqui hice setter y getter, un atributo, e inclui esta variable en comprar casilla e intentar construir
        consola.imprimir("pagoTasasEImpuestos: " + j.getImpuestos_tasas() + ",");
        consola.imprimir("pagoDeAlquileres: " + j.getAlquilerpagadojugador() + ",");
        consola.imprimir("cobroDeAlquileres: " + j.getAlquilercobradojugador() + ",");
        consola.imprimir("pasarPorCasillaDeSalida: " + (j.getVueltas() * 2000000) + ",");
        consola.imprimir("premiosIversionesObote: " + j.getPremiosinversiones() + ",");
        consola.imprimir("vecesEnLaCarcel: " + j.getVecesCarcel());
        consola.imprimir("}");
    }

    /* Función que una vez pasados los requisitos principales en edificar (de Solar.java), comprueba otros
    * y si los cumple, construye el edificio para cada caso de la función anterior.
    * Se le llama en cada case del switch, por eso hice una función específica, para no repetir
    * este fragmento tantas veces
     */
    public void intentarConstruir(Jugador actual, Solar solar, Edificio e) {
        int precio = e.getPrecio();
        if (actual.getFortuna() < precio ) { // de primeras compruebo si el jugador tiene el dinero suficiente
            System.err.println("La fortuna de " + actual.getNombre() + " no es suficiente para edificar un/a " + e.getTipo() + " en la casilla " + solar.getNombre());
        }
        else { // si lo tiene
            if (solar.getDuenho() == actual && solar.getGrupo().esDuenhoGrupo(actual)) { // miro si es dueño de la casilla en la que está y del grupo completo
                solar.anhadirEdificioACasilla(e); // añadimos el nuevo edificio a la casilla
                actual.anhadirEdificioAJugador(e); // añadimos el edificio también al jugador
                if (this.edificios == null) { // para crear el array edificios del menú la primera vez
                    this.edificios = new ArrayList<>();
                }
                this.edificios.add(e); // añadimos el edificio al array creado en esta clase Menu.java, sirve para luego hacer el listar edificios

                actual.sumarFortuna(-precio); // restamos lo que se acaba de gastar
                actual.setInversiones(actual.getInversiones()+precio); //actualizamos sus inversiones
                consola.imprimir("Se ha edificado un/a " +  e.getTipo() + " en " +  solar.getNombre() + ". La fortuna de " + actual.getNombre() + " se reduce en " + precio + "€");
            }
            else { // si no es dueño de la casilla o del grupo
                if (solar.getDuenho() != actual) {
                    System.err.println(actual.getNombre() + " no puede edificar en " + solar.getNombre() + " porque no le pertenece");
                }
                else if (!solar.getGrupo().esDuenhoGrupo(actual)) {
                    System.err.println(actual.getNombre() + " no puede edificar en " + solar.getNombre() + " porque no es propietario del grupo " + solar.getGrupo().getNombreColorGrupo());
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

    //tengo que hacer un metodo edificar aqui para poder acceder desde menu, ya que menu no puede acceder a solar
    public void edificarJuego(String tipo){
        Jugador actual = jugadores.get(this.turno);
        Casilla lugar = actual.getAvatar().getLugar();

        if(!lugar.getTipo().equalsIgnoreCase("Solar")){
            consola.imprimir("No se puede edificar aqui");
            return;
        }
        Solar solar = (Solar) lugar; //hago un casteo de casilla a solar en el caso de que la casilla sea un solar
        solar.edificar(tipo,actual);
    }
    // Función para vender x cantidad de un edificio concreto en una casilla específica
    public void gestionarVentaEdificios(String tipoEdificio, String nombreCasilla, int numEdificios) {
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
            consola.imprimir("No existe la casilla " +  nombreCasilla);
        }
    }

    private void requisitosVenta(String tipoEdificio, Casilla casilla, int numEdificios) {
        Jugador actual = this.jugadores.get(this.turno); // guardo el jugador que ejecuta el comando

        if (!(casilla instanceof Solar solar)) { // compruebo que el tipo sea solar
            consola.imprimir("No puedes hipotecar una casilla de tipo '" + casilla.getTipo() + "'.");
            return;
        }

        switch (tipoEdificio) {
            case "casas":
                if (solar.getDuenho() != null && solar.getDuenho().equals(actual)) {
                    solar.venderCasas(numEdificios, actual); // y esta función ya comprueba si es el dueño
                } else {
                    System.err.println("No se pueden vender casas en " + solar.getNombre() + ". Esta propiedad no pertenece a " + actual.getNombre() + ".");
                }
                break;
            case "hoteles":
                if (solar.getDuenho() != null && solar.getDuenho().equals(actual)) {
                    solar.venderHoteles(numEdificios, actual); // y esta función ya comprueba si es el dueño
                } else {
                    System.err.println("No se pueden vender hoteles en " + solar.getNombre() + ". Esta propiedad no pertenece a " + actual.getNombre() + ".");
                }
                break;
            case "piscina":
                if (solar.getDuenho() != null && solar.getDuenho().equals(actual)) {
                    solar.venderPiscinas(numEdificios, actual); // y esta función ya comprueba si es el dueño
                } else {
                    System.err.println("No se pueden vender piscinas en " + solar.getNombre() + ". Esta propiedad no pertenece a " + actual.getNombre() + ".");
                }
                break;
            case "pista":
                if (solar.getDuenho() != null && solar.getDuenho().equals(actual)) {
                    solar.venderPistas(numEdificios, actual); // y esta función ya comprueba si es el dueño
                } else {
                    System.err.println("No se pueden vender pistas de deporte en " + solar.getNombre() + ". Esta propiedad no pertenece a " + actual.getNombre() + ".");
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
        Transporte destino = null;
        int posActual = origen.getPosicion();
        int minDistancia = 41; // suponemos que la distancia minima  sera como máximo una vuelta al tablero

        for (ArrayList<Casilla> lado : lados) {
            for (Casilla c : lado) {
                if (c instanceof Transporte t) {
                    int distancia = (c.getPosicion() - posActual + 40) % 40;
                    if (distancia > 0 && distancia < minDistancia) {
                        minDistancia = distancia;
                        destino = t;
                    }
                }
            }
        }

        if (destino == null) {
            consola.imprimir("No se encontró ninguna casilla de transporte en el tablero.");
            return;
        }

        // mover el avatar hasta la casilla de transporte
        jugador.getAvatar().moverAvatar(this.tablero.getPosiciones(), minDistancia);
        consola.imprimir(jugador.getNombre() + " avanza hasta la casilla " + destino.getNombre() + ".");

        // si pasa por la salida, cobra 2.000.000€
        if (destino.getPosicion() < posActual) {
            jugador.sumarFortuna(Valor.SUMA_VUELTA);
            consola.imprimir(jugador.getNombre() + " pasa por la Salida y cobra " + Valor.SUMA_VUELTA + "€.");
        }

        // evaluar la casilla del destino, como es un caso diferente a caer de fomra normal en una casilla de transporte, hay que hacerlo aquí
        if (destino.getDuenho() == banca || destino.getDuenho() == null) {
            consola.imprimir("[" + destino.getNombre() + "] Propiedad libre por " + destino.getValor() + "€. Usa el comando 'comprar' para adquirirla.");
        } else if (destino.getDuenho() == jugador) {
            consola.imprimir("[" + destino.getNombre() + "] Ya posees esta propiedad.");
        } else {
            // si pertenece a otro jugador, paga el doble de alquiler
            float alquiler = destino.getImpuesto() * 2;
            consola.imprimir("La casilla pertenece a " + destino.getDuenho().getNombre() + ". Debes pagar el doble de alquiler (" + (int) alquiler + "€).");

            // si no puede pagar, comprobar si puede hipotecar o sino bancarrota
            if (jugador.getFortuna() < alquiler) {
                consola.imprimir(jugador.getNombre() + " no tiene suficiente dinero para pagar. ");
                // comprobar si tiene algún solar sin hipotecar
                boolean puedeHipotecar = jugador.getPropiedades() != null && !jugador.getPropiedades().isEmpty() && jugador.getHipotecas().size() < jugador.getPropiedades().size();

                if (puedeHipotecar) {
                    activarSubmenuBancarrota(jugador, alquiler, destino.getDuenho());
                } else {
                    declararBancarrota(jugador);
                    this.solvente = false;
                }
            } else {
                jugador.sumarFortuna(-alquiler);
                jugador.sumarGastos(alquiler);
                destino.getDuenho().sumarFortuna(alquiler);
                consola.imprimir(jugador.getNombre() + " paga " + (int) alquiler + "€ a " + destino.getDuenho().getNombre() + ".");
            }
        }
    }

    public boolean isSolvente() {
        return this.solvente;
    }

    public void setSolvente(boolean valor) {
        this.solvente = valor;
    }

    public Carta obtenerCarta(String tipo) {
        if (tipo.equalsIgnoreCase("suerte")) {
            return cartasSuerte.get(indiceSuerte);
        } else {
            return cartasComunidad.get(indiceComunidad);
        }
    }

    public void avanzarIndice(String tipo) {
        if (tipo.equalsIgnoreCase("suerte")) {
            indiceSuerte = (indiceSuerte + 1) % cartasSuerte.size();
        } else {
            indiceComunidad = (indiceComunidad + 1) % cartasComunidad.size();
        }
    }

    public Tablero getTablero() {
        return this.tablero;
    }

    public ArrayList<Jugador> getJugadores() {
        return this.jugadores;
    }

}