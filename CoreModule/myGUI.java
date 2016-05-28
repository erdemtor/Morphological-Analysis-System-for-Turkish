import javax.swing.*;
import java.io.IOException;

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
    private boolean iseditDistance;
    private String userinput;
    private String outputplace;

    public static void main(String[] args) throws IOException, ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        myGUI mygui = new myGUI();
        mygui.setIseditDistance(false);
        mygui.setUserinput("hodor");
        mygui.setOutputplace("AMGÖR");

    }
    public myGUI() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException, IOException {
        super("BOUN NLP");
        Core.train();
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 480, 360);
        setContentPane(panel1);
        setVisible(true);
        runButton.addActionListener(e -> {
            outputTextPane.setText("");
            String runtype = (String) runType.getSelectedItem();
            boolean isstemmer = runtype.equals("Stemmer");
            String result = Core.interactUser(userInput.getText(), isEditDistance.isSelected(), isstemmer);
            outputTextPane.setText(result);
            outputTextPane.updateUI();
            panel1.updateUI();
        });
    }


    public boolean isIseditDistance() {
        return iseditDistance;
    }

    public void setIseditDistance(final boolean iseditDistance) {
        this.iseditDistance = iseditDistance;
    }

    public String getUserinput() {
        return userinput;
    }

    public void setUserinput(final String userinput) {
        this.userinput = userinput;
    }

    public String getOutputplace() {
        return outputplace;
    }

    public void setOutputplace(final String outputplace) {
        this.outputplace = outputplace;
    }

    public void setData(myGUI data) {
        isEditDistance.setSelected(data.isIseditDistance());
        userInput.setText(data.getUserinput());
        outputTextPane.setText(data.getOutputplace());
    }

    public void getData(myGUI data) {
        data.setIseditDistance(isEditDistance.isSelected());
        data.setUserinput(userInput.getText());
        data.setOutputplace(outputTextPane.getText());
    }

    public boolean isModified(myGUI data) {
        if (isEditDistance.isSelected() != data.isIseditDistance()) return true;
        if (userInput.getText() != null ? !userInput.getText().equals(data.getUserinput()) : data.getUserinput() != null)
            return true;
        if (outputTextPane.getText() != null ? !outputTextPane.getText().equals(data.getOutputplace()) : data.getOutputplace() != null)
            return true;
        return false;
    }

}
