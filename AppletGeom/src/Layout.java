import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Layout extends JFrame {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

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
        bPanel.setLayout(new GridLayout(2,2,2,2));

            //maybe multiple text Panels for bottom row bottom panel
            JPanel texttupel0 = new JPanel();
            texttupel0.setLayout(new GridLayout(1,3,2,2));
            JTextField text1 = new JTextField("Umfang");
            JTextField text2 = new JTextField("Fläche");
            JTextField text3 = new JTextField("Zeit in s");
            texttupel0.add(text1);
            texttupel0.add(text2);
            texttupel0.add(text3);

            //maybe multiple text Panels for bottom row bottom panel
            JPanel texttupel1 = new JPanel();
            texttupel1.setLayout(new GridLayout(1,3,2,2));
            JTextField text4 = new JTextField("Umfang");
            JTextField text5 = new JTextField("Fläche");
            JTextField text6 = new JTextField("Zeit in s");
            texttupel1.add(text4);
            texttupel1.add(text5);
            texttupel1.add(text6);

        JButton colorCombRectButton = new JButton("Color Combination Rectangle");
        colorCombRectButton.addActionListener(new ColorcombrectButton(coordPanel));
        bPanel.add(colorCombRectButton);
        bPanel.add(button7);
        /*
        bPanel.add(button8);
        bPanel.add(button9);
        bPanel.add(button10);
        bPanel.add(button11);
        */
        bPanel.add(texttupel0);
        bPanel.add(texttupel1);
        mainContainer.add(bPanel, BorderLayout.SOUTH);

//        //find position of any Panel
//        new javax.swing.Timer(500,new ActionListener(){
//            @Override
//            public void actionPerformed(ActionEvent event) {
//                System.out.println(coordPanel.getBounds());
//            }
//        }).start();

    }


    public static void main(String[] args) {
        Layout mainLayout = new Layout();
        mainLayout.setVisible(true);
    }











}
