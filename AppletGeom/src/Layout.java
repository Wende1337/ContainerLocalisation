import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Layout extends JFrame {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    // for text fields
    public static JTextField circum1 = new JTextField("Umfang");
    public static JTextField area1 = new JTextField("Fläche");
    public static JTextField time1 = new JTextField("Zeit in ns");


    public static JTextField circum2 = new JTextField("Umfang");
    public static JTextField area2 = new JTextField("Fläche");
    public static JTextField time2 = new JTextField("Zeit in ns");

    public static JTextField circum3 = new JTextField("Umfang");
    public static JTextField area3 = new JTextField("Fläche");
    public static JTextField time3 = new JTextField("Zeit in ns");


    public Layout(){

        this.setTitle("Container Lokalisation");
        this.setSize(WIDTH,HEIGHT);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton Resetall = new JButton("Reset All");
        Resetall.setFocusPainted(false);



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

        mainContainer.add(topPanel, BorderLayout.NORTH);

        //Middle West Panel
        JPanel mwPanel = new JPanel();
        mwPanel.setBorder(new LineBorder(Color.GRAY, 2));
        mwPanel.setBackground(Color.LIGHT_GRAY);
        mwPanel.setLayout(new GridLayout(2,0,0,0));

        JButton rcolorButton = new JButton("Random Color");
        rcolorButton.addActionListener(new ColorButton());
        rcolorButton.setFocusPainted(false);
        mwPanel.add(rcolorButton);

        /*button5.setBackground(new Color(255,0,0));
        button5.setBorderPainted(false);
        button5.setOpaque(true);
        mwPanel.add(button5);*/

        JPanel colorPalette = new JPanel();
        colorPalette.setLayout(new GridLayout(6,2,0,0));

        JButton cbtn;
        int[] cidx = {0,9,4,6,1,11,5,10,2,8,7,3};

        for (int i=0; i<12; i++){
            cbtn = new JButton();
            int finalI = i;
            cbtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ColorButton.setColor(cidx[finalI]);
                }
            });
            cbtn.setBackground(ColorButton.getColor(cidx[i]));
            cbtn.setFocusPainted(false);
            cbtn.setOpaque(true);
            colorPalette.add(cbtn);
        }


        mwPanel.add(colorPalette);

        mainContainer.add(mwPanel, BorderLayout.WEST);


        //Bottom Panel
        JPanel bPanel = new JPanel();
        bPanel.setBorder(new LineBorder(Color.GRAY, 2));
        bPanel.setBackground(Color.LIGHT_GRAY);
        bPanel.setLayout(new GridLayout(2,4,2,2));


            //maybe multiple text Panels for bottom row bottom panel
            JPanel optRecttext = new JPanel();
            optRecttext.setLayout(new GridLayout(1,2,2,2));
            optRecttext.add(circum1);
            optRecttext.add(area1);
            optRecttext.add(time1);

            JPanel csettext = new JPanel();
            csettext.setLayout(new GridLayout(1,2,2,2));
            csettext.add(circum2);
            csettext.add(area2);
            csettext.add(time2);


        JPanel csetatext = new JPanel();
            csetatext.setLayout(new GridLayout(1,2,2,2));
            csetatext.add(circum3);
            csetatext.add(area3);
            csetatext.add(time3);



        JPanel OptRectButtons = new JPanel();
        OptRectButtons.setLayout(new GridLayout(0,2,2,2));
        JButton OptRectButton = new JButton("Optimum Rectangle");
        OptRectButton.addActionListener(new OptRectangleButton(coordPanel));
        OptRectButton.setFocusPainted(false);
        JButton OptRectStepButton = new JButton("Step");
        OptRectStepButton.addActionListener(new OptRectStepButton(coordPanel));
        OptRectStepButton.setFocusPainted(false);
        OptRectButtons.add(OptRectButton);
        OptRectButtons.add(OptRectStepButton);
        bPanel.add(OptRectButtons);

        JPanel CSETButtons = new JPanel();
        CSETButtons.setLayout(new GridLayout(0,2,2,2));
        JButton CSETButton = new JButton ("CSET");
        CSETButton.addActionListener(new CSETButton(coordPanel));
        CSETButton.setFocusPainted(false);
        JButton CSETStepButton = new JButton("Step");
        CSETStepButton.addActionListener(new CSETStepButton(coordPanel));
        CSETStepButton.setFocusPainted(false);
        CSETButtons.add(CSETButton);
        CSETButtons.add(CSETStepButton);
        bPanel.add(CSETButtons);


        JPanel CSETAButtons = new JPanel();
        CSETAButtons.setLayout(new GridLayout(0,2,2,2));
        JButton CSETAButton = new JButton ("CSETA");
        CSETAButton.addActionListener(new CSETAButton(coordPanel));
        CSETAButton.setFocusPainted(false);
        JButton CSETAStepButton = new JButton("Step");
        CSETAStepButton.addActionListener(new CSETAStepButton(coordPanel));
        CSETAStepButton.setFocusPainted(false);
        CSETAButtons.add(CSETAButton);
        CSETAButtons.add(CSETAStepButton);
        bPanel.add(CSETAButtons);



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
