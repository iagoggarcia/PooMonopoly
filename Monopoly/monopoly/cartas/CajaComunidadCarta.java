package monopoly.cartas;

import monopoly.Juego;
import monopoly.Valor;
import monopoly.casillas.Casilla;
import monopoly.casillas.accion.Accion;
import monopoly.excepciones.CasillaInexistenteException;
import monopoly.excepciones.JugadorBancarrotaException;
import monopoly.excepciones.JugadorNoExisteException;
import partida.Jugador;

public class CajaComunidadCarta extends Carta {

    public CajaComunidadCarta(int id, String descripcion) {
        super(id, descripcion);
    }

    @Override
    public void accion(Jugador jugador, Jugador banca, Accion casillaActual, Juego juego) throws JugadorBancarrotaException, CasillaInexistenteException, JugadorNoExisteException {

        Juego.consola.imprimir(jugador.getNombre() + " roba una carta de Caja de Comunidad. " + this.descripcion);

        switch (this.id) {

            case 1: { // pagar 500.000
                float cantidad = 500_000;

                if (jugador.getFortuna() < cantidad) {
                    boolean puedeHipotecar =
                        jugador.getPropiedades() != null &&
                        !jugador.getPropiedades().isEmpty() &&
                        jugador.getHipotecas().size() < jugador.getPropiedades().size();

                    if (puedeHipotecar)
                        juego.activarSubmenuBancarrota(jugador, cantidad, null);
                    else {
                        juego.declararBancarrota(jugador);
                        juego.setSolvente(false);
                    }
                    return;
                }

                jugador.sumarFortuna(-cantidad);
                jugador.sumarGastos(cantidad);
                banca.sumarFortuna(cantidad);
                Juego.consola.imprimir(jugador.getNombre() + " paga 500.000€.");
                break;
            }

            case 2: // cárcel
                jugador.encarcelar(juego.getTablero().getPosiciones());
                break;

            case 3: { // ir a salida
                Casilla salida = juego.getTablero().encontrar_casilla("Salida");

                jugador.getAvatar().getLugar().eliminarAvatar(jugador.getAvatar());
                salida.anhadirAvatar(jugador.getAvatar());
                jugador.getAvatar().setLugar(salida);

                jugador.sumarFortuna(Valor.SUMA_VUELTA);
                jugador.setVueltas(jugador.getVueltas() + 1);

                Juego.consola.imprimir(jugador.getNombre() + " va a Salida y cobra 2.000.000€.");
                salida.evaluarCasilla(jugador, banca, 0);
                break;
            }

            case 4: // cobrar 500.000
                jugador.sumarFortuna(500_000);
                jugador.setPremiosinversiones(jugador.getPremiosinversiones() + 500_000);
                Juego.consola.imprimir(jugador.getNombre() + " cobra 500.000€.");
                break;

            case 5: { // ir a Solar1
                Casilla destino = juego.getTablero().encontrar_casilla("Solar1");

                jugador.getAvatar().getLugar().eliminarAvatar(jugador.getAvatar());
                destino.anhadirAvatar(jugador.getAvatar());
                jugador.getAvatar().setLugar(destino);

                Juego.consola.imprimir(jugador.getNombre() + " retrocede hasta " + destino.getNombre() + ".");
                destino.evaluarCasilla(jugador, banca, 0);
                break;
            }

            case 6: { // ir a Solar20
                Casilla destino = juego.getTablero().encontrar_casilla("Solar20");

                jugador.getAvatar().getLugar().eliminarAvatar(jugador.getAvatar());
                destino.anhadirAvatar(jugador.getAvatar());
                jugador.getAvatar().setLugar(destino);

                if (destino.getPosicion() < casillaActual.getPosicion()) {
                    jugador.sumarFortuna(Valor.SUMA_VUELTA);
                    jugador.setVueltas(jugador.getVueltas() + 1);
                }

                Juego.consola.imprimir(jugador.getNombre() + " avanza hasta " + destino.getNombre() + ".");
                destino.evaluarCasilla(jugador, banca, 0);
                break;
            }
        }
    }
}
