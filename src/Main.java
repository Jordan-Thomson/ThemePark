import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Main method for theme park
 * @author Jordan Thomson - fjb19170
 */
public class Main {

    private static ThemePark tp;
    private static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        tp = new ThemePark();
        while (!tp.isQuit()) {
            int choice = tp.displayMenu();
            action(choice);
        }
        System.exit(0);
    }

    /**
     * Method to direct the program depending on the input choice
     * @param choice the option chosen
     */
    private static void action(int choice) {
        String keepgoing = "R";
        switch (choice) {
            case 1: // selecting a single ride

                tp.chooseRide();
                tp.setRide(getIntInput(1,tp.numRides()));
                tp.setAllRide(false);
                repeatUntilComplete(keepgoing);
                break;
            case 2: // selecting from all rides

                tp.setAllRide(true);
                repeatUntilComplete(keepgoing);
                break;
            case 3: // display the map

                tp.displayParkMap();
                break;
            case 4: // display a personal map

                tp.displayPersonalisedMap();
                break;
            case 5: // suggest a route

                int opt = 0;
                while (opt != 4) {
                    opt = tp.displayRouteMenu();
                    actionTwo(opt);
                }
                break;
            case 6: // quit
                System.out.println("Goodbye..");
                tp.setQuit(true);
                break;
            default:
                System.out.println("Uh oh... look a squirrel");
        }
    }

    /**
     * Switch to handle the menu options of routing
     * @param opt the chosen option.
     */
    private static void actionTwo(int opt) {
        switch (opt) {
            case 1:
                tp.preferredRide(true,false);
                break;
            case 2:
                tp.preferredRide(false,true);
                break;
            case 3:
                tp.preferredRide(false,false);
                break;
            case 4:
                tp.getRoute();
                break;
            default:
                System.out.println("Uh oh... another squirrel");
        }
    }

    /**
     * Keeps going in a circle until user is happy with results
     * @param keepgoing "R" will keep the loop going, "C" will continue on the program.
     */
    private static void repeatUntilComplete(String keepgoing) {
        generatePartyDetails();
        while(keepgoing.equals("R")) {
            recommendationMenu();
            tp.initRecommendations();
            tp.displayDetails();
            keepgoing = getYesNo("R", "C");
            if (keepgoing.equals("C")) {
                if (tp.getParty().getEmail().equals("")) {
                    tp.printDetails();
                } else {
                    tp.emailDetails();
                }
            }
        }
    }

    /**
     * Get input for a recommendation.
     */
    private static void recommendationMenu() {

        for (int i = 0; i < tp.getParty().getSize(); i++) {
            System.out.println("Please use the height chart next to this terminal to measure the height of person number " + (i+1));
            System.out.println();
            System.out.print("What height are you (in metres e.g. 1.40): ");
            tp.setPartyPreference("Height", Double.toString(getDoubleInput(0.5,2.8)),i);
            System.out.print("Are you a Wheelchair user (Y/N)? ");
            tp.setPartyPreference("Wheelchair",getYesNo("Y","N"),i);
            System.out.println("Which of the following types of rides do you like?");
            for (int j = 1; j < 5; j++) {
                System.out.print("\t" + RideType.get(j) + " (Y/N)     :");
                tp.setPartyPreference(RideType.get(j),getYesNo("Y","N"),i);
            }
        }
    }

    /**
     * Get and update party details (name, communication method, party size)
     */
    private static void generatePartyDetails() {
        System.out.print("Please enter your first name: ");
        tp.createParty(getStringInput());
        System.out.println("Hi " + tp.getParty().getName() + " would you prefer me to print your results or email them to you? " +
                "(Enter P to print or E to email : ");
        if (getYesNo("P","E").equals("E")) {
            System.out.println("You have chosen email please enter a valid email address : ");
            while (tp.getParty().getEmail().equals("")) {
                tp.getParty().setEmail(input.nextLine());
                if (tp.getParty().getEmail().equals("")) {
                    System.out.println("Sorry " + tp.getParty().getName() + " that is not a valid email address please try again: ");
                }
            }
            System.out.println("Thank you, your recommendations will be emailed to " + tp.getParty().getEmail());
        }
        System.out.print("How many people are in your party? ");
        tp.getParty().setSize(getIntInput(1,Integer.MAX_VALUE));
    }

    /**
     * helper method for input validation
     * @param a allowed value 1
     * @param b allowed value 2
     * @return either a or b
     */
    private static String getYesNo(String a, String b) {
        String yesNo;
        do {
            yesNo = input.nextLine().trim().toUpperCase();
            if (yesNo.equals(a) || yesNo.equals(b)) {
                return yesNo;
            }
            else {
                System.out.print("Please answer with \"" + a + "\" or \"" + b + "\": ");
            }
        } while (!yesNo.equals(a) && !yesNo.equals(b));
        return yesNo;
    }

    /**
     * Helper method for input validation
     * @param min the lowest number allowed to be entered
     * @param max the maximum number allowed to be entered
     * @return a double between the min and max (inclusive)
     */
    private static double getDoubleInput(double min, double max) {
        double d= min -1;
        do {
            try {
                d = input.nextDouble();
                if (d < min || d > max) {
                    System.out.print("Are you sure you are that height?\nPlease re-enter: ");
                }
            } catch (InputMismatchException ime) {
                System.out.print("Not a valid value, please re-enter: ");
                input.nextLine();
            }
        } while (d < min || d > max);
        input.nextLine();
        return d;
    }

    /**
     * Helper method to get a string
     * @return the string entered into the terminal
     */
    private static String getStringInput() {
        return input.next();
    }

    /**
     * Helper method to get an integer
     * @param min the minimum number allowed
     * @param max the maximum number allowed
     * @return an integer between the min and max (inclusive)
     */
    private static int getIntInput(int min, int max) {
        int c = min -1;
        do {
            try {
                System.out.print(">>>  ");
                c = input.nextInt();
                if (c < min || c > max) {
                    System.out.print("Not a valid value, please re-enter: ");
                }
            } catch (InputMismatchException ime) {
                System.out.print("Not a valid value, please re-enter: ");
                input.nextLine();
            }
        } while (c < min || c > max);
        return c;
    }



}
