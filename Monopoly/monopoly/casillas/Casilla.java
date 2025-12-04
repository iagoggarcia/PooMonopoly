package monopoly.casillas;

import java.util.ArrayList;
import partida.*;
import monopoly.edificios.*;
import monopoly.*;

import javax.print.DocFlavor;

public abstract class Casilla {

    //Atributos comunes a todas las casillas:
    protected String nombre; //Nombre de la casilla
    protected int posicion; //Posición que ocupa la casilla en el tablero (entero entre 1 y 40).
    protected ArrayList<Avatar> avatares; //Avatares que están situados en la casilla.
    protected String tipo; //Tipo de casilla (Solar, Especial, Transporte, Servicios, Comunidad, Suerte y Impuesto).
    protected int contador; // indica cuantas veces ha caido un jugador en esa casilla

    //CONSTRUCTOR:
    protected Casilla(String nombre, int posicion, String tipo) {
        this.nombre = nombre;
        this.posicion = posicion;
        this.tipo = tipo;
        this.avatares = new ArrayList<>();
        this.contador = 0;
    }

    /* ---------- MÉTODOS MÍNIMOS DEL ENUNCIADO Y OTROS ---------- */

    // Función que mira si un determinado avatar está en la casilla (tiene que estar en el array avatares)
    public boolean estaAvatar(Avatar avatar) {
        if (avatar == null || avatares == null) return false;
        return avatares.contains(avatar);
    }

    // Función que aumenta el contador de visitas. Se devuelve con la funcion de debajo frecuenciaVisita()
    public void registrarVisita(Avatar avatar) {
        anhadirAvatar(avatar);
        contador++;
    }

    // Función que devuelve el número de visitas de una casilla
    public int frecuenciaVisita() {
        return contador;
    }

    /* Se puede tener un toString por clase, así que hice este para que salgan los nombres de las casillas
     * bien impresos en el array propiedades del jugador
     */
    @Override
    public String toString() {
        return this.nombre;
    }

    /*
     * Devuelve un texto con los jugadores (por nombre) que están en esta casilla. Si no hay ninguno, devuelve una cadena vacía.
     */
    protected String infoJugadoresEnEstaCasilla() {
        if (avatares == null || avatares.isEmpty()) {
            return ""; // no hay jugadores
        }

        ArrayList<String> nombres = new ArrayList<>();
        for (Avatar av : avatares) {
            if (av != null && av.getLugar() == this && av.getJugador() != null) {
                nombres.add(av.getJugador().getNombre());
            }
        }

        if (nombres.isEmpty()) {
            return "";
        }

        return "\nJugadores: " + nombres;
    }

    /* ----------------------------------------------------------- */


    /* ---------- GESTIÓN DE AVATARES ---------- */

    //Método utilizado para añadir un avatar al array de avatares en casilla.
    public void anhadirAvatar(Avatar av) {
        if (av == null) return;
        // si la lista interna de avatares aún no existe se crea
        if (this.avatares == null) this.avatares = new ArrayList<>();

        if (!this.avatares.contains(av)) { // comprobamos que el avatar no esté ya en la lista
            this.avatares.add(av); // si no está, se añade
        }
        // aquí no se modifica el lugar, eso se hace en Avatar o Jugador
    }

    //Método utilizado para eliminar un avatar del array de avatares en casilla.
    public void eliminarAvatar(Avatar av) {
        if (av == null || this.avatares == null) return;
        this.avatares.remove(av);
    }

    /* ----------------------------------------- */

    /* ---------- GETTERS Y SETTERS ---------- */

    public String getNombre() {
        return nombre;
    }

    public int getPosicion() {
        return posicion;
    }

    public ArrayList<Avatar> getAvatares() {
        return avatares;
    }

    public String getTipo() {
        return tipo;
    }

    public int getContador() {
        return contador;
    }

    public void setContador(int contador) {
        this.contador = contador;
    }

    /* --------------------------------------- */

    /*Constructor para casillas Servicios o Transporte:
     * Parámetros: nombre casilla, tipo (debe ser solar, serv. o transporte), posición en el tablero, valor y dueño.
     */
    /*public Casilla(String nombre, String tipo, int posicion, float valor, Jugador duenho, float impuesto) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.posicion = posicion;
        this.valor = valor;
        this.duenho = duenho; // aquí puse dueño pero al crear la casilla por primera vez hay que poner banca, que es el dueño por defecto
        this.impuesto = impuesto;
        this.hipoteca = 0;
        this.rentabilidad = 0;
        this.impuestos_cobrados = 0;
        this.grupo = new Grupo();
        this.avatares = new ArrayList<>();
    }*/

    /*Constructor utilizado para inicializar las casillas de tipo IMPUESTOS.
     * Parámetros: nombre, posición en el tablero, impuesto establecido y dueño.
     */
    /*public Casilla(String nombre, int posicion, float impuesto, Jugador duenho) {
        this.nombre = nombre;
        this.tipo = "impuesto";
        this.posicion = posicion;
        this.valor = 0;
        this.duenho = duenho;
        this.impuesto = impuesto;
        this.hipoteca = 0;
        this.impuestos_cobrados = 0;
        this.rentabilidad = 0;
        this.grupo = new Grupo();
        this.avatares = new ArrayList<>();
    }*/

    /*Constructor utilizado para crear las otras casillas (Suerte, Caja de comunidad y Especiales):
     * Parámetros: nombre, tipo de la casilla (será uno de los que queda), posición en el tablero y dueño.
     */
    /*public Casilla(String nombre, String tipo, int posicion, Jugador duenho) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.posicion = posicion;
        this.valor = 0;
        this.duenho = duenho;
        this.impuesto = 0;
        this.hipoteca = 0;
        this.rentabilidad = 0;
        this.impuestos_cobrados = 0;
        this.grupo = new Grupo();
        this.avatares = new ArrayList<>();
    }*/

    /*Método para evaluar qué hacer en una casilla concreta. Parámetros:
     * - Jugador cuyo avatar está en esa casilla.
     * - La banca (para ciertas comprobaciones).
     * - El valor de la tirada: para determinar impuesto a pagar en casillas de servicios.
     * Valor devuelto: true en caso de ser solvente (es decir, de cumplir las deudas), y false
     * en caso de no cumplirlas.*/
    public abstract boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada);

    /*public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        if (actual == null || banca == null)
            throw new IllegalArgumentException("Los jugadores (actual o banca) no pueden ser nulos.");

        String tipoCasilla = (this.tipo == null) ? " " : this.tipo.toLowerCase();
        String n = (this.nombre == null) ? " " : this.nombre.toLowerCase();

        switch (tipoCasilla) {
            case "servicios":
                // si la casilla es de la banca (o sin dueño), está en venta
                if (this.duenho == null || this.duenho == banca) {
                    System.out.println("[" + this.nombre + "] Propiedad libre por " + this.valor + "€. Usa el comando 'comprar' para adquirirla.");
                    return true;
                }
                if (this.duenho == actual) {
                    System.out.println("[" + this.nombre + "] Ya posees esta propiedad.");
                    return true;
                }

                int serviciosPropietario = 0;
                for (Casilla c : this.duenho.getPropiedades()) {
                    if (c.getTipo().equalsIgnoreCase("servicios")) {
                        serviciosPropietario++;
                    }
                }
                // factor base (impuesto) viene del constructor
                float factor = this.impuesto;
                float alquiler;

                if (serviciosPropietario == 2) {
                    alquiler = 10 * tirada * factor;
                } else {
                    alquiler = 4 * tirada * factor;
                }
                // comprobamos solvencia, si no se puede pagar, declaramos bancarrota
                if (actual.getFortuna() < alquiler) {
                    System.out.println(actual.getNombre() + " no tiene suficiente dinero para pagar el alquiler de " + this.nombre + ".");
                    // comprobar si tiene algún solar sin hipotecar
                    boolean puedeHipotecar = actual.getPropiedades() != null && !actual.getPropiedades().isEmpty() && actual.getHipotecas().size() < actual.getPropiedades().size();

                    if (puedeHipotecar) {
                        Juego m = Juego.getInstancia();
                        m.activarSubmenuBancarrota(actual, alquiler, this.duenho);
                        return true;
                    } else {
                        Juego m = Juego.getInstancia();
                        m.declararBancarrota(actual);
                        return false;
                    }
                }
                // realizamos el pago
                actual.sumarFortuna(-alquiler);
                actual.sumarGastos(alquiler);
                actual.setAlquilerpagadojugador(actual.getAlquilerpagadojugador() + alquilerTotal); //aumentamos sus pagos de alquileres
                this.duenho.sumarFortuna(alquiler);
                this.duenho.setAlquilercobradojugador(this.duenho.getAlquilercobradojugador() + alquilerTotal); //aumentamos cobro de alquileres
                actual.getAvatar().getLugar().setImpuestos_cobrados(getImpuestoscobrados()+alquiler); //esto es para la rentabilidad de la casilla, influye al dueño, no al que paga alquiler
                System.out.println(actual.getNombre() + " paga " + (int) alquiler + "€ de alquiler a " + this.duenho.getNombre() + " por usar el servicio (" + serviciosPropietario + "servicio/s poseídos, tirada = " + tirada + ").");
                return true;
            // casilla de impuestos
            case "impuestos":
                float imp = this.impuesto;

                // comprobamos solvencia, si no se puede pagar, declaramos bancarrota
                if (actual.getFortuna() < imp) {
                    System.out.println(actual.getNombre() + " no tiene suficiente dinero para pagar el alquiler de " + this.nombre + ".");
                    // comprobar si tiene algún solar sin hipotecar
                    boolean puedeHipotecar = actual.getPropiedades() != null && !actual.getPropiedades().isEmpty() && actual.getHipotecas().size() < actual.getPropiedades().size();

                    if (puedeHipotecar) {
                        Juego m = Juego.getInstancia();
                        m.activarSubmenuBancarrota(actual, imp, this.duenho);
                        return true;
                    } else {
                        Juego m = Juego.getInstancia();
                        m.declararBancarrota(actual);
                        return false;
                    }
                }
                // el jugador paga a la banca
                actual.sumarFortuna(-imp);
                actual.sumarGastos(imp);
                banca.sumarFortuna(imp);
                actual.setImpuestos_tasas(actual.getImpuestos_tasas() + imp); //actualizamos los gastos del jugador

                System.out.println(actual.getNombre() + " paga " + imp + "€ en impuestos.");
                return true;
            // suerte / caja de comunidad
            case "suerte":
            case "caja de comunidad":
                System.out.println(actual.getNombre() + " ha caído en una casilla de " + this.tipo + ".");

                // ejecutamos la carta correspondiente
                Juego.getInstancia().ejecutarCartas(this.tipo, actual, banca, this);
                if (!Juego.getInstancia().isSolvente()) {
                    System.out.println(actual.getNombre() + " ha quedado insolvente tras ejecutar la carta y se declara en bancarrota.");
                    return false;
                }
                return true;
            // especiales
            case "especial":
                if (n.equals("parking")) {
                    // cobre del bote (valor) y reseteo
                    if (this.valor > 0) {
                        actual.sumarFortuna(this.valor);
                        System.out.println(actual.getNombre() + " cobra el bote del Parking: " + this.valor + "€.");
                        this.valor = 0;
                    } else {
                        System.out.println(actual.getNombre() + " descansa en el Parking. No hay bote acumulado.");
                    }
                    return true;
                }
                if (n.equals("salida")) {
                    System.out.println(actual.getNombre() + " está en la casilla de Salida. ¡Buen viaje!");
                    return true;
                }
                if (n.equals("ircarcel") || n.equals("ir a la carcel") || n.equals("ir a la cárcel")) {
                    // el menu es el que tiene que mover a la carcel al jugador
                    // si caes aquí tienes que moverte a la carcel como preso inmediatamente
                    System.out.println(actual.getNombre() + " ha caído en 'Ir a la Cárcel'. Será trasladado al finalizar la tirada.");
                    return true;
                }
                if (n.equals("cárcel") || n.equals("carcel")) {
                    // de visita o preso lo gestiona el menu
                    // caes por movimiento "normal", o ya estabas en la cárcel preso
                    System.out.println(actual.getNombre() + " está visitando la Cárcel. No está arrestado.");
                    System.out.println(actual.getNombre() + " está en la Cárcel (visita o preso).");
                    return true;
                }
                System.out.println("[" + this.nombre + "] Casilla especial (sin acción en parte 1)");
                return true;
            // desconocido
            default:
                System.out.println("Casilla no reconocida o sin comportamiento definido.");
                return true;
        }
    }*/


    /*Método para mostrar información sobre una casilla.
     * Devuelve una cadena con información específica de cada tipo de casilla.
     * Será implementado por cada subclase (solar, transporte, etc.) */
    public abstract String infoCasilla();

    /*public String infoCasilla() {

        StringBuilder informacion = new StringBuilder(); // aquí guardamos la info de la casilla
        String duenho;
        if (this.getDuenho().getNombre() == null) duenho = "banca";
        else duenho = this.getDuenho().getNombre();

        informacion.append("{");
        switch (tipo.toLowerCase()) {
            case "impuesto":
                informacion.append("\nTipo: ").append(tipo).append("\nA pagar: ").append(impuesto);
                informacion.append(infoJugadoresEnEstaCasilla());
                break;
            case "servicios":
                informacion.append("\nValor: ").append(valor);
                informacion.append(infoJugadoresEnEstaCasilla());
                break;
            case "especial":
                if (this.getNombre() != null && this.getNombre().equalsIgnoreCase("parking")) {
                    informacion.append("\nBote: ").append(valor);
                } else if (this.getNombre() != null && this.getNombre().equalsIgnoreCase("cárcel")) {
                    informacion.append("\nSalir: 500000");
                }

                // El siguiente bloque es para cualquier especial menos para el irCarcel:
                if (!(this.getNombre().equalsIgnoreCase("ircarcel"))) {
                    informacion.append(infoJugadoresEnEstaCasilla());
                }

                break;
        }
        informacion.append("\n}");
        return informacion.toString(); // si no existe una casilla con ese nombre devuelve null
    }*/
}