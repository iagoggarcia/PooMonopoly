package monopoly.casillas.propiedad;
import monopoly.*;
import monopoly.casillas.*;
import monopoly.casillas.Casilla;
import partida.*;

// Atributos que tienen las propiedades (solares, servicios y transportes):
public abstract class Propiedad extends Casilla {

    protected float valor;
    protected Jugador duenho;
    protected float impuesto;
    protected float hipoteca;   // útil sobre todo para Solar
    protected Grupo grupo;      // útil sobre todo para Solar
    protected float rentabilidad;
    protected float impuestosCobrados;

    // Constructor completo (Solar)
    protected Propiedad(String nombre, int posicion, String tipo,
                        float valor, Jugador duenho,
                        float impuesto, float hipoteca, Grupo grupo) {
        super(nombre, posicion, tipo);
        this.valor = valor;
        this.duenho = duenho;
        this.impuesto = impuesto;
        this.hipoteca = hipoteca;
        this.grupo = grupo;
        this.rentabilidad = 0;
        this.impuestosCobrados = 0;
    }

    // Constructor simple (Servicio/Transporte)
    protected Propiedad(String nombre, int posicion, String tipo,
                        float valor, Jugador duenho, float impuesto) {
        this(nombre, posicion, tipo, valor, duenho, impuesto, 0, null);
    }

    /* ---------- MÉTODOS DEl ENUNCIADO Y OTROS ---------- */

    public boolean perteneceAJugador(Jugador jugador) {
        return (jugador == this.duenho);
    }

    // Es abstract porque se declara en cada subclase que la use
    public abstract boolean alquiler(Jugador actual);

    // También se crea en cada subclase que la use
    public abstract float valor();

    /*Método usado para comprar una casilla determinada. Parámetros:
     * - Jugador que solicita la compra de la casilla.
     * - Banca del monopoly (es el dueño de las casillas no compradas aún).*/
    public void comprar(Jugador solicitante, Jugador banca) {
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
        solicitante.setInversiones(solicitante.getInversiones()+this.valor);
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
    public void sumarValor(float suma) { // TENGO QUE HACER LA SUBCLASE ESPECIAL ANTES DE ARREGLAR LOS RELATED PROBLEMS
        if (suma <= 0) {
            System.out.println("No se puede añadir un valor negativo o nulo a la casilla " + this.nombre + ".");
            return;
        }

        // Suma el valor
        this.valor += suma;

        System.out.println("Se han añadido " + suma + "€ al valor de la casilla '" + this.nombre + "'. Nuevo valor: " + this.valor + "€.");
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

    /* --------------------------------------------------- */

    /* ---------- GETTERS Y SETTERS --------- */

    // LOS RELATED PROBLEMS QUE SALEN SE VAN A IR CUANDO HAGA LAS SUBCLASES TRANSPORTE Y ESPECIAL
    public float getValor() { return valor; }
    public void setValor(float valor) { this.valor = valor; }

    public Jugador getDuenho() { return duenho; }
    public void setDuenho(Jugador duenho) { this.duenho = duenho; }

    public Grupo getGrupo() { return grupo; }
    public void setGrupo(Grupo grupo) { this.grupo = grupo; }

    public float getImpuesto() { return impuesto; }
    public void setImpuesto(float impuesto) { this.impuesto = impuesto; }

    public float getImpuestosCobrados() { return impuestosCobrados; }
    public void setImpuestosCobrados(float v) { this.impuestosCobrados = v; }

    public float getRentabilidad() {
        if (this.duenho != null) {
            setRentabilidad(this.impuestosCobrados - this.valor);
        } else {
            setRentabilidad(0);
        }
        return this.rentabilidad;
    }
    public void setRentabilidad(float rentabilidad) {this.rentabilidad = rentabilidad;}

    public float getHipoteca() {return hipoteca;}

    /* --------------------------------------- */

}

