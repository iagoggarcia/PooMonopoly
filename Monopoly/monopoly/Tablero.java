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
        ladoNorte.add(new Casilla("Parking", "especial", 21, banca));
        ladoNorte.add(new Casilla("Solar12", "solar", 22, 2200000, banca, 180000, 1100000, 1500000, 300000, 600000));
        ladoNorte.add(new Casilla("Suerte", "suerte", 23, banca));
        ladoNorte.add(new Casilla("Solar13", "solar", 24, 2200000, banca, 180000, 1100000, 1500000, 300000, 600000));
        ladoNorte.add(new Casilla("Solar14", "solar", 25, 2400000,banca,200000, 1200000, 1500000, 300000, 600000));
        ladoNorte.add(new Casilla("Trans3", "transporte", 26, 500000, banca,250000));
        ladoNorte.add(new Casilla("Solar15", "solar", 27, 2600000, banca, 220000, 1300000, 1500000, 300000, 600000));
        ladoNorte.add(new Casilla("Solar16", "solar", 28, 2600000, banca,220000, 1300000, 1500000, 300000, 600000));
        ladoNorte.add(new Casilla("Serv2", "servicios", 29, 500000, banca,50000));
        ladoNorte.add(new Casilla("Solar17", "solar", 30, 2800000,banca,240000, 1400000, 1500000. 300000, 600000));
        ladoNorte.add(new Casilla("IrCarcel", "especial", 31, banca));

        // Grupos (NORTE)
        Grupo rojo     = new Grupo(ladoNorte.get(1), ladoNorte.get(3), ladoNorte.get(4), Valor.RED);
        grupos.put("rojo", rojo);

        Grupo amarillo = new Grupo(ladoNorte.get(6), ladoNorte.get(7), ladoNorte.get(9), Valor.BROWN);
        grupos.put("amarillo", amarillo);


        posiciones.add(ladoNorte); // meto el array "norte" (que a su vez es otro array con las casillas) en posiciones
    }

    //Método para insertar las casillas del lado sur.
    private void insertarLadoSur() {
        ArrayList<Casilla> ladoSur = new ArrayList<>(); // lo mismo para el lado sur
        ladoSur.add(new Casilla("Salida", "especial", 1, banca));
        ladoSur.add(new Casilla("Solar1", "solar", 2, 600000,banca,20000, 300000, 500000, 100000, 200000));
        ladoSur.add(new Casilla("Caja", "caja de comunidad", 3, banca));
        ladoSur.add(new Casilla("Solar2", "solar", 4, 600000, banca, 40000, 300000, 500000, 100000, 200000));
        ladoSur.add(new Casilla("Imp1", 5, 200000, banca));
        ladoSur.add(new Casilla("Trans1", "transporte", 6, 500000, banca, 250000));
        ladoSur.add(new Casilla("Solar3", "solar", 7, 1000000, banca, 60000, 500000, 500000, 100000, 200000));
        ladoSur.add(new Casilla("Suerte", "suerte", 8, banca));
        ladoSur.add(new Casilla("Solar4", "solar", 9, 1000000, banca, 60000, 500000, 500000, 100000, 200000));
        ladoSur.add(new Casilla("Solar5", "solar", 10, 1200000, banca,80000, 600000, 500000, 100000, 200000));
        ladoSur.add(new Casilla("Cárcel", "especial", 11, banca));

        // Grupos (SUR)
        Grupo marron  = new Grupo(ladoSur.get(1), ladoSur.get(3), Valor.BLACK); // o el ANSI que uses para marrón
        grupos.put("marron", marron);

        Grupo celeste = new Grupo(ladoSur.get(6), ladoSur.get(8), ladoSur.get(9), Valor.CYAN);
        grupos.put("celeste", celeste);


        posiciones.add(ladoSur); // meto el array "sur" (que a su vez es otro array con las casillas) en posiciones
    }

    //Método que inserta casillas del lado oeste.
    private void insertarLadoOeste() {
        ArrayList<Casilla> ladoOeste = new ArrayList<>(); // lo mismo para el lado oeste
        ladoOeste.add(new Casilla("Solar6", "solar", 12, 1400000, banca,100000, 700000, 1000000, 200000, 400000));
        ladoOeste.add(new Casilla("Serv1", "servicios", 13, 500000, banca,50000));
        ladoOeste.add(new Casilla("Solar7", "solar", 14, 1400000, banca,100000, 700000, 1000000, 200000, 400000));
        ladoOeste.add(new Casilla("Solar8", "solar", 15, 1600000, banca,120000, 800000, 1000000, 200000, 400000));
        ladoOeste.add(new Casilla("Trans2", "transporte", 16, 500000, banca, 250000));
        ladoOeste.add(new Casilla("Solar9", "solar", 17, 1800000, banca, 140000, 900000, 1000000, 200000, 400000));
        ladoOeste.add(new Casilla("Caja", "caja de comunidad", 18, banca));
        ladoOeste.add(new Casilla("Solar10", "solar", 19, 1800000, banca,140000, 900000, 1000000, 200000, 400000));
        ladoOeste.add(new Casilla("Solar11", "solar", 20, 2200000, banca,160000, 1000000, 1000000, 200000, 400000));

        // Grupos (OESTE)
        Grupo rosa    = new Grupo(ladoOeste.get(0), ladoOeste.get(2), ladoOeste.get(3), Valor.PURPLE);
        grupos.put("rosa", rosa);

        Grupo naranja = new Grupo(ladoOeste.get(5), ladoOeste.get(7), ladoOeste.get(8), Valor.YELLOW);
        grupos.put("naranja", naranja);


        posiciones.add(ladoOeste); // meto el array "oeste" (que a su vez es otro array con las casillas) en posiciones
    }

    //Método que inserta las casillas del lado este.
    private void insertarLadoEste() {
        ArrayList<Casilla> ladoEste = new ArrayList<>(); // lo mismo para el lado este
        ladoEste.add(new Casilla("Solar18", "solar", 32, 3000000, banca, 260000, 1500000, 2000000, 400000, 800000));
        ladoEste.add(new Casilla("Solar19", "solar", 33, 3000000, banca, 260000, 1500000, 2000000, 400000, 800000));
        ladoEste.add(new Casilla("Caja", "caja de comunidad", 34, banca));
        ladoEste.add(new Casilla("Solar20", "solar", 35, 3200000, banca, 280000, 1600000, 2000000, 400000, 800000));
        ladoEste.add(new Casilla("Trans4", "transporte", 36, 500000, banca, 250000));
        ladoEste.add(new Casilla("Suerte", "suerte", 37, banca));
        ladoEste.add(new Casilla("Solar21", "solar", 38, 3500000, banca, 350000, 1600000, 2000000, 400000, 800000));
        ladoEste.add(new Casilla("Imp2", 39, 200000, banca));
        ladoEste.add(new Casilla("Solar22", "solar", 40, 40000000, banca, 500000, 2000000, 2000000, 400000, 800000));

        // Grupos (ESTE)
        Grupo verde = new Grupo(ladoEste.get(0), ladoEste.get(1), ladoEste.get(3), Valor.GREEN);
        grupos.put("verde", verde);

        Grupo azul  = new Grupo(ladoEste.get(6), ladoEste.get(8), Valor.BLUE);
        grupos.put("azul", azul);



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

    // ====== Helpers nuevos (solo texto, NO cambian el orden del tablero) ======
    private String etiquetaAvatares(Casilla c) {
        if (c == null || c.getAvatares() == null || c.getAvatares().isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (partida.Avatar a : c.getAvatares()) {
            if (a == null || a.getId() == null) continue;
            sb.append(" &").append(a.getId());
        }
        return sb.toString();
    }

    private String etiquetaCasilla(Casilla c) {
        String nombre = (c != null && c.getNombre() != null) ? c.getNombre() : "";
        return nombre + etiquetaAvatares(c);
    }

    // Función para poner la inicial del avatar en la casilla en la que está
    private String nombreConIniciales(Casilla c) {
        String nombre = (c != null && c.getNombre() != null) ? c.getNombre() : "";
        if (c == null || c.getAvatares() == null || c.getAvatares().isEmpty()) return nombre;

        StringBuilder sb = new StringBuilder(nombre);
        for (Avatar a : c.getAvatares()) {
            if (a == null || a.getJugador() == null || a.getJugador().getNombre() == null) continue;
            String n = a.getJugador().getNombre().trim();
            if (!n.isEmpty()) {
                sb.append(" ").append(Character.toUpperCase(n.charAt(0))); // añade la inicial
            }
        }
        return sb.toString();
    }

    // Para imprimir el tablero, modificamos el método toString().
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // ¡Este es el orden correcto!
        ArrayList<Casilla> ladoSur   = posiciones.get(0);
        ArrayList<Casilla> ladoOeste = posiciones.get(1);
        ArrayList<Casilla> ladoNorte = posiciones.get(2);
        ArrayList<Casilla> ladoEste  = posiciones.get(3);

        final int ANCHO_CASILLA = 12;

        java.util.function.Function<Casilla, String> ansiGrupo = c -> {
            String ansi = Valor.WHITE;
            if (c.getGrupo() != null && c.getGrupo().getColorGrupo() != null) {
                ansi = c.getGrupo().getColorGrupo();
            }
            return ansi;
        };

        // Norte
        for (Casilla c : ladoNorte) {
            String celda = "|" + ansiGrupo.apply(c)
                    + String.format("%-" + ANCHO_CASILLA + "s", nombreConIniciales(c))
                    + Valor.RESET;
            sb.append(celda);
        }
        sb.append("|\n");

        // Oeste y Este (ambos tienen 9)
        for (int i = ladoOeste.size() - 1; i >= 0; i--) {
            Casilla o = ladoOeste.get(i);
            Casilla e = ladoEste.get(ladoEste.size() - 1 - i);

            int espaciosCentro = ANCHO_CASILLA * (ladoNorte.size() - 2) + (ladoNorte.size() - 2);

            String celdaO = "|" + ansiGrupo.apply(o)
                    + String.format("%-" + ANCHO_CASILLA + "s", nombreConIniciales(o))
                    + Valor.RESET;

            String celdaE = "|" + ansiGrupo.apply(e)
                    + String.format("%-" + ANCHO_CASILLA + "s", nombreConIniciales(e))
                    + Valor.RESET;

            sb.append(celdaO)
                    .append(" ".repeat(espaciosCentro))
                    .append(celdaE)
                    .append("|\n");
        }

        // Sur (inverso)
        for (int i = ladoSur.size() - 1; i >= 0; i--) {
            Casilla c = ladoSur.get(i);
            String celda = "|" + ansiGrupo.apply(c)
                    + String.format("%-" + ANCHO_CASILLA + "s", nombreConIniciales(c))
                    + Valor.RESET;
            sb.append(celda);
        }
        sb.append("|\n");

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