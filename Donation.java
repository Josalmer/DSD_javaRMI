public class Donation {
    String entityName;
    int amount;

    Donation(String entityName) {
        this.entityName = entityName;
        this.amount = 0;
    }

    public String getEntity() {
        return this.entityName;
    }

    public int getDonated() {
        return this.amount;
    }

    public void addDonation(int amount) {
        this.amount += amount;
    }
}
