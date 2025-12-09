package monopoly.excepciones;

public class JugadorNoExisteException extends ComandoException {
    
    public JugadorNoExisteException(String mensaje) {
        super(mensaje);
    }
    
}
