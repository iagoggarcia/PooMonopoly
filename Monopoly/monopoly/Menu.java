package monopoly;

import java.util.ArrayList;
import java.util.Scanner;

import partida.*;


import static monopoly.Valor.FORTUNA_BANCA;


public class Menu {

    //Atributos
    private ArrayList<Jugador> jugadores; //Jugadores de la partida.
    private ArrayList<Avatar> avatares; //Avatares en la partida.
    private int turno = 0; //Índice correspondiente a la posición en el arrayList del jugador (y el avatar) que tienen el turno
    private int lanzamientos; //Variable para contar el número de lanzamientos de un jugador en un turno.
    private Tablero tablero; //Tablero en el que se juega.
    private Dado dado1; //Dos dados para lanzar y avanzar casillas.
    private Dado dado2;
    private Jugador banca; //El jugador banca.
    private boolean tirado; //Booleano para comprobar si el jugador que tiene el turno ha tirado o no.
    private boolean solvente; //Booleano para comprobar si el jugador que tiene el turno es solvente, es decir, si ha pagado sus deudas.
    private boolean enCurso;   // indica si la partida está activa


    public Menu(){ //constructor publico para iniciar partida
        iniciarPartida();
    }

    // Método para inciar una partida: crea los jugadores y avatares.
    public void iniciarPartida() {
        Scanner sc = new Scanner(System.in);

        // 1. Crear banca y tablero
        // Jugador banca = new Jugador(Valor.FORTUNA_BANCA);
        Jugador banca = new Jugador();
        banca.sumarFortuna(Valor.FORTUNA_BANCA); 
        Tablero tablero = new Tablero(banca);

        // 2. Obtener la casilla "Salida" donde se colocan los avatares //iago lo tiene en sur, que es get(1) y despues dentro de sur, get(0)
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

            // 5. Crear el jugador y añadirlo a la lista
            Jugador jugador = new Jugador(nombre, tipoAvatar, salida, avatares);
            jugadores.add(jugador);

            // Mostrar confirmación como en el ejemplo del enunciado
            System.out.println("{");
            System.out.println("  nombre: " + nombre + ",");
            System.out.println("  avatar: " + jugador.getAvatar().getId());
            System.out.println("}");
        }

        // 6. Guardar la información en los atributos de Menu (si los tienes)
        this.tablero = tablero;
        this.banca = banca;
        this.jugadores = jugadores;
        this.avatares = avatares;

        this.turno = 0;
        this.solvente = true;
        this.enCurso = true;


        System.out.println("\nPartida inicializada correctamente con " + numJugadores + " jugadores.\n");
    }



    /*Método que interpreta el comando introducido y toma la accion correspondiente.
    * Parámetro: cadena de caracteres (el comando).
    */
    private void analizarComando(String comando) {
        if(comando.equals("Describir jugador")){
            String partes[] = comando.split(" ");
            descJugador(partes);
        }
        else if(comando.startsWith("Describir avatar")){
            String Idavatar = comando.substring("Describir avatar ".length()).trim();
            descAvatar(Idavatar);
        }
        else if(comando.startsWith("Describir casilla")){
            String nombreCasilla = comando.substring("describir casilla".length()).trim();
            descCasilla(nombreCasilla);
        }
        else if(comando.startsWith("Lanzar dados")){
            String cadenaComprobar = comando.substring("lanzar dados".length()).trim();
            if(cadenaComprobar.isEmpty()){
                lanzarDados();
            }
            else{
                String[] valores = cadenaComprobar.split("\\+"); //separo la cadena en 2 usando el + como caracter separador
                lanzarDadosValor(valores); //le paso la cadena como un array y luego accedo a cada posicion a mi gusto
            }
        }
        else if(comando.startsWith("Comprar una propiedad")){
            String nombrePropiedad = comando.substring("comprar".length()).trim();
            comprar(nombrePropiedad);
        }
        else if(comando.equals("Salir de la cárcel")){
            salirCarcel();
        }
        else if(comando.equals("Listar propiedades en venta")){
            listarVenta();
        }
        else if(comando.equals("Listar jugadores")){
            listarJugadores();
        }
        else if(comando.equals("Listar avatares")){
            listarAvatares();
        }
        else if(comando.equals("Acabar turno")){
            acabarTurno();
        }

        //funciones que no hay pero hacen falta
        else if(comando.equals("Crear jugador")){
            String datosJugador = comando.substring("Crear jugador".length()).trim();
            String[] partes = datosJugador.split(" ");
            crearJugadorArchivo(partes);
        }
    }

    //función para crear jugador desde archivo
    private void crearJugadorArchivo(String[] partes){
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

    /*Método que realiza las acciones asociadas al comando 'describir jugador'.
    * Parámetro: comando introducido
     */
    private void descJugador(String[] partes) {
    }

    /*Método que realiza las acciones asociadas al comando 'describir avatar'.
    * Parámetro: id del avatar a describir.
    */
    private void descAvatar(String ID) {
    }

    /* Método que realiza las acciones asociadas al comando 'describir nombre_casilla'.
    * Parámetros: nombre de la casilla a describir.
    */
    private void descCasilla(String nombre) {
    }

    //Método que ejecuta todas las acciones relacionadas con el comando 'lanzar dados'.
private int dobles=0;

private void lanzarDados() {

    if (!this.enCurso){
        iniciarPartida();
    }

    if (this.dado1 == null){
        this.dado1 = new Dado();
    }
    if (this.dado2 == null){
        this.dado2 = new Dado();
    }

    int valor1 = this.dado1.hacerTirada();
    int valor2 = this.dado2.hacerTirada();
    int suma = valor1 + valor2;

    Jugador actual = this.jugadores.get(this.turno);
    Avatar av = actual.getAvatar();
    Casilla origen = av.getLugar();

    //movemos todas las posiciones sin comprobar antes
    av.moverAvatar(this.tablero.getPosiciones(), suma);

    // nueva casilla
    Casilla destino = av.getLugar();

    evaluarCasilla(destino);

    //aqui si compruebo si pase por la salida y sumo la fortuna al jugador
    /*if (origen != null && destino.getPosicion() < origen.getPosicion()) {
        actual.sumarFortuna(Valor.SUMA_VUELTA);
        // aquí podrías registrar la vuelta si llevas contador
    }

    System.out.println("Has caído en: " + destino.getNombre()+ " (pos " + destino.getPosicion() + ")");

    Casilla parking = this.tablero.getPosiciones.get(0).get(21);
    if(destino == parking){
        this.jugador.sumarFortuna(parking.getValor());
        }
    }*/

    //al final, despues de completar la funcion
    if(suma == 12){

        dobles++;
        if(dobles<3){
            lanzarDados();
        }

        else{
            Casilla carcel = this.tablero.getPosiciones().get(1).get(11);
            av.setLugar = carcel;
            dobles = 0;
        }
    }
    else{
        dobles = 0;
    }
}



        //continuar con todas las comprobaciones que hay que hacer al caer en nueva casilla
        //actualizar casilla
        //comprobar número de lanzamientos, caso del doble 6 y el castigo
        //comprobar si cae en casilla especial
        //comprobar si cae en casilla que ya es de alguien y si tiene que pagar, bancarrota...

    private void lanzarDadosValor(String[] valores) {
        //va a ser un copia y pega de lo de arriba, asi que mejor hago una sola función en la que le paso igualmente la cadena y dentro
        //compruebo si está vacía para decidir si hago hacerTirada o asignar un valor forzado
        //lo hago cuando acabe la anterior
    }

    /*Método que ejecuta todas las acciones realizadas con el comando 'comprar nombre_casilla'.
    * Parámetro: cadena de caracteres con el nombre de la casilla.
     */
    private void comprar(String nombre) {
    }

    //Método que ejecuta todas las acciones relacionadas con el comando 'salir carcel'. 
    private void salirCarcel() {
    }

    // Método que realiza las acciones asociadas al comando 'listar enventa'.
    private void listarVenta() {
    }

    // Método que realiza las acciones asociadas al comando 'listar jugadores'.
    private void listarJugadores() {
    }

    // Método que realiza las acciones asociadas al comando 'listar avatares'.
    private void listarAvatares() {
    }

    // Método que realiza las acciones asociadas al comando 'acabar turno'.
    private void acabarTurno() {
    }

}
