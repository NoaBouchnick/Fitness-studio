package gym.management.Sessions;

public enum  SessionType {

    ThaiBoxing(20,100),
    MachinePilates(10,80),
    Pilates(30,60),
    Ninja(5,150);

    private int maxCapacity;
    private int price;

    SessionType(int maxCapacity, int price) {
        this.maxCapacity = maxCapacity;
        this.price = price;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }
    public int getPrice() {
        return price;
    }
}


