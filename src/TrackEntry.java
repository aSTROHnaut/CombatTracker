
import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;

public class TrackEntry extends JPanel {

    private JTextField iniative = new JTextField();
    private JTextField name = new JTextField();
    private JTextField armorClass  = new JTextField();
    private JTextField currHP  = new JTextField();
    private JTextField tempHP  = new JTextField();
    private JTextField maxHP  = new JTextField();
    private JTextField deltaHP  = new JTextField();

    public TrackEntry(){
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        this.add(fieldEntry("Iniative: ", 6, "^\\d*+([.]\\d*+)?$", iniative));
        this.add(fieldEntry("Name: ", 20, "^.+$", name));
        this.add(fieldEntry("AC: ", 6, "^\\d*+([-]\\d*+)?$", armorClass));
        this.add(hpPanel());
        this.add(deltaPanel());

        this.setMaximumSize(new Dimension(900, 35));
        this.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
    }

    private JPanel fieldEntry(String fieldName, int size, String regex, JTextField textField){
        textField.setHorizontalAlignment(SwingConstants.RIGHT);
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(TextFieldrestrictions(regex));

        textField.setColumns(size);
        JLabel label = new JLabel(fieldName);
        label.setHorizontalAlignment(SwingConstants.RIGHT);

        JPanel panel = new JPanel(new FlowLayout());
        panel.add(label);
        panel.add(textField);
        panel.setBorder(BorderFactory.createLineBorder(Color.darkGray));
        return panel;
    }
    private JPanel hpPanel(){
        JPanel panel = new JPanel(new GridLayout(2, 1));
        JPanel labelPanel = new JPanel(new GridLayout(1, 2));
        JPanel textPanel = new JPanel(new GridLayout(1, 5));

        JLabel temp = new JLabel("Temp HP");
        JLabel norm = new JLabel("Hit Points");

        labelPanel.add(temp);
        labelPanel.add(norm);

        ((AbstractDocument) tempHP.getDocument()).setDocumentFilter(TextFieldrestrictions("\\d*?"));
        ((AbstractDocument) currHP.getDocument()).setDocumentFilter(TextFieldrestrictions("-?\\d*?"));
        ((AbstractDocument) maxHP.getDocument()).setDocumentFilter(TextFieldrestrictions("\\d*?"));

        JLabel space = new JLabel("        ");
        currHP.setHorizontalAlignment(SwingConstants.RIGHT);
        JLabel line = new JLabel("/");
        line.setHorizontalAlignment(SwingConstants.CENTER);
        maxHP.setHorizontalAlignment(SwingConstants.RIGHT);

        textPanel.add(tempHP);
        textPanel.add(space);
        textPanel.add(currHP);
        textPanel.add(line);
        textPanel.add(maxHP);

        panel.add(labelPanel);
        panel.add(textPanel);
        panel.setBorder(BorderFactory.createLineBorder(Color.darkGray));

        return panel;
    }
    private JPanel deltaPanel(){
        JPanel deltaPanel = new JPanel(new GridLayout(1,2));
        JButton heal = new JButton("+");
        JButton damage = new JButton("-");

        JPanel numberPanel = new JPanel(new GridLayout(2, 1));
        JPanel deltaButtons = new JPanel(new GridLayout(1,2));
        deltaPanel.add(new JLabel("    delta HP:"));

        ((AbstractDocument) deltaHP.getDocument()).setDocumentFilter(TextFieldrestrictions("\\d*?"));
        deltaHP.setHorizontalAlignment(SwingConstants.CENTER);
        deltaButtons.add(heal);
        heal.addActionListener(e -> healDamage());
        deltaButtons.add(damage);
        damage.addActionListener( e -> dealDamage());

        numberPanel.add(deltaHP);
        numberPanel.add(deltaButtons);

        deltaPanel.add(numberPanel);
        deltaPanel.setBorder(BorderFactory.createLineBorder(Color.darkGray));

        return deltaPanel;
    }

    private DocumentFilter TextFieldrestrictions(String regex){
         return new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (isValidInput(fb.getDocument().getText(0, fb.getDocument().getLength()), string, offset)) {
                    super.insertString(fb, offset, string, attr);
                }
            }
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (isValidInput(fb.getDocument().getText(0, fb.getDocument().getLength()), text, offset)) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
            private boolean isValidInput(String currentText, String newText, int offset) {
                StringBuilder sb = new StringBuilder(currentText);
                sb.insert(offset, newText);
                String fullText = sb.toString();

                // Allow empty string or valid number with optional '-' and '.'
                return fullText.matches(regex);
            }
        };
    }

    private void dealDamage(){
        int temphp, currhp, deltahp;

        deltahp = getDeltaHP();
        if(deltahp == 0) return;

        temphp = getTempHP();
        currhp = getCurrHP();

        if(temphp >= 0) {
            temphp -= deltahp;
            if (temphp < 0) {
                setTempHP(0);
                deltahp = temphp * -1;
            } else {
                setTempHP(temphp);
                return;
            }
        }

        currhp -= deltahp;
        if(currhp < 0) setCurrHP(0);
        else setCurrHP(currhp);

    }
    private void healDamage(){
        int maxhp, currhp, deltahp;

        deltahp = getDeltaHP();
        if(deltahp == 0) return;

        currhp = getCurrHP();
        currhp += deltahp ;

        maxhp = getMaxHP();

        if (maxhp == 0) {
            setCurrHP(currhp);
        } else{
            if(currhp > maxhp) currhp = maxhp;
            setCurrHP(currhp);
        }





    }

    public void setIniative(double iniative){
        this.iniative.setText(""+iniative);
    }
    public double getIniative(){
        if(iniative.getText().isBlank()) return 0;
        return Double.parseDouble(iniative.getText());
    }

    public void setCharName(String name){
        this.name.setText(name);
    }
    public String getCharName(){
        return name.getText();
    }

    public void setArmorClass(String armorClass){
        this.armorClass.setText(armorClass);
    }
    public String getArmorClass(){
        return armorClass.getText();
    }

    public void setCurrHP(int currHP){
        this.currHP.setText(""+currHP);
    }
    public int getCurrHP(){
        if(currHP.getText().isBlank()) return 0;
        return Integer.parseInt(currHP.getText());
    }

    public void setMaxHP(int maxHP){
        this.maxHP.setText(""+maxHP);
    }
    public int getMaxHP(){
        if(maxHP.getText().isBlank()) return 0;
        return Integer.parseInt(maxHP.getText());
    }

    public void setTempHP(int tempHP){
        this.tempHP.setText(""+tempHP);
    }
    public int getTempHP(){
        if(tempHP.getText().isBlank()) return 0;
        return Integer.parseInt(tempHP.getText());
    }

    private int getDeltaHP(){
        if(deltaHP.getText().isBlank()) return 0;
        return Integer.parseInt(deltaHP.getText());
    }

   @Override public String toString(){
        return getIniative() + "\",\"" + getCharName()  + "\",\"" + getArmorClass()  + "\",\"" + getCurrHP()  + "\",\"" + getTempHP()  + "\",\"" + getMaxHP() ;
   }

}