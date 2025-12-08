package monopoly.casillas;

import partida.Jugador;
import monopoly.Juego;

public class Impuesto extends Casilla {
    // ATRIBUTOS:
    private int impuesto;

    public Impuesto (String nombre, int posicion, int impuesto) {
        super(nombre,posicion, "impuesto");
        this.impuesto = impuesto;
    }

    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        int imp = this.impuesto;

        // comprobamos solvencia, si no se puede pagar, declaramos bancarrota
        if (actual.getFortuna() < imp) {
            Juego.consola.imprimir(actual.getNombre() + " no tiene suficiente dinero para pagar el impuesto de " + this.nombre + ".");
            // comprobar si tiene algún solar sin hipotecar
            boolean puedeHipotecar = actual.getPropiedades() != null && !actual.getPropiedades().isEmpty() && actual.getHipotecas().size() < actual.getPropiedades().size();

            if (puedeHipotecar) {
                Juego m = Juego.getInstancia();
                m.activarSubmenuBancarrota(actual, imp, banca);
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

        Juego.consola.imprimir(actual.getNombre() + " paga " + imp + "€ en impuestos.");
        return true;
    }

    @Override
    public String infoCasilla() {
        StringBuilder informacion = new StringBuilder();

        informacion.append("{");
        informacion.append("\nTipo: ").append(tipo).append("\nA pagar: ").append(impuesto);
        informacion.append(infoJugadoresEnEstaCasilla());
        informacion.append("\n}");

        return informacion.toString();
    }

    public int getImpuesto() {
        return impuesto;
    }


}
