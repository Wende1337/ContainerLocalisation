import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Layout extends JFrame {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    // for text fields
    public static JTextField circum1 = new JTextField("Umfang");
    public static JTextField area1 = new JTextField("Fläche");
    public static JTextField time1 = new JTextField("Zeit in s");

    public static JTextField circum2 = new JTextField("Umfang");
    public static JTextField area2 = new JTextField("Fläche");
    public static JTextField time2 = new JTextField("Zeit in s");

    public static JTextField circum3 = new JTextField("Umfang");
    public static JTextField area3 = new JTextField("Fläche");
    public static JTextField time3 = new JTextField("Zeit in s");

    public static JTextField circum4 = new JTextField("Umfang");
    public static JTextField area4 = new JTextField("Fläche");
    public static JTextField time4 = new JTextField("Zeit in s");


    public Layout(){

        this.setTitle("Container Lokalisation");
        this.setSize(WIDTH,HEIGHT);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton Resetall = new JButton("Reset All");
        JButton button2 = new JButton("Button2");
        JButton rcolorButton = new JButton("Random Color");
        rcolorButton.addActionListener(new ColorButton());
        JButton MovePointButton = new JButton("Move Point");
        JButton button5 = new JButton("Button5");
        JButton button6 = new JButton("Button6");
        JButton button7 = new JButton("Button7");
        JButton button8 = new JButton("Button8");
        JButton button9 = new JButton("Button9");
        JButton button10 = new JButton("Button10");
        JButton button11 = new JButton("Button11");


        Container mainContainer = this.getContentPane();
        mainContainer.setLayout(new BorderLayout(8,8));
        mainContainer.setBackground(Color.DARK_GRAY);
        this.getRootPane().setBorder(BorderFactory.createMatteBorder(8,8,8,8,Color.DARK_GRAY));

        //Middle Center Panel
        CoordPanel coordPanel = new CoordPanel();
        coordPanel.setBorder(new LineBorder(Color.GRAY, 2));
        coordPanel.setBackground(Color.WHITE);
        coordPanel.addMouseListener(new PointMouseListener(coordPanel));
        coordPanel.addMouseMotionListener(new PointMouseListener(coordPanel));
        mainContainer.add(coordPanel, BorderLayout.CENTER);
        
        //Top Panel
        JPanel topPanel = new JPanel();
        topPanel.setBorder(new LineBorder(Color.LIGHT_GRAY, 2));
        topPanel.setBackground(Color.LIGHT_GRAY);
        topPanel.setLayout(new FlowLayout(5));
        topPanel.add(Resetall);
        Resetall.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                coordPanel.emptyAll();
                }
        });
        topPanel.add(button2);
        mainContainer.add(topPanel, BorderLayout.NORTH);

        //Middle West Panel
        JPanel mwPanel = new JPanel();
        mwPanel.setBorder(new LineBorder(Color.GRAY, 2));
        mwPanel.setBackground(Color.LIGHT_GRAY);
        mwPanel.setLayout(new BoxLayout(mwPanel, BoxLayout.Y_AXIS));
        mwPanel.add(rcolorButton);
        mwPanel.add(MovePointButton);
        mwPanel.add(button5);
        mainContainer.add(mwPanel, BorderLayout.WEST);


        //Bottom Panel
        JPanel bPanel = new JPanel();
        bPanel.setBorder(new LineBorder(Color.GRAY, 2));
        bPanel.setBackground(Color.LIGHT_GRAY);
        bPanel.setLayout(new GridLayout(2,4,2,2));

            //maybe multiple text Panels for bottom row bottom panel
            JPanel colorRecttext = new JPanel();
            colorRecttext.setLayout(new GridLayout(1,3,2,2));
            colorRecttext.add(circum1);
            colorRecttext.add(area1);
            colorRecttext.add(time1);

            //maybe multiple text Panels for bottom row bottom panel
            JPanel optRecttext = new JPanel();
            optRecttext.setLayout(new GridLayout(1,3,2,2));
            optRecttext.add(circum2);
            optRecttext.add(area2);
            optRecttext.add(time2);

            JPanel csettext = new JPanel();
            csettext.setLayout(new GridLayout(1,3,2,2));
            csettext.add(circum3);
            csettext.add(area3);
            csettext.add(time3);

            JPanel csetatext = new JPanel();
            csetatext.setLayout(new GridLayout(1,3,2,2));
            csetatext.add(circum4);
            csetatext.add(area4);
            csetatext.add(time4);

        JButton colorCombRectButton = new JButton("Color Combination Rectangle");
        colorCombRectButton.addActionListener(new ColorcombrectButton(coordPanel));
        bPanel.add(colorCombRectButton);
        JButton OptRectButton = new JButton("Optimum Rectangle");
        OptRectButton.addActionListener(new OptRectangleButton(coordPanel));
        bPanel.add(OptRectButton);
        JButton CSETButton = new JButton ("CSET");
        CSETButton.addActionListener(new CSETButton(coordPanel));
        bPanel.add(CSETButton);
        JButton CSETAButton = new JButton ("CSETA");
        CSETAButton.addActionListener(new CSETAButton(coordPanel));
        bPanel.add(CSETAButton);


        bPanel.add(colorRecttext);
        bPanel.add(optRecttext);
        bPanel.add(csettext);
        bPanel.add(csetatext);

        mainContainer.add(bPanel, BorderLayout.SOUTH);
    }



    public static void main(String[] args) {
        Layout mainLayout = new Layout();
        mainLayout.setVisible(true);
    }











}
