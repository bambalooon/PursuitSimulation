package pursuitsimulation.GUI;

import pursuitsimulation.Clue;
import pursuitsimulation.Crossing;
import pursuitsimulation.Exceptions.NoGuiException;
import pursuitsimulation.Exceptions.NoPlayerException;
import pursuitsimulation.People.Catcher;
import pursuitsimulation.Simulation.SimulationProcess;
import pursuitsimulation.Simulation.SimulationProgram;
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
import java.awt.event.*;
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
    private static int runnerCircleDiameter = 15;
    private static int circleDiameter = 20;
    private static Color runnerCol = Color.RED;
    private static Color catcherCol = Color.BLUE;
    private static Color localClueCol = new Color(70, 70, 70, 90);
    private static Color globalClueCol = new Color(255, 0, 0, 90);
    private static Color destCol = Color.ORANGE;

    private static double ZOOM_MAX = 1.6;
    private static double ZOOM_MIN = 1.0;
    private static double ZOOM_STEP = 0.1;

    private static Position pos;
    private static double mapWidth;  //can be negative!
    private static double mapHeight;

    private static int selectedCatchingStrategyIndex = 0;
    private static int selectedRunningStrategyIndex = 0;

    private JFrame frame;

    private SimulationProcess process;
    private MainWindow window;
    private SimulationPlayer player;

    private BufferedImage mapImage;
    private MapPanel mapPanel;
    private int width = 800;
    private int height = 600;

    private LinkedList<Clue> localClues=new LinkedList<Clue>();

    public SimulationGUI(SimulationProcess process) {
        this.process = process;

        frame = new JFrame("Pursuit Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

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
    public void setCatchersCrossings(LinkedList<Crossing> c) {
        mapPanel.catchersCrossings = c;
    }
    public void setRunnerCrossing(Crossing c) {
        mapPanel.runnerCrossing = c;
        Clue cl = c.getClue();
        if((cl!=null) && !localClues.contains(cl)) {
            localClues.add(cl);
        }
    }
    public void setRunnerDestination(Crossing c) {
        mapPanel.runnerDestination = c;
    }
    public void setGlobalClue(Clue c) {
        mapPanel.globalClue = c;
    }
    public void resetLocalClues() {
        localClues.clear();
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
    private void simulationStart() throws NoGuiException {
        process = new SimulationProcess(process);
        SimulationProgram.process = process;
        for(int i=0; i<SimulationProcess.catchersNumber; i++) {
            try {
                CatchingStrategy s = (CatchingStrategy)
                                CatchingStrategy.catchingStrategies[SimulationGUI.selectedCatchingStrategyIndex]
                                        .getConstructor(SimulationProcess.class).newInstance(process);
                process.addCatcher(s, "Catcher #"+(i+1));
            } catch(Exception e) { System.out.println("Error while creating new object from class"); }
        }
        try {
            RunningStrategy s = (RunningStrategy)
                    RunningStrategy.runningStrategies[SimulationGUI.selectedRunningStrategyIndex]
                            .getConstructor(SimulationProcess.class).newInstance(process);
            process.setRunner(s, "Runner");
        } catch(Exception e) { System.out.println("Error while creating new object from class"); }
        player = new SimulationPlayer(this);
        player.attachProcess(process);
        process.simulationStart();
        process.start();
    }
    private void simulationStop() {
        process.simulationStop();
    }
    public void simulationEnd() {
        window.simStartBtn.setVisible(true);
        window.simStopBtn.setVisible(false);
    }
    public void playingEnd() {
        window.playBtn.setVisible(true);
        window.pauseBtn.setVisible(false);
    }
    public void showEndAlert() {
        JOptionPane.showMessageDialog(null, "Złapano Uciekiniera! Koniec symulacji...");
    }
    private class MainWindow extends JPanel implements ActionListener, ChangeListener {
        static final private String SIM_START = "sim_start";
        static final private String SIM_STOP = "sim_stop";
        static final private String PLAY = "play";
        static final private String PAUSE = "pause";
        static final private String STOP = "stop";
        static final private String CATCHING_STRATEGY = "CatchingStrategy";
        static final private String RUNNING_STRATEGY = "RunningStrategy";
        static final private String TIME_INTERVAL = "time_interval";
        static final private String LCP = "LCP";
        static final private String GCP = "GCP";

        private JPanel mainPanel;
        private MapPanel mapPanel=null;
        private JButton simStartBtn;
        private JButton simStopBtn;
        private JButton playBtn;
        private JButton pauseBtn;
        public MainWindow() {
            super(new BorderLayout());
            JToolBar toolBar = new JToolBar("Simulation Options Panel");
            addSimButtons(toolBar);
            JToolBar playBar = new JToolBar("Play Options Panel");
            addPlayButtons(playBar);
            mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            MyMouseAdapter myMouseAdapter = new MyMouseAdapter();
            mainPanel.addMouseMotionListener(myMouseAdapter);
            mainPanel.addMouseListener(myMouseAdapter);
            mainPanel.addMouseWheelListener(myMouseAdapter);

            add(toolBar, BorderLayout.PAGE_START);
            add(mainPanel, BorderLayout.CENTER);
            add(playBar, BorderLayout.PAGE_END);
        }
        protected void addSimButtons(JToolBar toolBar) {
            JButton button;
            JLabel label;
            JSpinner spinner;

            simStartBtn = new JButton();
            simStartBtn.addActionListener(this);
            simStartBtn.setActionCommand(SIM_START);
            simStartBtn.setText("START");
            toolBar.add(simStartBtn);

            simStopBtn = new JButton();
            simStopBtn.addActionListener(this);
            simStopBtn.setActionCommand(SIM_STOP);
            simStopBtn.setText("STOP");
            simStopBtn.setVisible(false);
            toolBar.add(simStopBtn);

            SpinnerNumberModel model = new SpinnerNumberModel( SimulationProcess.INIT_CATCHERS,
                    SimulationProcess.MIN_CATCHERS,
                    SimulationProcess.MAX_CATCHERS,
                    SimulationProcess.STEP_CATCHERS);

            label = new JLabel();
            label.setText(" Liczba poszukujących: ");
            toolBar.add(label);
            spinner = new JSpinner(model);
            spinner.setMaximumSize(new Dimension(40,30));
            spinner.addChangeListener(this);
            spinner.setName(TIME_INTERVAL);
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


            SpinnerNumberModel gcpModel = new SpinnerNumberModel( SimulationProcess.GCP_INIT,
                    SimulationProcess.GCP_MIN,
                    SimulationProcess.GCP_MAX,
                    SimulationProcess.GCP_STEP);

            label = new JLabel();
            label.setText(" Wskazówka globalna: ");
            toolBar.add(label);

            spinner = new JSpinner(gcpModel);
            spinner.setMaximumSize(new Dimension(55,30));
            spinner.addChangeListener(this);
            spinner.setName(GCP);
            toolBar.add(spinner);

            SpinnerNumberModel lcpModel = new SpinnerNumberModel( SimulationProcess.LCP_INIT,
                    SimulationProcess.LCP_MIN,
                    SimulationProcess.LCP_MAX,
                    SimulationProcess.LCP_STEP);

            label = new JLabel();
            label.setText(" Wskazówka lokalna: ");
            toolBar.add(label);

            spinner = new JSpinner(lcpModel);
            spinner.setMaximumSize(new Dimension(45,30));
            spinner.addChangeListener(this);
            spinner.setName(LCP);
            toolBar.add(spinner);
        }
        protected void addPlayButtons(JToolBar toolBar) {
            JButton button;

            playBtn = new JButton();
            playBtn.addActionListener(this);
            playBtn.setActionCommand(PLAY);
            playBtn.setText("PLAY");
            toolBar.add(playBtn);

            pauseBtn = new JButton();
            pauseBtn.addActionListener(this);
            pauseBtn.setActionCommand(PAUSE);
            pauseBtn.setText("PAUSE");
            pauseBtn.setVisible(false);
            toolBar.add(pauseBtn);

            button = new JButton();
            button.addActionListener(this);
            button.setActionCommand(STOP);
            button.setText("STOP");
            toolBar.add(button);

            JSlider intervalSlider = new JSlider(JSlider.HORIZONTAL, SimulationPlayer.INTERVAL_MIN, SimulationPlayer.INTERVAL_MAX, SimulationPlayer.INTERVAL_INIT);
            intervalSlider.addChangeListener(this);
            intervalSlider.setMajorTickSpacing(100);
            intervalSlider.setPaintTicks(true);
            intervalSlider.setMaximumSize(new Dimension(200, 50));

            Hashtable labelTable = new Hashtable();
            labelTable.put( new Integer( SimulationPlayer.INTERVAL_MIN ), new JLabel(Integer.toString(SimulationPlayer.INTERVAL_MIN)) );
            labelTable.put( new Integer( SimulationPlayer.INTERVAL_INIT ), new JLabel(Integer.toString(SimulationPlayer.INTERVAL_INIT)) );
            labelTable.put( new Integer( SimulationPlayer.INTERVAL_MAX ), new JLabel(Integer.toString(SimulationPlayer.INTERVAL_MAX)) );
            intervalSlider.setLabelTable( labelTable );
            intervalSlider.setPaintLabels(true);

            toolBar.add(intervalSlider);
        }
        public void attachMapPanel(MapPanel mapPanel) {
            this.mapPanel = mapPanel;
            mainPanel.add(mapPanel);
        }
        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();

            // Handle each button.
            try {
                if (SIM_START.equals(cmd)) {
                    simulationStart();
                    simStartBtn.setVisible(false);
                    simStopBtn.setVisible(true);
                } else if (SIM_STOP.equals(cmd)) {
                    simulationStop();
                    simStartBtn.setVisible(true);
                    simStopBtn.setVisible(false);
                } else if (PLAY.equals(cmd)) {
                    if(player==null) {
                        throw new NoPlayerException();
                    }
                    player.play();
                    playBtn.setVisible(false);
                    pauseBtn.setVisible(true);
                } else if (STOP.equals(cmd)) {
                    if(player==null) {
                        throw new NoPlayerException();
                    }
                    player.stop();
                } else if (PAUSE.equals(cmd)) {
                    if(player==null) {
                        throw new NoPlayerException();
                    }
                    player.pause();
                    playBtn.setVisible(true);
                    pauseBtn.setVisible(false);
                } else if (CATCHING_STRATEGY.equals(cmd)) {
                    JComboBox box = (JComboBox) e.getSource();
                    SimulationGUI.selectedCatchingStrategyIndex = box.getSelectedIndex();
                } else if (RUNNING_STRATEGY.equals(cmd)) {
                    JComboBox box = (JComboBox) e.getSource();
                    SimulationGUI.selectedRunningStrategyIndex = box.getSelectedIndex();
                }
            } catch(NoGuiException ex) {
                JOptionPane.showMessageDialog(null, "Brak podpiętego GUI! (Do procesu)");
            } catch(NoPlayerException ex) {
                JOptionPane.showMessageDialog(null, "Brak podpiętego Playera - prawdopodobnie symulacja nie została uruchomiona.");
            }

        }
        public void stateChanged(ChangeEvent e) {
            if(e.getSource().getClass()==JSlider.class) {
                JSlider source = (JSlider)e.getSource();
                if (!source.getValueIsAdjusting()) {
                    int interval = (int)source.getValue();
                    if(interval<SimulationPlayer.INTERVAL_MIN) {
                        interval = SimulationPlayer.INTERVAL_MIN;
                    }
                    Time.changeInterval(interval);
                    if(player!=null) {
                        player.updateTimer();
                    }
                }
            }
            else if(e.getSource().getClass()==JSpinner.class) {
                JSpinner source = (JSpinner)e.getSource();
                if(TIME_INTERVAL.equals(source.getName())) {
                    int val = (Integer) source.getValue();
                    process.changeCatchersNumber(val);
                }
                else if(LCP.equals(source.getName())) {
                    double val = (Double) source.getValue();
                    Runner.changeLCP(val);
                }
                else if(GCP.equals(source.getName())) {
                    double val = (Double) source.getValue();
                    Runner.changeGCP(val);
                }

            }
        }

        private class MyMouseAdapter extends MouseAdapter {
            boolean pressed = false;
            boolean inPanel = false;
            int prevX=0, prevY=0;
            @Override
            public void mousePressed(MouseEvent e) {
                prevX = e.getX();
                prevY = e.getY();
                pressed = true;
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if(pressed) {
                    updateMapPosition(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                pressed = false;
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                inPanel = true;
            }

            @Override
            public void mouseExited(MouseEvent e) {
                pressed = false;
                inPanel = false;
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if(inPanel) {
                    mapPanel.zoomImage(e.getWheelRotation()*ZOOM_STEP);
                }
            }

            public void updateMapPosition(MouseEvent e) {
                int moveX = prevX-e.getX();
                int moveY = prevY-e.getY();
                prevX = e.getX();
                prevY = e.getY();
                mapPanel.moveImage(moveX, moveY);
            }
        }
    }

    private class MapPanel extends JPanel {
        private BufferedImage image;
        private BufferedImage editedImage;
        private int imgX = 0;
        private int imgY = 0;
        private double zoom = SimulationGUI.ZOOM_MAX;

        private LinkedList<Crossing> catchersCrossings;
        private Crossing runnerCrossing;
        private Crossing runnerDestination;
        private Clue globalClue=null;

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
            if(!localClues.isEmpty()) {
                for(Clue clue : localClues) {
                    Position p = clue.getCrossing().getPos();
                    p = convert(p);
                    int x = (int) p.getX();
                    int y = (int) p.getY();

                    g2d.setPaint(SimulationGUI.localClueCol);
                    Ellipse2D.Double circle = new Ellipse2D.Double(x, y, SimulationGUI.circleDiameter, SimulationGUI.circleDiameter);
                    g2d.fill(circle);
                }
            }
            if(runnerDestination!=null) {
                Position p = runnerDestination.getPos();
                p = convert(p);
                int x = (int) p.getX();
                int y = (int) p.getY();

                g2d.setPaint(SimulationGUI.destCol);
                Ellipse2D.Double circle = new Ellipse2D.Double(x, y, SimulationGUI.circleDiameter, SimulationGUI.circleDiameter);
                g2d.fill(circle);
            }
            if(globalClue!=null) {
                Position p = globalClue.getCrossing().getPos();
                p = convert(p);
                int x = (int) p.getX();
                int y = (int) p.getY();

                g2d.setPaint(SimulationGUI.globalClueCol);
                Ellipse2D.Double circle = new Ellipse2D.Double(x, y, SimulationGUI.circleDiameter, SimulationGUI.circleDiameter);
                g2d.fill(circle);
            }
            if(catchersCrossings!=null) {
                for(Crossing c : catchersCrossings) {
                    Position p = c.getPos();
                    p = convert(p);
                    int x = (int) p.getX();
                    int y = (int) p.getY();

                    g2d.setPaint(SimulationGUI.catcherCol);
                    Ellipse2D.Double circle = new Ellipse2D.Double(x, y, SimulationGUI.circleDiameter, SimulationGUI.circleDiameter);
                    g2d.fill(circle);
                }
            }
            if(runnerCrossing!=null) {
                Position p = runnerCrossing.getPos();
                p = convert(p);
                int x = (int) p.getX();
                int y = (int) p.getY();

                g2d.setPaint(SimulationGUI.runnerCol);
                Ellipse2D.Double circle = new Ellipse2D.Double(x, y, SimulationGUI.runnerCircleDiameter, SimulationGUI.runnerCircleDiameter);
                g2d.fill(circle);

            }



            g2d.dispose();
            return img;
        }
        public void moveImage(int moveX, int moveY) {
            imgX += moveX;
            imgY += moveY;
            int pw = (int) Math.round(window.mainPanel.getWidth()*zoom);
            int ph = (int) Math.round(window.mainPanel.getHeight()*zoom);
            int offset = 0;
            int iw = editedImage.getWidth()+offset;
            int ih = editedImage.getHeight()+offset;

            if( (imgX+pw) > iw) {
                imgX = iw-pw;
            } else if(imgX<-offset) {
                imgX = -offset;
            }
            if( (imgY+ph) > ih) {
                imgY = ih-ph;
            } else if(imgY<-offset) {
                imgY = offset;
            }
            repaint();
        }
        public void zoomImage(double z) {
            zoom+=z;
            if(zoom>ZOOM_MAX) {
                zoom = ZOOM_MAX;
            } else if(zoom<ZOOM_MIN) {
                zoom = ZOOM_MIN;
            }
            moveImage(0,0);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int pw = window.mainPanel.getWidth();
            int ph = window.mainPanel.getHeight();
            int iw = (int) Math.round(pw*zoom);
            int ih = (int) Math.round(ph*zoom);

            g.drawImage(editedImage, 0, 0, pw, ph,
                    imgX, imgY, iw+imgX, ih+imgY, null);

        }
    }
}
