package monitoring;

import contracts.ICapacity;
import contracts.IStation;

public class MStation implements IStation{

    private int status = 0;
    private final ICapacity capacityMonitor;

    private MStation(int capacity) {
        this.capacityMonitor = new MCapacity(capacity);
    }

    public static IStation getInstance(int capacity) {
        return new MStation(capacity); // Retorna a interface IStation
    }

    @Override
    public boolean checkCapacity() {
        return capacityMonitor.canEnter();
    }

    @Override
    public void enterStation() {
        capacityMonitor.enter();
    }

    @Override
    public void leaveStation() {
        capacityMonitor.leave();
    }

    @Override
    public void close() {
        this.status = 1;
    }

    @Override
    public int getStatus() {
        return this.status;
    }

}