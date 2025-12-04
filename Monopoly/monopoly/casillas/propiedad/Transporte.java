package monopoly.casillas.propiedad;

import monopoly.*;
import monopoly.casillas.*;
import partida.*;

public class Transporte extends Propiedad {

    public Transporte(String nombre, int posicion, String tipo, float valor, Jugador duenho, float impuesto, float hipoteca, Grupo grupo) {
        super(nombre, posicion, tipo, valor, duenho, impuesto, hipoteca, grupo);
    }

    @Override
    public float valor() {
        return this.valor;
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
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        if (actual == null || banca == null) throw new IllegalArgumentException("Los jugadores no pueden ser nulos.");

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
        if (this.duenho.getPropiedades() != null) {
            for (Casilla c : this.duenho.getPropiedades()) {
                if (c instanceof Transporte t) {
                    alquilerTotal += t.getImpuesto();
                    contarTransporte++;
                }
            }
        }

        // comprobamos solvencia, si no se puede pagar, declaramos bancarrota
        if (actual.getFortuna() < alquilerTotal) {
            System.out.println(actual.getNombre() + " no tiene suficiente dinero para pagar el alquiler de " + this.nombre + ".");
            // comprobar si tiene algún solar sin hipotecar
            boolean puedeHipotecar = actual.getPropiedades() != null && !actual.getPropiedades().isEmpty() && actual.getHipotecas().size() < actual.getPropiedades().size();

            if (puedeHipotecar) {
                Juego m = Juego.getInstancia();
                m.activarSubmenuBancarrota(actual, alquilerTotal, this.duenho);
                return true;
            } else {
                Juego m = Juego.getInstancia();
                m.declararBancarrota(actual);
                return false;
            }
        }
        // realizamos el pago
        actual.sumarFortuna(-alquilerTotal);
        actual.sumarGastos(alquilerTotal);
        actual.setAlquilerpagadojugador(actual.getAlquilerpagadojugador() + alquilerTotal); //aumentamos sus pagos de alquileres
        this.duenho.sumarFortuna(alquilerTotal);
        this.duenho.setAlquilercobradojugador(this.duenho.getAlquilercobradojugador() + alquilerTotal); //aumentamos cobro de alquileres

        Casilla lugar = actual.getAvatar().getLugar();
        if (lugar instanceof Propiedad p) {
            p.setImpuestosCobrados(p.getImpuestosCobrados() + alquilerTotal);
        }

        System.out.println(actual.getNombre() + " paga " + (int) alquilerTotal + "€ de alquiler a " + this.duenho.getNombre() + " por usar el transporte (" + contarTransporte + " transporte/s poseídos).");
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
