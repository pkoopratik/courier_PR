import java.util.*;

public class DispatchCenter {
    private final PriorityQueue<Package> packageQueue = new PriorityQueue<>();
    private final Map<String, Rider> riders = new HashMap<>();
    private final Map<String, Package> allPackages = new HashMap<>();
    private final List<String> auditTrail = new ArrayList<>();

    public void processCommand(String commandLine) {
        String[] tokens = commandLine.split(" ");
        String command = tokens[0].toUpperCase();

        switch (command) {
            case "HELP" -> showHelp();
            case "PLACE_ORDER" -> placeOrder(tokens);
            case "ADD_RIDER" -> addRider(tokens);
            case "UPDATE_RIDER" -> updateRider(tokens);
            case "ASSIGN" -> assignPackages();
            case "DELIVER" -> completeDelivery(tokens);
            case "STATUS" -> showStatus();
            case "AUDIT" -> showAuditTrail();
            default -> System.out.println("Unknown command. Type HELP.");
        }
    }

    private void showHelp() {
        System.out.println("Commands:\n" +
                "PLACE_ORDER <packageId> <EXPRESS|STANDARD> <deadlineEpoch> <fragile:true|false>\n" +
                "ADD_RIDER <riderId> <reliabilityRating:0-5> <canHandleFragile:true|false>\n" +
                "UPDATE_RIDER <riderId> <ONLINE|OFFLINE>\n" +
                "ASSIGN\n" +
                "DELIVER <packageId>\n" +
                "STATUS\n" +
                "AUDIT\n" +
                "EXIT");
    }

    private void placeOrder(String[] tokens) {
        String id = tokens[1];
        PackagePriority priority = PackagePriority.valueOf(tokens[2]);
        long deadline = Long.parseLong(tokens[3]);
        boolean fragile = Boolean.parseBoolean(tokens[4]);

        Package pkg = new Package(id, priority, deadline, fragile);
        packageQueue.offer(pkg);
        allPackages.put(id, pkg);
        auditTrail.add("Order Placed: " + pkg);
        System.out.println("Order placed: " + id);
    }

    private void addRider(String[] tokens) {
        String id = tokens[1];
        int reliability = Integer.parseInt(tokens[2]);
        boolean fragile = Boolean.parseBoolean(tokens[3]);
        riders.put(id, new Rider(id, reliability, fragile));
        System.out.println("Rider added: " + id);
    }

    private void updateRider(String[] tokens) {
        Rider r = getRider(tokens[1]);
        r.setOnline(tokens[2].equalsIgnoreCase("ONLINE"));
        System.out.println("Rider status updated: " + r);
    }

    private void assignPackages() {
        for (Package pkg : new ArrayList<>(packageQueue)) {
            Optional<Rider> opt = riders.values().stream()
                    .filter(Rider::isOnline)
                    .filter(r -> r.getAssignedPackage() == null)
                    .filter(r -> !pkg.isFragile() || r.canHandleFragile())
                    .sorted(Comparator.comparingInt(Rider::getReliability).reversed())
                    .findFirst();

            if (opt.isPresent()) {
                Rider rider = opt.get();
                rider.assignPackage(pkg);
                pkg.markAssigned(rider.getId());
                packageQueue.remove(pkg);
                auditTrail.add("Package Assigned: " + pkg.getId() + " -> " + rider.getId());
                System.out.println("Assigned " + pkg.getId() + " to " + rider.getId());
            }
        }
    }

    private void completeDelivery(String[] tokens) {
        Package pkg = allPackages.get(tokens[1]);
        if (pkg == null || pkg.getStatus() != PackageStatus.ASSIGNED)
            throw new IllegalArgumentException("Invalid package status.");

        pkg.markDelivered();
        Rider rider = riders.get(pkg.getAssignedRiderId());
        rider.clearPackage();
        auditTrail.add("Delivered: " + pkg);
        System.out.println("Delivered package: " + pkg.getId());
    }

    private void showStatus() {
        System.out.println("-- Packages --");
        allPackages.values().forEach(System.out::println);
        System.out.println("-- Riders --");
        riders.values().forEach(System.out::println);
    }

    private void showAuditTrail() {
        System.out.println("-- Audit Trail --");
        auditTrail.forEach(System.out::println);
    }

    private Rider getRider(String id) {
        Rider r = riders.get(id);
        if (r == null) throw new IllegalArgumentException("Rider not found: " + id);
        return r;
    }
}