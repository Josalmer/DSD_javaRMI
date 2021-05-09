import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/* 
Author: Jose Saldaña Mercado
Clase que lanza las distintas replicas del servicio de donaciones de Médicos sin Fronteras
*/

public class MSF {

    static int replicasCount;
    static ArrayList<Server> replicas;

    MSF() {
        replicas = new ArrayList<>();
        System.out.println("Inicialización de " + replicasCount + " replicas");
        for (int i = 0; i < replicasCount; i++) {
            Server newReplica = new Server(i, replicasCount, "localhost", 1099);
            newReplica.init();
            replicas.add(newReplica);
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
            TimeUnit.SECONDS.sleep(2);
            System.out.println("Construcción del anillo");
            for (int i = 0; i < replicasCount; i++) {
                Server replica = replicas.get(i);
                replica.setReplicas();
            }
            System.out.println("Anillo establecido");
        } catch (Exception e) {
            //TODO: handle exception
        }
    }
}
