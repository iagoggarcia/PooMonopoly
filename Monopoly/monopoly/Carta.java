package monopoly;

public class Carta {
    private String tipo; // "suerte" o "caja"
    private int id; // número de carta del 1 al 6
    private String descripcion; 
    private String accion; // tipo de acciion (pagar, cobrar, mover, carcel)
    
    // constructor
    public Carta(String tipo, int id, String descripcion, String accion) {
        this.tipo = tipo;
        this.id = id;
        this.descripcion = descripcion;
        this.accion = accion;
    }

    public String getTipo() {
        return tipo;
    }

    public int getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }
    public String getAccion() {
        return accion;
    }
}

