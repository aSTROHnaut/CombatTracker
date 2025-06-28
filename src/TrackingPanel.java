import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TrackingPanel extends JPanel {

    private ArrayList<JPanel> allEntries = new ArrayList<>();

    public TrackingPanel(){
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.setVisible(true);
    }

    public void addEntry(TrackEntry trackEntry){

        JPanel trackPanel = new JPanel();
        trackPanel.setLayout(new BoxLayout(trackPanel, BoxLayout.X_AXIS));
        trackPanel.add(trackEntry);

        JButton delete = new JButton("X");
        delete.addActionListener( e -> deleteEvent(trackPanel));
        trackPanel.add(delete);

        allEntries.add(trackPanel);
        sortAllEntries();
    }
    public void addEntry(){
        TrackEntry trackEntry = new TrackEntry();
        JPanel trackPanel = new JPanel();
        trackPanel.setLayout(new BoxLayout(trackPanel, BoxLayout.X_AXIS));
        trackPanel.add(trackEntry);

        JButton delete = new JButton("X");
        delete.addActionListener( e -> deleteEvent(trackPanel));
        trackPanel.add(delete);

        allEntries.add(trackPanel);
        sortAllEntries();
    }
    public void sortAllEntries(){
        try {
            allEntries.sort((panel1, panel2)->{
                TrackEntry entry1 = null;
                TrackEntry entry2 = null;

                for (Component comp : panel1.getComponents()){
                    if(comp instanceof TrackEntry) {
                        entry1 = (TrackEntry) comp;
                    }
                }
                for (Component comp : panel2.getComponents()){
                    if(comp instanceof TrackEntry) {
                        entry2 = (TrackEntry) comp;
                    }
                }
                if(entry1 == null || entry2 == null) throw new IllegalStateException("One of the EntryPanels does not contain a Track Entry");

                return Double.compare(entry2.getIniative(), entry1.getIniative());
            });
        }catch (Exception e){
            JOptionPane.showMessageDialog(this, "Fehler beim Sortieren: " + e.getMessage());
        }

        this.removeAll();
        for (JPanel entry : allEntries){
            this.add(entry);
        }
        this.revalidate();
        this.repaint();
    }

    public ArrayList<JPanel> getAllEntries() {
        return allEntries;
    }

    private void deleteEvent(JPanel trackPanel){
        allEntries.remove(trackPanel);
        this.remove(trackPanel);
        this.revalidate();
        this.repaint();
    }
}
