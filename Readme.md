How to Run

Steps:

cd path/to/ChronosCouriers
javac *.java
java ChronosCouriersApp

*******************************************
Supported Commands

1. PLACE_ORDER <packageId> <EXPRESS|STANDARD> <deadlineEpoch> <fragile:true|false>

Adds a new delivery order.

PLACE_ORDER pkg1 EXPRESS 1720000000000 true

2. ADD_RIDER <riderId> <reliability:0-5> <canHandleFragile:true|false>

Adds a rider to the pool.

ADD_RIDER rider1 5 true

3. UPDATE_RIDER <riderId> <ONLINE|OFFLINE>

Updates the rider's availability.

UPDATE_RIDER rider1 OFFLINE

4. ASSIGN

Assigns pending packages to available riders based on defined logic.

ASSIGN

5. DELIVER <packageId>

Marks the package as delivered.

DELIVER pkg1

6. STATUS

Shows current system state.

STATUS

7. AUDIT

Shows log of actions like assignments and deliveries.

AUDIT

8. HELP

Lists all available commands.

HELP

9. EXIT

Exits the program.

EXIT
*******************************************


Sample Flow

ADD_RIDER rider1 5 true
PLACE_ORDER pkg1 EXPRESS 1720000000000 true
ASSIGN
STATUS
DELIVER pkg1
AUDIT

*******************************************
