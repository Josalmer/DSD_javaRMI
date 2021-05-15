import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

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

    // 1. registrarse (como entidad cliente)
    // Devuelve el identificador de la replica donde se ha registrado
    public int register(String entityName) throws RemoteException;

    public void donate(String entityName, int amount) throws RemoteException; // 2. donar

    public int totalAmount() throws RemoteException; // 3. consultar total donado

    public int amountBYEntity(String entityName) throws RemoteException; // 4. consultar cantidad donada por una entidad identificado por nombre

    public int totalEntities() throws RemoteException; // 5. consultar número total de entidades registradas

    public ArrayList<String> getEntities() throws RemoteException; // 6. consultar nombre de todos las entidades registradas
    
}
