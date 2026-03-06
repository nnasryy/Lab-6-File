package Principal;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class TablaManager {

    private final EditorFrame frame;

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

        insertarTabla(filas, cols);
        frame.setEstado("Tabla " + filas + "x" + cols + " insertada.");
    }

    private void insertarTabla(int numFilas, int numCols) {
        StyledDocument doc = frame.getStyledDocument();
        int pos = frame.getTextPane().getCaretPosition();

        int anchoCol   = 130;
        int altoFila   = 32;
        int bordeGap   = 1;
        int totalAncho = anchoCol * numCols + bordeGap * (numCols + 1);
        int editorW    = frame.getTextPane().getWidth() - 80;
        if (editorW > 100 && totalAncho > editorW) totalAncho = editorW;
        int totalAlto  = altoFila * numFilas + bordeGap * (numFilas + 1);

        JPanel tablaPanel = new JPanel(null);
        tablaPanel.setBackground(new Color(140, 160, 190));
        tablaPanel.setPreferredSize(new Dimension(totalAncho, totalAlto));
        tablaPanel.setMaximumSize(new Dimension(totalAncho, totalAlto));
        tablaPanel.setMinimumSize(new Dimension(totalAncho, totalAlto));

        int realColW = (totalAncho - bordeGap * (numCols + 1)) / numCols;
        int realRowH = (totalAlto  - bordeGap * (numFilas + 1)) / numFilas;

        for (int r = 0; r < numFilas; r++) {
            for (int c = 0; c < numCols; c++) {
                int x = bordeGap + c * (realColW + bordeGap);
                int y = bordeGap + r * (realRowH + bordeGap);
                JTextPane celda = crearCelda(r);
                celda.setBounds(x, y, realColW, realRowH);
                tablaPanel.add(celda);
            }
        }

        try {
            doc.insertString(pos, "\n", null);
            pos++;

            SimpleAttributeSet aset = new SimpleAttributeSet();
            StyleConstants.setComponent(aset, tablaPanel);
            doc.insertString(pos, " ", aset);
            pos++;

            doc.insertString(pos, "\n", null);
            frame.getTextPane().setCaretPosition(pos + 1);

        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }

    private JTextPane crearCelda(int fila) {
        JTextPane celda = new JTextPane();
        celda.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        celda.setBackground(fila % 2 == 0 ? Color.WHITE : new Color(235, 242, 252));
        celda.setMargin(new Insets(3, 5, 3, 5));
        celda.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));

        celda.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                frame.getBarraHerram().setTargetPane(celda);
                int p = celda.getCaretPosition();
                AttributeSet attr = celda.getStyledDocument()
                        .getCharacterElement(p > 0 ? p - 1 : p).getAttributes();
                frame.getBarraHerram().sincronizar(attr);
            }
        });

        celda.addCaretListener(e -> {
            if (celda.isFocusOwner()) {
                int p = celda.getCaretPosition();
                AttributeSet attr = celda.getStyledDocument()
                        .getCharacterElement(p > 0 ? p - 1 : p).getAttributes();
                frame.getBarraHerram().sincronizar(attr);
            }
        });

        return celda;
    }

    public List<String[]> getDatosTabla() {
        return new ArrayList<>();
    }
}