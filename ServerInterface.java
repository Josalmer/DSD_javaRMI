import java.rmi.Remote;
import java.rmi.RemoteException;

/* 
Author: Jose Saldaña Mercado
Interfaz que implementan las replicas de msf (Médicos Sin Fronteras)

Exclusión mutua, para las consultas que requieren la cooperación de más de una replica se usará un algoritmo distribuido en anillo
*/

public interface ServerInterface extends Remote {

    public boolean localEntity(String entityName) throws RemoteException; // indica si dicha entidad esta registrada

    public int localEntities() throws RemoteException; // indica nº de entidades registradas

    public void registerEntity(String entityName) throws RemoteException; // registra la entidad en la replica
    
    public void passToken() throws RemoteException; // pasa el token a la siguiente replica
}
