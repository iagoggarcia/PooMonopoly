//aquí metemos la lógica de las funciones de la interfaz consola, esto luego se usa en todos los lugares del código
package monopoly;
import java.util.Scanner;

public class Consolanormal implements Consola{

    private Scanner sc = new Scanner(System.in);

    @Override
    public void imprimir(String mensaje){
        System.out.println(mensaje); //funcion para imprimir mensajes 
    }

    @Override
    public void imprimirSinSalto(String mensaje) {
        System.out.print(mensaje);
    }

    @Override
    public String leer(){
        return sc.nextLine(); //recogemos lo que introduce el usuario en un string para poder utilizarlo donde corresponda
    }
}
