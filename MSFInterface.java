import java.rmi.Remote;
import java.rmi.RemoteException;

/* 
Author: Jose Saldaña Mercado
Interfaz que implementa el sistema MSF

Se utiliza este servicio para tener una variable compartida entre las n replicas y poder usar el algoritmo distribuido en anillo para exclusión mutua
*/

public interface MSFInterface extends Remote {

    public void addDonation(int amount) throws RemoteException; // aumenta total de donaciones

    public int totalAmount() throws RemoteException; // devuelve el total de donaciones

}
