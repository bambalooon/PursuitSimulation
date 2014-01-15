package pursuitsimulation.GUI;

import pursuitsimulation.People.Catcher;
import pursuitsimulation.Simulation.SimulationProcess;
import pursuitsimulation.Strategies.CatchingStrategy;
import pursuitsimulation.Strategies.RunningStrategy;
import pursuitsimulation.Strategies.StandardCatchingStrategy;
import pursuitsimulation.Strategies.StandardRunningStrategy;
import pursuitsimulation.util.Position;
import pursuitsimulation.People.Runner;
import pursuitsimulation.util.Time;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Vector;

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

    private static int selectedCatchingStrategyIndex = 0;
    private static int selectedRunningStrategyIndex = 0;

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
        window.attachMapPanel(mapPanel);

        frame.pack();
        frame.setVisible(true);
    }
    public void setCatchersHandle(LinkedList<Catcher> c) {
        catchers = c;
    }
    public void setRunnerHandle(Runner r) {
        runner = r;
    }
    public void setULpos(Position pos) {
        SimulationGUI.pos = pos;
    }
    public void setDRpos(Position pos) {
        mapWidth = pos.getX()-this.pos.getX();
        mapHeight = pos.getY()-this.pos.getY();
    }
    public void showEditedMap() {
        mapPanel.reload();
        mapPanel.repaint();
    }
    private void simulationStart() {
        System.out.println(CatchingStrategy.catchingStrategies[0].getSimpleName());


        for(int i=0; i<SimulationProcess.catchersNumber; i++) {
            try {
                CatchingStrategy s = (CatchingStrategy)
                                CatchingStrategy.catchingStrategies[SimulationGUI.selectedCatchingStrategyIndex]
                                        .getConstructor(SimulationProcess.class).newInstance(process);
                process.addCatcher(s, "Catcher #"+(i+1));
            } catch(Exception e) { System.out.println("Error while creating new object from class"); }
//            process.addCatcher(new StandardCatchingStrategy(process), "Catcher #"+(i+1));
        }
        try {
            RunningStrategy s = (RunningStrategy)
                    RunningStrategy.runningStrategies[SimulationGUI.selectedRunningStrategyIndex]
                            .getConstructor(SimulationProcess.class).newInstance(process);
            process.setRunner(s, "Runner");
        } catch(Exception e) { System.out.println("Error while creating new object from class"); }
//        process.setRunner(new StandardRunningStrategy(process), "Runner");
        setCatchersHandle(process.getCatchers());
        setRunnerHandle(process.getRunner());
    }
    private void simulationStop() {
        process.reset();
        setRunnerHandle(null);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                showEditedMap();
            }
        });
    }

    public void showEndAlert() {
        JOptionPane.showMessageDialog(null, "Złapano Uciekiniera! Koniec symulacji...");
    }
    private class MainWindow extends JPanel implements ActionListener, ChangeListener {
        static final private String PLAY = "play";
        static final private String PAUSE = "pause";
        static final private String STOP = "stop";
        static final private String CATCHING_STRATEGY = "CatchingStrategy";
        static final private String RUNNING_STRATEGY = "RunningStrategy";
        static final int INTERVAL_MIN = 0;
        static final int INTERVAL_MAX = 2000;
        static final int INTERVAL_INIT = 500;
        static final int SPINNER_STEP = 1;

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

            JSlider intervalSlider = new JSlider(JSlider.HORIZONTAL,INTERVAL_MIN, INTERVAL_MAX, INTERVAL_INIT);
            intervalSlider.addChangeListener(this);
            intervalSlider.setMajorTickSpacing(100);
            intervalSlider.setPaintTicks(true);
            intervalSlider.setMaximumSize(new Dimension(200, 50));

            Hashtable labelTable = new Hashtable();
            labelTable.put( new Integer( Time.minInterval ), new JLabel(Integer.toString(Time.minInterval)) );
            labelTable.put( new Integer( INTERVAL_INIT ), new JLabel(Integer.toString(INTERVAL_INIT)) );
            labelTable.put( new Integer( INTERVAL_MAX ), new JLabel(Integer.toString(INTERVAL_MAX)) );
            intervalSlider.setLabelTable( labelTable );
            intervalSlider.setPaintLabels(true);

            toolBar.add(intervalSlider);

            Integer value = new Integer(50);
            Integer min = new Integer(0);
            Integer max = new Integer(100);
            Integer step = new Integer(1);
            SpinnerNumberModel model = new SpinnerNumberModel( new Integer(SimulationProcess.INIT_CATCHERS),
                    new Integer(SimulationProcess.MIN_CATCHERS),
                    new Integer(SimulationProcess.MAX_CATCHERS),
                    new Integer(SPINNER_STEP));

            JLabel label = new JLabel();
            label.setText(" Liczba poszukujących: ");
            toolBar.add(label);
            JSpinner spinner = new JSpinner(model);
            spinner.setMaximumSize(new Dimension(40,30));
            spinner.addChangeListener(this);
            toolBar.add(spinner);

            Vector<String> cStrategies = new Vector<String>();
            for(Class strategy : CatchingStrategy.catchingStrategies) {
                cStrategies.add(strategy.getSimpleName());
            }
            JComboBox cStrategiesCombo = new JComboBox(cStrategies);
            cStrategiesCombo.setSelectedIndex(0);
            cStrategiesCombo.setMaximumSize(new Dimension(200, 30));
            cStrategiesCombo.addActionListener(this); //to do
            cStrategiesCombo.setActionCommand(CATCHING_STRATEGY);
            toolBar.add(cStrategiesCombo);

            Vector<String> rStrategies = new Vector<String>();
            for(Class strategy : RunningStrategy.runningStrategies) {
                rStrategies.add(strategy.getSimpleName());
            }
            JComboBox rStrategiesCombo = new JComboBox(rStrategies);
            rStrategiesCombo.setSelectedIndex(0);
            rStrategiesCombo.setMaximumSize(new Dimension(200, 30));
            rStrategiesCombo.addActionListener(this); //to do
            rStrategiesCombo.setActionCommand(RUNNING_STRATEGY);
            toolBar.add(rStrategiesCombo);

        }
        public void attachMapPanel(MapPanel mapPanel) {
            this.mapPanel = mapPanel;
            scrollPane.setViewportView(mapPanel);
        }
        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();

            // Handle each button.
            if (PLAY.equals(cmd)) {
                if(!process.isRunning())
                    simulationStart();
                process.simulationStart();
            } else if (STOP.equals(cmd)) {
                process.simulationStop();
                simulationStop();
            } else if (PAUSE.equals(cmd)) {
                process.simulationStop();
            } else if (CATCHING_STRATEGY.equals(cmd)) {
                JComboBox box = (JComboBox) e.getSource();
                SimulationGUI.selectedCatchingStrategyIndex = box.getSelectedIndex();
            } else if (RUNNING_STRATEGY.equals(cmd)) {
                JComboBox box = (JComboBox) e.getSource();
                SimulationGUI.selectedRunningStrategyIndex = box.getSelectedIndex();
            }

        }
        public void stateChanged(ChangeEvent e) {
            if(e.getSource().getClass()==JSlider.class) {
                JSlider source = (JSlider)e.getSource();
                if (!source.getValueIsAdjusting()) {
                    int interval = (int)source.getValue();
                    if(interval<Time.minInterval) {
                        interval = Time.minInterval;
                    }
                    Time.changeInterval(interval);
                    process.updateTimer();
                }
            }
            else if(e.getSource().getClass()==JSpinner.class) {
                JSpinner source = (JSpinner)e.getSource();
                int val = (Integer) source.getValue();
                process.changeCatchersNumber(val);
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
