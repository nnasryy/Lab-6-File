package Principal;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class BarraHerramientas extends JToolBar {

    private final EditorFrame frame;
    private FormatoTexto formato;
    private JTextPane lastFocusedPane;

    private JComboBox<String> comboFuente;
    private JComboBox<String> comboTamano;
    private JButton           btnColor;
    private JToggleButton     btnNegrita;
    private JToggleButton     btnCursiva;
    private JToggleButton     btnSubrayado;
    private JButton           btnAlignLeft;
    private JButton           btnAlignCenter;
    private JButton           btnAlignRight;
    private JButton           btnAlignJustify;

    private Color colorActual = Color.BLACK;
    private boolean sincronizando = false;

    private static final String[] FUENTES = {
        "Arial", "Times New Roman", "Courier New", "Georgia", "Verdana",
        "Tahoma", "Trebuchet MS", "Palatino Linotype", "Garamond",
        "Book Antiqua", "Comic Sans MS", "Impact", "Lucida Console", "Segoe UI"
    };

    private static final String[] TAMANOS = {
        "8","9","10","11","12","14","16","18","20","22",
        "24","28","32","36","40","48","56","64","72","96"
    };

    public BarraHerramientas(EditorFrame frame) {
        super(JToolBar.HORIZONTAL);
        this.frame          = frame;
        this.lastFocusedPane = frame.getTextPane();
        this.formato        = new FormatoTexto(frame.getTextPane());
        setFloatable(false);
        setRollover(true);
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(180, 180, 180)));
        construir();
    }

    public void setTargetPane(JTextPane pane) {
        this.lastFocusedPane = pane;
        this.formato = new FormatoTexto(pane);
    }

    private FormatoTexto getFormato() {
        return new FormatoTexto(lastFocusedPane);
    }

    private void construir() {
        add(new JLabel(" Fuente: "));

        comboFuente = new JComboBox<>(FUENTES);
        comboFuente.setMaximumSize(new Dimension(175, 28));
        comboFuente.setPreferredSize(new Dimension(175, 28));
        comboFuente.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setFont(new Font((String) value, Font.PLAIN, 13));
                return this;
            }
        });
        comboFuente.addActionListener(e -> {
            if (!sincronizando) {
                String fuente = (String) comboFuente.getSelectedItem();
                if (fuente != null) {
                    SimpleAttributeSet a = new SimpleAttributeSet();
                    StyleConstants.setFontFamily(a, fuente);
                    getFormato().aplicarCaracter(a);
                }
            }
        });
        add(comboFuente);

        addSeparator(new Dimension(6, 0));
        add(new JLabel("Tamano: "));

        comboTamano = new JComboBox<>(TAMANOS);
        comboTamano.setSelectedItem("12");
        comboTamano.setEditable(true);
        comboTamano.setMaximumSize(new Dimension(68, 28));
        comboTamano.setPreferredSize(new Dimension(68, 28));
        comboTamano.addActionListener(e -> {
            if (!sincronizando) {
                try {
                    int tam = Integer.parseInt(comboTamano.getSelectedItem().toString().trim());
                    if (tam > 0 && tam <= 500) {
                        SimpleAttributeSet a = new SimpleAttributeSet();
                        StyleConstants.setFontSize(a, tam);
                        getFormato().aplicarCaracter(a);
                    }
                } catch (NumberFormatException ignored) {}
            }
        });
        add(comboTamano);

        addSeparator(new Dimension(6, 0));

        btnColor = new JButton("A");
        btnColor.setFont(new Font("Arial", Font.BOLD, 14));
        btnColor.setForeground(colorActual);
        btnColor.setToolTipText("Color de fuente");
        btnColor.setPreferredSize(new Dimension(32, 28));
        btnColor.setMaximumSize(new Dimension(32, 28));
        btnColor.addActionListener(e -> {
            Color c = JColorChooser.showDialog(frame, "Color de fuente", colorActual);
            if (c != null) {
                colorActual = c;
                btnColor.setForeground(c);
                SimpleAttributeSet a = new SimpleAttributeSet();
                StyleConstants.setForeground(a, c);
                getFormato().aplicarCaracter(a);
            }
        });
        add(btnColor);

        addSeparator(new Dimension(8, 0));

        btnNegrita = toggle("<html><b>N</b></html>", "Negrita (Ctrl+B)");
        btnNegrita.addActionListener(e -> {
            if (!sincronizando) {
                SimpleAttributeSet a = new SimpleAttributeSet();
                StyleConstants.setBold(a, btnNegrita.isSelected());
                getFormato().aplicarCaracter(a);
            }
        });
        add(btnNegrita);

        btnCursiva = toggle("<html><i>C</i></html>", "Cursiva (Ctrl+I)");
        btnCursiva.addActionListener(e -> {
            if (!sincronizando) {
                SimpleAttributeSet a = new SimpleAttributeSet();
                StyleConstants.setItalic(a, btnCursiva.isSelected());
                getFormato().aplicarCaracter(a);
            }
        });
        add(btnCursiva);

        btnSubrayado = toggle("<html><u>S</u></html>", "Subrayado (Ctrl+U)");
        btnSubrayado.addActionListener(e -> {
            if (!sincronizando) {
                SimpleAttributeSet a = new SimpleAttributeSet();
                StyleConstants.setUnderline(a, btnSubrayado.isSelected());
                getFormato().aplicarCaracter(a);
            }
        });
        add(btnSubrayado);

        addSeparator(new Dimension(8, 0));

        btnAlignLeft    = boton("Izq",  "Alinear izquierda");
        btnAlignCenter  = boton("Cen",  "Centrar");
        btnAlignRight   = boton("Der",  "Alinear derecha");
        btnAlignJustify = boton("Just", "Justificar");

        btnAlignLeft   .addActionListener(e -> getFormato().aplicarAlineacion(StyleConstants.ALIGN_LEFT));
        btnAlignCenter .addActionListener(e -> getFormato().aplicarAlineacion(StyleConstants.ALIGN_CENTER));
        btnAlignRight  .addActionListener(e -> getFormato().aplicarAlineacion(StyleConstants.ALIGN_RIGHT));
        btnAlignJustify.addActionListener(e -> getFormato().aplicarAlineacion(StyleConstants.ALIGN_JUSTIFIED));

        add(btnAlignLeft);
        add(btnAlignCenter);
        add(btnAlignRight);
        add(btnAlignJustify);

        addSeparator(new Dimension(8, 0));

        JButton btnTabla = boton("Tabla", "Insertar tabla");
        btnTabla.addActionListener(e -> frame.getTablaManager().mostrarDialogo());
        add(btnTabla);
    }

    public void sincronizar(AttributeSet attr) {
        if (attr == null) return;
        sincronizando = true;
        try {
            String fuente = StyleConstants.getFontFamily(attr);
            if (fuente != null) comboFuente.setSelectedItem(fuente);

            int tam = StyleConstants.getFontSize(attr);
            if (tam > 0) comboTamano.setSelectedItem(String.valueOf(tam));

            Color c = StyleConstants.getForeground(attr);
            if (c != null) { colorActual = c; btnColor.setForeground(c); }

            btnNegrita  .setSelected(StyleConstants.isBold(attr));
            btnCursiva  .setSelected(StyleConstants.isItalic(attr));
            btnSubrayado.setSelected(StyleConstants.isUnderline(attr));
        } finally {
            sincronizando = false;
        }
    }

    public void toggleNegrita() {
        btnNegrita.setSelected(!btnNegrita.isSelected());
        SimpleAttributeSet a = new SimpleAttributeSet();
        StyleConstants.setBold(a, btnNegrita.isSelected());
        getFormato().aplicarCaracter(a);
    }

    public void toggleCursiva() {
        btnCursiva.setSelected(!btnCursiva.isSelected());
        SimpleAttributeSet a = new SimpleAttributeSet();
        StyleConstants.setItalic(a, btnCursiva.isSelected());
        getFormato().aplicarCaracter(a);
    }

    public void toggleSubrayado() {
        btnSubrayado.setSelected(!btnSubrayado.isSelected());
        SimpleAttributeSet a = new SimpleAttributeSet();
        StyleConstants.setUnderline(a, btnSubrayado.isSelected());
        getFormato().aplicarCaracter(a);
    }

    private JToggleButton toggle(String texto, String tooltip) {
        JToggleButton b = new JToggleButton(texto);
        b.setToolTipText(tooltip);
        b.setPreferredSize(new Dimension(32, 28));
        b.setMaximumSize(new Dimension(32, 28));
        return b;
    }

    private JButton boton(String texto, String tooltip) {
        JButton b = new JButton(texto);
        b.setToolTipText(tooltip);
        b.setPreferredSize(new Dimension(46, 28));
        b.setMaximumSize(new Dimension(46, 28));
        b.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return b;
    }
}