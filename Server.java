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
    TotalDonationInterface msfTotal;
    Registry registry;
    int pendingAmount;
    boolean haveToken;

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

        try {
            this.registry = LocateRegistry.getRegistry(host, port);
        } catch (Exception e) {
            //TODO: handle exception
        }
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
        this.pendingAmount = 0;
        this.haveToken = false;
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String remoteObjectName = Integer.toString(this.id);
            ServerInterface replica = this;
            ServerInterface stub = (ServerInterface) UnicastRemoteObject.exportObject(replica, 0);
            this.registry.rebind(remoteObjectName, stub);
            this.msfTotal = (TotalDonationInterface) this.registry.lookup("msfTotal");
            System.out.println("Replica " + this.id + " ready");
        } catch (Exception e) {
            //TODO: handle exception
            System.out.println("Error initializing replica: " + e);
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

    @Override
    public void donate(String entityName, int amount) {
        Donation don = null;
        Donation prov = null;
        int i = 0;
        while (don == null) {
            prov = this.donations.get(i);
            if (entityName.equals(prov.getEntity())) {
                don = prov;
            } else {
                i++;
            }
        }
        don.addDonation(amount);
        this.pendingAmount += amount;
        System.out.println(this.id + ": Recibo " + amount + " € de " + entityName);
        if (this.haveToken) {
            try {
                this.msfTotal.addDonation(this.id, this.pendingAmount);
                System.out.println(this.id + ": Actualizo donación total, sumo: " + this.pendingAmount);
                this.pendingAmount = 0;
            } catch (Exception e) {
                //TODO: handle exception
            }
        }
    }

    @Override
    public int totalAmount() {
        int total = 0;
        try {
            total = this.pendingAmount + this.msfTotal.totalAmount();
        } catch (Exception e) {
            //TODO: handle exception
        }
        return total;
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
        Donation nueva = new Donation(entityName);
        this.donations.add(nueva);
    }

    @Override
    public void passToken() {
        try {
            this.haveToken = true;
            if (this.pendingAmount > 0) {
                this.msfTotal.addDonation(this.id, this.pendingAmount);
                System.out.println(this.id + ": Actualizo donación total, sumo: " + this.pendingAmount);
                this.pendingAmount = 0;
            }
            Thread.sleep(2000);
            int nextReplica = (this.id + 1) % this.replicasCount;
            System.out.println(this.id + ": Paso el token a replica " + nextReplica);
            this.haveToken = false;
            this.replicas.get(nextReplica).passToken();
        } catch (Exception e) {
            //TODO: handle exception
        }
    }

    // Fin peticiones de server ---------------------------------------------------------------------------------------

}
