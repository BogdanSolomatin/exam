
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;


public class BinaryPlus {
    final static boolean shouldFill = true;
    final static boolean shouldWeightX = true;
    final static boolean RIGHT_TO_LEFT = false;

    public static void addComponentsToPane(Container pane) {
        if (RIGHT_TO_LEFT) {
            pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        if (shouldFill) {
            c.fill = GridBagConstraints.HORIZONTAL;
        }
        JLabel label = new JLabel();
        label.setText("<html>Ввод чисел осуществляется в поле ниже через знак +.<br>Затем необходимо нажать кнопку \"Вычислить\"</html>");
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
                data(text);
            }
        });
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Two numbers problem. Created by Bogdan Solomatin");
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

    public static void data(String text) {
        // TODO Auto-generated method stub
        String a = text.split("\\+")[0];
        String b = text.split("\\+")[1];
        String sum = "";
        solve(a,b);
    }

    public static void solve(String a, String b) {
        String sum = "";
        for(int i = 0;i<=b.length();i++) {
            sum+=" ";
        }
        sum += a + "+" + b + " ";
        char[]r = sum.toCharArray();
        int index = b.length()+1;
        state_change(State.state_1,r,index);
    }

    public static void state_change(State state,char[] r,int index) {
        switch(state) {
            case state_1:
                while(r[index] == '0' || r[index] == '1' || r[index] == '+') {
                    index++;
                }
                if(Character.isWhitespace(r[index])) {
                    index--;
                    state_change(State.state_2,r,index);
                }
                break;
            case state_2:
                while (r[index] == '0')
                {
                    r[index] = '1';
                    index--;
                }
                if (r[index] == '1')
                {
                    r[index] = '0';
                    index--;
                    state_change(State.state_3,r,index);
                }
                if (r[index] == '+') {
                    r[index] = ' ';
                    index++;
                    state_change(State.state_6,r,index);
                }
                break;
            case state_3:
                while (r[index] == '0'||r[index]=='1')
                {
                    index--;
                }
                if(r[index]=='+')
                {
                    index--;
                    state_change(state.state_4,r, index);
                }
                break;
            case state_4:
                while (r[index] == '1') {
                    r[index] = '0';
                    index--;
                }
                if (Character.isWhitespace(r[index])) {
                    r[index] = '1';
                    index++;
                    state_change(State.state_5,r, index);
                }
                if (r[index] == '0') {
                    r[index] = '1';
                    index++;
                    state_change(State.state_5,r, index);
                }
                break;
            case state_5:
                while (r[index] == '0' || r[index] == '1'||r[index]=='+') {
                    index++;
                }
                if (Character.isWhitespace(r[index])) {
                    index--;
                    state_change(state.state_2,r, index);
                }
                break;
            case state_6:
                while (r[index] == '1') {
                    r[index]=' ';
                    index++;
                }
                String temp = "";
                for (int j = 0; j < r.length; j++) {
                    temp += String.valueOf(r[j]);
                }
                JOptionPane.showMessageDialog(null, "Sum = "+Integer.parseInt(temp.trim()));
                break;
        }
    }
}

enum State {
    state_1,state_2,state_3,state_4,state_5,state_6

}

