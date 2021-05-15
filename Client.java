import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;

/* 
Author: Jose Saldaña Mercado
Clase que implementa los métodos que ejecutaran el cliente
El cliente es el encargado de almacenar el estado del usuario, si se encuentra registrado con una entidad o no
*/

public class Client {

    int myReplicaId;
    Registry registry;
    ClientInterface myReplica;
    String entityName;
    Scanner in;

    Client() {
        this.entityName = "";
        this.in = new Scanner(System.in);
    }

    public static void main(String args[]) {
        if (args.length != 1) {
            System.out.println("Ejecución Client <numero_de_replicas>");
            return;
        }
        int replicasCount = Integer.parseInt(args[0]);
        Client client = new Client();

        System.out.println("Bienvenido a Médicos Sin Fronteras. << Sistema de donaciones >>\n");
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            client.registry = LocateRegistry.getRegistry("localhost", 1099);
    
            Random rand = new Random();
            client.setReplica(client, rand.nextInt(replicasCount - 0));
            System.out.println("Le atiende la replica: " + client.myReplicaId);

            String action = "";
            while (action != "x") {
                action = userMenu(client.in, client.entityName);
                switch (action) {
                    case "r":
                        client.registerEntity(client);
                        break;
                    case "d":
                        break;
                    case "l":
                        client.entityName = "";
                        break;
                    case "x":
                        System.out.println("Gracias por confiar en Médicos Sin Fronteras, vuelva pronto.");
                        return;
                    default:
                        System.out.println("No se ejecuta acción.");
                }
            }
        } catch (Exception e) {
            System.err.println("Client exception: ");
            e.printStackTrace();
        }
    }

    public static String userMenu(Scanner in, String entityName) {
        ArrayList<String> allowedActions = new ArrayList<>();
        allowedActions.add("x");
        
        if (entityName != "") {
            System.out.println("\nUsted esta registrado con la entidad " + entityName);
            System.out.println("Indique que acción quiere realizar:\nd - realizar (D)onación\nl - (L)ogout de entidad: " + entityName + "\nx - e(X)it");
            allowedActions.add("d");
            allowedActions.add("l");
        } else {
            System.out.println("\nUsted no esta esta registrado");
            System.out.println("Indique que acción quiere realizar:\nr - (R)egistrar entidad\nx - e(X)it");
            allowedActions.add("r");
        }
        String action = "";
        if (in.hasNext()) {
            action = in.nextLine();
        }
        if (allowedActions.contains(action)) {
            return action;
        } else {
            System.out.println("Acción elegida no permitida o no existe.");
            return "";
        }
    }

    void setReplica(Client client, int nuevaReplicaId) {
        try {
            client.myReplicaId = nuevaReplicaId;
            client.myReplica = (ClientInterface) client.registry.lookup(Integer.toString(nuevaReplicaId));
        } catch (Exception e) {
            //TODO: handle exception
        }
    }

    void registerEntity(Client client) {
        try {
            System.out.println("Introduzca el nombre de la entidad que quiere registrar: ");
            String newEntityName = client.in.nextLine();
            int replicaAsignada = client.myReplica.register(newEntityName);
            client.entityName = newEntityName;
            System.out.println(client.entityName + " registrada correctamente");
            if (replicaAsignada != client.myReplicaId) {
                client.setReplica(client, replicaAsignada);
                System.out.println("Ahora le atiende la replica " + replicaAsignada);
            }
        } catch (Exception e) {
            //TODO: handle exception
        }
    }
}
