import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Main {

	/**
	 * JFrame utilities
	 */

	private static JFrame frame = new JFrame("Exam");
	private static JMenuBar bar = new JMenuBar();
	private static JMenu menu = new JMenu("File");
	private static JMenuItem open = new JMenuItem("Open");
	private static JMenu inZip = new JMenu("Write in ZIP archive");
	private static JMenuItem fromZip = new JMenuItem("Read from ZIP archive");
	private static JMenuItem close = new JMenuItem("Close");
	private static JMenuItem newArchive = new JMenuItem("Write in new ZIP archive");
	private static JMenuItem saveIn = new JMenuItem("Write in an archive which already created");
	private static JMenuItem author = new JMenuItem("Author");
	private static JTextArea area = new JTextArea();
	private static JScrollPane scroll = new JScrollPane(area);
	private static JFileChooser fc = new JFileChooser("C:\\Users\\1\\Desktop\\Compass\\Java");

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		/**
		 * Add components, locate JFrame
		 */

		inZip.add(newArchive);
		inZip.add(saveIn);
		menu.add(open);
		menu.add(inZip);
		menu.add(fromZip);
		menu.add(author);
		menu.add(close);
		bar.add(menu);
		LinkedList<String> list = new LinkedList<String>();
		File file = null;
		frame.add(scroll);
		frame.add(bar,BorderLayout.NORTH);
		frame.setSize(400, 400);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		/**
		 * Listeners for menu item
		 */

		open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				File file = null;
				fc.setDialogTitle("Choose .txt file from your PC");
				JButton open = new JButton();
				if(fc.showOpenDialog(open) == JFileChooser.APPROVE_OPTION) {
					file = fc.getSelectedFile();
					try {
						readFile(file);
					} catch (Exception exc) {
						exc.printStackTrace();
					}
				}
			}
		});

		newArchive.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				File file = null;
				fc.setDialogTitle("Select directory for create new ZIP archive");
				fc.addChoosableFileFilter(new FileNameExtensionFilter("Archive files", "zip"));
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				JButton open = new JButton();
				if (fc.showOpenDialog(open) == JFileChooser.APPROVE_OPTION) {
					file = fc.getSelectedFile();
					try {
						newArchive(file);
					} catch (Exception exc) {
						exc.printStackTrace();
					}
				}
			}
		});

		saveIn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				File file = null;
				fc.setDialogTitle("Select the ZIP archive to which the new section will be added");
				fc.addChoosableFileFilter(new FileNameExtensionFilter("Archive files", "zip"));
				fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				JButton open = new JButton();
				if(fc.showOpenDialog(open) == JFileChooser.APPROVE_OPTION) {
					file = fc.getSelectedFile();	
					try {
						saveIn(file);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});

		fromZip.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				File file = null;
				JButton open = new JButton();
				if(fc.showOpenDialog(open) == JFileChooser.APPROVE_OPTION) {
					file = fc.getSelectedFile();
					try {
						unZip(file);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

		close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
		});

		author.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				JOptionPane.showMessageDialog(null, "Created by Bogdan Solomatin. 09/07/2020");
			}	
		});
	}

	/**
	 * File -> Open listener call this method
	 * @param file - reading file
	 * @return file content in String
	 * @throws FileNotFoundException
	 * @throws IOException 
	 */

	public static String readFile(File file) throws FileNotFoundException, IOException {
		String buffer = "";
		try(final InputStream is = new FileInputStream(file);
				final Reader rd = new InputStreamReader(is);
				final BufferedReader br = new BufferedReader(rd);){
			String line;
			while((line = br.readLine()) != null) {
				buffer+=line+"\n";
			}
			statistic(buffer);
			return buffer;
		}
	}

	/**
	 * File -> Write in Zip Archive -> Write in new Zip Archive lsitener call it
	 * @param file - directory where the new archive will be created
	 * @throws FileNotFoundException
	 * @throws IOException
	 */

	public static void newArchive(File file) throws FileNotFoundException, IOException {
		file = new File(file.getAbsolutePath()+"/resultArchive.zip");
		try(final OutputStream os = new FileOutputStream(file);
				final ZipOutputStream zos = new ZipOutputStream(os)){
			ZipEntry ze = new ZipEntry("Result.txt");
			ze.setMethod(ZipEntry.DEFLATED);
			zos.putNextEntry(ze);
			final Writer wr = new OutputStreamWriter(zos);
			String line = System.getProperty("line.separator");
			for(int i = 0;i<area.getText().split("\n").length;i++) {
				wr.write(area.getText().split("\n")[i]);
				wr.write(line);
			}
			wr.flush();
			zos.closeEntry();
			zos.finish();		
		}
	}

	/**
	 * File -> Write in Zip Archive -> Write in an archive which already created listener call it
	 * This method's code got from https://docs.oracle.com/javase/7/docs/technotes/guides/io/fsp/zipfilesystemprovider.html
	 * @param file - The archive where the file will be added to
	 * @throws FileNotFoundException
	 * @throws IOException
	 */

	public static void saveIn(File file) throws FileNotFoundException, IOException {	
		Map<String, String> env = new HashMap<>();
		env.put("create", "true");
		Path path = Paths.get(file.getAbsolutePath());
		URI uri = URI.create("jar:" + path.toUri());
		String line = System.getProperty("line.separator");
		try (FileSystem fs = FileSystems.newFileSystem(uri, env)) {
			Path nf = fs.getPath("Result.txt");
			try (Writer wr = Files.newBufferedWriter(nf, StandardCharsets.UTF_8, StandardOpenOption.CREATE)) {
				for(int i = 0;i<area.getText().split("\n").length;i++) {
					wr.write(area.getText().split("\n")[i]);
					wr.write(line);
				}
			}
		}
	}

	/**
	 * File -> Read from archive listener call it
	 * @param file - file archive
	 * @throws FileNotFoundException
	 * @throws IOException
	 */

	public static void unZip(File file) throws FileNotFoundException, IOException {
		try(final FileInputStream is = new FileInputStream(file);
				final ZipInputStream zis = new ZipInputStream(is)){
			LinkedList<Object> list = new LinkedList<Object>();
			ZipEntry ze;
			while((ze = zis.getNextEntry()) != null) {
				if(!ze.isDirectory())list.add(ze.getName());
			}
			Object obj = list.toArray();
			String str = (String) JOptionPane.showInputDialog(scroll,"Choose a file from list","Zip entry mode",JOptionPane.PLAIN_MESSAGE,null,(Object[]) obj,null);
			if(str == null) System.err.println("You didn't choose a file. Please, try again");
			else {
				try(final FileInputStream fis = new FileInputStream(file);
						final ZipInputStream zisStream = new ZipInputStream(fis)){
					ZipEntry zes;
					while((zes = zisStream.getNextEntry()) != null) {
						if(str.equals(zes.getName())) {
							final Reader			rdr = new InputStreamReader(zisStream);
							final BufferedReader	brdr = new BufferedReader(rdr);
							String line;
							String buffer = "";
							while((line = brdr.readLine()) != null) {
								buffer+=line+"\n";
							}
							statistic(buffer);
						}
					}
				}
			}
		}
	}

	/**
	 * Counting statistics method 
	 * @param str - file content in String
	 */
	
	public static void statistic(String str) {
		/*
		area.setText(null);
		double average = 0.0;
		String[] arr = str.split("\n");
		for(int i = 0;i<arr.length;i++) {
			average+=Integer.valueOf(arr[i].split("\\,")[3]);
		}
		average/=arr.length;
		area.setText("Statistics:\nAverage age is "+average+"\nNumber of item is "+arr.length);
		 */
		area.setText(null);
		String[] arr = str.split("\n");
		LinkedList<Integer> year = new LinkedList<Integer>();
		for(int i = 0;i<arr.length;i++) {
			year.add(Integer.valueOf(arr[i].split("\\,")[2]));
		}
		area.setText("Statistics:\nNumber if item "+arr.length+"\nThe oldest book "+arr[year.indexOf(Collections.min(year))].split("\\,")[1]);
	}
}
