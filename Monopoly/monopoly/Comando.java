package monopoly;

public interface Comando {

    void imprimirTablero();
    void descJugador(String nombre);
    void descAvatar(String id);
    void descCasilla(String nombreCasilla);
    void lanzarDados();
    void lanzarDadosValor(int d1, int d2);
    void comprar(String casilla);
    void salirCarcel();
    void listarJugadores();
    void listarAvatares();
    void listarVenta();
    void listarEdificios();
    void listarEdificiosGrupo(String grupo);
    void gestionarVentaEdificios(String tipo, String casilla, int cantidad);
    void acabarTurno();
    void crearJugadorArchivo(String nombre, String tipo);
    void hipotecar(String casilla);
    void deshipotecar(String casilla);
    void edificarJuego(String tipoEdificio); //esto aun tengo que mirarlo
    void estadisticas();
    void estadisticasjugador(String nombreJugador);
    void bancarrota();

}
