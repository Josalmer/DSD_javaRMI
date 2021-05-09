import java.rmi.Remote;
import java.rmi.RemoteException;

/* 
Author: Jose Saldaña Mercado
Interfaz que implementan los socios donantes de msf (Médicos Sin Fronteras)
Los metodos permiten a los clientes:
1. registrarse (como entidad cliente)
2. donar
3. consultar total donado
Adicionalmente podrán consultar:
4. consultar cantidad donada por una entidad identificado por nombre
5. consultar número total de entidades registradas
6. consultar nombre de todos las entidades registradas
*/

public interface ClientInterface extends Remote {

    public boolean register(String entityName) throws RemoteException; // 1. registrarse (como entidad cliente)

    // public boolean donate(String entityName, int amount) throws RemoteException; // 2. donar

    // public int totalAmount() throws RemoteException; // 3. consultar total donado

    // public int amountBYEntity(String entityName) throws RemoteException; // 4. consultar cantidad donada por una entidad identificado por nombre

    // public int totalEntities() throws RemoteException; // 5. consultar número total de entidades registradas

    // public String[] getEntities() throws RemoteException; // 6. consultar nombre de todos las entidades registradas
    
}
