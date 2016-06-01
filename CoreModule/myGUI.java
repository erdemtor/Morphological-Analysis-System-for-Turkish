import TurkishDictParser.TurkishRenderer;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by Atakan Arıkan on 28.05.2016.
 */
public class myGUI extends JFrame {
    public JPanel panel1;
    public JTextField userInput;
    public JCheckBox isEditDistance;
    public JButton runButton;
    public JComboBox runType;
    public JTextPane outputTextPane;
    public JTabbedPane outputPanes;
    public JTextPane detailedOutput;
    public JTextPane prettyOutput;
    public JScrollPane prettyScroll;
    public JScrollPane detailedScroll;
    private boolean iseditDistance;
    private String userinput;
    private String outputplace;
    HashMap<String, String> result = new HashMap<>();
    public static void main(String[] args) throws IOException, ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        myGUI mygui = new myGUI();

    }
    public myGUI() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException, IOException {
        super("A Morphological Analysis System for Turkish");
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu about = new JMenu("About");
        JMenu help = new JMenu("Help");
        JMenuItem contact = new JMenuItem("Contact Authors");
        JMenuItem authors = new JMenuItem("Authors");
        JMenuItem program = new JMenuItem("Program");
        about.add(authors);
        about.add(program);
        help.add(contact);
        menuBar.add(about);
        menuBar.add(help);
        authors.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Atakan Arıkan: https://tr.linkedin.com/in/atakanarikan\n" +
                    "Erdem Toraman: https://tr.linkedin.com/in/erdemtoraman\n",
                    "About the authors",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        program.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Developed in Boğaziçi University as a graduation project.\n" +
                    "Use \"Help\" to contact the authors.\n" +
                    "Source code:    https://github.com/erdemtoraman/bounnlp\n"+
                    "Special thanks to our supervisor, Arzucan Özgür:  http://www.cmpe.boun.edu.tr/~ozgur/",
                    "About the program",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        contact.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Atakan Arıkan: arikan.atakan@yahoo.com\n" +
                    "Erdem Toraman: erdemtoraman93@gmail.com",
                    "Contact the authors",
                    JOptionPane.INFORMATION_MESSAGE);
        });
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        JWindow window = new JWindow();
        window.getContentPane().add(
                new JLabel("", new ImageIcon(new URL("http://www.ogrencibulteni.com/upload/galeri/logo_bogazici_univ.png")), SwingConstants.CENTER));
        window.setBounds(dim.width/2-510/2, dim.height/2-370/2, 510, 370);
        window.setVisible(true);
        try {
            Core.train();
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        window.setVisible(false);
        window.dispose();
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(dim.width/2-1200/2, dim.height/2-560/2, 1200, 560);
        setResizable(true);
        setContentPane(panel1);
        outputPanes.setVisible(false);
        setVisible(true);
        runButton.addActionListener(e -> {
            outputPanes.setVisible(true);
            detailedOutput.setText("");
            prettyOutput.setText("");
            String runtype = (String) runType.getSelectedItem();
            if (userInput.getText().contains(" ")) {
                String[] tokenized = TurkishRenderer.tokenize(userInput.getText().toLowerCase()).split(" ");
                result.put("normal", "");
                result.put("pretty", "");
                for (String token : tokenized) {
                    HashMap<String, String> res = new HashMap<>();
                    res = Core.interactUser(token, isEditDistance.isSelected(), runtype.equals("Stemmer"));
                    result.put("normal", result.get("normal") + "\n" + res.get("normal"));
                    result.put("pretty", result.get("pretty") + "\n" + res.get("pretty"));
                }
            } else {
                result = Core.interactUser(userInput.getText().toLowerCase(), isEditDistance.isSelected(), runtype.equals("Stemmer"));
            }
            prettyOutput.setText(result.get("pretty"));
            detailedOutput.setText(result.get("normal"));
            detailedOutput.updateUI();
            prettyOutput.updateUI();
            outputPanes.updateUI();
            panel1.updateUI();
        });
    }
}
