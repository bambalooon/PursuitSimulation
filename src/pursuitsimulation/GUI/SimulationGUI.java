package pursuitsimulation.GUI;

import pursuitsimulation.People.Catcher;
import pursuitsimulation.Simulation.SimulationProcess;
import pursuitsimulation.util.Position;
import pursuitsimulation.People.Runner;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    private SimulationProcess process;
    private MainWindow window;

    private BufferedImage mapImage;
    private MapPanel mapPanel;
    private int width = 800;
    private int height = 600;
    private LinkedList<Catcher> catchers;
    private Runner runner;
    public SimulationGUI(SimulationProcess process) {
        this.process = process;

        frame = new JFrame("Pursuit Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        window = new MainWindow();
        frame.add(window);

        frame.pack();
        frame.setVisible(true);

    }
    public void chooseMapFile(String filename) throws IOException {
        mapImage = ImageIO.read(new File(filename));
        mapPanel = new MapPanel(mapImage);

        //JScrollPane scrollPane = new JScrollPane();
        //scrollPane.setViewportView(mapPanel);//new MapPanel(mapImage));
        window.attachMapPanel(mapPanel);

        //scrollPane.setPreferredSize(new Dimension(width, height));

        //frame.add(scrollPane);

        frame.pack();
        frame.setVisible(true);
    }
    public void setCatchersHandle(LinkedList<Catcher> c) {
        catchers = c;
    }
    public void setRunnersHandle(Runner r) {
        runner = r;
    }
    public void setULpos(Position pos) {
        SimulationGUI.pos = pos;
    }
    public void setDRpos(Position pos) {
        mapWidth = pos.getX()-this.pos.getX();
        mapHeight = pos.getY()-this.pos.getY();
    }
    /*
    public void showMap() {
        JLabel map = new JLabel(new ImageIcon(mapImage));
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(map);
        frame.add(scrollPane);
        frame.pack();
        frame.setVisible(true);
    }
    */
    public void showEditedMap() {
        mapPanel.reload();
        mapPanel.repaint();
    }

    private class MainWindow extends JPanel implements ActionListener {
        static final private String PLAY = "play";
        static final private String PAUSE = "pause";
        static final private String STOP = "stop";
        JScrollPane scrollPane;
        MapPanel mapPanel=null;
        public MainWindow() {
            super(new BorderLayout());
            JToolBar toolBar = new JToolBar("Simulation Options Panel");
            addButtons(toolBar);
            this.scrollPane = new JScrollPane();

            add(toolBar, BorderLayout.PAGE_START);
            add(scrollPane, BorderLayout.CENTER);

        }
        protected void addButtons(JToolBar toolBar) {
            JButton button;

            button = new JButton();
            button.addActionListener(this);
            button.setActionCommand(PLAY);
            button.setText("play");
            toolBar.add(button);

            button = new JButton();
            button.addActionListener(this);
            button.setActionCommand(PAUSE);
            button.setText("pause");
            toolBar.add(button);

            button = new JButton();
            button.addActionListener(this);
            button.setActionCommand(STOP);
            button.setText("stop");
            toolBar.add(button);
        }
        public void attachMapPanel(MapPanel mapPanel) {
            this.mapPanel = mapPanel;
            scrollPane.setViewportView(mapPanel);
        }
        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();

            // Handle each button.
            if (PLAY.equals(cmd)) {
                process.simulationStart();
            } else if (STOP.equals(cmd)) {
                process.simulationStop();
            } else if (PAUSE.equals(cmd)) {
                process.simulationStop();
            }
        }

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
            if(runner!=null) {
                Position p = runner.getPos();
                p = convert(p);
                int x = (int) p.getX();
                int y = (int) p.getY();

                g2d.setPaint(SimulationGUI.runnerCol);
                Ellipse2D.Double circle = new Ellipse2D.Double(x, y, SimulationGUI.personCircleDiameter, SimulationGUI.personCircleDiameter);
                g2d.fill(circle);
                g2d.setColor(SimulationGUI.outerCol);
                g2d.drawOval(x, y, SimulationGUI.personCircleDiameter, SimulationGUI.personCircleDiameter);
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
