import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DispatchCenterTest {
    private DispatchCenter dispatchCenter;

    @BeforeEach
    void setup() {
        dispatchCenter = new DispatchCenter();
    }

    @Test
    void testAddAndUpdateRider() {
        dispatchCenter.processCommand("ADD_RIDER rider1 5 true");
        dispatchCenter.processCommand("UPDATE_RIDER rider1 OFFLINE");

        Map<String, Rider> riderMap = dispatchCenter.getRiders();
        assertTrue(riderMap.containsKey("rider1"));
        assertFalse(riderMap.get("rider1").isOnline());
    }

    @Test
    void testPlaceExpressAndStandardOrder() {
        dispatchCenter.processCommand("PLACE_ORDER pkg1 EXPRESS 1720000000000 false");
        dispatchCenter.processCommand("PLACE_ORDER pkg2 STANDARD 1730000000000 false");

        Map<String, Package> packageMap = dispatchCenter.getAllPackages();
        assertEquals(2, packageMap.size());
        assertEquals(PackagePriority.EXPRESS, packageMap.get("pkg1").getPriority());
        assertEquals(PackagePriority.STANDARD, packageMap.get("pkg2").getPriority());
    }

    @Test
    void testAssignExpressOverStandard() {
        dispatchCenter.processCommand("ADD_RIDER rider1 4 true");
        dispatchCenter.processCommand("PLACE_ORDER pkg1 STANDARD 1730000000000 false");
        dispatchCenter.processCommand("PLACE_ORDER pkg2 EXPRESS 1720000000000 false");
        dispatchCenter.processCommand("ASSIGN");

        Package pkg2 = dispatchCenter.getAllPackages().get("pkg2");
        assertEquals(PackageStatus.ASSIGNED, pkg2.getStatus());
        assertEquals("rider1", pkg2.getAssignedRiderId());
    }

    @Test
    void testFragileAssignmentRequiresCapableRider() {
        dispatchCenter.processCommand("ADD_RIDER rider1 5 false");
        dispatchCenter.processCommand("PLACE_ORDER pkg1 EXPRESS 1720000000000 true");
        dispatchCenter.processCommand("ASSIGN");

        Package pkg = dispatchCenter.getAllPackages().get("pkg1");
        assertEquals(PackageStatus.PENDING, pkg.getStatus());
        assertNull(pkg.getAssignedRiderId());
    }

    @Test
    void testDeliveryFlow() {
        dispatchCenter.processCommand("ADD_RIDER rider1 5 true");
        dispatchCenter.processCommand("PLACE_ORDER pkg1 EXPRESS 1720000000000 false");
        dispatchCenter.processCommand("ASSIGN");
        dispatchCenter.processCommand("DELIVER pkg1");

        Package pkg = dispatchCenter.getAllPackages().get("pkg1");
        Rider rider = dispatchCenter.getRiders().get("rider1");
        assertEquals(PackageStatus.DELIVERED, pkg.getStatus());
        assertNull(rider.getAssignedPackage());
    }

    @Test
    void testAuditTrailCapturesEvents() {
        dispatchCenter.processCommand("ADD_RIDER rider1 5 true");
        dispatchCenter.processCommand("PLACE_ORDER pkg1 EXPRESS 1720000000000 false");
        dispatchCenter.processCommand("ASSIGN");
        dispatchCenter.processCommand("DELIVER pkg1");

        assertTrue(dispatchCenter.getAuditTrail().size() >= 3); // Placed, Assigned, Delivered
    }
}
