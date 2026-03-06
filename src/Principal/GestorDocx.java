/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Principal;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.io.*;

import org.apache.poi.xwpf.usermodel.*;
/**
 *
 * @author nasry
 */

public class GestorDocx {

    public void guardarDocx(File archivo, JTextPane areaTexto) {

        try {

            StyledDocument doc = areaTexto.getStyledDocument();

            XWPFDocument documento = new XWPFDocument();
            XWPFParagraph parrafo = documento.createParagraph();

            for (int i = 0; i < doc.getLength(); i++) {

                Element elemento = doc.getCharacterElement(i);
                AttributeSet atributos = elemento.getAttributes();

                XWPFRun run = parrafo.createRun();

                String texto = doc.getText(i, 1);
                run.setText(texto);

                // FUENTE
                String fuente = StyleConstants.getFontFamily(atributos);
                run.setFontFamily(fuente);

                // TAMAÑO
                int size = StyleConstants.getFontSize(atributos);
                run.setFontSize(size);

                // NEGRITA
                run.setBold(StyleConstants.isBold(atributos));

                // CURSIVA
                run.setItalic(StyleConstants.isItalic(atributos));

                // SUBRAYADO
                if (StyleConstants.isUnderline(atributos)) {
                    run.setUnderline(UnderlinePatterns.SINGLE);
                }

                // COLOR
                Color c = StyleConstants.getForeground(atributos);

                if (c != null) {

                    String hex = String.format("%02X%02X%02X",
                            c.getRed(),
                            c.getGreen(),
                            c.getBlue());

                    run.setColor(hex);
                }
            }

            FileOutputStream out = new FileOutputStream(archivo);

            documento.write(out);

            out.close();
            documento.close();

            JOptionPane.showMessageDialog(null, "Archivo guardado correctamente");

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "Error al guardar archivo");
            e.printStackTrace();
        }
    }

    // ABRIR DOCX Y RESTAURAR FORMATO
    public void abrirDocx(File archivo, JTextPane areaTexto) {

        try {

            StyledDocument styledDoc = areaTexto.getStyledDocument();
            styledDoc.remove(0, styledDoc.getLength());

            FileInputStream fis = new FileInputStream(archivo);

            XWPFDocument documento = new XWPFDocument(fis);

            for (XWPFParagraph p : documento.getParagraphs()) {

                for (XWPFRun run : p.getRuns()) {

                    SimpleAttributeSet estilo = new SimpleAttributeSet();

                    // FUENTE
                    if (run.getFontFamily() != null) {
                        StyleConstants.setFontFamily(estilo, run.getFontFamily());
                    }

                    // TAMAÑO
                    if (run.getFontSize() > 0) {
                        StyleConstants.setFontSize(estilo, run.getFontSize());
                    }

                    // NEGRITA
                    StyleConstants.setBold(estilo, run.isBold());

                    // CURSIVA
                    StyleConstants.setItalic(estilo, run.isItalic());

                    // SUBRAYADO
                    if (run.getUnderline() != UnderlinePatterns.NONE) {
                        StyleConstants.setUnderline(estilo, true);
                    }

                    // COLOR
                    if (run.getColor() != null) {

                        Color color = Color.decode("#" + run.getColor());
                        StyleConstants.setForeground(estilo, color);
                    }

                    styledDoc.insertString(
                            styledDoc.getLength(),
                            run.getText(0),
                            estilo
                    );
                }

                styledDoc.insertString(
                        styledDoc.getLength(),
                        "\n",
                        null
                );
            }

            fis.close();
            documento.close();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "Error al abrir archivo");
            e.printStackTrace();
        }
    }
}
