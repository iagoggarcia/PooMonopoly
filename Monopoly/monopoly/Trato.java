package monopoly;

import monopoly.casillas.propiedad.Propiedad;
import partida.Jugador;

public class Trato {

    private String idTrato; 
    private  Jugador emisor; // quien propone el trato 
    private Jugador receptor; // a quien se le propone el trato
    private Propiedad propiedadEmisor; // la propiedad que se ofrece
    private int dineroEmisor; // el dinero que se ofrece
    private Propiedad propiedadReceptor; // la propiedad que quieres a cambio
    private int dineroReceptor; // el dinero que se pide a cambio
    private String descripcionTrato;

    public Trato (String idTrato, Jugador emisor, Jugador receptor, Propiedad propiedadEmisor, int dineroEmisor, Propiedad propiedadReceptor, int dineroReceptor, String descripcionTrato) {
        
        this.idTrato = idTrato;
        this.emisor = emisor;
        this.receptor = receptor;
        this.propiedadEmisor = propiedadEmisor;
        this.dineroEmisor = dineroEmisor;
        this.propiedadReceptor = propiedadReceptor;
        this.dineroReceptor = dineroReceptor;
        this.descripcionTrato = descripcionTrato;

    }

    public String getIdTrato() {
        return idTrato;
    }

    public Jugador getEmisor() {
        return emisor;
    }

    public Jugador getReceptor() {
        return receptor;
    }

    public Propiedad getPropiedadEmisor() {
        return propiedadEmisor;
    }

    public int getDineroEmisor() {
        return dineroEmisor;
    }

    public Propiedad getPropiedadReceptor() {
        return propiedadReceptor;
    }

    public int getDineroReceptor() {
        return dineroReceptor;
    }

    public String getDescripcionTrato() {
        return descripcionTrato;
    }
    
    public boolean comprobacionEmisor() {
        boolean comprobacion = true;

        if (propiedadEmisor != null && propiedadEmisor.getDuenho() != emisor) {
            comprobacion = false;
        }

        if (dineroEmisor > 0 && emisor.getFortuna() < dineroEmisor) {
            comprobacion = false;
        }

        return comprobacion;
    }

    public boolean comprobacionReceptor() {
        boolean comprobacion = true;

        if (propiedadReceptor != null && propiedadReceptor.getDuenho() != receptor) {
            comprobacion = false;
        }

        if (dineroReceptor > 0 && receptor.getFortuna() < dineroReceptor) {
            comprobacion = false;
        }

        return comprobacion;
    }
}
