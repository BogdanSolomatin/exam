package domino;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

public class Main extends JFrame {

    //1 2;1 3;2 6;3 6;5 6
    //5 6;6 2;2 1;1 3;3 6
    public static JLabel nInfo = new JLabel("Input the numbers of board");
    public static JTextField countN = new JTextField(2);
    public static JLabel boardInfo = new JLabel("<html><div style='text-align: center;'>Input the numbers of point on the board" +
            "<br>Use whitespace as separator and ; for items</div></html>");
    public static JTextField boardInput = new JTextField(20);
    public static JCheckBox random = new JCheckBox("Use random boards(Input numbers)");
    public static JTextField numbers = new JTextField(2);
    public static JButton solve = new JButton("solve");

    public static LinkedList<Domino> list = new LinkedList<Domino>();
    public static LinkedList<Domino> chains = new LinkedList<Domino>();
    public static LinkedList<Domino> bestchain = new LinkedList<Domino>();
    public static LinkedList<Domino> defaultDomino = new LinkedList<Domino>();

    public static int maxlen = 0;

    public static void main(String[] args) {
        Main m = new Main();
    }

    public Main() {
        super("Domino");
        FlowLayout flo = new FlowLayout();
        setLayout(flo);
        add(nInfo);
        add(countN);
        add(boardInfo);
        add(boardInput);
        add(random);
        add(numbers);
        add(solve);
        solve.setPreferredSize(new Dimension(100,30));
        solve.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solve();
                JOptionPane.showMessageDialog(null,print(bestchain));
                dispose();
            }
        });
        random.setSelected(true);
        numbers.setVisible(true);
        boardInput.setEditable(false);
        random.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub
                if(random.isSelected()) {
                    numbers.setVisible(true);
                    boardInput.setEditable(false);
                }
                else {
                    boardInput.setEditable(true);
                    numbers.setVisible(false);
                }
            }

        });
        deffill(defaultDomino);
        setSize(320,300);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void solve() {
        if(!random.isSelected()) {
            String str = boardInput.getText();
            int val = Integer.valueOf(countN.getText());
            while (!check(str, val)) {
                str = JOptionPane.showInputDialog(null, "Invalid input. Try again");
                check(str, val);
            }
            filler(str,false);
        } else {
            filler("",true);
        }
    }

    public static void filler(String str, boolean flag) {
        if(!flag) {
            for (int i = 0; i < str.split("\\;").length; i++) {
                String temp = str.split("\\;")[i];
                list.add(new Domino(Integer.parseInt(temp.split(" ")[0]), Integer.parseInt(temp.split(" ")[1])));
            }
        }
        else {
            for(int i = 0; i < Integer.valueOf(numbers.getText());i++) {
                int temp = ThreadLocalRandom.current().nextInt(defaultDomino.size());
                list.add(defaultDomino.get(temp));
                defaultDomino.remove(temp);
            }
            JOptionPane.showMessageDialog(null,"Random boards:\n"+list+"\nLets press button OK to see solve");
        }
        createChains(chains, list);
    }

    public static boolean isBestChain(LinkedList<Domino> list) {
        if(list.size() > maxlen) {
            maxlen = list.size();
            bestchain = new LinkedList<Domino>(list);
            return true;
        }
        return false;
    }

    public static void createChains(LinkedList<Domino> chains, LinkedList<Domino> list) {
        if(list.isEmpty()) return;
        for(int i = 0; i < list.size(); i++) {
            Domino dom = list.get(i);
            if(checkAppend(list.get(i),chains)) {
                chains.add(list.get(i));
                isBestChain(chains);
                Domino temp = list.get(i);
                list.remove(i);
                createChains(chains, list);
                if(chains.isEmpty()) return;
                list.add(i,temp);
                chains.remove(chains.size()-1);

            } else {
                dom = dom.flip();
                if(checkAppend(dom,chains)) {
                    chains.add(dom);
                    isBestChain(chains);
                    Domino temp = list.get(i);
                    list.remove(i);
                    createChains(chains, list);
                    if(chains.isEmpty()) return;
                    list.add(i,temp);
                    chains.remove(chains.size()-1);
                }
            }
        }
    }

    public static String print(LinkedList<Domino> chains) {
        String str = "";
        for(int i = 0; i < chains.size(); i++) {
            str = str + chains.get(i).toString() + " ";
        }
        str = str + "\nNumbers of board: " + chains.size();
        return str;
    }

    public static boolean checkAppend(Domino domino, LinkedList<Domino> chains) {
        return chains.isEmpty() || chains.get(chains.size() - 1).low == domino.high;
    }

    public static void deffill(LinkedList<Domino> defaultDomino) {
        for(int i = 0; i <= 6; i++) {
            for(int j = 0; j <= 6; j++) {
                defaultDomino.add(new Domino(i,j));
            }
        }
    }

    public static boolean check(String str, int val) {
        return str.split("\\;").length == val;
    }

}

class Domino {
    int high;
    int low;

    public Domino(int high, int low) {
        this.high = high;
        this.low = low;
    }

    public Domino flip() {
        Domino dom = new Domino(this.low,this.high);
        return dom;
    }

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    public int getLow() {
        return low;
    }

    public void setLow(int low) {
        this.low = low;
    }

    @Override
    public String toString() {
        return "[" +high + "/" + low + "]";
    }
}