package backpack;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;


public class Main {

	/**
	 * Variant - 8
	 * 9 - 8 , 10 - 12, 7 - 5, total - 16 
	 */

	public static int N = 3;
	public static int limit = 16;

	public static LinkedList<Item> items = new LinkedList<>();
	public static LinkedList<Boolean> X = new LinkedList<>();
	public static LinkedList<Boolean> optX = new LinkedList<>();

	public static int sumcost;
	public static int optCost;

	public static Scanner scan = new Scanner(System.in);

	public static final MyTableModel myModel = new MyTableModel();
	public static final JTable table = new JTable(myModel);
	public static final JScrollPane	scroll = new JScrollPane(table);

	public static boolean flag = true;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getColumnModel().getColumn(6).setPreferredWidth(100);
		table.getColumnModel().getColumn(8).setPreferredWidth(200);
		solve();
		//JOptionPane.showMessageDialog(null,scroll);
		JFrame frame = new JFrame("Backpack problem");
		JButton button = new JButton("Programing solve");
		frame.getContentPane().add(scroll, BorderLayout.CENTER);
		frame.getContentPane().add(button, BorderLayout.SOUTH);
		frame.setLocationRelativeTo(null);
		frame.setSize(400, 400);
		frame.add(scroll);
		frame.setVisible(true);
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				frame.dispose();
				NewFrame framer = new NewFrame();
				flag = false;
				framer.btn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						N = Integer.valueOf(framer.input.getText());
						limit = Integer.valueOf(framer.newlimit.getText());
						items.clear();
						X.clear();
						optX.clear();
						solve();
					}

				});
			}
		});
	}

	public static void solve() {
		String str = JOptionPane.showInputDialog(null, "Input weight and price for every item, use whitespace.\nUse ; as separator for items");
		while(!check(str,N)) {
			str = JOptionPane.showInputDialog(null,"Invalid input. Try again");
			check(str,N);
		}
		for(int i = 0; i < N; i++) {
			String temp = str.split("\\;")[i];
			items.add(new Item(Integer.valueOf(temp.split(" ")[0]),Integer.valueOf(temp.split(" ")[1])));
		}

		sumcost = 0;
		for(int i = 0; i < N; i++) {
			sumcost += items.get(i).getPrice();
			X.add(i, true);
			optX.add(i, true);
		}

		optCost = 0;
		myTry(0, 0, sumcost);     
		showOptCost();
	}

	public static boolean check(String str, int N) {
		return str.split("\\;").length == N;
	}

	public static void showOptCost() {
		String str = "OptX\n";
		for(int i = 0; i < N; i++) {
			str = str + "Item " + (i+1) + " " + !optX.get(i) + "\n";
		}
		str = str + "OptCost " + optCost;
		JOptionPane.showMessageDialog(null, str);
	}

	public static void insert(int index, int pos_st, String note) {
		String numbers = "";
		int cost = 0;
		int weight = 0;
		for(int i = 0; i <= index; i++) {
			numbers += !X.get(i) ? "+" : "-";
			if(!X.get(i)) {
				cost += items.get(i).getPrice();
				weight += items.get(i).getWeight();
			}
		}

		for(int i = index + 1; i < N; i++) {
			numbers += "?";
		}
		if(flag)myModel.insertRow("try ("+String.valueOf(index+1) + ")", numbers.charAt(0),numbers.charAt(1),numbers.charAt(2), weight, cost, pos_st,optCost, note);
	}

	public static void myTry(int index, int sum_v, int pos_st) {
		if(sum_v + items.get(index).getWeight() <= limit) {
			X.set(index,false);
			if(index + 1 == N && pos_st > optCost) {
				//optX = X;
				optX = new LinkedList<>(X);
				optCost = pos_st;
				insert(index, pos_st, "Optimum");
			} else {
				insert(index, pos_st, "Inclusion " + (index + 1));
				myTry(index + 1, sum_v + items.get(index).getWeight(), pos_st);
			}
			X.set(index, true);
		}

		int st1 = pos_st - items.get(index).getPrice();
		if(st1 > optCost) {
			if(index +1  == N) {
				//optX = X;
				optX = new LinkedList<>(X);
				optCost = st1;
				insert(index, pos_st, "Optimum");
			}
			else {
				insert(index, pos_st, st1 + " > " + optCost + " Non-inclusion " + (index+1));
				myTry(index + 1, sum_v, st1);
			}
		} else {
			insert(index, pos_st, st1 + " <= " + optCost + " Non-inclusuion " + (index+1));
		}
	}


	private static class Record {
		String tryi;
		char n1;
		char n2;
		char n3;
		int totalWeight;
		int totalCost;
		int potencialCost;
		int opt;
		String ps;

		public Record(String tryi, char n1, char n2, char n3, int totalWeight, int totalCost, int potencialCost, int opt, String ps) {
			super();
			this.tryi = tryi;
			this.n1 = n1;
			this.n2 = n2;
			this.n3 = n3;
			this.totalWeight = totalWeight;
			this.totalCost = totalCost;
			this.potencialCost = potencialCost;
			this.opt = opt;
			this.ps = ps;
		}
	}

	private static class MyTableModel extends DefaultTableModel {
		LinkedList<Record> data = new LinkedList<>();

		MyTableModel() {
		}

		public void insertRow(String tryi, char n1, char n2, char n3, int totalWeight, int totalCost, int potencialCost, int opt, String note) {
			data.add(new Record(tryi, n1, n2, n3, totalWeight, totalCost, potencialCost, opt, note));
			fireTableDataChanged();
		}

		@Override
		public Class<?> getColumnClass(int colNum) {
			switch (colNum) {
			case 0 : return String.class;
			case 1 : return char.class;
			case 2 : return char.class;
			case 3 : return char.class;
			case 4 : return Integer.class;
			case 5 : return Integer.class;
			case 6 : return Integer.class;
			case 7 : return Integer.class;
			case 8 : return String.class;
			}
			return null;
		}

		@Override
		public int getColumnCount() {
			return 9;
		}

		@Override
		public String getColumnName(int colNum) {
			switch (colNum) {
			case 0 : return "Try(i)";
			case 1 : return "¹1";
			case 2 : return "¹2";
			case 3 : return "¹3";
			case 4 : return "Total weigth";
			case 5 : return "Total cost";
			case 6 : return "Potencial cost";
			case 7 : return "Optimal cost";
			case 8 : return "Note";
			}
			return null;
		}

		@Override
		public int getRowCount() {
			return data == null ? 0 : data.size();
		}

		@Override
		public Object getValueAt(int rowNum, int colNum) {
			switch (colNum) {
			case 0 : return data.get(rowNum).tryi;
			case 1 : return data.get(rowNum).n1;
			case 2 : return data.get(rowNum).n2;
			case 3 : return data.get(rowNum).n3;
			case 4 : return data.get(rowNum).totalWeight;
			case 5 : return data.get(rowNum).totalCost;
			case 6 : return data.get(rowNum).potencialCost;
			case 7 : return data.get(rowNum).opt;
			case 8 : return data.get(rowNum).ps;
			}
			return null;
		}

		@Override
		public boolean isCellEditable(int rowNum, int colNum) {
			return true;
		}

		@Override
		public void setValueAt(Object arg0, int rowNum, int colNum) {
			switch (colNum) {
			case 0 : data.get(rowNum).tryi = (String)arg0; break;
			case 1 : data.get(rowNum).n1 = (char)arg0; break;
			case 2 : data.get(rowNum).n2 = (char)arg0; break;
			case 3 : data.get(rowNum).n3 = (char)arg0; break;
			case 4 : data.get(rowNum).totalWeight = (Integer)arg0; break;
			case 5 : data.get(rowNum).totalCost = (Integer)arg0; break;
			case 6 : data.get(rowNum).potencialCost = (Integer)arg0; break;
			case 7 : data.get(rowNum).opt = (Integer)arg0; break;
			case 8 : data.get(rowNum).ps = (String)arg0; break;
			}
			fireTableRowsUpdated(rowNum,rowNum);
		}

	}

}

class Item {
	int weight;
	int price;

	public Item(int weight, int price) {
		super();
		this.weight = weight;
		this.price = price;
	}

	public int getPrice() {
		return price;
	}

	public int getWeight() {
		return weight;
	}


}

class NewFrame extends JFrame {
	public static JLabel info = new JLabel("Input the number of items: ");
	public static JTextField input = new JTextField(10);
	public static JLabel o = new JLabel("Input backpack limit");
	public static JTextField newlimit = new JTextField(10);
	public static JButton btn = new JButton("Solve");
	public NewFrame () {
		super("Backpack problem part 2");
		FlowLayout flo = new FlowLayout();
		setLayout(flo);
		add(info);
		add(input);
		add(o);
		add(newlimit);
		add(btn);
		setSize(200,200);
		setVisible(true);
		setLocationRelativeTo(null);

	}
}
