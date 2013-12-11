package pursuitsimulation.GUI;

import pursuitsimulation.People.Catcher;
import pursuitsimulation.util.Position;
import pursuitsimulation.People.Runner;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: BamBalooon
 * Date: 10.12.13
 * Time: 13:13
 * To change this template use File | Settings | File Templates.
 */
public class SimulationGUI {
    private static int personCircleDiameter = 10;
    private static Color runnerCol = Color.RED;
    private static Color catcherCol = Color.BLUE;
    private static Color outerCol = Color.BLACK;

    private static Position pos;
    private static double mapWidth;  //can be negative!
    private static double mapHeight;

    private boolean running=false;
    private JFrame frame;
    private BufferedImage mapImage;
    private MapPanel mapPanel;
    private int width = 800;
    private int height = 600;
    private LinkedList<Catcher> catchers;
    private LinkedList<Runner> runners;
    public SimulationGUI() {
        frame = new JFrame("Pursuit Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.pack();
        frame.setVisible(true);

    }
    public void chooseMapFile(String filename) throws IOException {
        mapImage = ImageIO.read(new File(filename));
        mapPanel = new MapPanel(mapImage);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(mapPanel);//new MapPanel(mapImage));
        scrollPane.setPreferredSize(new Dimension(width, height));
        frame.add(scrollPane);
        frame.pack();
        frame.setVisible(true);
    }
    public void setCatchersHandle(LinkedList<Catcher> c) {
        catchers = c;
    }
    public void setRunnersHandle(LinkedList<Runner> r) {
        runners = r;
    }
    public void setULpos(Position pos) {
        SimulationGUI.pos = pos;
    }
    public void setDRpos(Position pos) {
        mapWidth = pos.getX()-this.pos.getX();
        mapHeight = pos.getY()-this.pos.getY();
    }
    public void showMap() {
        JLabel map = new JLabel(new ImageIcon(mapImage));
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(map);
        frame.add(scrollPane);
        frame.pack();
        frame.setVisible(true);
    }
    public void showEditedMap() {
        mapPanel.reload();
        mapPanel.revalidate();
        mapPanel.repaint();
        frame.pack();
        frame.setVisible(true);
    }

    private class MapPanel extends JPanel {
        private BufferedImage image;
        private BufferedImage editedImage;

        public MapPanel(BufferedImage img) {
            image = img;
            this.setPreferredSize(new Dimension(
                    image.getWidth(), image.getHeight()));
            editedImage = process(image);
        }
        public void reload() {
            editedImage = process(image);
        }

        private Position convert(Position pos) {
            int w = editedImage.getWidth();
            int h = editedImage.getHeight();
            int x = (int) ((pos.getX()-SimulationGUI.pos.getX())*w/SimulationGUI.mapWidth);
            int y = (int) ((pos.getY()-SimulationGUI.pos.getY())*h/SimulationGUI.mapHeight);
            return new Position(x,y);
        }

        private BufferedImage process(BufferedImage old) {
            int w = old.getWidth();
            int h = old.getHeight();
            BufferedImage img = new BufferedImage(
                    w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = img.createGraphics();
            g2d.drawImage(old, 0, 0, null);
            if(catchers!=null) {
                for(Catcher c : catchers) {
                    Position p = c.getPos();
                    p = convert(p);
                    int x = (int) p.getX();
                    int y = (int) p.getY();

                    g2d.setPaint(SimulationGUI.catcherCol);
                    Ellipse2D.Double circle = new Ellipse2D.Double(x, y, SimulationGUI.personCircleDiameter, SimulationGUI.personCircleDiameter);
                    g2d.fill(circle);
                    g2d.setColor(SimulationGUI.outerCol);
                    g2d.drawOval(x, y, SimulationGUI.personCircleDiameter, SimulationGUI.personCircleDiameter);
                }
            }
            if(runners!=null) {
                for(Runner r : runners) {
                    Position p = r.getPos();
                    p = convert(p);
                    int x = (int) p.getX();
                    int y = (int) p.getY();

                    g2d.setPaint(SimulationGUI.runnerCol);
                    Ellipse2D.Double circle = new Ellipse2D.Double(x, y, SimulationGUI.personCircleDiameter, SimulationGUI.personCircleDiameter);
                    g2d.fill(circle);
                    g2d.setColor(SimulationGUI.outerCol);
                    g2d.drawOval(x, y, SimulationGUI.personCircleDiameter, SimulationGUI.personCircleDiameter);
                }
            }
            g2d.dispose();
            return img;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(editedImage, 0, 0, null);
        }
    }
}
