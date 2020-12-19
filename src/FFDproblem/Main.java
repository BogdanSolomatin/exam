package FFDproblem;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Main extends JFrame {

	public static JMenu menu = new JMenu("File");
	public static JMenuItem about = new JMenuItem("About");
	public static JMenuItem close = new JMenuItem("Close");
	public static JMenuBar bar = new JMenuBar();
	public static JLabel einfo = new JLabel("Input eps. Default eps 0.001");
	public static JLabel itemsinfo = new JLabel("Input items. Use whitespace as separator");
	public static JTextField e = new JTextField(5);
	public static JTextField items = new JTextField(10);
	public static JCheckBox random = new JCheckBox("Use random item's cost (Input numbers)");
	public static JTextField numbers = new JTextField(2);
	public static JButton solve = new JButton("Solve");
	public static JTextArea jta = new JTextArea();

	public static LinkedList<Double> A = new LinkedList<Double>();
	public static LinkedList<Double> V = new LinkedList<Double>();
	public static LinkedList<Integer> Cont = new LinkedList<Integer>();
	public static LinkedList<Integer> OptCont = new LinkedList<Integer>();
	public static LinkedList<Integer> res = new LinkedList<Integer>();
	public static LinkedList<String> output = new LinkedList<String>();

	public static int[] totalBox = {0};
	public static int Box, OptBox, N,cnt;
	public static double eps;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Main m = new Main();
	}

	public Main () {
		super("Main");
		FlowLayout flo = new FlowLayout();
		setLayout(flo);
		random.setSelected(true);
		numbers.setVisible(true);
		items.setEditable(false);
		random.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(random.isSelected()) {
					numbers.setVisible(true);
					items.setEditable(false);
				}
				else {
					items.setEditable(true);
					numbers.setVisible(false);				
				}
			}	

		});
		solve.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(checkerEps() && fill(random.isSelected())) {
					solve();
					JOptionPane.showMessageDialog(null, print());
					dispose();

				}

			}

		});
		about.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				JOptionPane.showMessageDialog(null, "FFD problem by Bogdan Solomatin");
			}

		});
		add(einfo);
		add(e);
		add(itemsinfo);
		add(items);
		add(random);
		add(numbers);
		add(solve);
		//add(new JTextArea(5,25));
		menu.add(about);
		menu.addSeparator();
		menu.add(close);
		bar.add(menu);
		setJMenuBar(bar);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(320, 400);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public static boolean checkerEps() {
		if(e.getText().length() == 0) {
			JOptionPane.showMessageDialog(null,"You don't input eps, so eps install by default - 0.001");
			eps = 0.001;
		}
		else if(Double.valueOf(e.getText()) > 0.001) {
			JOptionPane.showMessageDialog(null,"Incorrect input. Eps must be less than 0.001. Try again");
			return false;
		}
		else eps = Double.valueOf(e.getText());
		return true;
	}

	public static boolean fill (boolean flag) {
		if(flag) {
			for(int i = 0; i < Integer.valueOf(numbers.getText()); i++) {
				A.add(Math.round(ThreadLocalRandom.current().nextDouble(0.1, 0.99)*100)/100.00);
				//A.add(Double.valueOf(df.format(ThreadLocalRandom.current().nextDouble(0.1, 0.99))));
			}
			N = A.size();
			filler(A.size());
			return true;
		}
		else {
			for(int i = 0; i < items.getText().split(" ").length; i++) {
				A.add(Double.valueOf(items.getText().split(" ")[i]));
				if(A.get(i) >= 1) {
					JOptionPane.showMessageDialog(null, "Every item must be less than 1. Try again");
					break;
				}
			}
			N = A.size();
			filler(A.size());
		}	
		return true;
	}


	public static void filler (int size) {
		for(int i = 0; i < size; i++) {
			V.add(0.0);
			Cont.add(0);
			OptCont.add(0);
		}
	}

	public static void solve () {
		Cont.set(0, 1);
		Box = 1;
		V.set(0, A.get(0));
		for (int i = 0; i < N; i++) OptCont.set(i, i);
		OptBox = N;
		pack(1);
		ffdPack();
	}

	public static void ffdPack() {
		LinkedList<Double> items = new LinkedList<Double>(A);
		LinkedList<Integer> FFD = new LinkedList<Integer>();
		Collections.sort(items);
		Collections.reverse(items);
		V.forEach((item) -> item = 0.0);
		for(int i = 0; i < N; i++){
			FFD.add(0);
		}
		V.set(0, items.get(0));
		int totalbox = 1;
		for(int i = 1; i < N; i++){
			for(int j = 0; j < N; j++){
				if(V.get(j) + items.get(i) <= 1 + eps){
					if(V.get(j) <= eps)totalbox++;
					V.set(j, V.get(j) + items.get(i));
					FFD.set(i, j);
					break;
				}
			}
		}
		
		String buffer = "FFD packing\n";
		for (int i = 0; i < totalbox; i++){

			buffer = buffer + (i+1) + " : ";
            for (int j = 0; j < items.size(); j++) {
                if (FFD.get(j) == i)
                    buffer = buffer + " " + (items.get(j).toString());

            }
            buffer += ";\n";
        }
		buffer +="\nWhen you press button OK, you show default variant of packing algoritm";
		JOptionPane.showMessageDialog(null, buffer);
	}

	public static void pack(int i) {
		for (int j = 0; j < N; j++) {
			if (V.get(j) <= eps && Box + 1 >= OptBox) break; 
			if (j > 1 && V.get(j-1) <= eps) break; 
			if (V.get(j) + A.get(i) <= 1 + eps) { 
				int B = Box;
				if (V.get(j) <= eps) Box++; 
				V.set(j,V.get(j)+A.get(i));
				Cont.set(i, j+1);
				if (i < N - 1) pack(i + 1);
				else {
					for (int k = 0; k < N; k++) {
						OptCont.set(k, Cont.get(k));
					}
					OptBox = Box;
				}
				V.set(j, V.get(j)-A.get(i));
				Cont.set(i, 0);
				Box = B; 
			}
		}
	}


	public static String print() {
		String buff = "";
		int count = 0;
		int max = OptCont.stream().max(Integer::compare).get();
		output.add(";");
		for(int i = 0; i < OptCont.size(); i++) {
			int index = OptCont.get(i);
			if(res.contains(index)) continue;
			for(int j = 0; j < OptCont.size(); j++) {
				if(OptCont.get(j) == index) output.add(A.get(j).toString());
			}
			res.add(index);
			output.add(";");
		}
		for(int i = 0; i < output.size(); i++) {
			if(output.get(i).equals(";")) {
				buff+="\n";
				count++;
				if(count != max+1) {
					buff+=String.valueOf(count);
					buff+=" : ";				
				} 
			}
			else {
				buff+=output.get(i);
				buff+=", ";
			}
		}
		return buff;
	}

}
