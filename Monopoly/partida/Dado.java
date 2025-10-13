package partida;

import java.util.concurrent.ThreadLocalRandom;


public class Dado {
    //El dado solo tiene un atributo en nuestro caso: su valor.
    private int valor; // guarda el último valor obtenido (estado  del dado)

    //Metodo para simular lanzamiento de un dado: devolverá un valor aleatorio entre 1 y 6.
    public int hacerTirada() {
        this.valor = ThreadLocalRandom.current().nextInt(1,7); // guardamos el resultado
        // se pone de 1 a 7 porque el límite superior es exclusivo (el 7 no se cuenta)
        return this.valor;
    }
    
}
