package Principal;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TablaManager {

    private final EditorFrame frame;
    private DefaultTableModel modelo;

    public TablaManager(EditorFrame frame) {
        this.frame = frame;
    }

    public void mostrarDialogo() {
        JTextField filasField = new JTextField("3");
        JTextField colsField  = new JTextField("3");

        JPanel panel = new JPanel(new GridLayout(2, 2, 6, 6));
        panel.add(new JLabel("Filas:"));
        panel.add(filasField);
        panel.add(new JLabel("Columnas:"));
        panel.add(colsField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Insertar Tabla",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return;

        int filas, cols;
        try {
            filas = Integer.parseInt(filasField.getText().trim());
            cols  = Integer.parseInt(colsField.getText().trim());
            if (filas < 1 || cols < 1) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Ingrese numeros validos.");
            return;
        }

        modelo = new DefaultTableModel(filas, cols) {
            @Override
            public boolean isCellEditable(int row, int col) { return true; }
        };

        insertarTablaEditable(filas, cols);
        frame.setEstado("Tabla " + filas + "x" + cols + " insertada.");
    }

    private void insertarTablaEditable(int numFilas, int numCols) {
        StyledDocument doc = frame.getStyledDocument();
        int pos = frame.getTextPane().getCaretPosition();

        JTable tabla = new JTable(modelo) {
            @Override
            public boolean isCellEditable(int row, int col) { return true; }
        };

        tabla.setTableHeader(null);
        tabla.setRowHeight(28);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setGridColor(new Color(180, 195, 215));
        tabla.setSelectionBackground(new Color(180, 210, 240));
        tabla.setSelectionForeground(Color.BLACK);
        tabla.setIntercellSpacing(new Dimension(1, 1));
        tabla.setShowGrid(true);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
                if (!isSelected) {
                    setBackground(row % 2 == 0 ? Color.WHITE : new Color(235, 242, 252));
                    setForeground(Color.BLACK);
                }
                setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(180, 195, 215)));
                return this;
            }
        };
        renderer.setHorizontalAlignment(SwingConstants.LEFT);

        for (int i = 0; i < numCols; i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        int anchoCol   = 120;
        int totalAncho = Math.min(anchoCol * numCols + 2, frame.getTextPane().getWidth() - 60);
        if (totalAncho < 200) totalAncho = Math.min(anchoCol * numCols + 2, 700);
        int totalAlto  = (28 + 1) * numFilas + 4;

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.add(tabla, BorderLayout.CENTER);
        contenedor.setBorder(BorderFactory.createLineBorder(new Color(180, 195, 215), 1));
        contenedor.setPreferredSize(new Dimension(totalAncho, totalAlto));
        contenedor.setMaximumSize(new Dimension(totalAncho, totalAlto));
        contenedor.setMinimumSize(new Dimension(totalAncho, totalAlto));

        try {
            doc.insertString(pos, "\n", null);
            pos++;

            SimpleAttributeSet aset = new SimpleAttributeSet();
            StyleConstants.setComponent(aset, contenedor);
            doc.insertString(pos, " ", aset);
            pos++;

            doc.insertString(pos, "\n", null);
            frame.getTextPane().setCaretPosition(pos + 1);

        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }

    public List<String[]> getDatosTabla() {
        List<String[]> data = new ArrayList<>();
        if (modelo == null) return data;
        for (int r = 0; r < modelo.getRowCount(); r++) {
            String[] fila = new String[modelo.getColumnCount()];
            for (int c = 0; c < modelo.getColumnCount(); c++) {
                Object val = modelo.getValueAt(r, c);
                fila[c] = (val == null) ? "" : val.toString();
            }
            data.add(fila);
        }
        return data;
    }
}