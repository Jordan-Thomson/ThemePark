import javax.swing.*;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A class to represent functionality in recommending a themepark ride at a themepark
 * @author Jordan - fjb19170
 */

public class ThemePark {

    private static final String PARKNAME = "ThemePark";
    private Party party;
    private boolean quit;
    private Ride ride;
    private HashMap<Ride,DecisionTree<Data>> forrestMap;
    private HashMap<Ride, Integer> successes;
    private HashMap<Ride, FailData> failures;
    private ArrayList<Ride> rides;
    private boolean allRides;
    private Graph parkMap;
    private JFrame displayFrame = new JFrame();
    private Map<Vertex,Integer> minMap;
    private final int WAIT_WEIGHT = 10;                   // used in calculation of routing
    private final int RUN_WEIGHT = 5;                     // used in calculation of routing
    /* the average human walking speed at crosswalks is about 5.0 kilometres per hour (km/h),
       or about 1.4 meters per second (m/s) - https://en.wikipedia.org/wiki/Walking
     */
    private final double SPEED = 1.4;                     // used to calculate time traveling

    /**
     * Constructor to set-up the themepark
     */
    public ThemePark() {
        quit = false;
        setupRides();
        plantForrest();
        generateParkMap();
    }

    /**
     * Creates a graph of the park and pre-generates the minimum distance from entrance to all rides
     * so that it doesn't need to calculate it everytime the graph is displaued.
     */
    private void generateParkMap() {
        HashMap<String, Vertex> vertices = new HashMap<>();
        vertices.put("Entrance", new Vertex(0,new Ride("Entrance",0,0,0,0,
                null,null,null,null,null,"",450,80,0,0)));
        int i = 0;
        for (Ride r : rides) {
            Vertex v = new Vertex(++i,r);
            vertices.put(r.getName(),v);
        }
        parkMap = new Graph(false);
        parkMap.addEdge(vertices.get("Entrance"),vertices.get("The Descent"),253);
        parkMap.addEdge(vertices.get("Entrance"),vertices.get("Hatchling Nest"),156);
        parkMap.addEdge(vertices.get("Entrance"),vertices.get("Rex Rampage"),159);
        parkMap.addEdge(vertices.get("Entrance"),vertices.get("Crisis at Farpoint"),262);
        parkMap.addEdge(vertices.get("Crisis at Farpoint"),vertices.get("SauroPods"),213);
        parkMap.addEdge(vertices.get("Crisis at Farpoint"),vertices.get("Build a Bot"),130);
        parkMap.addEdge(vertices.get("Crisis at Farpoint"),vertices.get("Robot Conflicts"),108);
        parkMap.addEdge(vertices.get("Robot Conflicts"),vertices.get("Build a Bot"),114);
        parkMap.addEdge(vertices.get("Build a Bot"),vertices.get("Laser Lock"),130);
        parkMap.addEdge(vertices.get("Build a Bot"),vertices.get("Trench Chase"),134);
        parkMap.addEdge(vertices.get("Trench Chase"),vertices.get("Laser Lock"),139);
        parkMap.addEdge(vertices.get("Laser Lock"),vertices.get("SauroPods"),172);
        parkMap.addEdge(vertices.get("Laser Lock"),vertices.get("Mystic Fortunes"),127);
        parkMap.addEdge(vertices.get("Mystic Fortunes"),vertices.get("Tower of Terror"),153);
        parkMap.addEdge(vertices.get("Mystic Fortunes"),vertices.get("Cloister of Cruelty"),135);
        parkMap.addEdge(vertices.get("Cloister of Cruelty"),vertices.get("Tower of Terror"),142);
        parkMap.addEdge(vertices.get("Cloister of Cruelty"),vertices.get("Pony Jousts"),153);
        parkMap.addEdge(vertices.get("Pony Jousts"),vertices.get("The Iron Jaws"),124);
        parkMap.addEdge(vertices.get("The Iron Jaws"),vertices.get("Steampunk Cups"),122);
        parkMap.addEdge(vertices.get("The Iron Jaws"),vertices.get("Tower of Terror"),178);
        parkMap.addEdge(vertices.get("Tower of Terror"),vertices.get("Raptor Races"),130);
        parkMap.addEdge(vertices.get("Tower of Terror"),vertices.get("Steampunk Cups"),260);
        parkMap.addEdge(vertices.get("Steampunk Cups"),vertices.get("Hall O Mirrors"),130);
        parkMap.addEdge(vertices.get("Hall O Mirrors"),vertices.get("High Noon"),120);
        parkMap.addEdge(vertices.get("High Noon"),vertices.get("The Descent"),130);
        parkMap.addEdge(vertices.get("The Descent"),vertices.get("The Pain Train"),130);
        parkMap.addEdge(vertices.get("The Pain Train"),vertices.get("Steampunk Cups"),130);
        parkMap.addEdge(vertices.get("The Pain Train"),vertices.get("Hatchling Nest"),186);
        parkMap.addEdge(vertices.get("The Pain Train"),vertices.get("Pet a Saur"),153);
        parkMap.addEdge(vertices.get("Pet a Saur"),vertices.get("Raptor Races"),109);
        parkMap.addEdge(vertices.get("Raptor Races"),vertices.get("SauroPods"),114);
        parkMap.addEdge(vertices.get("SauroPods"),vertices.get("Rex Rampage"),106);
        parkMap.addEdge(vertices.get("Rex Rampage"),vertices.get("Hatchling Nest"),143);
        parkMap.addEdge(vertices.get("Hatchling Nest"), vertices.get("Pet a Saur"),102);
        DijkstraAlgorithm da = new DijkstraAlgorithm(parkMap);
        da.execute(parkMap.getVertex(0));
        minMap = da.getDistances();
    }

    /**
     * Method to display the park map as output to terminal and frame in a JPanel, continues to either
     * print or TODO email
     */
    public void displayParkMap() {
        for (String theme : minMap.keySet().stream().map(r -> r.getRide().getTheme()).distinct().collect(Collectors.toList())) {
            if (!theme.equals("")) {
                System.out.println(theme + ":\n");
                System.out.println("Ride name\t\t\t Distance from Park Entrance (metres)");
                for (Vertex vert : minMap.keySet().stream().filter(r -> r.getRide().getTheme().equals(theme)).collect(Collectors.toList())) {
                    System.out.println(vert.getRide().getName() + (vert.getRide().getName().length() < 12 ? "\t\t\t " :
                            (vert.getRide().getName().length() < 16 ? "\t\t " : "\t ")) + minMap.get(vert));
                }
                System.out.println();
            }
        }

        updateGraph(parkMap,minMap,"Map of Park",null,null);

        if (party != null) {
            if (!party.getEmail().equals("")) {
                DrawGraph.getImageForEmail(displayFrame);
                System.out.println("\nThe map has been e-mailed to " + getParty().getEmail());
                // TODO email the image
            } else {
                System.out.println("\nPlease follow the printing instructions");
                DrawGraph.printImage(displayFrame);
            }
        }
    }

    /**
     * Create a collection of all the trees by the ride that represents the tree.
     */
    private void plantForrest() {
        forrestMap = new HashMap<>();
        for (Ride r : rides) {
            DecisionTree<Data> t;
            t = setupDecisionTree(r);
            forrestMap.put(r,t);
        }
    }

    /**
     * The main menu
     * @return choice made
     */
    public int displayMenu() {
        System.out.println("\nWelcome to " + PARKNAME + "\n");

        // use menu style used in SQL group project
        Menu mainMenu = new Menu("Choose an option", new String[] {"\t 1. Get your recommendation for a ride of your choice",
                "\t 2. Get recommendation for the entire park",
                "\t 3. Generate a map of the park",
                "\t 4. Generate a personalised map",
                "\t 5. Recommend a route around the park",
                "\t 6. Quit"});
        return mainMenu.choose();
    }

    /**
     * Menu to get preferences for travelling around the park.
     * @return choice made
     */
    public int displayRouteMenu() {
        // if nothing to path around continue on and advise
        if (successes == null || successes.isEmpty()) {
            return 4;
        }
        String[] options;
        if (successes.size() == 1) {
            options = new String[] {"\t 1. Visit " + successes.keySet().iterator().next().getName() +  " more than once",
                    "\t 2. Continue to get the recommended route\n"};
        }
        else {
            options = new String[] {"\t 1. Set a preferred first ride",
                    "\t 2. Set a preferred last ride",
                    "\t 3. Visit a ride more than once",
                    "\t 4. Continue to get the recommended route\n"};
        }
        Menu menu = new Menu("Would you like to set any of the following:", options);
        return (successes.size() > 1 ? menu.choose() : (menu.choose() == 2 ? 4 : 1));
    }

    /**
     * Load/create all rides from a csv file
     */
    public void setupRides() {
        try {
            List<String[]> collect = Files.lines(Paths.get("data.csv"))
                    .map(line -> line.split(","))
                    .collect(Collectors.toList());
            // remove the headers
            collect.remove(0);
            rides = new ArrayList<>();
            for (String[] strArray : collect) {
                Ride r = new Ride(strArray[10],Double.parseDouble(strArray[0]),Double.parseDouble(strArray[1])
                        ,Integer.parseInt(strArray[3]),Integer.parseInt(strArray[4]),strArray[2],strArray[7]
                        ,strArray[6],strArray[8],strArray[9],strArray[5],Integer.parseInt(strArray[11]),
                        Integer.parseInt(strArray[12]), Integer.parseInt(strArray[13]), Integer.parseInt(strArray[14]));
                rides.add(r);
            }
            ride = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Clears previous recommendations so a new party can get their recommendations
     */
    public void initRecommendations() {
        successes = new HashMap<>();
        failures = new HashMap<>();
    }

    /**
     * Sets up a single ride
     */
    private void setupRide() {
        ride = new Ride("The Pain Train",1.0,999,2,8,"Y","Y","Y","N","Y","Industrial",0,0,0,0);
    }

    /**
     * Generate a tree for a ride
     * @param ride The ride to crate a decision tree for
     * @return DecisionTree of the ride
     */
    private DecisionTree<Data> setupDecisionTree(Ride ride) {
        DecisionTree<Data> t = new DecisionTree();
        Node<Data> n = null;
        for (int i = 0; i <5; i++) {
            if (t.getSize() == 0) {
                if (ride.getWheelchair().equals("N")) {
                    n = t.addRoot(new Data(RideType.get(i)));
                } else {
                    i++;
                    n = t.addRoot(new Data(RideType.get(i)));
                }
            } else {
                if (ride.getSpecificType(i-1).equals("Y")) {
                    t.addLeft(n, new Data("Doesn't want " + RideType.get(i-1) + " attractions"));
                    n = t.addRight(n,new Data(RideType.get(i)));

                } else {
                    t.addRight(n, new Data("Wants " + RideType.get(i-1) + " attractions"));
                    n = t.addLeft(n,new Data(RideType.get(i)));

                }
            }
        }
        if (ride.getSpecificType(4).equals("Y")) {
            t.addLeft(n, new Data("Doesn't want " + RideType.get(4) + " attractions"));
            n = t.addRight(n,new Data(ride.getMinHeight(),ride.getMaxHeight()));
        } else {
            t.addRight(n, new Data("Wants + " + RideType.get(4) + " attractions"));
            n = t.addLeft(n,new Data(ride.getMinHeight(),ride.getMaxHeight()));
        }
        t.addLeft(n,new Data("Doesn't meet height requirements"));
        n = t.addRight(n,new Data(ride.getMinGroup(),ride.getMaxGroup()));
        t.addLeft(n,new Data("Doesn't meet group requirements"));
        t.addRight(n,new Data(ride));
        return t;
    }

    /**
     * Uses the list of Rides and calls a traversal for each ride.
     */
    private void traversAllTrees() {
        System.out.println("\nGenerating recommendation...\n");
        for (Ride ride : rides) {
            traverseTree(ride);

        }
    }

    /**
     * Traverse the decision tree - checking against each person in the party,
     * adds fail reasons and successful traversals to HashMaps for later analysis
     */
    private void traverseTree(Ride ride) {
        DecisionTree<Data> tree = forrestMap.get(ride);
        //System.out.println("\nGenerating recommendation...\n");
        for (int j = 0; j < party.getSize(); j++) {
            Node<Data> n = tree.root();
            // do until we reach a leaf node (either a ride, or a fail point)
            while (!n.isLeaf()) {
                Data d = n.getElement();
                // if the node is a yes/no node (Wheelchair, Horror, Adrenaline, Kids, Water)
                if (d.getType() == DataType.YESNO) {
                    if (party.getPreference(d.getQuestion(),j).equals("Y") && (!n.getRight().isLeaf()
                            || n.getElement().getQuestion().equals("Wheelchair"))) {
                        n = n.getRight();
                    } else {
                        n = n.getLeft();
                    }
                }
                // if the node is a height node
                else if (d.getType() == DataType.HEIGHT) {
                    if (d.isTrue(Double.parseDouble(party.getPreference(d.getQuestion(), j)))) {
                        n = n.getRight();
                    } else {
                        n = n.getLeft();
                    }
                } else if (d.getType() == DataType.GROUP) {
                    if (d.isTrue(party.getSize())) {
                        n = n.getRight();
                    } else {
                        n = n.getLeft();
                    }
                }
            }
            // if the leaf node reached is a failure node
            if (n.getElement().getType() == DataType.YESNO) {
                if (failures.containsKey(ride)) {
                    failures.get(ride).addFailure(n.getElement().getQuestion());
                }
                else {
                    FailData fd = new FailData();
                    fd.addFailure(n.getElement().getQuestion());
                    failures.put(ride,fd);
                }
            }
            // if the leaf node reached is a Ride.
            else {
                if (successes.containsKey(n.getElement().getRide())) {
                    successes.put(n.getElement().getRide(), successes.get(n.getElement().getRide()) + 1);
                } else {
                    successes.put(n.getElement().getRide(), 1);
                }
            }
        }
    }

    /**
     * Boolean to stop signify quiting the program
     * @param b set to false to end
     */
    public void setQuit(boolean b) {
        quit = b;
    }

    /**
     * Boolean to check if the program should end
     * @return boolean value representing state
     */
    public boolean isQuit() {
        return quit;
    }

    /**
     * Create a new party
     * @param name String name of the party head
     */
    public void createParty(String name) {
        party = new Party(name);
    }

    /**
     * Returns the party object
     * @return Party object
     */
    public Party getParty() {
        return party;
    }

    /**
     * Sets a party preference
     * @param type String of the type of preference (Height, Horror, Adrenaline, Kids, Water)
     * @param value The value to be used as the preference
     * @param person The person in the party to be updated
     */
    public void setPartyPreference(String type, String value, int person) {
        party.setPreference(type,value,person);
    }

    /**
     * Builds a string of recommendation details of a single ride to be used in output
     * @return String with recommendation details.
     */
    private String getDetails() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        StringBuilder output = new StringBuilder("Details for " + party.getName() + "'s party at ThemePark\n");
        output.append("Recommendations for ").append(party.getName()).append("'s party created on ").append(dtf.format(now)).append("\n");
        output.append("Based on your inputs the ride ").append(ride.getName()).append(" is ");
        if (failures.size() ==0 || failures.get(ride).getFailures().values().stream().reduce(0, Integer::sum) < (double)party.getSize()/2) {
            output.append("suitable for your party because ");
            for (Map.Entry<Ride, Integer> e: successes.entrySet()) {
                output.append(e.getValue()).append(" people match the\n criteria for ").append(e.getKey().getName());
            }
        } else {
            output.append("not suitable for your party because:\n");
            for(Map.Entry<String, Integer> e : failures.get(ride).getFailures().entrySet()) {
                output.append(e.getValue()).append(" out of ").append(party.getSize()).append(" in your party ").append(e.getKey()).append("\n");
            }
        }
        return output.toString();
    }

    /**
     * Builds a string of recommendation details for all rides to be used in output
     * @return String with recommendation details/
     */
    private String getAllDetails() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        StringBuilder output = new StringBuilder("Details for " + party.getName() + "'s party at ThemePark\n");
        output.append("Recommendations for ").append(party.getName()).append("'s party created on ").append(dtf.format(now)).append("\n");
        List<String> themes = rides.stream().map(r -> r.getTheme()).collect(Collectors.toList());
        Map<String, List<Ride>> ridesByTheme = new HashMap<>();
        for (String theme : themes) {
            ridesByTheme.put(theme,rides.stream().filter(r -> r.getTheme().equals(theme)).collect(Collectors.toList()));
        }
        // tidy up the successes and failures
        List<Ride> removeSuccesses = new ArrayList<>();
        List<Ride> removeFailures = new ArrayList<>();
        // Check each ride in the list of successes to see if it has more failures, collect to later be removed
        for (Ride ride: rides) {
            if (successes.containsKey(ride) && failures.containsKey(ride) && successes.get(ride) < failures.get(ride).getTotalFails()) {
                removeSuccesses.add(ride);
            }
            else if (successes.containsKey(ride) && failures.containsKey(ride) && successes.get(ride) > failures.get(ride).getTotalFails()) {
                removeFailures.add(ride);
            }
        }
        // remove so each hashmap should not have duplication of rides.
        successes.keySet().removeAll(removeSuccesses);
        failures.keySet().removeAll(removeFailures);

        // the actual output
        for (String theme : ridesByTheme.keySet()) {
            output.append(theme).append(":\n");

             // Calculate if there are any rides to show
            if (successes.keySet().stream().anyMatch(r -> r.getTheme().equals(theme))) {
                for (Ride r : ridesByTheme.get(theme)) {
                    if (successes.containsKey(r)) {
                        output.append("\t").append(r.getName()).append("\n");
                    }
                }
            } else {
                output.append("Based on your preferences there are no suitable rides in this zone because:\n");
                HashMap<String, Integer> failMap = new HashMap<>();
                for (Ride r : ridesByTheme.get(theme)) {
                    if (failures.containsKey(r)) {
                            for (String s : failures.get(r).getFailures().keySet()) {
                                if (!failMap.containsKey(s)) {
                                    failMap.put(s,failures.get(r).getFailures().get(s));
                                }
                                else {
                                    if (failMap.get(s) < failures.get(r).getFailures().get(s)) {
                                        failMap.put(s,failures.get(r).getFailures().get(s));
                                    }
                                }
                            }

                    }
                }
            // if there were no rides in this zone recommended, output reasons why
            for (String s : failMap.keySet()) {
                    if (s.contains("group")) {
                        output.append("The party doesn't meet group requirements\n");
                    } else {
                        if (failMap.get(s) > party.getSize()/2) {
                            output.append(failMap.get(s)).append("/").append(party.getSize()).append(" people in the party ").append(s).append("\n");
                        }
                    }
                }
            }
        }
        return output.toString();
    }

    /**
     * Method to use java default print interface to print the details to a printer
     */
    public void printDetails() {
        System.out.println("Thank you " + party.getName() + ". Please follow the printing instructions: ");
        String output = allRides ? getAllDetails() : getDetails();
        JTextArea printArea = new JTextArea();
        printArea.append(output);
        try {
            printArea.print();
            System.out.println("Goodbye.\n\n\n");
        } catch (PrinterException e) {
            System.out.println("Error printing");
            System.out.println(e.getMessage());
        }
    }

    /**
     * Displays details of recommendations to terminal after traversing a tree
     */
    public void displayDetails() {
        if (allRides) {
            traversAllTrees();
            System.out.println(getAllDetails());
        } else {
            traverseTree(ride);
            System.out.println(getDetails());
        }
        System.out.print("Do you want to revise your preferences or continue to print/email? (Enter R to revise or " +
                "C to continue): ");
    }

    /**
     * Method to pretend e-mail details.
     */
    public void emailDetails() {
        // TODO actually e-mail
        System.out.println("Thank you " + party.getName() + " your recommendations have been emailed to " +
                "" + party.getEmail() + ", I hope you enjoy your day at "+ PARKNAME + "\n");
        System.out.println("Goodbye.\n\n\n");
    }

    /**
     * returns a list of successful traversals through the rides.
     * @return HashMap of Rides and Integers, the integers representing the number of times a successful traversal has been
     * met.
     */
    public HashMap<Ride,Integer> getSuccesses() {
        return successes;
    }

    /**
     * returns a hashmap of failures for a single ride
     * @param ride the ride to get the list of failures from
     * @return HashMap<String, Integer> of failure reasons and number of times fail was encountered.
     */
    public HashMap<String,Integer> getFailsForRide(Ride ride) {
        return failures.get(ride).getFailures();
    }

    /**
     * Sets field for use in other methods to determine if process is for all rides or a single ride
     * @param allRides boolean, true for all rides, false for one ride.
     */
    public void setAllRide(boolean allRides) {
        this.allRides = allRides;
    }

    /**
     * sets the single ride to The Pain Train so original tests still work
     * rather than using the random ride.
     */
    protected void setTestRide() {
        this.ride = rides.get(18);
    }

    /**
     * get single ride (used in tests)
     * @return the ride set as the single ride.
     */
    public Ride getSingleRide() {
        return ride;
    }

    /**
     * Generates and displays a graph based on group preferences
     */
    public void displayPersonalisedMap() {
        if (successes == null || successes.isEmpty()) {
            System.out.println("No preferences entered for park");
            return;
        }

        // Create list of vertices to remove
        List<Vertex> removeVertex = new ArrayList<>(parkMap.getVertices());
        removeVertex.removeIf(v -> successes.containsKey(v.getRide()) || v.getRide().getName().equals("Entrance"));
        // remove them
        Graph personalMap;
        personalMap = parkMap.subgraphWithoutVertices(removeVertex);
        // because removing the vertices/edges will likely have a graph with disjointed vertices
        // create edges from each vertex being kept as a shortest path edge
        DijkstraAlgorithm da = new DijkstraAlgorithm(parkMap);
        for(Vertex v : personalMap.getVertices()) {
            da.execute(v);
            Map<Vertex, Integer> minDists = da.getDistances();
            for (Vertex w : personalMap.getVertices()) {
                if (v != w && personalMap.hasEdge(v,w) == null && personalMap.hasEdge(w,v) == null) {
                    personalMap.addEdge(v,w,minDists.get(w));
                }
            }
        }
        // now make it a Minimum span tree for reasons unbeknown to me
        List<Edge> prim = PrimJarnik.primJarnik(personalMap);
        Graph mst = new Graph(false);
        for (Edge e : prim) {
            mst.addEdge(e.source(),e.target(),e.weight());
        }
        // draw it
        updateGraph(mst,minMap,party.getName()+"'s Personal Map",null,null);
        // output it as text as per example
        System.out.println("Hi " + party.getName() + ", here is your personalised map of the Park");
        for (String theme : mst.getVertices().stream().map(r -> r.getRide().getTheme()).distinct().collect(Collectors.toList())) {
            if (!theme.equals("")) {
                System.out.println(theme + ":\n");
                System.out.println("Ride name\t\t\t Distance from Park Entrance (metres)");
                for (Vertex vert : mst.getVertices().stream().filter(r -> r.getRide().getTheme().equals(theme)).collect(Collectors.toList())) {
                    System.out.println(vert.getRide().getName() + (vert.getRide().getName().length() < 12 ? "\t\t\t " :
                            (vert.getRide().getName().length() < 16 ? "\t\t " : "\t ")) + minMap.get(vert));
                }
                System.out.println();
            }
        }
        System.out.println();
        if (!party.getEmail().equals("")) {
            DrawGraph.getImageForEmail(displayFrame);
            System.out.println("\nThe map has been e-mailed to " + getParty().getEmail());
            // TODO Email the image
        } else {
            System.out.println("\nPlease follow the printing instructions");
            //DrawGraph.printComponent(displayFrame);
            DrawGraph.printImage(displayFrame);
        }
    }

    /**
     * Method to allow the user to choose a single ride
     */
    public void chooseRide() {
        System.out.println("Please choose a ride: ");
        for (int i = 0; i < rides.size(); i++) {
            System.out.println((i+1) + ": " + rides.get(i).getName());
        }
        System.out.println("\nEnter ride number: ");
    }

    /**
     * Method to assign preferences on a route round the park
     * If both parameter values are false, assumes adding to repeat visit rides
     * Both parameter values should not be set to true
     * @param first set to true if the first ride is being set
     * @param last set to true if the last ride is being set
     */
    public void preferredRide(boolean first, boolean last) {
        if (first && last) {
            return;
        }
        List<Ride> rides = new ArrayList<>(successes.keySet());
        // if a ride has been set as the last ride and isn't set for repeat visits remove it
        if (first && !party.getRepeatRides().contains(party.getLastRide())) {
            rides.remove(party.getLastRide());
        }
        // if a ride has been set as the first ride and isn't set for repeat trips remove it
        if (last && !party.getRepeatRides().contains(party.getFirstRide())) {
            rides.remove(party.getFirstRide());
        }
        String[] rideArray = new String[rides.size()];
        for (int i = 0; i < rides.size(); i++) {
            rideArray[i] = (i+1) + ": " + rides.get(i).getName();
        }
        if (successes.size() > 1) {
            Menu m = new Menu("Please choose a Ride: ", rideArray);
            if (first) {
                party.setFirstRide(rides.get(m.choose() - 1));
            } else if (last) {
                party.setLastRide(rides.get(m.choose() - 1));
            } else {
                party.addRepeatRide(rides.get(m.choose() - 1));
            }
        } else {
            party.addRepeatRide(successes.keySet().stream().iterator().next());
        }
    }

    /**
     * Sets the single ride
     * if the paramter is out of bounds, does nothing.
     * @param i the index of the ride to set
     */
    public void setRide(int i) {
        if (i > 0 && i <= rides.size()) {
            ride = rides.get(i - 1);
        }
    }

    /**
     * returns the number of rides
     * @return integer number of rides.
     */
    public int numRides() {
        return rides.size();
    }

    /**
     * Starts getting a personal route around the park
     */
    public void getRoute() {
        if (successes == null || successes.size() == 0) {
            System.out.println("No rides to visit, please enter your preferences\n");
            return;
        }
        DijkstraAlgorithm da = new DijkstraAlgorithm(parkMap);
        LinkedList<Vertex> path = new LinkedList<>();
        ArrayList<Ride> include = new ArrayList<>(successes.keySet());
        // if a ride wants to be visited more than once
        if (party.getRepeatRides()!= null && !party.getRepeatRides().isEmpty()) {
            include.addAll(party.getRepeatRides());
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Your personal route around the park: \n");
        HashMap<Integer,Vertex> targets = new HashMap<>();
        LocalTime lt = LocalTime.of(9,0); // IRL would use LocalTime.now()
        getPath(da,parkMap.getVertex(0),include, path, sb,0,lt);
        System.out.println(sb.toString());

        updateGraph(parkMap,minMap,party.getName()+"'s Personal Route",path,sb.toString());

        if (party.getEmail().equals("")) {
            DrawGraph.printImage(displayFrame);
        }
        else {
            emailRoute(sb);
        }
    }

    private void emailRoute(StringBuilder sb) {
        DrawGraph.getImageForEmail(displayFrame);
        System.out.println("Your route has been e-mailed to " + party.getEmail());
        // TODO more emailing
    }

    /**
     * Method to recursively collect the path
     * @param da DijkstraAlgorithm to use
     * @param v Vertex used as the starting point
     * @param include List of rides that are to be included
     * @param path path to be updated
     * @param sb String builder to collate output
     * @param step which step in the path we are in.
     */
    public void getPath(DijkstraAlgorithm da, Vertex v, ArrayList<Ride> include, LinkedList<Vertex> path, StringBuilder sb, int step, LocalTime time) {
        if (!include.isEmpty()) {
            step++;
            da.execute(v);
            Map<Vertex, Integer> shortest = da.getDistances();
            int closest = Integer.MAX_VALUE;
            Vertex target = null;
            // if a specified first ride is set
            if (step == 1 && party.getFirstRide() != null) {
                target = parkMap.getVertex(party.getFirstRide());
                closest = shortest.get(target);
            }
            else {
                for (Vertex w : shortest.keySet()) {
                    if ((shortest.get(w) + (w.getRide().getWait()* WAIT_WEIGHT) + (w.getRide().getWait()* RUN_WEIGHT)
                            < closest && include.contains(w.getRide()) && party.getLastRide() != w.getRide())
                        || (shortest.get(w) + (w.getRide().getWait()* WAIT_WEIGHT) + (w.getRide().getWait()* RUN_WEIGHT) <
                            closest && Collections.frequency(include,w.getRide()) > 1)
                        || (w.getRide() == party.getLastRide() && include.size() == 1 && include.contains(w.getRide()))) {
                        closest = shortest.get(w);
                        target = w;
                    }
                }
            }
            assert target != null;
            // calculate the time taken to get to the next ride
            time = time.plusMinutes((long) Math.ceil(closest/SPEED/60));
            include.remove(target.getRide());
            LinkedList<Vertex> trip = da.getPath(target);
            // herein follows a collation for output
            sb.append("Step: ").append(step).append("\n");
            if (trip == null) {
                sb.append("Stay at: ").append(target);
            } else {
                sb.append("Head to ").append(target.getRide().getName());
                if (trip.size() > 1) {
                    sb.append(" Via: ");
                }
                for (int i = 0; i < trip.size() - 1; i++) {
                    sb.append(trip.get(i));
                    if (i + 1 < trip.size() - 1) {
                        sb.append(", ");
                    }
                }
            }
            String timeString = "" + time.getHour() + ":" + (time.getMinute() < 10 ? "0" + time.getMinute() : time.getMinute());
            sb.append("\n").append("Distance: ").append(shortest.get(target)).append("m")
                    .append("\t  Arrive at: ").append(timeString)
                    .append("\t  Queue time: ").append(target.getRide().getWait()).append(" mins ")
                    .append("\t  Ride time: ").append(target.getRide().getRideTime()).append(" mins ").append("\n");
            // collect this path and call the next
            if (trip == null) {
                path.add(target);
            } else {
                path.addAll(trip);
            }
            // add the time spent at this ride
            time = time.plusMinutes(target.getRide().getRideTime()+target.getRide().getWait());
            getPath(da, target, include,path,sb,step,time);
        }
    }

    /**
     * Method to use InvokeAndWait from Swing to assist in displaying frame correctly
     * code borrowed and amended from
     * https://www.pascal-man.com/navigation/faq-java-browser/java-concurrent/ThreadsAndSwing.pdf
     * @param graph The Graph being drawn
     * @param minMap Map from PrimJarnik Algorithm of distances
     * @param title Title to use in the frame
     * @param path Djikstra Algorithm path if required, null if not
     * @param instruct Instructions generated from using the Djikstra Algorithm, "" if not required
     */
    private void updateGraph(Graph graph, Map<Vertex,Integer> minMap, String title, LinkedList<Vertex> path, String instruct) {
        try {

            Runnable drawGraphRun = new Runnable() {
                public void run() {

                    displayFrame.setVisible(false);
                    displayFrame.dispose();
                    displayFrame = new JFrame();
                    if (path == null) {
                        DrawGraph.drawGraph(graph,minMap,displayFrame,title);
                    } else {
                        DrawGraph.drawGraph(graph, minMap, displayFrame, title, path, successes.keySet(), instruct);
                    }
                }
            };

            SwingUtilities.invokeAndWait(drawGraphRun);
            displayFrame.resize(displayFrame.getWidth()+1,displayFrame.getHeight()+1);

        } catch (InterruptedException ie) {
            System.out.println("interrupted while waiting on invokeAndWait()");
        } catch (InvocationTargetException ite) {
            System.out.println("exception thrown from run()");
            System.err.println(ite.getMessage());
            System.out.println(ite.getMessage());
            ite.printStackTrace();
        }
    }

}
