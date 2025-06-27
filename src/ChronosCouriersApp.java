import java.util.Scanner;

public class ChronosCouriersApp {
    public static void main(String[] args) {
        DispatchCenter dispatchCenter = new DispatchCenter();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Chronos Couriers. Type 'HELP' to see available commands.");

        while (true) {
            System.out.print("\n>> ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("EXIT")) break;
            try {
                dispatchCenter.processCommand(input);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}