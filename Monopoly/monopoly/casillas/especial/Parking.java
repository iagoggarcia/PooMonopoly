package monopoly.casillas.especial;

import partida.Jugador;

public class Parking extends Especial {
    // ATRIBUTOS:
    private int bote;

    public Parking (String nombre, int posicion) {
        super(nombre, posicion);
    }

    /* ---------- MÉTODOS HEREDADOS ---------- */
    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        if (actual == null) return true;

        // cobre del bote (valor) y reseteo
        if (this.bote > 0) {
            actual.sumarFortuna(this.bote);
            System.out.println(actual.getNombre() + " cobra el bote del Parking: " + this.bote + "€.");
            this.bote = 0;
        } else {
            System.out.println(actual.getNombre() + " descansa en el Parking. No hay bote acumulado.");
        }
        return true;
    }

    @Override
    public String infoCasilla() {
        StringBuilder informacion = new StringBuilder();

        informacion.append("{");
        informacion.append("\nBote: ").append(bote);
        informacion.append(infoJugadoresEnEstaCasilla());
        informacion.append("\n}");

        return informacion.toString();
    }
    /* --------------------------------------- */

    /* ---------- MÉTODOS QUE HACEN FALTA PARA EL BOTE ---------- */

    public int getBote() {
        return bote;
    }

    public void sumarBote(float cantidad) {
        if (cantidad > 0) {
            this.bote += cantidad;
        }
    }

    public void resetearBote() {
        this.bote = 0;
    }

    /* ---------------------------------------------------------- */
}
