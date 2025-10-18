package monopoly;

import java.util.ArrayList;
import partida.*;


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

    //Constructores:
    public Casilla() {
    }//Parámetros vacíos

    /*Constructor para casillas tipo Solar, Servicios o Transporte:
     * Parámetros: nombre casilla, tipo (debe ser solar, serv. o transporte), posición en el tablero, valor y dueño.
     */
    public Casilla(String nombre, String tipo, int posicion, float valor, Jugador duenho) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.posicion = posicion;
        this.valor = valor;
        this.duenho = duenho; // aquí puse dueño pero al crear la casilla por primera vez hay que poner banca, que es el dueño por defecto
        this.impuesto = 0;
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

    
    public void sumarValor(float suma) {
        this.valor += suma;
        if (this.valor < 0) this.valor = 0; // por si acaso
    }

    /*Método para evaluar qué hacer en una casilla concreta. Parámetros:
     * - Jugador cuyo avatar está en esa casilla.
     * - La banca (para ciertas comprobaciones).
     * - El valor de la tirada: para determinar impuesto a pagar en casillas de servicios.
     * Valor devuelto: true en caso de ser solvente (es decir, de cumplir las deudas), y false
     * en caso de no cumplirlas.*/
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
      if  (actual == null || banca == null) throw new IllegalArgumentException("Los jugadores (actual o banca) no pueden ser nulos.");
      
      String tipoCasilla = (this.tipo == null) ? " " : this.tipo.toLowerCase();
      String n = (this.nombre == null) ? " " : this.nombre.toLowerCase();

      switch (tipoCasilla) {
        // casillas de tipo solar, servicios o transporte
        case "solar":
        case "servicios":
        case "transporte":
            // si la casilla es de la banca (o sin dueño), está en venta
            if (this.duenho == null || this.duenho == banca) {
                System.out.println("[" + this.nombre + "] Propiedad libre por " + this.valor + "€. Usa el comando 'comprar' para adquirirla.");
                return true;
            } else if (this.duenho != actual) { // si es de otro jugador, en esta parte aun no se cobra el alquiler (no se pide en el guión)
                System.out.println("[" + this.nombre + "] Propiedad de " + this.duenho.getNombre() + ". (Alquiler aún no implementado).");
                return true;
            }
            else { // si es suya nada q hacer
                System.out.println("[" + this.nombre + "] Ya posees esta propiedad.");
                return true;
            }
        // casilla de impuestos
        case "impuestos":
            float imp = this.impuesto;
            // comprobamos solvencia, si no llega devolvemos false
            if (actual.getFortuna() < imp) {
                System.out.println("[" + this.nombre + "] " + actual.getNombre() + " no tiene suficiente dinero para pagar " + imp + "€. Fortuna actual: " + actual.getFortuna());   
                return false;
            }
            // paga: resta a fortuna y suma a gastos de actual, suma fortuna a la banca
            actual.sumarFortuna(-imp);
            actual.sumarGastos(imp);
            banca.sumarFortuna(imp);

            System.out.println(actual.getNombre() + " paga " + imp + "€ de impuestos. ");      
            return true;
        // suerte / caja de comunidad
        case "suerte":
        case "caja de comunidad":
            System.out.println(actual.getNombre() + " ha caído en una casilla de " + this.tipo + ". (Aún no implementado).");
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
            if (n.equals("ircarcel") || n.equals("ir a la carcel") ||  n.equals("ir a la cárcel")) {
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

    /*Método para añadir valor a una casilla. Utilidad:
     * - Sumar valor a la casilla de parking.
     * - Sumar valor a las casillas de solar al no comprarlas tras cuatro vueltas de todos los jugadores.
     * Este método toma como argumento la cantidad a añadir del valor de la casilla.*/
    /*public void sumarValor(float suma) {
    }*/

    /*Método para mostrar información sobre una casilla.
     * Devuelve una cadena con información específica de cada tipo de casilla.*/
    /* public String infoCasilla(String nombre) {

        
        /*for (arrayList<Casilla> lado : posiciones) {   // posiciones: norte, sur, oeste, este
            for (Casilla c : lado) {
                String n = c.getNombre();
                if (n != null && n.equals(nombre)) {
                    System.out.println("Nombre: ");
                }
            }
        }
        return null; // si no existe una casilla con ese nombre devuelve null
    } 

    /* Método para mostrar información de una casilla en venta.
     * Valor devuelto: texto con esa información.
     */
    /*public String casEnVenta() {
    }*/

}