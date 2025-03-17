package monitoring;

public class IClerk {

    private static IClerk instance;
    private final MVoteLimit voteLimitMonitor;
    private final IStation station = IStation.getInstance(0);

    private IClerk(int votingLimit) {
        this.voteLimitMonitor = new MVoteLimit(votingLimit);
    }

    public boolean validate(int id) {
        boolean isValid = voteLimitMonitor.validateAndAdd(id);
        if (isValid) {
            station.close();
        }
        return isValid;
    }

    public static IClerk getInstance(int votingLimit){
        if (instance == null) {
            instance = new IClerk(votingLimit);
        }
        return instance;
    }
}