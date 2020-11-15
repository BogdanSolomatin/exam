import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Main {
    final static boolean shouldFill = true;
    final static boolean shouldWeightX = true;
    final static boolean RIGHT_TO_LEFT = false;
    public static int[] kratn;
    public static int[] sol;
    public static List<String> list = new ArrayList<String>();

    public static void addComponentsToPane(Container pane) {
        if (RIGHT_TO_LEFT) {
            pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }

        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        if (shouldFill) {
            //natural height, maximum width
            c.fill = GridBagConstraints.HORIZONTAL;
        }
        JLabel label = new JLabel();
        label.setText("<html>Введите искомый вес в поле ниже.<br>Затем необходимо нажать кнопку \"Вычислить\"</html>");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.insets = new Insets(10,10,10,10);
        pane.add(label,c);

        JTextField field = new JTextField(5);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.0;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 1;
        pane.add(field,c);

        JButton solve = new JButton("Вычислить");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 0;
        c.weighty = 1.0;
        c.anchor = GridBagConstraints.PAGE_END;
        c.gridx = 1;
        c.gridwidth = 2;
        c.gridy = 2;
        pane.add(solve, c);

        solve.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                String text = field.getText();
                data(Integer.valueOf(text));
            }
        });


    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Main");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addComponentsToPane(frame.getContentPane());
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    public static void data(int val) {
        String str = JOptionPane.showInputDialog(null, "Введите в строку ниже количество гирь каждого веса, используя запятую в качестве разделителя\nПри отсутствии гирь определенного веса используйте 0");
        while(!check(str,val)) {
            str = JOptionPane.showInputDialog(null,"Несоответствие веса и гирь. Проверьте правильность ввода");
            check(str,val);
        }
        kratn = new int[val];
        sol = new int[val];
        for(int i = 0;i<val;i++) {
            kratn[i] = Integer.valueOf(str.split("\\,")[i]);
        }
        nabor(val,val);
        res();
    }

    public static boolean check(String str,int val) {
        return str.split("\\,").length == val;
    }

    public static void nabor(int g,int v) {
        for (int i = g; i > 0; i--){
            if((kratn[i-1] > 0) && (i <=v)){
                kratn[i-1]--;
                sol[i-1]++;
                int v1 = v - i;
                if(v1 == 0) list.add(Arrays.toString(sol));
                else nabor(i,v1);
                kratn[i-1]++;
                sol[i-1]--;
            }
        }
    }

    public static void res() {
        JOptionPane.showMessageDialog(null, list);
    }

}