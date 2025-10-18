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
        this.insertarLadoNorte();
        this.insertarLadoSur();
        this.insertarLadoOeste();
        this.insertarLadoEste();
    }
    
    //Método para insertar las casillas del lado norte.
    private void insertarLadoNorte() {
        ArrayList<Casilla> ladoNorte = new ArrayList<>(); // creo el array de casillas del lado norte, también voy a meter el parking (esquina superior izquierda)
        ladoNorte.add(new Casilla("Parking", "especial", 21, banca));
        ladoNorte.add(new Casilla("Solar12", "solar", 22, 2200000, banca));
        ladoNorte.add(new Casilla("Suerte", "suerte", 23, banca));
        ladoNorte.add(new Casilla("Solar13", "solar", 24, 2200000, banca));
        ladoNorte.add(new Casilla("Solar14", "solar", 25, 2400000,banca));
        ladoNorte.add(new Casilla("Trans3", "transporte", 26, 500000, banca));
        ladoNorte.add(new Casilla("Solar15", "solar", 27, 2600000, banca));
        ladoNorte.add(new Casilla("Solar16", "solar", 28, 2600000, banca));
        ladoNorte.add(new Casilla("Serv2", "servicios", 29, 500000, banca));
        ladoNorte.add(new Casilla("Solar17", "solar", 30, 2800000,banca));
        ladoNorte.add(new Casilla("IrCarcel", "especial", 31, banca));
        posiciones.add(ladoNorte); // meto el array "norte" (que a su vez es otro array con las casillas) en posiciones
    }

    //Método para insertar las casillas del lado sur.
    private void insertarLadoSur() {
        ArrayList<Casilla> ladoSur = new ArrayList<>(); // lo mismo para el lado sur
        ladoSur.add(new Casilla("Salida", "especial", 1, banca));
        ladoSur.add(new Casilla("Solar1", "solar", 2, 600000,banca));
        ladoSur.add(new Casilla("Caja", "caja de comunidad", 3, banca));
        ladoSur.add(new Casilla("Solar2", "solar", 4, 600000, banca));
        ladoSur.add(new Casilla("Imp1", "impuestos", 5, banca));
        ladoSur.add(new Casilla("Trans1", "transporte", 6, 500000, banca));
        ladoSur.add(new Casilla("Solar3", "solar", 7, 1000000, banca));
        ladoSur.add(new Casilla("Suerte", "suerte", 8, banca));
        ladoSur.add(new Casilla("Solar4", "solar", 9, 1000000, banca));
        ladoSur.add(new Casilla("Solar5", "solar", 10, 1200000, banca));
        ladoSur.add(new Casilla("Cárcel", "especial", 11, banca));

        posiciones.add(ladoSur); // meto el array "sur" (que a su vez es otro array con las casillas) en posiciones
    }

    //Método que inserta casillas del lado oeste.
    private void insertarLadoOeste() {
        ArrayList<Casilla> ladoOeste = new ArrayList<>(); // lo mismo para el lado oeste
        ladoOeste.add(new Casilla("Solar6", "solar", 12, 1400000, banca));
        ladoOeste.add(new Casilla("Serv1", "servicios", 13, 500000, banca));
        ladoOeste.add(new Casilla("Solar7", "solar", 14, 1400000, banca));
        ladoOeste.add(new Casilla("Solar8", "solar", 15, 1600000, banca));
        ladoOeste.add(new Casilla("Trans2", "transporte", 16, 500000, banca));
        ladoOeste.add(new Casilla("Solar9", "solar", 17, 1800000, banca));
        ladoOeste.add(new Casilla("Caja", "caja de comunidad", 18, banca));
        ladoOeste.add(new Casilla("Solar10", "solar", 19, 1800000, banca));
        ladoOeste.add(new Casilla("Solar11", "solar", 20, 2200000, banca));

        posiciones.add(ladoOeste); // meto el array "oeste" (que a su vez es otro array con las casillas) en posiciones
    }

    //Método que inserta las casillas del lado este.
    private void insertarLadoEste() {
        ArrayList<Casilla> ladoEste = new ArrayList<>(); // lo mismo para el lado este
        ladoEste.add(new Casilla("Solar18", "solar", 32, 3000000, banca));
        ladoEste.add(new Casilla("Solar19", "solar", 33, 3000000, banca));
        ladoEste.add(new Casilla("Caja", "caja de comunidad", 34, banca));
        ladoEste.add(new Casilla("Solar20", "solar", 35, 3200000, banca));
        ladoEste.add(new Casilla("Trans4", "transporte", 36, 500000, banca));
        ladoEste.add(new Casilla("Suerte", "suerte", 37, banca));
        ladoEste.add(new Casilla("Solar21", "solar", 38, 3500000, banca));
        ladoEste.add(new Casilla("Imp2", "impuestos", 39, banca));
        ladoEste.add(new Casilla("Solar22", "solar", 40, 4000000, banca));

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
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("====TABLERO MONOPOLY====\n");

        ArrayList<Casilla> ladoNorte = posiciones.get(0);
        ArrayList<Casilla> ladoSur = posiciones.get(1);
        ArrayList<Casilla> ladoOeste = posiciones.get(2);
        ArrayList<Casilla> ladoEste = posiciones.get(3);

        // Definir el ancho fijo para cada casilla
        final int ANCHO_CASILLA = 8;


    // Primero imprimo el norte:
        sb.append("_".repeat(ANCHO_CASILLA));
        for (Casilla c : ladoNorte) {
            String nombreFormateado = String.format("|%-" + ANCHO_CASILLA + "s", c.getNombre());
            sb.append(nombreFormateado);
        }

        sb.append("\n");

        // Ahora imprimo simultáneamente el oeste y el este:
        for (int i = ladoOeste.size() - 1; i >= 0; i--) {
            // Usar variables para hacer el código más claro
            Casilla casillaOeste = ladoOeste.get(i);
            Casilla casillaEste = ladoEste.get(ladoEste.size() - 1 - i);
            int espaciosCentro = ANCHO_CASILLA * (ladoNorte.size() - 2) + (ladoNorte.size() - 2);

            sb.append(String.format("|%-" + ANCHO_CASILLA + "s", casillaOeste.getNombre()))
                    .append(" ".repeat(espaciosCentro))
                    .append(String.format("|%-" + ANCHO_CASILLA + "s", casillaEste.getNombre()))
                    .append("|\n");
        }

        // Por último imprimo el sur:
        for (Casilla c : ladoSur) {
            String nombreFormateado = String.format("|%-" + ANCHO_CASILLA + "s", c.getNombre());
            sb.append(nombreFormateado);
        }

        return sb.toString();
    }
    
    //Método usado para buscar la casilla con el nombre pasado como argumento:
    public Casilla encontrar_casilla(String nombre){
        // Busco la casilla en cada lado del tablero:
        for (ArrayList<Casilla> lado : posiciones) {   // posiciones: norte, sur, oeste, este
            for (Casilla c : lado) {
                String n = c.getNombre();
                if (n != null && n.equals(nombre)) {
                    return c;
                }
            }
        }
        return null; // si no existe una casilla con ese nombre devuelve null
    }
}
