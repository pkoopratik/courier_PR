public class Rider {
    private final String id;
    private final int reliability;
    private final boolean canHandleFragile;
    private boolean online;
    private Package assignedPackage;

    public Rider(String id, int reliability, boolean canHandleFragile) {
        this.id = id;
        this.reliability = reliability;
        this.canHandleFragile = canHandleFragile;
        this.online = true;
    }

    public boolean isOnline() { return online; }
    public void setOnline(boolean online) { this.online = online; }
    public int getReliability() { return reliability; }
    public boolean canHandleFragile() { return canHandleFragile; }
    public Package getAssignedPackage() { return assignedPackage; }
    public void assignPackage(Package pkg) { this.assignedPackage = pkg; }
    public void clearPackage() { this.assignedPackage = null; }
    public String getId() { return id; }

    @Override
    public String toString() {
        return String.format("Rider[%s] %s reliability:%d assigned:%s", id, online ? "Online" : "Offline", reliability,
                assignedPackage == null ? "none" : assignedPackage.getId());
    }
}