package partida;

import java.util.ArrayList;
import monopoly.*;


public class Jugador {

    //Atributos:
    private String nombre; //Nombre del jugador
    private Avatar avatar; //Avatar que tiene en la partida.
    private float fortuna; //Dinero que posee.
    private float gastos; //Gastos realizados a lo largo del juego.
    private boolean enCarcel; //Será true si el jugador está en la carcel
    private int tiradasCarcel; //Cuando está en la carcel, contará las tiradas sin éxito que ha hecho allí para intentar salir (se usa para limitar el numero de intentos).
    private int vueltas; //Cuenta las vueltas dadas al tablero.
    private ArrayList<Casilla> propiedades; // Propiedades que posee el jugador (casillas).
    private ArrayList<Edificio> edificios; // ArrayList donde se guardan los edificios que le pertenecen al jugador
    private float patrimonio; //valor total de las posesiones del jugador
    private float inversiones; //dinero que invierte en casillas/edificaciones
    private float impuestos_tasas; //dinero que paga el jugador al caer en las casillas con penalizacion economica
    private float alquilercobradojugador; //variables para la funcion de estadisticas
    private float alquilerpagadojugador;
    private float premiosinversiones; //todo lo recibido por cartas, casillas de suerte, bote del parking...
    private int vecescarcel; //contador para la carcel

    //Constructor vacío. Se usará para crear la banca.
    // estaba mal, como pone en el esqueleto es VACÍO, no hay que completar nada
    // en el menu luego se haria antes de crear el tablero algo como Jugador banca = new Jugador(); Tablero tablero = new Tablero(banca);
    public Jugador() {
    }

    /*Constructor principal. Requiere parámetros:
    * Nombre del jugador, tipo del avatar que tendrá, casilla en la que empezará y ArrayList de
    * avatares creados (usado para dos propósitos: evitar que dos jugadores tengan el mismo nombre y
    * que dos avatares tengan mismo ID). Desde este constructor también se crea el avatar.
     */
    public Jugador(String nombre, String tipoAvatar, Casilla inicio, ArrayList<Avatar> avCreados) {
        this.nombre = nombre;
        this.avatar = new Avatar(tipoAvatar, this, inicio, avCreados); // para el avatar hay que crearlo y por defecto le puse que lo cree en la casilla inicial
        this.fortuna = Valor.FORTUNA_INICIAL; // puse la fortuna inicial
        this.gastos = 0; // antes = gastos, incorrecto porque está sin inicializar, no se pasa como parámetro, daría error
        this.enCarcel = false; // antes = enCarcel, lo mismo que arriba
        this.tiradasCarcel = 0; // antes = tiradasCarcel
        this.vueltas = 0; 
        this.propiedades = new ArrayList<>(); // solo hay que inicializar propiedades como  una lista vaćía
        this.edificios = new ArrayList<>(); // aquí guardamos los edificios que creó el jugador
        this.patrimonio = this.fortuna;
        this.inversiones = 0;
        this.impuestos_tasas = 0;
        this.alquilercobradojugador = 0;
        this.alquilerpagadojugador = 0;
        this.vecescarcel = 0;
    }

    public Jugador(float valor){
        this.fortuna = valor;
        this.patrimonio = this.fortuna;
        this.inversiones = 0;
        this.impuestos_tasas = 0;
        this.alquilercobradojugador = 0;
        this.alquilerpagadojugador = 0;
        this.vecescarcel = 0;
    }

    // ----- Getters mínimos que necesita el menú ----- //los añado porque me hacen falta en menu
    public String getNombre() {
        return nombre;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public float getFortuna() {
        return fortuna;
    }

    public boolean isEnCarcel() {
        return enCarcel;
    }

    public void setEnCarcel(boolean enCarcel) {
        this.enCarcel = enCarcel;
    }

    public ArrayList<Casilla> getPropiedades() {
        return propiedades;
    }

    public int getTiradasCarcel() {
        return this.tiradasCarcel;
    }

    public void setTiradasCarcel(int tiradasCarcel) {
        this.tiradasCarcel = tiradasCarcel;
    }

    public int getVueltas() {
        return vueltas;
    }

    public void setVueltas(int vueltas) {
        this.vueltas = vueltas;
    }

    public ArrayList<Edificio> getEdificios() {
        return edificios;
    }

    public void setEdificios(ArrayList<Edificio> edificios) {
        this.edificios = edificios;
    }

    public ArrayList<Casilla> getHipotecas() {
        ArrayList<Casilla> solaresHipotecados = new ArrayList<>();
        if (this.propiedades != null) {
            for (Casilla c : this.propiedades) {
                if (c.getTipo().equalsIgnoreCase("solar") && c.isHipotecada()) {
                    solaresHipotecados.add(c);
                }
            }
        }
        return solaresHipotecados;
    }
    public float getPatrimonio() {
        float valorPropiedades = 0;
        if (this.propiedades != null) {
            for (Casilla c : this.propiedades) {
                if (c != null) valorPropiedades += c.getValor();
            }
        }

        float valorEdificios = 0;
        if (this.edificios != null) {
            for (Edificio e : this.edificios) {
                if (e != null) valorEdificios += e.getPrecio();
            }
        }

        setPatrimonio(valorPropiedades + valorEdificios + this.fortuna);
        return this.fortuna + valorPropiedades + valorEdificios;
    }

    public void setPatrimonio(float patrimonio) {
        this.patrimonio = patrimonio;
    }

    public float getInversiones() {
        return this.inversiones;
    }

    public void setInversiones(float inversiones) {
        this.inversiones = inversiones;
    }

    public float getGastos() {
        return this.gastos;
    } //realmente el setter de esta variable es la funcion sumargastos

    public float getImpuestos_tasas() {
        return this.impuestos_tasas;
    }

    public void setImpuestos_tasas(float impuestos_tasas) {
        this.impuestos_tasas = impuestos_tasas;
    }
    public float getAlquilercobradojugador() {
        return alquilercobradojugador;
    }

    public void setAlquilercobradojugador(float alquilercobradojugador) {
        this.alquilercobradojugador = alquilercobradojugador;
    }

    public float getAlquilerpagadojugador() {
        return alquilerpagadojugador;
    }

    public void setAlquilerpagadojugador(float alquilerpagadojugador) {
        this.alquilerpagadojugador = alquilerpagadojugador;
    }

    public float getPremiosinversiones() {
        return premiosinversiones;
    }

    public void setPremiosinversiones(float premiosinversiones) {
        this.premiosinversiones = premiosinversiones;
    }

    public int getVecesCarcel(){
        return this.vecescarcel;
    }



    //Otros métodos:
    //Método para añadir una propiedad al jugador (cuando la compra). Como parámetro, la casilla a añadir.
    public void anhadirPropiedad(Casilla casilla) {
        if (casilla == null) {
            System.out.println("No se puede añadir una propiedad nula.");
            return;
        }
        if (!propiedades.contains(casilla)) { // comprobamos que la casilla no está ya en la lista de propiedades del jugador
            propiedades.add(casilla); // si no está, la añadimos
            System.out.println(nombre + " ha adquirido la propiedad " + casilla.getNombre() + ".");
            System.out.println("Ahora posee " + propiedades.size() + " propiedades.");
            // aqui creo que se añadiria casilla.setPropietario(this), pero en esta entrega creo que aun no se pide
        } else {
            System.out.println(nombre + " ya posee la propiedad " + casilla.getNombre() + ".");            
        }
    }

    //Método para eliminar una propiedad del arraylist de propiedades de jugador (venta, hipoteca o eliminación).
    public void eliminarPropiedad(Casilla casilla) {
        if (casilla == null) {
            System.out.println("No se puede eliminar una propiedad nula.");
            return;
        }
        if (propiedades.contains(casilla)) { // comprobamos que la  casilla está en la lista de propiedades del jugador
            propiedades.remove(casilla); // si está, la eliminamos
            System.out.println(nombre + " ha perdido la propiedad " + casilla.getNombre() + ".");
            System.out.println("Ahora posee " + propiedades.size() + " propiedades.");
            // aqui creo que se añadiria casilla.setPropietario(banca), pero en esta entrega creo que aun no se pide
        } else {
            System.out.println(nombre + " no posee la propiedad " + casilla.getNombre() + ".");
        }
    }

    //Método para añadir fortuna a un jugador
    //Como parámetro se pide el valor a añadir. Si hay que restar fortuna, se pasaría un valor negativo.
    public void sumarFortuna(float valor) { 
        this.fortuna += valor;
        if (this.fortuna < 0) {
            System.out.println(nombre + "ha caído en bancarrota. Fortuna actual: " + this.fortuna);
        }
    }

    //Método para sumar gastos a un jugador.
    //Parámetro: valor a añadir a los gastos del jugador (será el precio de un solar, impuestos pagados...).
    public void sumarGastos(float valor) { // sumarGastos no modifica nunca fortuna, solo es para contabilizar los gastos
        // hay que asegurarse de que lo que se acumula es no negativo (siempre se meten valor positivos a gastos)
        if (valor < 0) valor = -valor;
        this.gastos += valor;
    }

    /*Método para establecer al jugador en la cárcel. 
    * Se requiere disponer de las casillas del tablero para ello (por eso se pasan como parámetro).*/
    
    private static final int NUM_CASILLAS = 40;
    private static final int IDX_CARCEL   = 10; // posición fija de "Cárcel"

    // ddevuelve  la casilla con indice lineal en el orden el que fue construido
    private Casilla casillaPorIndice(ArrayList<ArrayList<Casilla>> pos, int indice) {
        if (pos == null || indice < 0 || indice >= NUM_CASILLAS) return null;

        int k = 0; // k ira contando para cada casilla visitada
        for (ArrayList<Casilla> lado : pos) {
            for (Casilla c : lado) {
                if (k == indice) return c; // encontramos la casilla en la posicion buscada
                k++; // avanzamosa a la siguiente
            }
        }
        return null; 
    }
    
    // por ahora solo mueve fisicamente el avatar a la carcel y marca el estado en enCarcel y modifica tiradasCarcel
    // las otras "reglas" (cobrar o pagar dinero, comprobar si se puede salir, etc) se gestionan desde otro sitio, por ejemplo evaluarcasilla
    public void encarcelar(ArrayList<ArrayList<Casilla>> pos) {
        if (pos == null || pos.isEmpty()) {
            throw new IllegalArgumentException("El tablero (pos) no puede ser nulo ni vacío");
        }
        if (this.avatar == null) {
            throw new IllegalStateException("El jugador no tiene avatar asociado");
        }

        // localizamos la casilla cárcel por índice fijo
        Casilla carcel = casillaPorIndice(pos, IDX_CARCEL);
        if (carcel == null) {
            throw new IllegalStateException("No se encontró la casilla de Cárcel (índice 10)");
        }
        // quitamos al avatar de su casilla actual y lo colocamos en carcel
        Casilla actual = this.avatar.getLugar();
        if (actual != null) actual.eliminarAvatar(this.avatar);  // cuando esté implementado en casilla
        carcel.anhadirAvatar(this.avatar);                       
        this.avatar.setLugar(carcel);                            // actualiza el puntero del avatar
        this.vecescarcel += 1;

        this.enCarcel = true;
        this.tiradasCarcel = 0;
        System.out.println(nombre + " ha sido encarcelado y trasladado a la casilla " + carcel.getNombre() + ".");
    }

    /* Función que simplemente coge un edificio
     * y lo añade a los edificios que le pertenecen
     * al jugador
     */
    public void anhadirEdificioAJugador(Edificio edificio) {
        if (edificio == null) {
            System.out.println("No se puede añadir un edificio nulo.");
            return;
        }
        edificios.add(edificio);
    }

    /* Esta función recibe un edificio, comprueba si está en el array
    * edificios del jugador y si está, lo elimina
    * Se usa al construir un hotel porque hay que quitar 4 casas
    * y más adelante también lo uso en venderEdificio
     */
    public void eliminarEdificioDeJugador(Edificio edificio) {
        if (edificio == null) {
            System.out.println("No se puede eliminar un edificio nulo.");
            return;
        }
        if (edificios.contains(edificio)) { // miro si está en los edificios que le pertenecen al jugador
            edificios.remove(edificio); // y si está lo elimino
        } else { // si no está, mensaje de error
            System.err.println("El edificio " + edificio.getId() + " no pertenece a " + this.nombre);
        }
    }
}
