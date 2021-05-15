import java.rmi.Remote;
import java.rmi.RemoteException;

/* 
Author: Jose Saldaña Mercado
Interfaz que implementa el sistema acumulador del total de donaciones

Se utiliza este servicio para tener una variable compartida entre las n replicas y poder usar el algoritmo distribuido en anillo para exclusión mutua
*/

public interface TotalDonationInterface extends Remote {

    public void addDonation(int sender, int amount) throws RemoteException; // aumenta total de donaciones

    public int totalAmount() throws RemoteException; // devuelve el total de donaciones

}
