/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Principal;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class FormatoTexto {

    private JTextPane areaTexto;

    public FormatoTexto(JTextPane areaTexto) {
        this.areaTexto = areaTexto;
    }

    // MÉTODO BASE: Ahora es más inteligente para combinar estilos
    private void aplicarEstilo(SimpleAttributeSet estilo) {
        int inicio = areaTexto.getSelectionStart();
        int fin = areaTexto.getSelectionEnd();

        if (inicio == fin) return; // Pro Tip: No hace nada si no hay selección

        StyledDocument doc = areaTexto.getStyledDocument();
        // El 'false' es clave para que NO borre los estilos anteriores
        doc.setCharacterAttributes(inicio, fin - inicio, estilo, false);
    }

    // FUENTE Y TAMAÑO
    public void cambiarFuente(String fuente) {
        SimpleAttributeSet estilo = new SimpleAttributeSet();
        StyleConstants.setFontFamily(estilo, fuente);
        aplicarEstilo(estilo);
    }

    public void cambiarTamano(int tamano) {
        SimpleAttributeSet estilo = new SimpleAttributeSet();
        StyleConstants.setFontSize(estilo, tamano);
        aplicarEstilo(estilo);
    }

    public void cambiarColor(Color color) {
        SimpleAttributeSet estilo = new SimpleAttributeSet();
        StyleConstants.setForeground(estilo, color);
        aplicarEstilo(estilo);
    }

    // MEJORA: MÉTODOS TIPO "TOGGLE" (Prender/Apagar)
    public void alternarNegrita() {
        StyledDocument doc = areaTexto.getStyledDocument();
        Element elemento = doc.getCharacterElement(areaTexto.getSelectionStart());
        AttributeSet atributos = elemento.getAttributes();

        // Si ya es negrita, lo quita; si no, lo pone
        boolean esNegrita = StyleConstants.isBold(atributos);
        SimpleAttributeSet estilo = new SimpleAttributeSet();
        StyleConstants.setBold(estilo, !esNegrita);
        
        aplicarEstilo(estilo);
    }

    public void alternarCursiva() {
        StyledDocument doc = areaTexto.getStyledDocument();
        Element elemento = doc.getCharacterElement(areaTexto.getSelectionStart());
        AttributeSet atributos = elemento.getAttributes();

        boolean esCursiva = StyleConstants.isItalic(atributos);
        SimpleAttributeSet estilo = new SimpleAttributeSet();
        StyleConstants.setItalic(estilo, !esCursiva);
        
        aplicarEstilo(estilo);
    }

    public void alternarSubrayado() {
        StyledDocument doc = areaTexto.getStyledDocument();
        Element elemento = doc.getCharacterElement(areaTexto.getSelectionStart());
        AttributeSet atributos = elemento.getAttributes();

        boolean esSubrayado = StyleConstants.isUnderline(atributos);
        SimpleAttributeSet estilo = new SimpleAttributeSet();
        StyleConstants.setUnderline(estilo, !esSubrayado);
        
        aplicarEstilo(estilo);
    }
}