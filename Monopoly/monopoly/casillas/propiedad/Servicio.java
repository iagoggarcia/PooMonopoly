package monopoly.casillas.propiedad;

import monopoly.casillas.*;
import partida.Jugador;
import monopoly.casillas.*;
import monopoly.*;

public class Servicio extends Propiedad {

    public Servicio(String nombre, int posicion, float valor, Jugador duenho, float impuesto) {
        super(nombre, posicion, "servicios", valor, duenho, impuesto);
    }

    @Override
    public boolean alquiler(Jugador actual) {
        int cantidad = (int)impuesto;

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

    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
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
        if (this.duenho.getPropiedades() != null) {
            for (Casilla c : this.duenho.getPropiedades()) {
                if (c.getTipo().equalsIgnoreCase("servicios")) {
                    serviciosPropietario++;
                }
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
        actual.setAlquilerpagadojugador(actual.getAlquilerpagadojugador() + alquiler); //aumentamos sus pagos de alquileres
        this.duenho.sumarFortuna(alquiler);
        this.duenho.setAlquilercobradojugador(this.duenho.getAlquilercobradojugador() + alquiler); //aumentamos cobro de alquileres

        Casilla lugar = actual.getAvatar().getLugar();
        if (lugar instanceof Propiedad p) {
            p.setImpuestosCobrados(p.getImpuestosCobrados() + alquiler);
        }

        System.out.println(actual.getNombre() + " paga " + (int) alquiler + "€ de alquiler a " + this.duenho.getNombre() + " por usar el servicio (" + serviciosPropietario + " servicio/s poseídos, tirada = " + tirada + ").");

        return true;
    }

    @Override
    public String infoCasilla() {
        StringBuilder informacion = new StringBuilder(); // aquí guardamos la info de la casilla
        Jugador d = this.getDuenho();
        String duenho;
        if (d == null || d.getNombre() == null) {
            duenho = "banca";
        } else {
            duenho = d.getNombre();
        }

        informacion.append("\nValor: ").append(this.valor).append("\nPropietario: ").append(duenho);
        informacion.append(infoJugadoresEnEstaCasilla());

        informacion.append("\n}");
        return informacion.toString();
    }

}
