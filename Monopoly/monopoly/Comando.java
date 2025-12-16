package monopoly;

import monopoly.excepciones.CasillaInexistenteException;
import monopoly.excepciones.FondosInsuficientesException;
import monopoly.excepciones.JugadorBancarrotaException;
import monopoly.excepciones.JugadorEncarceladoException;
import monopoly.excepciones.JugadorNoExisteException;
import monopoly.excepciones.MaximoEdificiosException;
import monopoly.excepciones.NoEdificableException;
import monopoly.excepciones.NoEresPropietarioException;
import monopoly.excepciones.PropiedadHipotecadaException;
import monopoly.excepciones.UsoIncorrectoComandoException;

public interface Comando {

    void imprimirTablero();
    void descJugador(String nombre) throws JugadorNoExisteException, UsoIncorrectoComandoException;
    void descCasilla(String nombreCasilla) throws CasillaInexistenteException, UsoIncorrectoComandoException;
    void lanzarDados() throws JugadorEncarceladoException, JugadorBancarrotaException, CasillaInexistenteException, JugadorNoExisteException;
    void lanzarDadosValor(int d1, int d2) throws JugadorEncarceladoException, JugadorBancarrotaException, CasillaInexistenteException, JugadorNoExisteException;
    void comprar(String casilla) throws CasillaInexistenteException, NoEresPropietarioException, JugadorBancarrotaException, FondosInsuficientesException;
    void salirCarcel() throws JugadorNoExisteException, JugadorBancarrotaException, CasillaInexistenteException, UsoIncorrectoComandoException;
    void listarJugadores();
    void listarVenta();
    void listarEdificios();
    void listarEdificiosGrupo(String grupo) throws UsoIncorrectoComandoException;
    void gestionarVentaEdificios(String tipo, String casilla, int cantidad) throws CasillaInexistenteException, NoEdificableException, NoEresPropietarioException, JugadorBancarrotaException;
    void acabarTurno();
    void crearJugadorArchivo(String nombre, String tipo) throws CasillaInexistenteException;
    void hipotecar(String casilla) throws CasillaInexistenteException, JugadorBancarrotaException, NoEresPropietarioException, PropiedadHipotecadaException;
    void deshipotecar(String casilla) throws CasillaInexistenteException, JugadorBancarrotaException, NoEresPropietarioException, FondosInsuficientesException;
    void edificarJuego(String tipoEdificio) throws NoEdificableException, JugadorNoExisteException, MaximoEdificiosException, FondosInsuficientesException, JugadorBancarrotaException, NoEresPropietarioException; //esto aun tengo que mirarlo
    void estadisticas() throws JugadorNoExisteException, CasillaInexistenteException;
    void estadisticasjugador(String nombreJugador) throws JugadorNoExisteException;
    void bancarrota() throws CasillaInexistenteException;
    void proponerTrato(String comando) throws JugadorNoExisteException, NoEresPropietarioException, FondosInsuficientesException, CasillaInexistenteException, UsoIncorrectoComandoException;
    void aceptarTrato(String idTrato) throws UsoIncorrectoComandoException, NoEresPropietarioException, FondosInsuficientesException, CasillaInexistenteException, JugadorBancarrotaException;
    void listarTratos();
    void eliminarTrato(String idTrato) throws UsoIncorrectoComandoException;
    
}
