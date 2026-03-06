package Principal;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class EditorFrame extends JFrame {

    private JTextPane textPane;
    private StyledDocument document;
    private BarraHerramientas barraHerramientas;
    private MenuArchivo menuArchivo;
    private TablaManager tablaManager;
    private JLabel labelEstado;
    private File archivoActual;
    private boolean modificado;

    public EditorFrame() {
        super("Editor de Texto");
        modificado = false;
        archivoActual = null;
        initComponents();
        initMenu();
        initLayout();
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                menuArchivo.accionSalir();
            }
        });
        setSize(1050, 750);
        setMinimumSize(new Dimension(700, 500));
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        textPane = new JTextPane();
        textPane.setBackground(Color.WHITE);
        textPane.setMargin(new Insets(20, 32, 20, 32));
        textPane.setFont(new Font("Times New Roman", Font.PLAIN, 12));

        DefaultStyledDocument styledDoc = new DefaultStyledDocument();
        textPane.setDocument(styledDoc);
        document = styledDoc;

        document.addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e)  { modificado = true; }
            public void removeUpdate(DocumentEvent e)  { modificado = true; }
            public void changedUpdate(DocumentEvent e) {}
        });

        tablaManager      = new TablaManager(this);
        menuArchivo       = new MenuArchivo(this);
        barraHerramientas = new BarraHerramientas(this);

        textPane.addCaretListener(e -> {
            int pos = textPane.getCaretPosition();
            AttributeSet attr = document.getCharacterElement(pos > 0 ? pos - 1 : pos).getAttributes();
            barraHerramientas.sincronizar(attr);
        });

        labelEstado = new JLabel("  Listo");
        labelEstado.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        labelEstado.setBorder(BorderFactory.createEtchedBorder());
        labelEstado.setPreferredSize(new Dimension(0, 22));
    }

    private void initMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu mArchivo = new JMenu("Archivo");
        mArchivo.setMnemonic(KeyEvent.VK_A);

        JMenuItem iNuevo   = item("Nuevo",           KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        JMenuItem iAbrir   = item("Abrir...",         KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        JMenuItem iGuardar = item("Guardar",          KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        JMenuItem iGuarCom = item("Guardar como...",  KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        JMenuItem iSalir   = item("Salir",            KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));

        iNuevo  .addActionListener(e -> menuArchivo.accionNuevo());
        iAbrir  .addActionListener(e -> menuArchivo.accionAbrir());
        iGuardar.addActionListener(e -> menuArchivo.accionGuardar());
        iGuarCom.addActionListener(e -> menuArchivo.accionGuardarComo());
        iSalir  .addActionListener(e -> menuArchivo.accionSalir());

        mArchivo.add(iNuevo); mArchivo.add(iAbrir); mArchivo.addSeparator();
        mArchivo.add(iGuardar); mArchivo.add(iGuarCom); mArchivo.addSeparator();
        mArchivo.add(iSalir);

        JMenu mEditar = new JMenu("Editar");
        JMenuItem iCortar  = item("Cortar",      KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
        JMenuItem iCopiar  = item("Copiar",      KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
        JMenuItem iPegar   = item("Pegar",       KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));
        JMenuItem iSelTodo = item("Selec. todo", KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));

        iCortar .addActionListener(e -> textPane.cut());
        iCopiar .addActionListener(e -> textPane.copy());
        iPegar  .addActionListener(e -> textPane.paste());
        iSelTodo.addActionListener(e -> textPane.selectAll());

        mEditar.add(iCortar); mEditar.add(iCopiar); mEditar.add(iPegar);
        mEditar.addSeparator(); mEditar.add(iSelTodo);

        JMenu mInsertar = new JMenu("Insertar");
        JMenuItem iTabla = item("Tabla...", null);
        iTabla.addActionListener(e -> tablaManager.mostrarDialogo());
        mInsertar.add(iTabla);

        JMenu mFormato = new JMenu("Formato");
        JMenuItem iNegrita   = item("Negrita",   KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK));
        JMenuItem iCursiva   = item("Cursiva",   KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_DOWN_MASK));
        JMenuItem iSubrayado = item("Subrayado", KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_DOWN_MASK));

        iNegrita  .addActionListener(e -> barraHerramientas.toggleNegrita());
        iCursiva  .addActionListener(e -> barraHerramientas.toggleCursiva());
        iSubrayado.addActionListener(e -> barraHerramientas.toggleSubrayado());

        mFormato.add(iNegrita); mFormato.add(iCursiva); mFormato.add(iSubrayado);

        menuBar.add(mArchivo); menuBar.add(mEditar);
        menuBar.add(mInsertar); menuBar.add(mFormato);
        setJMenuBar(menuBar);
    }

    private void initLayout() {
        setLayout(new BorderLayout());
        add(barraHerramientas, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(textPane);
        scroll.getViewport().setBackground(new Color(200, 200, 200));
        scroll.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24));
        add(scroll, BorderLayout.CENTER);

        add(labelEstado, BorderLayout.SOUTH);
    }

    private JMenuItem item(String texto, KeyStroke atajo) {
        JMenuItem mi = new JMenuItem(texto);
        if (atajo != null) mi.setAccelerator(atajo);
        return mi;
    }

    public void setEstado(String msg) {
        labelEstado.setText("  " + msg);
    }

    public JTextPane         getTextPane()       { return textPane; }
    public StyledDocument    getStyledDocument() { return document; }
    public BarraHerramientas getBarraHerram()    { return barraHerramientas; }
    public TablaManager      getTablaManager()   { return tablaManager; }
    public File              getArchivoActual()  { return archivoActual; }
    public boolean           isModificado()      { return modificado; }

    public void setArchivoActual(File f) {
        archivoActual = f;
        setTitle("Editor de Texto" + (f != null ? " - " + f.getName() : ""));
    }

    public void setModificado(boolean m) { modificado = m; }
}