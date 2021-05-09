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

    public static void main(String args[]) {
        Random rand = new Random();
        int replicasCount = 1;
        int myReplicaId = rand.nextInt(replicasCount - 0);
        String entityName = "";

        System.out.println("Bienvenido a Médicos Sin Fronteras.\nSistema de donaciones.\n");
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            Registry registry = LocateRegistry.getRegistry();
            ClientInterface myReplica = (ClientInterface) registry.lookup(Integer.toString(myReplicaId));
            Scanner in = new Scanner(System.in);
            while (true) {
                String action = userMenu(in, entityName);
                switch (action) {
                    case "r":
                        String newEntityName;
                        System.out.println("Introduzca el nombre de la entidad que quiere registrar: ");
                        newEntityName = in.nextLine();
                        if (myReplica.register(newEntityName)) {
                            System.out.println(newEntityName + " registrada correctamente\n");
                        }
                        else
                            System.out.println("Error: La entidad ya estaba registrada\n");
                        break;
                    case "d":
                        break;
                    default:
                        System.out.println("No se ejecuta acción.\n");
                }
            }
        } catch (Exception e) {
            System.err.println("Client exception: ");
            e.printStackTrace();
        }
    }

    public static String userMenu(Scanner in, String entityName) {
        ArrayList<String> allowedActions = new ArrayList<>();
        
        if (entityName != "") {
            System.out.println("Usted esta registrado con la entidad " + entityName + "\n");
            System.out.println("Indique que acción quiere realizar:\nd - realizar Donación\n");
            allowedActions.add("d");
        } else {
            System.out.println("Usted no esta esta registrado\n");
            System.out.println("Indique que acción quiere realizar:\nr - Registrar entidad\n");
            allowedActions.add("r");
        }
        String action = "";
        if (in.hasNext()) {
            action = in.nextLine();
        }
        if (allowedActions.contains(action)) {
            return action;
        } else {
            System.out.println("Acción elegida no permitida o no existe.\n");
            return "";
        }
    }
}
