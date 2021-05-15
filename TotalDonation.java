import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class TotalDonation implements TotalDonationInterface {
    int totalAmount;
    Registry registry;

    public TotalDonation(String host, int port) {
        this.totalAmount = 0;

        try {
            this.registry = LocateRegistry.getRegistry(host, port);
            String remoteObjectName = "msfTotal";
            TotalDonationInterface msfInterface = this;
            TotalDonationInterface stub = (TotalDonationInterface) UnicastRemoteObject.exportObject(msfInterface, 0);
            this.registry.rebind(remoteObjectName, stub);
        } catch (Exception e) {
            //TODO: handle exception
        }
    }

    // Gestion del total de donaciones ----------------------------------------------------------------------------------------

    @Override
    public void addDonation(int sender, int amount) {
        this.totalAmount += amount;
        System.out.println("MSF recibe " + amount + " de replica " + sender + ", donaciones totales = " + this.totalAmount);
    }

    @Override
    public int totalAmount() {
        return this.totalAmount;
    }

    // Fin Gestion del total de donaciones ------------------------------------------------------------------------------------
    
}
