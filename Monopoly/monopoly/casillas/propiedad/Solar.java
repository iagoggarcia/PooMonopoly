package monopoly.casillas.propiedad;

import java.util.ArrayList;
import monopoly.Juego;
import monopoly.casillas.Casilla;
import monopoly.edificios.*;
import monopoly.excepciones.CasillaInexistenteException;
import monopoly.excepciones.FondosInsuficientesException;
import monopoly.excepciones.JugadorBancarrotaException;
import monopoly.excepciones.JugadorNoExisteException;
import monopoly.excepciones.MaximoEdificiosException;
import monopoly.excepciones.NoEdificableException;
import monopoly.excepciones.NoEresPropietarioException;
import monopoly.excepciones.PropiedadHipotecadaException;
import partida.Jugador;

public class Solar extends Propiedad {
    // ATRIBUTOS:
    int valorCasayHotel;
    int valorPiscina;
    int valorPistaDeporte;
    ArrayList<Edificio> edificios;
    int numCasas;
    int numHoteles;
    int numPiscinas;
    int numPistas;
    int alquilerCasilla;
    int alquilerCasa;
    int alquilerHotel;
    int alquilerPiscinaYPista;
    int alquilerTotal;
    boolean hipotecada;

    public Solar(String nombre, int posicion, float valor, Jugador duenho, float impuesto, float hipoteca, int valorCasayHotel, int  valorPiscina, int valorPistaDeporte, int alquilerCasilla, int alquilerCasa, int alquilerHotel, int alquilerPiscinaYPista) {
        super(nombre, posicion, "solar", valor, duenho, impuesto, hipoteca, null);
        this.valorCasayHotel = valorCasayHotel;
        this.valorPiscina = valorPiscina;
        this.valorPistaDeporte = valorPistaDeporte;
        this.alquilerCasilla = alquilerCasilla;
        this.alquilerCasa = alquilerCasa;
        this.alquilerHotel = alquilerHotel;
        this.alquilerPiscinaYPista =  alquilerPiscinaYPista;
        this.alquilerTotal = 0; // se calcula en otras funciones
        this.numCasas = 0;
        this.numHoteles = 0;
        this.numPiscinas = 0;
        this.numPistas = 0;
        this.edificios = new ArrayList<>();
        this.hipotecada = false;
    }

    /* --------- MÉTODOS DEL ENUNCIADO Y OTROS ---------- */

    @Override
    public boolean alquiler(Jugador actual) throws JugadorBancarrotaException {
        int cantidad = calcularAlquiler(actual);

        if (cantidad <= 0) return true; // si no hay nada que pagar devuelvo true

        if (actual.getFortuna() < cantidad) {
            return false; // devolvemos false si no tiene dinero suficiente
        }

        // Si el jugador sí que puede pagar:
        actual.sumarFortuna(-cantidad);
        actual.sumarGastos(cantidad);
        duenho.sumarFortuna(cantidad);
        return true;
    }

    @Override
    public float valor() {
        return this.valor;
    }

    public boolean estaHipotecada () {
        return hipotecada;
    }

    @Override
    public String infoCasilla() {
        StringBuilder informacion = new StringBuilder(); // aquí guardamos la info de la casilla
        String duenho;
        if (this.getDuenho().getNombre() == null) duenho = "banca";
        else duenho = this.getDuenho().getNombre();

        informacion.append("{").append("\nTipo: ").append(tipo).append("\nGrupo: ").append(grupo.getNombreColorGrupo()).append("\nPropietario: ").append(duenho).append("\nValor: ")
                .append(valor).append("\nAlquiler: ").append(calcularAlquilerParaMostrar()).append("\nValor hotel: ").append(valorCasayHotel).append("\nValor casa: ").append(valorCasayHotel)
                .append("\nValor piscina: ").append(valorPiscina).append("\nValor pista de deporte: ").append(valorPistaDeporte).append("\nAlquiler casa: ").append(alquilerCasa)
                .append("\nAlquiler hotel: ").append(alquilerHotel).append("\nAlquiler piscina: ").append(alquilerPiscinaYPista).append("\nAlquiler pista de deporte: ").append(alquilerPiscinaYPista)
                .append(infoJugadoresEnEstaCasilla());

        informacion.append("\n}");
        return informacion.toString();
    }

    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) throws JugadorNoExisteException, JugadorBancarrotaException, CasillaInexistenteException {
        if (actual == null || banca == null)
            throw new JugadorNoExisteException("Los jugadores (actual o banca) no pueden ser nulos.");

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
        if (estaHipotecada()) {
            System.out.println("[" + this.nombre + "] está hipotecada. No se cobra alquiler.");
            return true;
        }
        // si pertenece a otro jugador, calculamos el alquiler
        //float alquilerGrupo = this.impuesto; // alquiler base, definido en el tablero
        int alquilerGrupo = this.calcularAlquiler(actual); // usamos la función que calcula el alquiler para los solares

        // comprobamos si el dueño tiene todo el grupo y que no hay edificios para doblar el alquiler
        boolean sinEdificios = (getNumCasas() + getNumHoteles() + getNumPiscinas() + getNumPistas()) == 0;

        if (sinEdificios && this.grupo != null && this.grupo.esDuenhoGrupo(this.duenho)) {
            // si tiene todo el grupo
            alquilerGrupo *= 2;
            System.out.println(this.duenho.getNombre() + " posee todo el grupo de " + this.grupo.getColorGrupo() + ". Se cobra el doble del alquiler.");
        }

        // Si no se puede pagar, se le da la opción a hipotecar
        if (actual.getFortuna() < alquilerGrupo) {
            System.out.println(actual.getNombre() + " no tiene suficiente dinero para pagar el alquiler de " + this.nombre + ".");
            // comprobar si tiene algún solar sin hipotecar
            boolean puedeHipotecar = actual.getPropiedades() != null && !actual.getPropiedades().isEmpty() && actual.getHipotecas().size() < actual.getPropiedades().size();

            if (puedeHipotecar) {
                Juego m = Juego.getInstancia();
                m.activarSubmenuBancarrota(actual, alquilerGrupo, this.duenho);
                return true;
            } else {
                Juego m = Juego.getInstancia();
                m.declararBancarrota(actual);
                return false;
            }
        }
        // realizamos el pago
        actual.sumarFortuna(-alquilerGrupo);
        actual.sumarGastos(alquilerGrupo);
        actual.setAlquilerpagadojugador(actual.getAlquilerpagadojugador() + alquilerGrupo); //aumentamos sus pagos de alquileres
        this.duenho.sumarFortuna(alquilerGrupo);
        this.duenho.setAlquilercobradojugador(this.duenho.getAlquilercobradojugador() + alquilerGrupo); //aumentamos cobro de alquileres

        Casilla lugar = actual.getAvatar().getLugar();
        if (lugar instanceof Propiedad p) { // instanceof nos permite tratar 'lugar' como Propiedad y usar setImpuestosCobrados
            p.setImpuestosCobrados(p.getImpuestosCobrados() + alquilerGrupo);
        }

        System.out.println(actual.getNombre() + " paga " + (int) alquilerGrupo + "€ de alquiler a " + this.duenho.getNombre() + " por caer en " + this.nombre + ".");
        return true;
    }

    /* -------------------------------------------------- */

    /* ---------- GESTIÓN DE EDIFICIOS ---------- */

    public void edificar(String tipoEdificio, Jugador actual) throws JugadorNoExisteException, MaximoEdificiosException, FondosInsuficientesException, JugadorBancarrotaException, NoEdificableException, NoEresPropietarioException {

        if (actual == null) {
            throw new JugadorNoExisteException("Jugador no válido.");
        }

        // Necesitamos el Juego para reutilizar intentarConstruir(...)
        Juego juego = Juego.getInstancia();
        
        if (juego == null) {
            System.err.println("No hay partida inicializada.");
            return;
        }

        switch (tipoEdificio.toLowerCase()) {
            case "casa":
                try {
                    Casa.puedeEdificarCasa(this);
                    Edificio casa = new Casa(actual, this);
                    juego.intentarConstruir(actual, this, casa);
                } catch (MaximoEdificiosException e) {
                    Juego.consola.imprimir(e.getMessage());
                }
                break;
            case "hotel":
                try {
                    Hotel.puedeEdificarHotel(this);
                    this.quitarCuatroCasas();
                    Edificio hotel = new Hotel(actual, this);
                    juego.intentarConstruir(actual, this, hotel);
                } catch (MaximoEdificiosException e) {
                    Juego.consola.imprimir(e.getMessage());
                }
                break;
            case "piscina":
                try {
                    Piscina.puedeEdificarPiscina(this);
                    Edificio piscina = new Piscina(actual, this);
                    juego.intentarConstruir(actual, this, piscina);
                } catch (MaximoEdificiosException e) {
                    Juego.consola.imprimir(e.getMessage());
                }
                break;
            case "pista":
                try {
                    PistaDeporte.puedeEdificarPista(this);
                    Edificio pista = new PistaDeporte(actual, this);
                    juego.intentarConstruir(actual, this, pista);
                } catch (MaximoEdificiosException e) {
                    Juego.consola.imprimir(e.getMessage());
                }
                break;
            default:
                System.err.println("Tipo de edificio no válido.");
                break;
        }
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
    public void quitarCuatroCasas() throws NoEresPropietarioException {
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

            Juego m = Juego.getInstancia(); // con getInstancia() guardo una referencia al menú real y así puedo modificar la lista de edificios del menú
            if (m != null) {
                m.eliminarEdificioGlobal(e);
            }
        }
    }

    /* Función que uso en gestionarVentaEdificios para vender única y específicamente casas
     * Los mensajes son personalizados para las casas, los otros tipos de edificio
     * también tienen su correspondiente función
     */
    public void venderCasas(int nCasas, Jugador j) throws NoEresPropietarioException, JugadorBancarrotaException {
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

                    Juego m = Juego.getInstancia(); // con getInstancia() guardo una referencia al menú real y así puedo modificar la lista de edificios del menú
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
    public void venderHoteles(int nHoteles, Jugador j) throws NoEresPropietarioException, JugadorBancarrotaException {
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

                    Juego m = Juego.getInstancia(); // con getInstancia() guardo una referencia al menú real y así puedo modificar la lista de edificios del menú
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
            } else if (getNumHoteles() == 1) {
                System.err.println("Solamente se puede vender 1 hotel, recibiendo " + getValorCasayHotel() + "€");
            }
        }
    }

    /* Función que uso en gestionarVentaEdificios para vender única y específicamente piscinas
     * Los mensajes son personalizados para las piscinas, los otros tipos de edificio
     * también tienen su correspondiente función
     */
    public void venderPiscinas(int nPiscinas, Jugador j) throws NoEresPropietarioException, JugadorBancarrotaException {
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

                    Juego m = Juego.getInstancia(); // con getInstancia() guardo una referencia al menú real y así puedo modificar la lista de edificios del menú
                    if (m != null) {
                        m.eliminarEdificioGlobal(e);
                    }

                    int ganancia = this.getValorPiscina();
                    e.getPropietario().sumarFortuna(ganancia);

                    vendidas++;
                } else {
                    i++; // solo avanzo cuando no elimino nada
                }
            }

            if (vendidas == 1 && getNumPiscinas() == 0) {
                System.out.println(this.getDuenho().getNombre() + " ha vendido " + nPiscinas + " piscina en " + getNombre() + ", recibiendo " + nPiscinas * getValorPiscina() + "€. En la propiedad quedan " + this.getNumPiscinas() + " piscinas.");
            }
        } else {
            if (getNumPiscinas() == 0) {
                System.err.println("No se pueden vender piscinas en " + getNombre() + ", no hay ninguna construida");
            } else if (getNumPiscinas() == 1) {
                System.err.println("Solamente se puede vender 1 piscina, recibiendo " + getValorPiscina() + "€");
            }
        }
    }

    /* Función que uso en gestionarVentaEdificios para vender única y específicamente pistas de deporte
     * Los mensajes son personalizados para las pistas de deporte, los otros tipos de edificio
     * también tienen su correspondiente función
     */
    public void venderPistas(int nPistas, Jugador j) throws NoEresPropietarioException, JugadorBancarrotaException {
        int vendidas = 0;
        if (nPistas <= getNumPistas()) {
            // Recorremos la lista y paramos al vender nHoteles
            for (int i = 0; i < edificios.size() && vendidas < nPistas; ) {
                Edificio e = edificios.get(i);
                if (e != null && "pista".equalsIgnoreCase(e.getTipo())) {
                    edificios.remove(i);
                    if (numPistas > 0) numPistas--;

                    if (e.getPropietario() != null) {
                        e.getPropietario().eliminarEdificioDeJugador(e); // también quitamos el hotel de los edificios del jugador
                    }

                    Juego m = Juego.getInstancia(); // con getInstancia() guardo una referencia al menú real y así puedo modificar la lista de edificios del menú
                    if (m != null) {
                        m.eliminarEdificioGlobal(e);
                    }

                    int ganancia = this.getValorPistaDeporte();
                    e.getPropietario().sumarFortuna(ganancia);

                    vendidas++;
                } else {
                    i++; // solo avanzo cuando no elimino nada
                }
            }

            if (vendidas == 1 && getNumPistas() == 0) {
                System.out.println(this.getDuenho().getNombre() + " ha vendido " + nPistas + " pista de deporte en " + getNombre() + ", recibiendo " + nPistas * getValorPistaDeporte() + "€. En la propiedad quedan " + this.getNumPistas() + " pistas de deporte.");
            }
        } else {
            if (getNumPistas() == 0) {
                System.err.println("No se pueden vender pistas de deporte en " + getNombre() + ", no hay ninguna construida");
            } else if (getNumPistas() == 1) {
                System.err.println("Solamente se puede vender 1 pista de deporte, recibiendo " + getValorPistaDeporte() + "€");
            }
        }
    }

    /*
        Función que lo único que hace es calcular la cantidad que se le restará a la fortuna de un
        jugador cuando cae en una casilla que no le pertenece. Cada edificación tiene un alquiler
        específico según la casilla en la que se encuentre. Para usarla hay que guardar el valor
        que retorna en una variable y esa variable usarla en el menú, en evaluarCasilla.
     */
    private int calcularAlquiler(Jugador actual) {
        // Si no es un solar devuelve 0 porque sería otro case que ya se realiza dentro de evaluarCasilla()
        if (getTipo() == null || !"solar".equalsIgnoreCase(getTipo())) return 0;

        if (actual == null) return 0; // si hay un error con el jugador actual
        if (estaHipotecada()) return 0; // si está hipotecada no se paga
        if (getDuenho() == null) return 0; // si no hay dueño no se paga
        if (actual == getDuenho()) return 0; // el dueño no paga

        int totalEdificios = getNumCasas() + getNumHoteles() + getNumPiscinas() + getNumPistas();
        if (totalEdificios == 0) return alquilerCasilla;

        // Ahora calculamos el total:
        int alquiler = 0;
        alquiler += getNumCasas()   * getAlquilerCasa();
        alquiler += getNumHoteles() * getAlquilerHotel();
        alquiler += (getNumPiscinas() + getNumPistas()) * getAlquilerPiscinaYPista();

        return alquiler;
    }

    /*
        Esta otra función es muy similar a la anterior pero en vez de usarse en evaluarCasilla
        se utiliza en listarEdificiosGrupo. Hacemos una nueva porque con la anterior, si el jugador
        quería listar edificios de un grupo que le pertenece haciendo "listar edificios grupoDeSuPropiedad",
        el alquiler le salía a 0, porque si él cae ahí no debe pagar. Esta función devuelve el valor
        del alquiler sin importar qué jugador realice el comando "listar edificios grupo"
     */
    public int calcularAlquilerParaMostrar() {
        if (getTipo() == null || !"solar".equalsIgnoreCase(getTipo())) return 0;
        if (estaHipotecada()) return 0; // si está hipotecada no se paga

        int base = getAlquilerCasilla();

        // Sumamos el valor del alquiler de los edificios
        int numEdificios = getNumCasas() + getNumHoteles() + getNumPiscinas() + getNumPistas();

        int alquiler = 0;
        if (numEdificios != 0) {
            alquiler += getNumCasas() * getAlquilerCasa();
            alquiler += getNumHoteles() * getAlquilerHotel();
            alquiler += (getNumPiscinas() + getNumPistas()) * getAlquilerPiscinaYPista();
        }
        else {
            alquiler = base;
        }

        // Ahora si todas las casillas del grupo tienen el mismo dueño, este no es la banca, y además no hay edificios, entonces el alquiler se multiplica por 2
        if (getGrupo() != null && getDuenho() != null && getDuenho().getNombre() != null && getGrupo().esDuenhoGrupo(getDuenho()) && numEdificios == 0) {
            alquiler *= 2;
        }

        return alquiler;
    }

    public void hipotecar(Jugador actual) throws NoEresPropietarioException, JugadorBancarrotaException, PropiedadHipotecadaException {

        if (this.getDuenho() == null || this.getDuenho() != actual) {
            throw new NoEresPropietarioException("No puedes hipotecar " + this.getNombre() + " porque no eres su propietario.");
        }

        if (estaHipotecada()) {
            throw new PropiedadHipotecadaException("La casilla '" + this.getNombre() + "' ya está hipotecada.");
        }

        // añadir después: si tiene edificios, pedir que los venda y demás
        if (this.getEdificios() != null && !this.getEdificios().isEmpty()) {
            System.out.println("La casilla " + this.getNombre() + " tiene edificios. Se venderán automáticamente antes de hipotecar la casilla.");

            int nCasas = this.getNumCasas();
            int nHoteles = this.getNumHoteles();
            int nPiscinas = this.getNumPiscinas();
            int nPistas = this.getNumPistas();

            if (nCasas > 0) this.venderCasas(nCasas, actual);
            if (nHoteles > 0) this.venderHoteles(nHoteles, actual);
            if (nPiscinas > 0) this.venderPiscinas(nPiscinas, actual);
            if (nPistas > 0) this.venderPistas(nPistas, actual);
            return;
        }

        float valorHipoteca = this.getHipoteca();
        this.setHipotecada(true);
        actual.sumarFortuna(valorHipoteca);

        String colorGrupo = (this.getGrupo() != null && this.getGrupo().getNombreColorGrupo() != null ) ? this.getGrupo().getNombreColorGrupo() : "sin grupo";
        System.out.println(actual.getNombre() + " recibe " + valorHipoteca + "€ por la hipoteca de " + this.getNombre() + ". No se puede recibir alquileres ni edificar en el grupo " + colorGrupo + ".");
    }

    public void deshipotecar(Jugador actual) throws NoEresPropietarioException, FondosInsuficientesException, JugadorBancarrotaException {

        if (this.getDuenho() == null || this.getDuenho() != actual) {
            throw new NoEresPropietarioException("No puedes deshipotecar " + this.getNombre() + " porque no eres su propietario.");
        }

        if (!this.isHipotecada()) {
            System.out.println("La casilla '" + this.getNombre() + "' no está hipotecada.");
            return;
        }

        float valorHipoteca = this.getHipoteca();

        if (actual.getFortuna() < valorHipoteca) {
            throw new FondosInsuficientesException("La fortuna de " + actual.getNombre() + " no es suficiente para deshipotecar " + this.getNombre() + ".");
        }

        this.setHipotecada(false);
        actual.sumarFortuna(-valorHipoteca);
        actual.sumarGastos(valorHipoteca);

        String colorGrupo = (this.getGrupo() != null && this.getGrupo().getNombreColorGrupo() != null ) ? this.getGrupo().getNombreColorGrupo() : "sin grupo";
        System.out.println(actual.getNombre() + " paga " + valorHipoteca + "€ por deshipotecar " + this.getNombre() + ". Ahora puede recibir alquileres y edificar en el grupo " + colorGrupo + ".");
    }

    /* ------------------------------------------ */

    /* ---------- GETTERS  ---------- */

    public int getValorCasayHotel() {
        return valorCasayHotel;
    }

    public int getValorPiscina() {
        return valorPiscina;
    }

    public int getValorPistaDeporte() {
        return valorPistaDeporte;
    }

    public int getNumCasas() {
        return numCasas;
    }

    public int getNumHoteles() {
        return numHoteles;
    }

    public int getNumPiscinas() {
        return numPiscinas;
    }

    public int getNumPistas() {
        return numPistas;
    }

    public int getAlquilerCasilla() {
        return alquilerCasilla;
    }

    public int getAlquilerCasa() {
        return alquilerCasa;
    }

    public int getAlquilerHotel() {
        return alquilerHotel;
    }

    public int getAlquilerPiscinaYPista() {
        return alquilerPiscinaYPista;
    }

    public ArrayList<Edificio> getEdificios() {
        return edificios;
    }

    public boolean isHipotecada() { return hipotecada; }
    public void setHipotecada(boolean hipotecada) { this.hipotecada = hipotecada; }

    /* ------------------------------ */

}

