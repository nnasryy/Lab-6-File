package Principal;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class FormatoTexto {

    private JTextPane areaTexto;

    public FormatoTexto(JTextPane areaTexto) {
        this.areaTexto = areaTexto;
    }

    public void aplicarCaracter(SimpleAttributeSet estilo) {
        int inicio = areaTexto.getSelectionStart();
        int fin    = areaTexto.getSelectionEnd();
        StyledDocument doc = areaTexto.getStyledDocument();
        if (inicio == fin) {
            areaTexto.setCharacterAttributes(estilo, false);
        } else {
            doc.setCharacterAttributes(inicio, fin - inicio, estilo, false);
        }
        areaTexto.requestFocusInWindow();
    }

    public void aplicarAlineacion(int alineacion) {
        StyledDocument doc = areaTexto.getStyledDocument();
        int inicio = areaTexto.getSelectionStart();
        int fin    = areaTexto.getSelectionEnd();
        SimpleAttributeSet estilo = new SimpleAttributeSet();
        StyleConstants.setAlignment(estilo, alineacion);
        if (inicio == fin) {
            Element elem = doc.getParagraphElement(inicio);
            doc.setParagraphAttributes(elem.getStartOffset(),
                elem.getEndOffset() - elem.getStartOffset(), estilo, false);
        } else {
            doc.setParagraphAttributes(inicio, fin - inicio, estilo, false);
        }
        areaTexto.requestFocusInWindow();
    }

    public void cambiarFuente(String fuente) {
        SimpleAttributeSet estilo = new SimpleAttributeSet();
        StyleConstants.setFontFamily(estilo, fuente);
        aplicarCaracter(estilo);
    }

    public void cambiarTamano(int tamano) {
        SimpleAttributeSet estilo = new SimpleAttributeSet();
        StyleConstants.setFontSize(estilo, tamano);
        aplicarCaracter(estilo);
    }

    public void cambiarColor(Color color) {
        SimpleAttributeSet estilo = new SimpleAttributeSet();
        StyleConstants.setForeground(estilo, color);
        aplicarCaracter(estilo);
    }

    public void negrita(boolean activo) {
        SimpleAttributeSet estilo = new SimpleAttributeSet();
        StyleConstants.setBold(estilo, activo);
        aplicarCaracter(estilo);
    }

    public void cursiva(boolean activo) {
        SimpleAttributeSet estilo = new SimpleAttributeSet();
        StyleConstants.setItalic(estilo, activo);
        aplicarCaracter(estilo);
    }

    public void subrayado(boolean activo) {
        SimpleAttributeSet estilo = new SimpleAttributeSet();
        StyleConstants.setUnderline(estilo, activo);
        aplicarCaracter(estilo);
    }
}