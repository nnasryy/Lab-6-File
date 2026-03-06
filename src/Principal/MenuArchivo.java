package Principal;

import java.io.File;
import javax.swing.*;
import javax.swing.text.BadLocationException;

public class MenuArchivo extends JMenuBar {

    JMenu archivo;
    JMenuItem nuevo, abrir, guardar, salir;

    GestorDocx gestor;
    EditorFrame frame;

    public MenuArchivo(EditorFrame frame) {
        this.frame = frame;
        gestor = new GestorDocx();

        archivo = new JMenu("Archivo");

        nuevo   = new JMenuItem("Nuevo");
        abrir   = new JMenuItem("Abrir");
        guardar = new JMenuItem("Guardar");
        salir   = new JMenuItem("Salir");

        archivo.add(nuevo);
        archivo.add(abrir);
        archivo.add(guardar);
        archivo.addSeparator();
        archivo.add(salir);

        add(archivo);

        eventos();
    }

    private void eventos() {
        nuevo.addActionListener(e -> accionNuevo());
        abrir.addActionListener(e -> accionAbrir());
        guardar.addActionListener(e -> accionGuardar());
        salir.addActionListener(e -> accionSalir());
    }

    public void accionNuevo() {
        if (!confirmarDescarte()) return;
        try {
            frame.getStyledDocument().remove(0, frame.getStyledDocument().getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        frame.setArchivoActual(null);
        frame.setModificado(false);
        frame.setEstado("Nuevo documento.");
    }

    public void accionAbrir() {
        if (!confirmarDescarte()) return;
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            gestor.abrirDocx(f, frame.getTextPane());
            frame.setArchivoActual(f);
            frame.setModificado(false);
            frame.setEstado("Abierto: " + f.getName());
        }
    }

    public void accionGuardar() {
        if (frame.getArchivoActual() == null) {
            accionGuardarComo();
        } else {
            gestor.guardarDocx(frame.getArchivoActual(), frame.getTextPane());
            frame.setModificado(false);
            frame.setEstado("Guardado: " + frame.getArchivoActual().getName());
        }
    }

    public void accionGuardarComo() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            if (!f.getName().toLowerCase().endsWith(".docx"))
                f = new File(f.getAbsolutePath() + ".docx");
            gestor.guardarDocx(f, frame.getTextPane());
            frame.setArchivoActual(f);
            frame.setModificado(false);
            frame.setEstado("Guardado: " + f.getName());
        }
    }

    public void accionSalir() {
        if (confirmarDescarte()) frame.dispose();
    }

    private boolean confirmarDescarte() {
        if (!frame.isModificado()) return true;
        int r = JOptionPane.showConfirmDialog(
            frame,
            "Hay cambios sin guardar. Desea guardarlos?",
            "Cambios sin guardar",
            JOptionPane.YES_NO_CANCEL_OPTION
        );
        if (r == JOptionPane.YES_OPTION)  { accionGuardar(); return true; }
        if (r == JOptionPane.NO_OPTION)   return true;
        return false;
    }
}