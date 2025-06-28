import lombok.Getter;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class ButtonPanel extends JPanel{

    @Getter
    private JButton addEntry, sortList, exportAsTemplate, importTemplate;

    public ButtonPanel(TrackingPanel trackingPanel){
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 2));
        addEntry = new JButton("New");
        addEntry.addActionListener(e -> addEvent(trackingPanel));

        sortList = new JButton("Sort");
        sortList.addActionListener(e -> sortEvent(trackingPanel));

        importTemplate = new JButton("Import");
        importTemplate.addActionListener(e -> importEvent(trackingPanel));
        exportAsTemplate = new JButton("Export");
        exportAsTemplate.addActionListener(e -> exportEvent(trackingPanel));

        this.add(addEntry);
        this.add(sortList);
        this.add(importTemplate);
        this.add(exportAsTemplate);
    }

    private void addEvent(TrackingPanel trackingPanel){
        trackingPanel.addEntry();
    }
    private void sortEvent(TrackingPanel trackingPanel) {trackingPanel.sortAllEntries();}
    private void exportEvent(TrackingPanel trackingPanel){
        JFileChooser fc = new JFileChooser(getTemplateDirectory());
        fc.setDialogTitle("Export as CSV");
        fc.setSelectedFile(new File("template.csv"));

        int userSelection = fc.showSaveDialog(trackingPanel);

        if(userSelection == JFileChooser.APPROVE_OPTION){
            File file = fc.getSelectedFile();
            if(!file.getName().toLowerCase().endsWith(".csv")){
                file = new File(file.getAbsolutePath()+".csv");
            }

            try(FileWriter writer = new FileWriter(file)){

                writer.write("\"Iniative\",\"Name\",\"Armor Class\",\"Current HP\",\"Temporary HP\",\"Max HP\"\n");
                for (JPanel jpanel : trackingPanel.getAllEntries()){

                    for (Component comp : jpanel.getComponents()){
                        if(comp instanceof TrackEntry) {
                            writer.write("\"" + comp.toString() + "\"\n");
                        }
                    }
                }
                JOptionPane.showMessageDialog(trackingPanel, "CSV file saved successfully");
            }catch (IOException e){
                JOptionPane.showMessageDialog(trackingPanel, "Error saving file: " + e.getMessage());
            }
        }

    }
    private void importEvent(TrackingPanel trackingPanel){
        JFileChooser fc = new JFileChooser(getTemplateDirectory());
        fc.setDialogTitle("Import CSV File");

        int userSelection = fc.showOpenDialog(null);


        if(userSelection == JFileChooser.APPROVE_OPTION){
            File file = fc.getSelectedFile();
            try(BufferedReader br = new BufferedReader(new FileReader(file))){
                String line;
                ArrayList<String[]> rows = new ArrayList<>();
                String[] columnNames = null;

                boolean isFirstLine = true;

                while((line = br.readLine()) != null){
                    String[] fields = parseCSVLine(line);
                    if (isFirstLine) {
                        columnNames = fields;
                        isFirstLine = false;
                    } else {
                        rows.add(fields);
                    }
                }

                trackingPanel.getAllEntries().clear();
                trackingPanel.removeAll();
                for (String[] entry : rows){
                    TrackEntry trackEntry = new TrackEntry();
                    trackEntry.setIniative(Double.parseDouble(entry[0]));
                    trackEntry.setCharName(entry[1]);
                    trackEntry.setArmorClass(entry[2]);
                    trackEntry.setCurrHP(Integer.parseInt(entry[3]));
                    trackEntry.setTempHP(Integer.parseInt(entry[4]));
                    trackEntry.setMaxHP(Integer.parseInt(entry[5]));

                    trackingPanel.addEntry(trackEntry);
                }
                trackingPanel.sortAllEntries();

                JOptionPane.showMessageDialog(trackingPanel, "CSV imported successfully");

            }catch (IOException e){
                JOptionPane.showMessageDialog(trackingPanel, "Error opening CSV file: " + e.getMessage());
            }
        }
    }

    private static String[] parseCSVLine(String line) {
        ArrayList<String> tokens = new ArrayList<>();
        boolean insideQuote = false;
        StringBuilder sb = new StringBuilder();

        for (char c : line.toCharArray()) {
            if (c == '\"') {
                insideQuote = !insideQuote; // toggle
            } else if (c == ',' && !insideQuote) {
                tokens.add(sb.toString().trim());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }

        tokens.add(sb.toString().trim()); // last field
        return tokens.toArray(new String[0]);
    }

    public static File getTemplateDirectory() {
        try {
            // Get the location of the .jar file
            String path = Application.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath() ;
            File jarFile = new File(path);
            File parentDir = new File(jarFile.getParentFile().getPath()+"/templates"); // This is the directory the jar is in
            return parentDir;
        } catch (Exception e) {
            e.printStackTrace();
            return new File("."); // fallback to current working dir
        }
    }
}

