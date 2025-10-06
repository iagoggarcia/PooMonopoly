package monopoly;

import java.util.ArrayList;
import java.util.HashMap;
import partida.*;


public class Tablero {
    //Atributos.
    private ArrayList<ArrayList<Casilla>> posiciones; //Posiciones del tablero: se define como un arraylist de arraylists de casillas (uno por cada lado del tablero).
    private HashMap<String, Grupo> grupos; //Grupos del tablero, almacenados como un HashMap con clave String (será el color del grupo).
    private Jugador banca; //Un jugador que será la banca.

    //Constructor: únicamente le pasamos el jugador banca (que se creará desde el menú).
    public Tablero(Jugador banca) {
        this.banca = banca;
        this.posiciones = new ArrayList<>();
        this.grupos = new HashMap<>();
        generarCasillas();
    }

    
    //Método para crear todas las casillas del tablero. Formado a su vez por cuatro métodos (1/lado).
    private void generarCasillas() {
        this.insertarLadoSur();
        this.insertarLadoOeste();
        this.insertarLadoNorte();
        this.insertarLadoEste();
    }
    
    //Método para insertar las casillas del lado norte.
    private void insertarLadoNorte() {
        ArrayList<Casilla> ladoNorte = new ArrayList<>(); // creo el array de casillas del lado norte, también voy a meter el parking (esquina superior izquierda)
        ladoNorte.add(new Casilla("Parking", "especial", 20, banca));
        ladoNorte.add(new Casilla("Solar12", "solar", 21, 2200000, banca));
        ladoNorte.add(new Casilla("Suerte", "suerte", 22, banca));
        ladoNorte.add(new Casilla("Solar13", "solar", 23, 2200000, banca));
        ladoNorte.add(new Casilla("Solar14", "solar", 24, 2400000,banca));
        ladoNorte.add(new Casilla("Trans3", "transporte", 25, 500000, banca));
        ladoNorte.add(new Casilla("Solar15", "solar", 26, 2600000, banca));
        ladoNorte.add(new Casilla("Solar16", "solar", 27, 2600000, banca));
        ladoNorte.add(new Casilla("Serv2", "servicios", 28, 500000, banca));
        ladoNorte.add(new Casilla("Solar17", "solar", 29, 2800000,banca));

        posiciones.add(ladoNorte); // meto el array "norte" (que a su vez es otro array con las casillas) en posiciones
    }

    //Método para insertar las casillas del lado sur.
    private void insertarLadoSur() {
        ArrayList<Casilla> ladoSur = new ArrayList<>(); // lo mismo para el lado sur, y también meto la casilla de inicio (salida)
        ladoSur.add(new Casilla("Salida", "especial", 0, banca));
        ladoSur.add(new Casilla("Solar1", "solar", 1, 600000,banca));
        ladoSur.add(new Casilla("Caja", "caja de comunidad", 2, banca));
        ladoSur.add(new Casilla("Solar2", "solar", 3, 600000, banca));
        ladoSur.add(new Casilla("Imp1", "impuestos", 4, banca));
        ladoSur.add(new Casilla("Trans1", "transporte", 5, 500000, banca));
        ladoSur.add(new Casilla("Solar3", "solar", 6, 1000000, banca));
        ladoSur.add(new Casilla("Suerte", "suerte", 7, banca));
        ladoSur.add(new Casilla("Solar4", "solar", 8, 1000000, banca));
        ladoSur.add(new Casilla("Solar5", "solar", 9, 1200000, banca));

        posiciones.add(ladoSur); // meto el array "sur" (que a su vez es otro array con las casillas) en posiciones
    }

    //Método que inserta casillas del lado oeste.
    private void insertarLadoOeste() {
        ArrayList<Casilla> ladoOeste = new ArrayList<>(); // lo mismo para el lado oeste, y también meto la cárcel
        ladoOeste.add(new Casilla("Cárcel", "especial", 10, banca));
        ladoOeste.add(new Casilla("Solar6", "solar", 11, 1400000, banca));
        ladoOeste.add(new Casilla("Serv1", "servicios", 12, 500000, banca));
        ladoOeste.add(new Casilla("Solar7", "solar", 13, 1400000, banca));
        ladoOeste.add(new Casilla("Solar8", "solar", 14, 1600000, banca));
        ladoOeste.add(new Casilla("Trans2", "transporte", 15, 500000, banca));
        ladoOeste.add(new Casilla("Solar9", "especial", 16, 1800000, banca));
        ladoOeste.add(new Casilla("Caja", "caja de comunidad", 17, banca));
        ladoOeste.add(new Casilla("Solar10", "solar", 18, 1800000, banca));
        ladoOeste.add(new Casilla("Solar11", "solar", 19, 2200000, banca));

        posiciones.add(ladoOeste); // meto el array "oeste" (que a su vez es otro array con las casillas) en posiciones
    }

    //Método que inserta las casillas del lado este.
    private void insertarLadoEste() {
        ArrayList<Casilla> ladoEste = new ArrayList<>(); // lo mismo para el lado este, y también meto "Ir a la cárcel"
        ladoEste.add(new Casilla("IrCarcel", "especial", 30, banca));
        ladoEste.add(new Casilla("Solar18", "solar", 31, 3000000, banca));
        ladoEste.add(new Casilla("Solar19", "solar", 32, 3000000, banca));
        ladoEste.add(new Casilla("Caja", "caja de comunidad", 33, banca));
        ladoEste.add(new Casilla("Solar20", "solar", 34, 3200000, banca));
        ladoEste.add(new Casilla("Trans4", "transporte", 35, 500000, banca));
        ladoEste.add(new Casilla("Suerte", "suerte", 36, banca));
        ladoEste.add(new Casilla("Solar21", "solar", 37, 3500000, banca));
        ladoEste.add(new Casilla("Imp2", "impuestos", 38, banca));
        ladoEste.add(new Casilla("Solar22", "solar", 39, 40000000, banca));

        posiciones.add(ladoEste); // meto el array "este" (que a su vez es otro array con las casillas) en posiciones
    }

    /*
    * los getters y setters de lo anterior no sé si hacen falta más adelante pero yo solo los creé para hacer alguna
    * prueba en el ejecutable, si no hacen falta ya los borraremos:
    */
    public ArrayList<ArrayList<Casilla>> getPosiciones() {
        return posiciones;
    }

    public void setPosiciones(ArrayList<ArrayList<Casilla>> posiciones) {
        this.posiciones = posiciones;
    }

    public HashMap<String, Grupo> getGrupos() {
        return grupos;
    }

    public void setGrupos(HashMap<String, Grupo> grupos) {
        this.grupos = grupos;
    }

    public Jugador getBanca() {
        return banca;
    }

    public void setBanca(Jugador banca) {
        this.banca = banca;
    }

    //Para imprimir el tablero, modificamos el método toString().
    //@Override
    //public String toString() {
    //}
    
    //Método usado para buscar la casilla con el nombre pasado como argumento:
    //public Casilla encontrar_casilla(String nombre){
    //}

}
