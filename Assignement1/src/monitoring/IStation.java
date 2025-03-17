package monitoring;

public class IStation {

    private static IStation instance;
    private int status = 0;
    private final MCapacity capacityMonitor;

    private IStation(int capacity) {
        this.capacityMonitor = new MCapacity(capacity);
    }

    public boolean checkCapacity() {
        return capacityMonitor.canEnter();
    }

    public void enterStation() {
        capacityMonitor.enter();
        System.out.println("Voters inside: " + capacityMonitor.getVotersInside());
    }

    public void leaveStation() {
        capacityMonitor.leave();
    }

    public void close() {
        this.status = 1;
    }

    public int getStatus() {
        return this.status;
    }

    public static IStation getInstance(int capacity) {
        if (instance == null) {
            System.out.println("Station: Creating new station with capacity " + capacity);
            instance = new IStation(capacity);
        }
        return instance;
    }
}