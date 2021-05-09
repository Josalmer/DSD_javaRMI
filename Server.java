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
    public int register(String entityName) {
        try {
            int replicaId = 0;
            boolean registered = false;
            while (replicaId < this.replicasCount && !registered) {
                registered = this.replicas.get(replicaId).localEntity(entityName);
                if (!registered) {
                    replicaId++;
                }
            }

            if (registered) { // ya esta registrado
                System.out.println(this.id + ": Entidad " + entityName + " ya registrada en replica " + replicaId);
                return replicaId;
            } else { // hay que registrarlo
                int min = 99999;
                int idMin = -1;
                for (int i = 0; i < this.replicasCount; i++) {
                    int entitiesAtReplica = this.replicas.get(i).localEntities();
                    if (entitiesAtReplica < min) {
                        min = entitiesAtReplica;
                        idMin = i;
                    }
                }
                this.replicas.get(idMin).registerEntity(entityName);
                System.out.println(this.id + ": Nuevo registro de Entidad " + entityName + " en replica " + idMin);
                return idMin;
            }
        } catch (Exception e) {
            //TODO: handle exception
        }
        return -1;
    }

    // Fin peticiones de clientes -------------------------------------------------------------------------------------

    // Peticiones de server -------------------------------------------------------------------------------------------

    @Override
    public boolean localEntity(String entityName) {
        return this.entities.stream().filter(entity -> entityName.equals(entity)).count() > 0;
    }

    @Override
    public int localEntities() {
        return this.entities.size();
    }

    @Override
    public void registerEntity(String entityName) {
        this.entities.add(entityName);
    }

    // Fin peticiones de server ---------------------------------------------------------------------------------------

}
