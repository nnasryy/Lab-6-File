package Principal;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.io.*;
import org.apache.poi.xwpf.usermodel.*;

public class GestorDocx {

    public void guardarDocx(File archivo, JTextPane areaTexto) {
        try {
            StyledDocument doc = areaTexto.getStyledDocument();
            XWPFDocument documento = new XWPFDocument();
            XWPFParagraph parrafoActual = documento.createParagraph();

            int i = 0;
            while (i < doc.getLength()) {
                Element elem = doc.getCharacterElement(i);
                AttributeSet attr = elem.getAttributes();
                Component comp = StyleConstants.getComponent(attr);

                if (comp instanceof JPanel) {
                    JPanel tablaPanel = (JPanel) comp;
                    guardarTablaEnDocx(documento, tablaPanel);
                    parrafoActual = documento.createParagraph();
                    i++;
                    continue;
                }

                String texto = doc.getText(i, 1);
                if (texto.equals("\n")) {
                    parrafoActual = documento.createParagraph();
                    i++;
                    continue;
                }

                XWPFRun run = parrafoActual.createRun();
                run.setText(texto);

                String fuente = StyleConstants.getFontFamily(attr);
                if (fuente != null) run.setFontFamily(fuente);

                int size = StyleConstants.getFontSize(attr);
                if (size > 0) run.setFontSize(size);

                run.setBold(StyleConstants.isBold(attr));
                run.setItalic(StyleConstants.isItalic(attr));

                if (StyleConstants.isUnderline(attr))
                    run.setUnderline(UnderlinePatterns.SINGLE);

                Color c = StyleConstants.getForeground(attr);
                if (c != null)
                    run.setColor(String.format("%02X%02X%02X", c.getRed(), c.getGreen(), c.getBlue()));

                i++;
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

    private void guardarTablaEnDocx(XWPFDocument documento, JPanel tablaPanel) {
        Component[] celdas = tablaPanel.getComponents();
        if (celdas.length == 0) return;

        int numCols = 0;
        int maxX = -1;
        for (Component c : celdas) {
            if (c.getX() > maxX) { maxX = c.getX(); numCols++; }
            else if (c.getX() <= maxX) break;
        }
        if (numCols == 0) numCols = 1;
        int numFilas = celdas.length / numCols;

        XWPFTable tabla = documento.createTable(numFilas, numCols);
        tabla.setWidth("100%");

        for (int r = 0; r < numFilas; r++) {
            XWPFTableRow fila = tabla.getRow(r);
            for (int c = 0; c < numCols; c++) {
                int idx = r * numCols + c;
                if (idx >= celdas.length) continue;

                Component comp = celdas[idx];
                if (!(comp instanceof JTextPane)) continue;

                JTextPane celda = (JTextPane) comp;
                XWPFTableCell cell = fila.getCell(c);
                if (cell == null) cell = fila.addNewTableCell();

                StyledDocument celdaDoc = celda.getStyledDocument();
                XWPFParagraph parrafo = cell.getParagraphs().get(0);

                try {
                    int len = celdaDoc.getLength();
                    for (int k = 0; k < len; k++) {
                        Element el = celdaDoc.getCharacterElement(k);
                        AttributeSet at = el.getAttributes();
                        String texto = celdaDoc.getText(k, 1);
                        if (texto.equals("\n")) continue;

                        XWPFRun run = parrafo.createRun();
                        run.setText(texto);

                        String fuente = StyleConstants.getFontFamily(at);
                        if (fuente != null) run.setFontFamily(fuente);

                        int size = StyleConstants.getFontSize(at);
                        if (size > 0) run.setFontSize(size);

                        run.setBold(StyleConstants.isBold(at));
                        run.setItalic(StyleConstants.isItalic(at));

                        if (StyleConstants.isUnderline(at))
                            run.setUnderline(UnderlinePatterns.SINGLE);

                        Color col = StyleConstants.getForeground(at);
                        if (col != null)
                            run.setColor(String.format("%02X%02X%02X", col.getRed(), col.getGreen(), col.getBlue()));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void abrirDocx(File archivo, JTextPane areaTexto) {
        try {
            StyledDocument styledDoc = areaTexto.getStyledDocument();
            styledDoc.remove(0, styledDoc.getLength());

            FileInputStream fis = new FileInputStream(archivo);
            XWPFDocument documento = new XWPFDocument(fis);

            for (IBodyElement elemento : documento.getBodyElements()) {
                if (elemento instanceof XWPFParagraph) {
                    XWPFParagraph p = (XWPFParagraph) elemento;
                    for (XWPFRun run : p.getRuns()) {
                        SimpleAttributeSet estilo = new SimpleAttributeSet();

                        if (run.getFontFamily() != null)
                            StyleConstants.setFontFamily(estilo, run.getFontFamily());
                        if (run.getFontSize() > 0)
                            StyleConstants.setFontSize(estilo, run.getFontSize());

                        StyleConstants.setBold(estilo, run.isBold());
                        StyleConstants.setItalic(estilo, run.isItalic());

                        if (run.getUnderline() != UnderlinePatterns.NONE)
                            StyleConstants.setUnderline(estilo, true);

                        if (run.getColor() != null)
                            StyleConstants.setForeground(estilo, Color.decode("#" + run.getColor()));

                        String texto = run.getText(0);
                        if (texto != null)
                            styledDoc.insertString(styledDoc.getLength(), texto, estilo);
                    }
                    styledDoc.insertString(styledDoc.getLength(), "\n", null);

                } else if (elemento instanceof XWPFTable) {
                    XWPFTable tabla = (XWPFTable) elemento;
                    cargarTablaDeDocx(styledDoc, areaTexto, tabla);
                }
            }

            fis.close();
            documento.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al abrir archivo");
            e.printStackTrace();
        }
    }

    private void cargarTablaDeDocx(StyledDocument styledDoc, JTextPane areaTexto, XWPFTable tabla) {
        try {
            int numFilas = tabla.getRows().size();
            int numCols  = numFilas > 0 ? tabla.getRows().get(0).getTableCells().size() : 0;
            if (numCols == 0) return;

            int anchoCol   = 130;
            int altoFila   = 32;
            int bordeGap   = 1;
            int totalAncho = anchoCol * numCols + bordeGap * (numCols + 1);
            int totalAlto  = altoFila * numFilas + bordeGap * (numFilas + 1);
            int realColW   = (totalAncho - bordeGap * (numCols + 1)) / numCols;
            int realRowH   = (totalAlto  - bordeGap * (numFilas + 1)) / numFilas;

            JPanel tablaPanel = new JPanel(null);
            tablaPanel.setBackground(new Color(140, 160, 190));
            tablaPanel.setPreferredSize(new Dimension(totalAncho, totalAlto));
            tablaPanel.setMaximumSize(new Dimension(totalAncho, totalAlto));
            tablaPanel.setMinimumSize(new Dimension(totalAncho, totalAlto));

            for (int r = 0; r < numFilas; r++) {
                XWPFTableRow fila = tabla.getRows().get(r);
                for (int c = 0; c < numCols; c++) {
                    int x = bordeGap + c * (realColW + bordeGap);
                    int y = bordeGap + r * (realRowH + bordeGap);

                    JTextPane celda = crearCeldaVacia(areaTexto, r);
                    celda.setBounds(x, y, realColW, realRowH);

                    if (c < fila.getTableCells().size()) {
                        XWPFTableCell cell = fila.getTableCells().get(c);
                        StyledDocument celdaDoc = celda.getStyledDocument();
                        for (XWPFParagraph p : cell.getParagraphs()) {
                            for (XWPFRun run : p.getRuns()) {
                                SimpleAttributeSet estilo = new SimpleAttributeSet();
                                if (run.getFontFamily() != null)
                                    StyleConstants.setFontFamily(estilo, run.getFontFamily());
                                if (run.getFontSize() > 0)
                                    StyleConstants.setFontSize(estilo, run.getFontSize());
                                StyleConstants.setBold(estilo, run.isBold());
                                StyleConstants.setItalic(estilo, run.isItalic());
                                if (run.getUnderline() != UnderlinePatterns.NONE)
                                    StyleConstants.setUnderline(estilo, true);
                                if (run.getColor() != null)
                                    StyleConstants.setForeground(estilo, Color.decode("#" + run.getColor()));
                                String texto = run.getText(0);
                                if (texto != null)
                                    celdaDoc.insertString(celdaDoc.getLength(), texto, estilo);
                            }
                        }
                    }

                    tablaPanel.add(celda);
                }
            }

            styledDoc.insertString(styledDoc.getLength(), "\n", null);
            SimpleAttributeSet aset = new SimpleAttributeSet();
            StyleConstants.setComponent(aset, tablaPanel);
            styledDoc.insertString(styledDoc.getLength(), " ", aset);
            styledDoc.insertString(styledDoc.getLength(), "\n", null);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private JTextPane crearCeldaVacia(JTextPane areaTexto, int fila) {
        JTextPane celda = new JTextPane();
        celda.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        celda.setBackground(fila % 2 == 0 ? Color.WHITE : new Color(235, 242, 252));
        celda.setMargin(new Insets(3, 5, 3, 5));
        celda.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
        return celda;
    }
}