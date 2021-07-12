import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * Class to draw the graph as a map.
 * @author Jordan - fjb19170
 */
public class DrawGraph extends JPanel {

    private static Graph elg;
    private static Map<Vertex, Integer> minDist;
    private static String desc;
    private static LinkedList<Vertex> path;
    private static String instructions;
    private static Set<Ride> successes;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        setOpaque(true);
        // draw title and key
        g2.drawString(desc,400,25);
        g2.drawOval(50,50,100,50);
        g2.drawString("Attraction Name",60,80);
        g2.drawString("Distance from Entrance",45,50);

        if (path != null) {
            g2.setColor(Color.RED);
            g2.setStroke(new BasicStroke(6));
            int step = 0;
            for (int i = 0; i < path.size(); i++) {
                Ride p;
                Ride r = path.get(i).getRide();
                if (i == 0) {
                    p = elg.getVertex(0).getRide();
                } else {
                    p = path.get(i - 1).getRide();
                }
                g2.drawLine(p.getxPos(), p.getyPos(), r.getxPos(), r.getyPos());
                if (successes.contains(r)) {
                    g2.drawOval(r.getxPos() - (g2.getFontMetrics().stringWidth(r.getName()) / 2), r.getyPos() - 20, g2.getFontMetrics().stringWidth(r.getName()), 40);
                }

            }
            g2.setColor(Color.BLACK);
            int x = 50;
            int y = 650;
            for (String line : instructions.split("\n")) {
                g2.drawString(line,x,y += g.getFontMetrics().getHeight());
            }
        }
        g2.setStroke(new BasicStroke(2));
        // draw the edges
        for(Edge e : elg.getEdges()) {
            g2.drawLine(e.source().getRide().getxPos(), e.source().getRide().getyPos(),
                        e.target().getRide().getxPos(), e.target().getRide().getyPos());
            g2.drawString(Integer.toString(e.weight()), (e.source().getRide().getxPos() + e.target().getRide().getxPos()) / 2,
                    (e.source().getRide().getyPos() + e.target().getRide().getyPos()) / 2);
        }
        // draw the vertices
        for(Vertex v : elg.getVertices()) {
            g2.drawOval(v.getRide().getxPos() - (g2.getFontMetrics().stringWidth(v.getRide().getName()) / 2), v.getRide().getyPos() - 20,
                    g2.getFontMetrics().stringWidth(v.getRide().getName()), 40);
            g2.setColor((v.getRide().getTheme().equals("Jurassic") ? Color.GREEN :
                    (v.getRide().getTheme().equals("Futuristic") ? Color.CYAN :
                    (v.getRide().getTheme().equals("Medieval") ? Color.ORANGE :
                    (v.getRide().getTheme().equals("Industrial") ? Color.PINK : Color.GRAY)))));
            g2.fillOval(v.getRide().getxPos() - (g2.getFontMetrics().stringWidth(v.getRide().getName()) / 2), v.getRide().getyPos() - 20,
                    g2.getFontMetrics().stringWidth(v.getRide().getName()), 40);
            g2.setColor(Color.BLACK);
            g2.drawString(v.getRide().getName(), v.getRide().getxPos() - (g2.getFontMetrics().stringWidth(v.getRide().getName()) / 2),
                    v.getRide().getyPos());
            String min = String.valueOf(minDist.get(v));
            g2.drawString(min,v.getRide().getxPos() - (g2.getFontMetrics().stringWidth(min) / 2),v.getRide().getyPos()-25);
        }
    }

    /**
     * Method to disply a graph
     * @param graph The graph to be displayed
     * @param minDist A Map of vertices/Integer of distance from source Vertex
     * @param frame the frame to be updated
     * @param title A title for the frame
     */
    public static void drawGraph(Graph graph, Map<Vertex, Integer> minDist, JFrame frame, String title) {
        DrawGraph.desc = title;
        DrawGraph.elg = graph;
        DrawGraph.minDist = minDist;
        DrawGraph.path = null;
        DrawGraph.instructions = "";
        DrawGraph.successes = null;
        frame.add(new DrawGraph());
        frame.setSize(900, 800);
        frame.setVisible(true);
    }

    /**
     * Overload method to handle route
     * @param graph The graph to be displayed
     * @param minDist A Map of vertices/Integer of distance from source Vertex
     * @param frame the frame to be updated
     * @param title A title for the frame
     * @param path A path for the route as a LinkedList of vertices
     * @param instructions String of directions
     */
    public static void drawGraph(Graph graph, Map<Vertex, Integer> minDist, JFrame frame, String title, LinkedList<Vertex> path, Set<Ride> successes, String instructions) {
        DrawGraph.desc = title;
        DrawGraph.elg = graph;
        DrawGraph.minDist = minDist;
        DrawGraph.path = path;
        DrawGraph.instructions = instructions;
        DrawGraph.successes = successes;
        frame.add(new DrawGraph());
        frame.setSize(900, 1400);
        frame.setVisible(true);
    }


    /**
     * Prints the JPanel component to print dialogue
     * @param component component to be printed
     */
    public static void printImage(Component component) {
        BufferedImage image = new BufferedImage(component.getWidth(), component.getHeight(), BufferedImage.TYPE_INT_RGB);
        // paints into image's Graphics
        component.paint(image.getGraphics());
        PrinterJob pj = PrinterJob.getPrinterJob();
        pj.setPrintable(new Printable() {
            public int print(Graphics pg, PageFormat pf, int pageNum)
            {
                if (pageNum > 0) {
                    return Printable.NO_SUCH_PAGE;
                }
                Graphics2D g3 = (Graphics2D) pg;
                g3.translate(pf.getImageableX(),pf.getImageableY());
                g3.scale(pf.getImageableWidth()/component.getWidth(), pf.getImageableHeight()/component.getHeight());
                g3.translate(pf.getImageableX(), pf.getImageableY());
                component.paint(g3);
                return Printable.PAGE_EXISTS;
            }
        });

        if (pj.printDialog()) {
            try {
                pj.print();
            } catch (PrinterException prt) {
                prt.printStackTrace();
            }
        }
    }

    /**
     * Save the component so that it may be emailed (if that is ever added)
     * @param component the frame to turn into an image.
     */
    public static void getImageForEmail(Component component) {
        BufferedImage image = new BufferedImage(component.getWidth(), component.getHeight(), BufferedImage.TYPE_INT_RGB);
        // paints into image's Graphics
        component.paint(image.getGraphics());
        try {
            ImageIO.write(image, "png", new File("image.jpg"));
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
