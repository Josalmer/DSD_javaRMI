import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

/* 
Author: Jose Saldaña Mercado
Clase que lanza las distintas replicas del servicio de donaciones de Médicos sin Fronteras
*/

public class MSF {

    static int replicasCount;
    static ArrayList<Server> replicas;
    int port = 1099;
    Registry registry;

    MSF() {
        try {
            this.registry = LocateRegistry.createRegistry(this.port);
            TotalDonation msfTotal = new TotalDonation("localhost", this.port);
            System.out.println("Inicialización del acumulador de donaciones");
            replicas = new ArrayList<>();
            System.out.println("Inicialización de " + replicasCount + " replicas");
            for (int i = 0; i < replicasCount; i++) {
                Server newReplica = new Server(i, replicasCount, "localhost", this.port);
                newReplica.init();
                replicas.add(newReplica);
            }
        } catch (Exception e) {
            //TODO: handle exception
        }
    }

    public static void main(String args[]) {
        if (args.length != 1) {
            System.out.println("Ejecución MSF <numero_de_replicas>");
            return;
        }
        replicasCount = Integer.parseInt(args[0]);
        MSF msf = new MSF();
        try {
            TimeUnit.SECONDS.sleep(1);
            System.out.println("Construcción del anillo");
            for (int i = 0; i < replicasCount; i++) {
                Server replica = replicas.get(i);
                replica.setReplicas();
            }
            System.out.println("Anillo establecido");
            TimeUnit.SECONDS.sleep(1);
            System.out.println("MSF comienza a circular el token, se lo manda a replica 0");
            replicas.get(0).passToken();
        } catch (Exception e) {
            //TODO: handle exception
            System.out.println("Error register MSF: " + e);
        }
    }
}
