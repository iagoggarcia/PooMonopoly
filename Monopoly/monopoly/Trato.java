package monopoly;

import java.util.ArrayList;
import java.util.List;

import monopoly.casillas.propiedad.Propiedad;
import partida.Jugador;

public class Trato {

    private static int contadorTratos = 1;
    private String idTrato; 
    private  Jugador emisor; // quien propone el trato 
    private Jugador receptor; // a quien se le propone el trato
    private Propiedad propiedadEmisor; // la propiedad que se ofrece
    private int dineroEmisor; // el dinero que se ofrece
    private Propiedad propiedadReceptor; // la propiedad que quieres a cambio
    private int dineroReceptor; // el dinero que se pide a cambio
    private String descripcionTrato;

    public Trato (Jugador emisor, Jugador receptor, Propiedad propiedadEmisor, int dineroEmisor, Propiedad propiedadReceptor, int dineroReceptor, String descripcionTrato) {
        
        this.idTrato = "trato" + (contadorTratos++);
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

    public String descripcionAceptacion() {

        ArrayList<String> partes = new ArrayList<>();

        if (propiedadEmisor != null)
            partes.add(propiedadEmisor.getNombre());

        if (dineroEmisor > 0)
            partes.add(dineroEmisor + "€");

        if (propiedadReceptor != null)
            partes.add(propiedadReceptor.getNombre());

        if (dineroReceptor > 0)
            partes.add(dineroReceptor + "€");

        return String.join(" y ", partes);
    }

}
