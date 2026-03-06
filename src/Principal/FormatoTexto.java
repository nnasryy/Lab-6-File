/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Principal;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
/**
 *
 * @author jeremy
 */

public class FormatoTexto {

    private JTextPane areaTexto;

    public FormatoTexto(JTextPane areaTexto) {
        this.areaTexto = areaTexto;
    }

    private void aplicarEstilo(SimpleAttributeSet estilo) {

        int inicio = areaTexto.getSelectionStart();
        int fin = areaTexto.getSelectionEnd();

        StyledDocument doc = areaTexto.getStyledDocument();

        doc.setCharacterAttributes(inicio, fin - inicio, estilo, false);
    }

    // Cambiar fuente
    public void cambiarFuente(String fuente) {

        SimpleAttributeSet estilo = new SimpleAttributeSet();
        StyleConstants.setFontFamily(estilo, fuente);

        aplicarEstilo(estilo);
    }

    // Cambiar tamaño
    public void cambiarTamano(int tamano) {

        SimpleAttributeSet estilo = new SimpleAttributeSet();
        StyleConstants.setFontSize(estilo, tamano);

        aplicarEstilo(estilo);
    }

    // Cambiar color
    public void cambiarColor(Color color) {

        SimpleAttributeSet estilo = new SimpleAttributeSet();
        StyleConstants.setForeground(estilo, color);

        aplicarEstilo(estilo);
    }

    // Negrita
    public void negrita() {

        SimpleAttributeSet estilo = new SimpleAttributeSet();
        StyleConstants.setBold(estilo, true);

        aplicarEstilo(estilo);
    }

    // Cursiva
    public void cursiva() {

        SimpleAttributeSet estilo = new SimpleAttributeSet();
        StyleConstants.setItalic(estilo, true);

        aplicarEstilo(estilo);
    }

    // Subrayado
    public void subrayado() {

        SimpleAttributeSet estilo = new SimpleAttributeSet();
        StyleConstants.setUnderline(estilo, true);

        aplicarEstilo(estilo);
    }
}
