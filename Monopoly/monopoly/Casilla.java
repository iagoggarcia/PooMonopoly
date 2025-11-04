package monopoly;

import java.util.ArrayList;
import partida.*;
import monopoly.Menu;

public class Casilla {

    //Atributos:
    private String nombre; //Nombre de la casilla
    private String tipo; //Tipo de casilla (Solar, Especial, Transporte, Servicios, Comunidad, Suerte y Impuesto).
    private float valor; //Valor de esa casilla (en la mayoría será valor de compra, en la casilla parking se usará como el bote).
    private int posicion; //Posición que ocupa la casilla en el tablero (entero entre 1 y 40).
    private Jugador duenho; //Dueño de la casilla (por defecto sería la banca).
    private Grupo grupo; //Grupo al que pertenece la casilla (si es solar).
    private float impuesto; //Cantidad a pagar por caer en la casilla: el alquiler en solares/servicios/transportes o impuestos.
    private float hipoteca; //Valor otorgado por hipotecar una casilla
    private ArrayList<Avatar> avatares; //Avatares que están situados en la casilla.
    private boolean hipotecada; // indica si la casilla está hipotecada
    private int contador; // indica cuantas veces ha caido un jugador en esa casilla
    private int valorCasayHotel; // Indica el coste de edificar una casa o un hotel en una casilla específica (es la misma variable porque cuestan lo mismo)
    private int valorPiscina; // Indica el coste de edificar una piscina en una casilla específica
    private int valorPistaDeporte; // Indica el coste de edificar una pista de deporte en una casilla específica
    private ArrayList<Edificio> edificios; // ArrayList donde guardaré los edificios de cada casilla
    private int numCasas; // Para controlar que se puedan construir 4 casas en una casilla COMO MÁXIMO
    private int numHoteles; // Para controlar que haya un ÚNICO hotel si ya se han construido 4 casas, y las casas se sustituyen por el hotel
    private int numPiscinas; // Para construir una ÚNICA piscina si ya hay un hotel
    private int numPistas; // Para construir una ÚNICA pista de deporte si ya hay un hotel y una piscina

    //Constructores:
    public Casilla() {
    }//Parámetros vacíos


    /*Constructor para casillas de tipo Solar:
    * Parámetros: nombre casilla, tipo (solar), posición en el tablero, valor, dueño, hipoteca, y el coste de edificar cada tipo en esta casilla.
    */
    public Casilla (String nombre, String tipo, int posicion, float valor, Jugador duenho, float impuesto, float hipoteca, int valorCasayHotel, int valorPiscina, int valorPistaDeporte) {
        this.nombre = nombre;
        this.tipo = "solar";
        this.posicion = posicion;
        this.valor = valor;
        this.duenho = duenho; // aquí puse dueño pero al crear la casilla por primera vez hay que poner banca, que es el dueño por defecto
        this.impuesto = impuesto;
        this.grupo = new Grupo();
        this.avatares = new ArrayList<>();
        this.hipoteca = hipoteca;
        this.valorCasayHotel = valorCasayHotel;
        this.valorPiscina = valorPiscina;
        this.valorPistaDeporte = valorPistaDeporte;
        this.edificios = new ArrayList<>();
        this.numCasas = 0;
        this.numHoteles = 0;
        this.numPiscinas = 0;
        this.numPistas = 0;
    }

    /*Constructor para casillas Servicios o Transporte:
     * Parámetros: nombre casilla, tipo (debe ser solar, serv. o transporte), posición en el tablero, valor y dueño.
     */
    public Casilla(String nombre, String tipo, int posicion, float valor, Jugador duenho, float impuesto) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.posicion = posicion;
        this.valor = valor;
        this.duenho = duenho; // aquí puse dueño pero al crear la casilla por primera vez hay que poner banca, que es el dueño por defecto
        this.impuesto = impuesto;
        this.hipoteca = 0;
        this.grupo = new Grupo();
        this.avatares = new ArrayList<>();
    }

    /*Constructor utilizado para inicializar las casillas de tipo IMPUESTOS.
     * Parámetros: nombre, posición en el tablero, impuesto establecido y dueño.
     */
    public Casilla(String nombre, int posicion, float impuesto, Jugador duenho) {
        this.nombre = nombre;
        this.tipo = "impuesto";
        this.posicion = posicion;
        this.valor = 0;
        this.duenho = duenho;
        this.impuesto = impuesto;
        this.hipoteca = 0;
        this.grupo = new Grupo();
        this.avatares = new ArrayList<>();
    }

    /*Constructor utilizado para crear las otras casillas (Suerte, Caja de comunidad y Especiales):
     * Parámetros: nombre, tipo de la casilla (será uno de los que queda), posición en el tablero y dueño.
     */
    public Casilla(String nombre, String tipo, int posicion, Jugador duenho) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.posicion = posicion;
        this.valor = 0;
        this.duenho = duenho;
        this.impuesto = 0;
        this.hipoteca = 0;
        this.grupo = new Grupo();
        this.avatares = new ArrayList<>();
    }

    /*
    Como los atributos son private ya hice los getters y setters
    para acceder a ellos o modificarlos en los otros archivos .java, si no no se puede.

    Los puse aquí porque se supone que se ponen después de los constructores y antes de las demás funciones.
    */
    public int getContador(){
        return contador;
    }
    
    public void setContador(int contador){
        this.contador = contador;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public Jugador getDuenho() {
        return duenho;
    }

    public void setDuenho(Jugador duenho) {
        this.duenho = duenho;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public float getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(float impuesto) {
        this.impuesto = impuesto;
    }

    public float getHipoteca() {
        return hipoteca;
    }

    public void setHipoteca(float hipoteca) {
        this.hipoteca = hipoteca;
    }

    public ArrayList<Avatar> getAvatares() {
        return avatares;
    }

    public void setAvatares(ArrayList<Avatar> avatares) {
        this.avatares = avatares;
    }

    public boolean isHipotecada() {
        return hipotecada;
    }

    public void setHipotecada(boolean hipotecada) {
        this.hipotecada = hipotecada;
    }

    public int getValorCasayHotel() {
        return valorCasayHotel;
    }

    public void setValorCasayHotel(int valorCasa) {
        this.valorCasayHotel = valorCasayHotel;
    }

    public int getValorPiscina() {
        return valorPiscina;
    }

    public void setValorPiscina(int valorPiscina) {
        this.valorPiscina = valorPiscina;
    }

    public int getValorPistaDeporte() {
        return valorPistaDeporte;
    }

    public void setValorPistaDeporte(int valorPistaDeporte) {
        this.valorPistaDeporte = valorPistaDeporte;
    }

    public ArrayList<Edificio> getEdificios() {
        return edificios;
    }

    public void setEdificios(ArrayList<Edificio> edificios) {
        this.edificios = edificios;
    }

    public int getNumCasas() {
        return numCasas;
    }

    public void setNumCasas(int numCasas) {
        this.numCasas = numCasas;
    }

    public int getNumHoteles() {
        return numHoteles;
    }

    public void setNumHoteles(int numHoteles) {
        this.numHoteles = numHoteles;
    }

    public int getNumPiscinas() {
        return numPiscinas;
    }

    public void setNumPiscinas(int numPiscinas) {
        this.numPiscinas = numPiscinas;
    }

    public int getNumPistas() {
        return numPistas;
    }

    public void setNumPistas(int numPistas) {
        this.numPistas = numPistas;
    }

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

    /*Método para evaluar qué hacer en una casilla concreta. Parámetros:
     * - Jugador cuyo avatar está en esa casilla.
     * - La banca (para ciertas comprobaciones).
     * - El valor de la tirada: para determinar impuesto a pagar en casillas de servicios.
     * Valor devuelto: true en caso de ser solvente (es decir, de cumplir las deudas), y false
     * en caso de no cumplirlas.*/
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        if (actual == null || banca == null)
            throw new IllegalArgumentException("Los jugadores (actual o banca) no pueden ser nulos.");

        String tipoCasilla = (this.tipo == null) ? " " : this.tipo.toLowerCase();
        String n = (this.nombre == null) ? " " : this.nombre.toLowerCase();

        switch (tipoCasilla) {
            // casillas de tipo solar, servicios o transporte
            case "solar":
                // si la casilla es de la banca (o sin dueño), está en venta
                if (this.duenho == null || this.duenho == banca) {
                    System.out.println("[" + this.nombre + "] Propiedad libre por " + this.valor + "€. Usa el comando 'comprar' para adquirirla.");
                    return true;
                }
                if (this.duenho == actual) {
                    System.out.println("[" + this.nombre + "] Ya posees esta propiedad.");
                    return true;
                }
                //  si la casilla está hipotecada, no se cobra el alquiler
                if (this.isHipotecada()) {
                    System.out.println("[" + this.nombre + "] está hipotecada. No se cobra alquiler.");
                    return true;
                }
                // si pertenece a otro jugador, calculamos el alquiler
                float alquilerGrupo = this.impuesto; // alquiler base, definido en el tablero

                // comprobamos si el dueño tiene todo el grupo
                if (this.grupo != null && this.grupo.esDuenhoGrupo(this.duenho)) {
                    // si tiene todo el grupo y no hay edificaciones (edificaciones no implementamos)
                    alquilerGrupo *= 2;
                    System.out.println(this.duenho.getNombre() + " posee todo el grupo de " + this.grupo.getColorGrupo() + ". Se cobra el doble del alquiler.");
                }

                // Si no se puede pagar, se le da la opción a hipotecar 
                if (actual.getFortuna() < alquilerGrupo) {
                    System.out.println(actual.getNombre() + " no tiene suficiente dinero para pagar el alquiler de " + this.nombre + ".");
                    // comprobar si tiene algún solar sin hipotecar
                    boolean puedeHipotecar = actual.getPropiedades() != null && !actual.getPropiedades().isEmpty() && actual.getHipotecas().size() < actual.getPropiedades().size();
        
                    if (puedeHipotecar) {
                        System.out.println("Debe hipotecar alguna propiedad para poder pagar o se declarará en bancarrota.");
                        return true;
                    } else {
                        System.out.println(actual.getNombre() + " no puede pagar y se declara en bancarrota.");
                        for (Casilla c : actual.getPropiedades()) {
                            c.setDuenho(this.duenho); // las pasa al propietario de la deuda
                            this.duenho.anhadirPropiedad(c);
                        }
                        actual.getPropiedades().clear();
                        return false;
                    }
                }
                // realizamos el pago
                actual.sumarFortuna(-alquilerGrupo);
                actual.sumarGastos(alquilerGrupo);
                this.duenho.sumarFortuna(alquilerGrupo);
                System.out.println(actual.getNombre() + " paga " + (int) alquilerGrupo + "€ de alquiler a " + this.duenho.getNombre() + " por caer en " + this.nombre + ".");
                return true;
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
                    System.out.println(actual.getNombre() + " no puede pagar y se declara en bancarrota.");
                    for (Casilla c : actual.getPropiedades()) {
                        c.setDuenho(this.duenho); // las pasa al propietario de la deuda
                        this.duenho.anhadirPropiedad(c);
                    }
                    actual.getPropiedades().clear();
                    return false;
                }
                // realizamos el pago
                actual.sumarFortuna(-alquiler);
                actual.sumarGastos(alquiler);
                this.duenho.sumarFortuna(alquiler);
                System.out.println(actual.getNombre() + " paga " + (int) alquiler + "€ de alquiler a " + this.duenho.getNombre() + " por usar el servicio (" + serviciosPropietario + "servicio/s poseídos, tirada = " + tirada + ").");
                return true;
            case "transporte":
                // si la casilla es de la banca (o sin dueño), está en venta
                if (this.duenho == null || this.duenho == banca) {
                    System.out.println("[" + this.nombre + "] Propiedad libre por " + this.valor + "€. Usa el comando 'comprar' para adquirirla.");
                    return true;
                }
                if (this.duenho == actual) {
                    System.out.println("[" + this.nombre + "] Ya posees esta propiedad.");
                    return true;
                }
                // si pertenece a otro jugador, calcular el alquiler total
                float alquilerTotal = 0;
                int contarTransporte = 0;
                for (Casilla c : this.duenho.getPropiedades()) {
                    if (c.getTipo().equalsIgnoreCase("transporte")) {
                        alquilerTotal += c.getImpuesto(); // suma los alquileres de todos sus transportes
                        contarTransporte++;
                    }
                }
                // comprobamos solvencia, si no se puede pagar, declaramos bancarrota
                if (actual.getFortuna() < alquilerTotal) {
                    System.out.println(actual.getNombre() + " no puede pagar y se declara en bancarrota.");
                    for (Casilla c : actual.getPropiedades()) {
                        c.setDuenho(this.duenho); // las pasa al propietario de la deuda
                        this.duenho.anhadirPropiedad(c);
                    }
                    actual.getPropiedades().clear();
                    return false;
                }
                // realizamos el pago
                actual.sumarFortuna(-alquilerTotal);
                actual.sumarGastos(alquilerTotal);
                this.duenho.sumarFortuna(alquilerTotal);
                System.out.println(actual.getNombre() + " paga " + (int) alquilerTotal + "€ de alquiler a " + this.duenho.getNombre() + " por usar el transporte (" + contarTransporte + "transporte/s poseídos).");
                return true;
            // casilla de impuestos
            case "impuestos":
                float imp = this.impuesto;

                // comprobamos solvencia, si no se puede pagar, declaramos bancarrota
                if (actual.getFortuna() < imp) {
                    System.out.println(actual.getNombre() + " no puede pagar y se declara en bancarrota.");
                    for (Casilla c : actual.getPropiedades()) {
                        c.setDuenho(banca); // las pasa al propietario de la deuda
                        banca.anhadirPropiedad(c);
                    }
                    actual.getPropiedades().clear();
                    return false;
                }
                // el jugador paga a la banca
                actual.sumarFortuna(-imp);
                actual.sumarGastos(imp);
                banca.sumarFortuna(imp);

                System.out.println(actual.getNombre() + " paga " + imp + "€ en impuestos.");
                return true;
            // suerte / caja de comunidad
            case "suerte":
            case "caja de comunidad":
                System.out.println(actual.getNombre() + " ha caído en una casilla de " + this.tipo + ".");
                
                // ejecutamos la carta correspondiente
                Menu.getInstancia().ejecutarCartas(this.tipo, actual, banca, this);
                if (!Menu.getInstancia().isSolvente()) {
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
    }

    /*Método usado para comprar una casilla determinada. Parámetros:
     * - Jugador que solicita la compra de la casilla.
     * - Banca del monopoly (es el dueño de las casillas no compradas aún).*/
    public void comprarCasilla(Jugador solicitante, Jugador banca) {
        //comprobaciones
        if (this.duenho == solicitante) {
            System.out.println("Ya posees la casilla " + this.nombre + ".");
            return;
        }
        if (this.duenho != banca && this.duenho != null) {
            System.out.println("No puedes comprar " + this.nombre + ": pertenece a " + this.duenho.getNombre() + ".");
            return;
        }
        if (solicitante.getFortuna() < this.valor) {
            System.out.println("No tienes suficiente dinero para comprar " + this.nombre + ".");
            return;
        }

        solicitante.sumarFortuna(-this.valor);
        solicitante.sumarGastos(this.valor);
        solicitante.anhadirPropiedad(this);
        this.setDuenho(solicitante);
        banca.sumarFortuna(this.valor);
        System.out.println(solicitante.getNombre() + " ha comprado " + this.nombre + " por " + this.valor + "€. Fortuna restante: " + solicitante.getFortuna() + "€.");
    }

    /**
     * Método para añadir valor a una casilla.
     * Utilidad:
     * - Sumar valor a la casilla de Parking.
     * - Sumar valor a las casillas de tipo "Solar" al no comprarlas tras cuatro vueltas.
     * Este método toma como argumento la cantidad a añadir al valor de la casilla.
     */
    public void sumarValor(float suma) {
        if (suma <= 0) {
            System.out.println("No se puede añadir un valor negativo o nulo a la casilla " + this.nombre + ".");
            return;
        }

        // Si la casilla no admite valor (por ejemplo, cárcel o suerte), avisamos
        if (this.tipo == null ||
                this.tipo.equalsIgnoreCase("carcel") ||
                this.tipo.equalsIgnoreCase("cárcel") ||
                this.tipo.equalsIgnoreCase("ir a la cárcel") ||
                this.tipo.equalsIgnoreCase("ircarcel") ||
                this.tipo.equalsIgnoreCase("suerte") ||
                this.tipo.equalsIgnoreCase("caja de comunidad")) {
            System.out.println("No se puede modificar el valor de una casilla de tipo " + this.tipo + ".");
            return;
        }

        // Suma el valor
        this.valor += suma;

        System.out.println("Se han añadido " + suma + "€ al valor de la casilla '" + this.nombre + "'. Nuevo valor: " + this.valor + "€.");
    }


    /*Método para mostrar información sobre una casilla.
     * Devuelve una cadena con información específica de cada tipo de casilla.*/
    public String infoCasilla() {

        StringBuilder informacion = new StringBuilder(); // aquí guardamos la info de la casilla

        informacion.append("{");
        switch (tipo.toLowerCase()) {
            case "solar": // falta por printear el valor de los alquileres
                informacion.append("\nTipo: ").append(tipo).append("\nGrupo: ").append(grupo.getNombreColorGrupo()).append("\nPropietario: ").append(duenho.getNombre())
                        .append("\nValor: ").append(valor).append("\nValor hotel: ").append(valorCasayHotel).append("\nValor casa: ").append(valorCasayHotel)
                        .append("\nValor piscina: ").append(valorPiscina).append("\nValor pista de deporte: ").append(valorPistaDeporte)
                        .append(infoJugadoresEnEstaCasilla());
                break;
            case "impuesto":
                informacion.append("\nTipo: ").append(tipo).append("\nA pagar: ").append(impuesto);
                informacion.append(infoJugadoresEnEstaCasilla());
                break;
            case "transporte":
                informacion.append("\nValor: ").append(valor);
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
        return informacion.toString(); // si no existe una casilla con ese nombre devuelve null*/
    }

    /* Método para mostrar información de una casilla en venta.
     * Valor devuelto: texto con esa información.
     */
    public String casEnVenta() {
        // Comprobamos si la casilla es comprable
        if (this.tipo == null)
            return ""; //en el caso de que no se cumpla alguna condicion necesaria, devolvemos una cadena vacia
        String tipoLower = this.tipo.toLowerCase();
        boolean esComprable = tipoLower.equals("solar") || tipoLower.equals("transporte") || tipoLower.equals("servicios"); //solo es comprable si es de alguno de estos tipos

        // Comprobamos si está en venta (sin dueño o con dueño banca)
        boolean enVenta = esComprable && (this.duenho == null || this.duenho.getNombre() == null || this.duenho.getNombre().equalsIgnoreCase("banca"));


        if (!enVenta) return "";

        // Si está en venta, devolvemos la información formateada
        String nombreColor = (this.grupo != null) ? this.grupo.getNombreColorGrupo() : "";

        String info = "{ tipo: " + this.tipo
                + ", \nvalor: " + (int) this.valor
                + ", \ngrupo: " + nombreColor
                + " }";

        return info; //devolvemos la cadena
    }


    /**
     * Devuelve un texto con los jugadores (por nombre) que están en esta casilla.
     * Si no hay ninguno, devuelve una cadena vacía.
     */
    private String infoJugadoresEnEstaCasilla() {
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

    /* Esta función añade un edificio nuevo al array edificios
    * de la casilla en la que se encuentra el jugador
     */
    public void anhadirEdificioACasilla(Edificio edificio) {
        if (edificio == null) {
            System.out.println("No se puede añadir un edificio nulo.");
            return;
        }
        edificios.add(edificio);
        switch (edificio.getTipo()) {
            case "casa":
                numCasas++;
                break;
            case "hotel":
                numHoteles++;
                break;
            case "piscina":
                numPiscinas++;
                break;
            case "pista":
                numPistas++;
                break;
        }
    }

    /* Función que va iterando sobre el array de edificios que son del jugador
     * y va comprobando si el tipo de cada edificio es casa, si lo es,
     * se elimina del array
     */
    public void quitarCuatroCasas() {
        int retiradas = 0; // para controlar que solo se eliminen 4
        for (int i = 0; i < edificios.size() && retiradas < 4; i++) { // mientras no se hayan eliminado 4 casas y no se sobrepase el tamaño del array
            Edificio e = edificios.get(i); // cogemos el edificio número i del array
            if (e != null && "casa".equalsIgnoreCase(e.getTipo())) { // comprobamos que no es null y que su tipo es "casa
                edificios.remove(i);   // quito la casa del array
                numCasas--;            // actualizo contador
                i--;                   // retrocedo índice porque la lista ha disminuido, ahora el último elemento es el anterior
                retiradas++;
            }

            if (e.getPropietario() != null) {
                e.getPropietario().eliminarEdificioDeJugador(e); // también quitamos las casas de los edificios del jugador
            }

            Menu m = Menu.getInstancia(); // con getInstancia() guardo una referencia al menú real y así puedo modificar la lista de edificios del menú
            if (m != null) {
                m.eliminarEdificioGlobal(e);
            }
        }
    }

    /* Función que uso en gestionarVentaEdificios para vender única y específicamente casas
     * Los mensajes son personalizados para las casas, los otros tipos de edificio
     * también tienen su correspondiente función
     */
    public void venderCasas(int nCasas, Jugador j) {
        int vendidas = 0;
        if (nCasas <= getNumCasas()) {
            // Recorremos la lista y paramos al vender nCasas
            for (int i = 0; i < edificios.size() && vendidas < nCasas; ) {
                Edificio e = edificios.get(i);
                if (e != null && "casa".equalsIgnoreCase(e.getTipo())) {
                    edificios.remove(i);
                    if (numCasas > 0) numCasas--;

                    if (e.getPropietario() != null) {
                        e.getPropietario().eliminarEdificioDeJugador(e); // también quitamos las casas de los edificios del jugador
                    }

                    Menu m = Menu.getInstancia(); // con getInstancia() guardo una referencia al menú real y así puedo modificar la lista de edificios del menú
                    if (m != null) {
                        m.eliminarEdificioGlobal(e);
                    }

                    int ganancia = this.getValorCasayHotel();
                    e.getPropietario().sumarFortuna(ganancia);

                    vendidas++;
                } else {
                    i++; // solo avanzo cuando no elimino nada
                }
            }

            if (vendidas >= 1 && getNumCasas() != 1) {
                System.out.println(this.getDuenho().getNombre() + " ha vendido " + nCasas + " casas en " + getNombre() + ", recibiendo " + nCasas * getValorCasayHotel() + "€. En la propiedad quedan " + this.getNumCasas() + " casas.");
            } else if (vendidas >= 1 && getNumCasas() == 1) {
                System.out.println(this.getDuenho().getNombre() + " ha vendido " + nCasas + " casa en " + getNombre() + ", recibiendo " + nCasas * getValorCasayHotel() + "€. En la propiedad queda " + this.getNumCasas() + " casa.");
            }
        } else {
            if (getNumCasas() == 0) {
                System.err.println("No se pueden vender casas en " + getNombre() + ", no hay casas construidas");
            }
            if (getNumCasas() == 1) {
                System.err.println("Solamente se puede vender 1 casa, recibiendo " + getValorCasayHotel() + "€");
            } else if (getNumCasas() > 1) {
                System.err.println("Solamente se pueden vender " + getNumCasas() + " casas, recibiendo " + getNumCasas() * getValorCasayHotel() + "€");
            }
        }
    }

    /* Función que uso en gestionarVentaEdificios para vender única y específicamente hoteles
     * Los mensajes son personalizados para los hoteles, los otros tipos de edificio
     * también tienen su correspondiente función
     */
    public void venderHoteles (int nHoteles, Jugador j) {
        int vendidas = 0;
        if (nHoteles <= getNumHoteles()) {
            // Recorremos la lista y paramos al vender nHoteles
            for (int i = 0; i < edificios.size() && vendidas < nHoteles; ) {
                Edificio e = edificios.get(i);
                if (e != null && "hotel".equalsIgnoreCase(e.getTipo())) {
                    edificios.remove(i);
                    if (numHoteles > 0) numHoteles--;

                    if (e.getPropietario() != null) {
                        e.getPropietario().eliminarEdificioDeJugador(e); // también quitamos el hotel de los edificios del jugador
                    }

                    Menu m = Menu.getInstancia(); // con getInstancia() guardo una referencia al menú real y así puedo modificar la lista de edificios del menú
                    if (m != null) {
                        m.eliminarEdificioGlobal(e);
                    }

                    int ganancia = this.getValorCasayHotel();
                    e.getPropietario().sumarFortuna(ganancia);

                    vendidas++;
                } else {
                    i++; // solo avanzo cuando no elimino nada
                }
            }

            if (vendidas == 1 && getNumHoteles() == 0) {
                System.out.println(this.getDuenho().getNombre() + " ha vendido " + nHoteles + " hotel en " + getNombre() + ", recibiendo " + nHoteles * getValorCasayHotel() + "€. En la propiedad quedan " + this.getNumHoteles() + " hoteles.");
            }
        } else {
            if (getNumHoteles() == 0) {
                System.err.println("No se pueden vender hoteles en " + getNombre() + ", no hay ninguno construido");
            }
            else if (getNumHoteles() == 1) {
                System.err.println("Solamente se puede vender 1 hotel, recibiendo " + getValorCasayHotel() + "€");
            }
        }
    }

    /* Función que uso en gestionarVentaEdificios para vender única y específicamente hoteles
     * Los mensajes son personalizados para los hoteles, los otros tipos de edificio
     * también tienen su correspondiente función
     */
    public void venderPiscinas (int nPiscinas, Jugador j) {
        int vendidas = 0;
        if (nPiscinas <= getNumPiscinas()) {
            // Recorremos la lista y paramos al vender nHoteles
            for (int i = 0; i < edificios.size() && vendidas < nPiscinas; ) {
                Edificio e = edificios.get(i);
                if (e != null && "piscina".equalsIgnoreCase(e.getTipo())) {
                    edificios.remove(i);
                    if (numPiscinas > 0) numPiscinas--;

                    if (e.getPropietario() != null) {
                        e.getPropietario().eliminarEdificioDeJugador(e); // también quitamos el hotel de los edificios del jugador
                    }

                    Menu m = Menu.getInstancia(); // con getInstancia() guardo una referencia al menú real y así puedo modificar la lista de edificios del menú
                    if (m != null) {
                        m.eliminarEdificioGlobal(e);
                    }

                    int ganancia = this.getValorCasayHotel();
                    e.getPropietario().sumarFortuna(ganancia);

                    vendidas++;
                } else {
                    i++; // solo avanzo cuando no elimino nada
                }
            }

            if (vendidas == 1 && getNumPiscinas() == 0) {
                System.out.println(this.getDuenho().getNombre() + " ha vendido " + nPiscinas + " piscina en " + getNombre() + ", recibiendo " + nPiscinas * getValorCasayHotel() + "€. En la propiedad quedan " + this.getNumHoteles() + " piscinas.");
            }
        } else {
            if (getNumPiscinas() == 0) {
                System.err.println("No se pueden vender piscinas en " + getNombre() + ", no hay ninguno construido");
            }
            else if (getNumPiscinas() == 1) {
                System.err.println("Solamente se puede vender 1 piscina, recibiendo " + getValorPiscina() + "€");
            }
        }
    }

    /* Función que uso en gestionarVentaEdificios para vender única y específicamente hoteles
     * Los mensajes son personalizados para los hoteles, los otros tipos de edificio
     * también tienen su correspondiente función
     */
    public void venderPistas (int nPistas, Jugador j) {
        int vendidas = 0;
        if (nPistas <= getNumHoteles()) {
            // Recorremos la lista y paramos al vender nHoteles
            for (int i = 0; i < edificios.size() && vendidas < nPistas; ) {
                Edificio e = edificios.get(i);
                if (e != null && "piscina".equalsIgnoreCase(e.getTipo())) {
                    edificios.remove(i);
                    if (numPistas > 0) numPistas--;

                    if (e.getPropietario() != null) {
                        e.getPropietario().eliminarEdificioDeJugador(e); // también quitamos el hotel de los edificios del jugador
                    }

                    Menu m = Menu.getInstancia(); // con getInstancia() guardo una referencia al menú real y así puedo modificar la lista de edificios del menú
                    if (m != null) {
                        m.eliminarEdificioGlobal(e);
                    }

                    int ganancia = this.getValorCasayHotel();
                    e.getPropietario().sumarFortuna(ganancia);

                    vendidas++;
                } else {
                    i++; // solo avanzo cuando no elimino nada
                }
            }

            if (vendidas == 1 && getNumPistas() == 0) {
                System.out.println(this.getDuenho().getNombre() + " ha vendido " + nPistas + " pista de deporte en " + getNombre() + ", recibiendo " + nPistas * getValorPistaDeporte() + "€. En la propiedad quedan " + this.getNumHoteles() + " pistas de deporte.");
            }
        } else {
            if (getNumPistas() == 0) {
                System.err.println("No se pueden vender pistas de deporte en " + getNombre() + ", no hay ninguno construido");
            }
            else if (getNumPistas() == 1) {
                System.err.println("Solamente se puede vender 1 hotel, recibiendo " + getValorPistaDeporte() + "€");
            }
        }
    }

    /** Se puede tener un toString por clase, así que hice este
    * para que salgan los nombres de las casillas bien printeados
    * en el array propiedades del jugador
    * */
    @Override
    public String toString() {
        return this.nombre;
    }
}