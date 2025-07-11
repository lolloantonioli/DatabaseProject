package it.unibo.view.amministratore;

import java.awt.BorderLayout;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import it.unibo.controller.Controller;

class TopRiderConsegnePanel extends JPanel {

    private final DefaultTableModel model = new DefaultTableModel(new String[]{"Codice", "Nome", "Cognome", "Consegne"}, 0);

    public TopRiderConsegnePanel(Controller controller) {
        setLayout(new BorderLayout());
        JPanel inputPanel = new JPanel();
        JTextField fromField = new JTextField(10); // "yyyy-mm-dd"
        JTextField toField = new JTextField(10);
        JButton searchBtn = new JButton("Cerca");
        inputPanel.add(new JLabel("Da (yyyy-mm-dd):")); inputPanel.add(fromField);
        inputPanel.add(new JLabel("A (yyyy-mm-dd):")); inputPanel.add(toField);
        inputPanel.add(searchBtn);
        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(new JTable(model)), BorderLayout.CENTER);

        searchBtn.addActionListener(e -> {
            model.setRowCount(0);
            try {
                var from = LocalDate.parse(fromField.getText());
                var to = LocalDate.parse(toField.getText());

                var fromTimestamp = Timestamp.valueOf(from.atStartOfDay());
                var toTimestamp = Timestamp.valueOf(to.atTime(23, 59, 59));
                List<Object[]> results = controller.getModel().topRiderPerConsegneInPeriodo(fromTimestamp, toTimestamp);
                for (Object[] row : results)
                    model.addRow(row);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Formato data non valido!", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
