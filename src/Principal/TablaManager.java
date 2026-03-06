/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Principal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TablaManager {
    
    private final EditorFrame frame;
    private JTable tablaVisual;
    private DefaultTableModel modelo;

    public TablaManager(EditorFrame frame) {
        this.frame = frame;
    }

    public void mostrarDialogo() {
        // Pedir dimensiones
        JTextField filasField = new JTextField("3");
        JTextField colsField = new JTextField("3");
        
        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Filas:"));
        panel.add(filasField);
        panel.add(new JLabel("Columnas:"));
        panel.add(colsField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Crear Tabla", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                int filas = Integer.parseInt(filasField.getText());
                int cols = Integer.parseInt(colsField.getText());
                
                crearTablaVisual(filas, cols);
                
                // Mostrar tabla en un diálogo para edición
                JScrollPane scroll = new JScrollPane(tablaVisual);
                scroll.setPreferredSize(new Dimension(400, 200));
                
                int res = JOptionPane.showConfirmDialog(frame, scroll, "Ingrese datos de la tabla", JOptionPane.OK_CANCEL_OPTION);
                if (res == JOptionPane.OK_OPTION) {
                    JOptionPane.showMessageDialog(frame, "Tabla lista para guardar.\nUse Archivo -> Guardar para guardarla en el DOCX.");
                }
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Ingrese números válidos");
            }
        }
    }
    
    private void crearTablaVisual(int filas, int cols) {
        modelo = new DefaultTableModel(filas, cols);
        tablaVisual = new JTable(modelo);
        
        // Nombres de columnas por defecto
        for (int i = 0; i < cols; i++) {
            modelo.setColumnIdentifiers(generarNombres(cols));
        }
    }
    
    private Object[] generarNombres(int n) {
        String[] names = new String[n];
        for (int i = 0; i < n; i++) names[i] = "Col " + (i+1);
        return names;
    }
    
    // Convierte la tabla a una lista para guardar en DOCX
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