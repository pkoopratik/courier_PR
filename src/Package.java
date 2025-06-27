public class Package implements Comparable<Package> {
    private final String id;
    private final PackagePriority priority;
    private final long deadline;
    private final long orderTime;
    private final boolean fragile;
    private PackageStatus status;
    private String assignedRiderId;

    public Package(String id, PackagePriority priority, long deadline, boolean fragile) {
        this.id = id;
        this.priority = priority;
        this.deadline = deadline;
        this.fragile = fragile;
        this.status = PackageStatus.PENDING;
        this.orderTime = System.currentTimeMillis();
    }

    public String getId() { return id; }
    public boolean isFragile() { return fragile; }
    public PackageStatus getStatus() { return status; }
    public String getAssignedRiderId() { return assignedRiderId; }

    public void markAssigned(String riderId) {
        this.status = PackageStatus.ASSIGNED;
        this.assignedRiderId = riderId;
    }

    public void markDelivered() {
        this.status = PackageStatus.DELIVERED;
    }

    @Override
    public int compareTo(Package other) {
        if (this.priority != other.priority) return this.priority == PackagePriority.EXPRESS ? -1 : 1;
        if (this.deadline != other.deadline) return Long.compare(this.deadline, other.deadline);
        return Long.compare(this.orderTime, other.orderTime);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s) deadline:%d rider:%s", id, priority, status, deadline, assignedRiderId);
    }
    public PackagePriority getPriority() {
    return this.priority;
}
}