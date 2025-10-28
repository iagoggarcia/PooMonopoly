package monopoly;

import partida.*;
import java.util.ArrayList;


class Grupo {

    //Atributos
    private ArrayList<Casilla> miembros; //Casillas miembros del grupo.
    private String colorGrupo; //Color del grupo
    private int numCasillas; //Número de casillas del grupo.

    //Constructor vacío.
    public Grupo() {
    }

    /*Constructor para cuando el grupo está formado por DOS CASILLAS:
    * Requiere como parámetros las dos casillas miembro y el color del grupo.
     */
    public Grupo(Casilla cas1, Casilla cas2, String colorGrupo) {
        this();
        this.colorGrupo = colorGrupo;
        anhadirCasilla(cas1);
        anhadirCasilla(cas2);
    }


    /*Constructor para cuando el grupo está formado por TRES CASILLAS:
    * Requiere como parámetros las tres casillas miembro y el color del grupo.
     */
    public Grupo(Casilla cas1, Casilla cas2, Casilla cas3, String colorGrupo) {
    this();
    this.colorGrupo = colorGrupo;
    anhadirCasilla(cas1);
    anhadirCasilla(cas2);
    anhadirCasilla(cas3);
    }


    public String getColorGrupo() {
        return colorGrupo;
    }

    public void setColorGrupo(String colorGrupo) {
        this.colorGrupo = colorGrupo;
    }

    public ArrayList<Casilla> getMiembros() {
        return miembros;
    }

    public void setMiembros(ArrayList<Casilla> miembros) {
        this.miembros = miembros;
    }

    public int getNumCasillas() {
        return numCasillas;
    }

    public void setNumCasillas(int numCasillas) {
        this.numCasillas = numCasillas;
    }

    /* Método que añade una casilla al array de casillas miembro de un grupo.
    * Parámetro: casilla que se quiere añadir.
     */
    public void anhadirCasilla(Casilla miembro) {
        if (miembro == null) return;
        if (this.miembros == null) this.miembros = new ArrayList<>();
        if (!this.miembros.contains(miembro)) {
            this.miembros.add(miembro);
            this.numCasillas = this.miembros.size();
            miembro.setGrupo(this);
        }
    }

    /*Método que comprueba si el jugador pasado tiene en su haber todas las casillas del grupo:
    * Parámetro: jugador que se quiere evaluar.
    * Valor devuelto: true si es dueño de todas las casillas del grupo, false en otro caso.
     */
    public boolean esDuenhoGrupo(Jugador jugador) {
        if (jugador == null || this.miembros == null || this.miembros.isEmpty()) return false;

        for (Casilla c : this.miembros) {
            Jugador d = c.getDuenho();
            // Si no hay dueño, es banca o el dueño no es el jugador pasado, no controla el grupo
            if (d == null) return false;
            String nombreDuenho = d.getNombre();
            if (nombreDuenho != null && nombreDuenho.equalsIgnoreCase("banca")) return false;
            if (d != jugador) return false; // comparamos por instancia; en este diseño es lo más seguro
        }
        return true;
    }

    // Función para usar en listar en venta
    public String getNombreColorGrupo() {
        if (this.colorGrupo == null) return "";
        String c = this.colorGrupo;
        if (c.equals(Valor.RED))    return "rojo";
        if (c.equals(Valor.GREEN))  return "verde";
        if (c.equals(Valor.BLUE))   return "azul";
        if (c.equals(Valor.YELLOW)) return "amarillo";
        if (c.equals(Valor.PURPLE)) return "rosa";
        if (c.equals(Valor.CYAN))   return "celeste";
        if (c.equals(Valor.BROWN))  return "marrón";
        if (c.equals(Valor.BLACK))  return "negro";
        if (c.equals(Valor.WHITE))  return "blanco";
        return "";
    }


}
