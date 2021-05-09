import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/* 
Author: Jose Saldaña Mercado
Clase que lanza las distintas replicas del servicio de donaciones de Médicos sin Fronteras
*/

public class MSF {

    static int replicasCount = 1;
    static ArrayList<Server> replicas;

    MSF() {
        replicas = new ArrayList<>();
        System.out.println("Incialización de replicas");
        for (int i = 0; i < replicasCount; i++) {
            Server newReplica = new Server(i, "localhost", 1099);
            newReplica.init();
            replicas.add(newReplica);
        }
    }

    public static void main(String args[]) {
        MSF msf = new MSF();
        try {
            TimeUnit.SECONDS.sleep(2);
            System.out.println("Construcción del anillo");
            for (int i = 0; i < replicasCount; i++) {
                Server replica = replicas.get(i);
                replica.setNextServer((i+1) % replicasCount);
            }
            System.out.println("Anillo establecido");
        } catch (Exception e) {
            //TODO: handle exception
        }
    }
}
