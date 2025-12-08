package monopoly.cartas;

import monopoly.Juego;
import monopoly.Valor;
import monopoly.casillas.Casilla;
import monopoly.casillas.accion.Accion;
import partida.Jugador;

public class SuerteCarta extends Carta {

    public SuerteCarta(int id, String descripcion) {
        super(id, descripcion);
    }

    @Override
    public void accion(Jugador jugador, Jugador banca, Accion casillaActual, Juego juego) {

        Juego.consola.imprimir(jugador.getNombre() + " roba una carta de Suerte. " + this.descripcion);
        
        switch (this.id) {

            case 1: {   // mover Solar19
                Casilla destino = juego.getTablero().encontrar_casilla("Solar19");

                jugador.getAvatar().getLugar().eliminarAvatar(jugador.getAvatar());
                destino.anhadirAvatar(jugador.getAvatar());
                jugador.getAvatar().setLugar(destino);

                if (destino.getPosicion() < casillaActual.getPosicion()) {
                    jugador.sumarFortuna(Valor.SUMA_VUELTA);
                    jugador.setVueltas(jugador.getVueltas() + 1);
                    Juego.consola.imprimir(jugador.getNombre() + " cobra 2.000.000€ por pasar por Salida.");
                }

                destino.evaluarCasilla(jugador, banca, 0);
                break;
            }

            case 2: // cárcel
                jugador.encarcelar(juego.getTablero().getPosiciones());
                break;

            case 3: // cobrar 1.000.000
                jugador.sumarFortuna(1_000_000);
                jugador.setPremiosinversiones(jugador.getPremiosinversiones() + 1_000_000);
                Juego.consola.imprimir(jugador.getNombre() + " cobra 1.000.000€.");
                break;

            case 4: {  // pagar a jugadores
                float cantidad = 250_000;
                float total = cantidad * (juego.getJugadores().size() - 1);

                if (jugador.getFortuna() < total) {
                    boolean puedeHipotecar =
                        jugador.getPropiedades() != null &&
                        !jugador.getPropiedades().isEmpty() &&
                        jugador.getHipotecas().size() < jugador.getPropiedades().size();

                    if (puedeHipotecar)
                        juego.activarSubmenuBancarrota(jugador, total, null);
                    else {
                        juego.declararBancarrota(jugador);
                        juego.setSolvente(false);
                    }
                    return;
                }

                for (Jugador j : juego.getJugadores()) {
                    if (j != jugador) {
                        jugador.sumarFortuna(-cantidad);
                        jugador.sumarGastos(cantidad);
                        j.sumarFortuna(cantidad);
                    }
                }

                Juego.consola.imprimir(jugador.getNombre() + " paga 250.000€ a cada jugador.");
                break;
            }

            case 5: { // retroceder 3 casillas
                int nuevaPos = casillaActual.getPosicion() - 3;
                if (nuevaPos < 1) nuevaPos += 40;

                Casilla destino = juego.getTablero().casillaPorPosicion(nuevaPos);

                jugador.getAvatar().getLugar().eliminarAvatar(jugador.getAvatar());
                destino.anhadirAvatar(jugador.getAvatar());
                jugador.getAvatar().setLugar(destino);

                Juego.consola.imprimir(jugador.getNombre() + " retrocede 3 casillas hasta " + destino.getNombre());
                destino.evaluarCasilla(jugador, banca, 0);
                break;
            }

            case 6: // pagar multa
                float multa = 150_000;

                if (jugador.getFortuna() < multa) {
                    boolean puedeHipotecar =
                        jugador.getPropiedades() != null &&
                        !jugador.getPropiedades().isEmpty() &&
                        jugador.getHipotecas().size() < jugador.getPropiedades().size();

                    if (puedeHipotecar)
                        juego.activarSubmenuBancarrota(jugador, multa, null);
                    else {
                        juego.declararBancarrota(jugador);
                        juego.setSolvente(false);
                    }
                    return;
                }

                jugador.sumarFortuna(-multa);
                jugador.sumarGastos(multa);
                banca.sumarFortuna(multa);
                Juego.consola.imprimir(jugador.getNombre() + " paga 150.000€ de multa.");
                break;

            case 7: // transporte más cercano
                juego.moverTransporteMasCercano(jugador, banca);
                break;
        }
    }
}
