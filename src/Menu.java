import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Class to handle menu options and correct selection of menu options
 *
 * @author Jordan - fjb19170
 */
public class Menu {

    private final String title;
    private final String[] options;
    private final Scanner scanner;
    private int choice;

    /**
     * Default constructor
     * @param title Title of the menu
     * @param options String Array of menu options
     */
    public Menu(String title, String[] options) {
        this.title = title;
        this.options = options;
        choice = -1;
        scanner = new Scanner(System.in);
    }

    /**
     * receive input from the user for menu option selection
     * @return integer value of chosen option
     */
    private int getInput() {
        int c= -1;
        do {
            System.out.print("Choice: ");
            try {
                c = scanner.nextInt();
                if (c < 1 || c > getNumOptions()) {
                    System.out.println("Not in range");
                    System.out.print("Please try a number from 1 to: " + getNumOptions());
                }
            } catch (InputMismatchException e) {
                System.out.print("Must be a number: ");
                scanner.next();
            }
        } while (isValidOption(c));
        choice = c;
        return choice;
    }

    /**
     * Returns true if the user chose a valid option
     * @param choice the users menu option choice
     * @return boolean indicating valid choice
     */
    private boolean isValidOption(int choice) {
        return (choice < 1 || choice > options.length);
    }

    /**
     * Display menu title and options
     */
    private void display() {
        System.out.println(title);
        for (String option : options) {
            System.out.println(option);
        }
    }

    /**
     * Displays menu, and validates input returning option chosen
     * @return Option chosen
     */
    public int choose() {
        do {
            display();
            choice = getInput();
        } while (isValidOption(choice));
        return choice;
    }

    public int getNumOptions() {
        return options.length;
    }

}
