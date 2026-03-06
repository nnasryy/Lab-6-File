/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Principal;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextPane;

/**
 *
 * @author nasry
 */
public class MenuArchivo extends JMenuBar{
    JMenu archivo;
    JMenuItem nuevo, abrir, guardar, salir;
    
    GestorDocx gestor;
    JTextPane areaTexto;
    
     public MenuArchivo(JTextPane areaTexto) {

        this.areaTexto = areaTexto;
        gestor = new GestorDocx();

        archivo = new JMenu("Archivo");

        nuevo = new JMenuItem("Nuevo");
        abrir = new JMenuItem("Abrir");
        guardar = new JMenuItem("Guardar");
        salir = new JMenuItem("Salir");

        archivo.add(nuevo);
        archivo.add(abrir);
        archivo.add(guardar);
        archivo.addSeparator();
        archivo.add(salir);

        add(archivo);

        eventos();
    }
    
    private void eventos(){
    nuevo.addActionListener(e -> areaTexto.setText(""));
    abrir.addActionListener(e -> {

            JFileChooser chooser = new JFileChooser();

            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                File archivo = chooser.getSelectedFile();
                gestor.abrirDocx(archivo.getAbsolutePath(), areaTexto);
            }

        });
    guardar.addActionListener(e -> {

            JFileChooser chooser = new JFileChooser();

            if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                File archivo = chooser.getSelectedFile();
                gestor.guardarDocx(archivo.getAbsolutePath(), areaTexto);
            }

        });

        salir.addActionListener(e -> System.exit(0));

    }
}
