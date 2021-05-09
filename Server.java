import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/* 
Author: Jose Saldaña Mercado
Clase que implementa los métodos que ejecutaran las replicas del servicio de donaciones de msf
*/

public class Server implements ServerInterface, ClientInterface {
    int id;
    int replicasCount;
    ArrayList<Donation> donations;
    ArrayList<String> entities;
    ArrayList<ServerInterface> replicas;
    Registry registry;

    String host;
    int port;

    public Server(int id, int replicasCount, String host, int port) {
        this.id = id;
        this.replicasCount = replicasCount;
        this.host = host;
        this.port = port;
    
        this.donations = new ArrayList<>();
        this.entities = new ArrayList<>();
        this.replicas = new ArrayList<>();
    }

    public void setReplicas() {
        try {
            for (int i = 0; i < this.replicasCount; i++) {
                ServerInterface replica = (ServerInterface) this.registry.lookup(Integer.toString(i));
                this.replicas.add(replica);
            }
        } catch (Exception e) {
            //TODO: handle exception
        }
    }

    public void init() {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String remoteObjectName = Integer.toString(this.id);
            ServerInterface replica = this;
            ServerInterface stub = (ServerInterface) UnicastRemoteObject.exportObject(replica, 0);
            this.registry = LocateRegistry.getRegistry(host, port);
            this.registry.rebind(remoteObjectName, stub);
            System.out.println("Replica " + this.id + " ready");
        } catch (Exception e) {
            //TODO: handle exception
        }
    }

    // Peticiones de clientes -----------------------------------------------------------------------------------------

    @Override
    public boolean register(String entityName) {
        if (localEntity(entityName)) {
            return true;
        } else {
            try {
                for (int i = 0; i < )
                return false;
            } catch (Exception e) {
                //TODO: handle exception
            }
        }
        return false;
    }

    // Fin peticiones de clientes -------------------------------------------------------------------------------------

    // Peticiones de server -------------------------------------------------------------------------------------------

    @Override
    public boolean localEntity(String entityName) {
        return this.donations.stream().filter(donation -> entityName.equals(donation.getEntity())).count() > 0;
    }

    // Fin peticiones de server ---------------------------------------------------------------------------------------

}
