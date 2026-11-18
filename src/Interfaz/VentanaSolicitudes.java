package Interfaz;

import modelo.Adoptante;
import modelo.Animal;
import negocio.GestorAdoptantes;
import negocio.GestorAnimales;
import negocio.GestorSolicitudes;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class VentanaSolicitudes extends JFrame {
    private GestorSolicitudes gestor;
    private GestorAdoptantes gestorAdoptantes;
    private GestorAnimales gestorAnimales;

    private JTextField txtId;
    private JTextField txtCedulaAdoptante;
    private JTextField txtCodigoAnimal;
    private JTextArea txtResultado;

    public VentanaSolicitudes(GestorSolicitudes gestor, GestorAdoptantes gestorAdoptantes, GestorAnimales gestorAnimales) {
        this.gestor = gestor;
        this.gestorAdoptantes = gestorAdoptantes;
        this.gestorAnimales = gestorAnimales;
        inicializarComponentes();
    }

    public VentanaSolicitudes() {
        this(new GestorSolicitudes(), new GestorAdoptantes(), new GestorAnimales());
    }

    private void inicializarComponentes() {
        setTitle("Gestión de Solicitudes de Adopción - Módulo 3");
        setSize(525, 520);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblTitulo = new JLabel("Módulo 3: Gestión de Solicitudes");
        lblTitulo.setBounds(150, 20, 250, 25);
        add(lblTitulo);

        JLabel lblId = new JLabel("ID Solicitud:");
        lblId.setBounds(50, 60, 140, 25);
        add(lblId);

        txtId = new JTextField("SOL-001");
        txtId.setBounds(200, 60, 230, 25);
        ((AbstractDocument) txtId.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) return;
                StringBuilder sb = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
                sb.insert(offset, string.toUpperCase());
                if (sb.toString().matches("^(S(O(L(-\\d*)?)?)?)?$")) {
                    super.insertString(fb, offset, string.toUpperCase(), attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) return;
                StringBuilder sb = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
                sb.replace(offset, offset + length, text.toUpperCase());
                if (sb.toString().matches("^(S(O(L(-\\d*)?)?)?)?$")) {
                    super.replace(fb, offset, length, text.toUpperCase(), attrs);
                }
            }
        });
        add(txtId);

        JLabel lblAdoptante = new JLabel("Cédula Adoptante:");
        lblAdoptante.setBounds(50, 100, 140, 25);
        add(lblAdoptante);

        txtCedulaAdoptante = new JTextField();
        txtCedulaAdoptante.setBounds(200, 100, 230, 25);
        ((AbstractDocument) txtCedulaAdoptante.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) return;
                StringBuilder sb = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
                sb.insert(offset, string);
                if (sb.toString().matches("^\\d{0,10}$")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) return;
                StringBuilder sb = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
                sb.replace(offset, offset + length, text);
                if (sb.toString().matches("^\\d{0,10}$")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
        add(txtCedulaAdoptante);

        JLabel lblAnimalAsignado = new JLabel("Código Animal:");
        lblAnimalAsignado.setBounds(50, 140, 140, 25);
        add(lblAnimalAsignado);

        txtCodigoAnimal = new JTextField();
        txtCodigoAnimal.setBounds(200, 140, 230, 25);
        ((AbstractDocument) txtCodigoAnimal.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) return;
                StringBuilder sb = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
                sb.insert(offset, string.toUpperCase());
                if (sb.toString().matches("^(A(N(I(-\\d*)?)?)?)?$")) {
                    super.insertString(fb, offset, string.toUpperCase(), attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) return;
                StringBuilder sb = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
                sb.replace(offset, offset + length, text.toUpperCase());
                if (sb.toString().matches("^(A(N(I(-\\d*)?)?)?)?$")) {
                    super.replace(fb, offset, length, text.toUpperCase(), attrs);
                }
            }
        });
        add(txtCodigoAnimal);

        JButton btnRegistrar = new JButton("Registrar Solicitud");
        btnRegistrar.setBounds(140, 185, 240, 30);
        add(btnRegistrar);

        JButton btnEvaluar = new JButton("Procesar Evaluación");
        btnEvaluar.setBounds(140, 225, 240, 30);
        add(btnEvaluar);

        JButton btnLimpiar = new JButton("Limpiar Campos");
        btnLimpiar.setBounds(140, 265, 240, 30);
        add(btnLimpiar);

        txtResultado = new JTextArea();
        txtResultado.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtResultado);
        scrollPane.setBounds(50, 310, 410, 140);
        add(scrollPane);

        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = txtId.getText().trim();
                String cedula = txtCedulaAdoptante.getText().trim();
                String codigoAnimal = txtCodigoAnimal.getText().trim();

                if (id.isEmpty() || cedula.isEmpty() || codigoAnimal.isEmpty()) {
                    txtResultado.setText("Error: Todos los campos (ID, Cédula y Código de Animal) son obligatorios.");
                    return;
                }

                if (!id.matches("^SOL-\\d+$")) {
                    txtResultado.setText("Error: El ID de solicitud debe tener exactamente el formato SOL-(número), por ejemplo: SOL-001.");
                    return;
                }

                if (!cedula.matches("^\\d{10}$")) {
                    txtResultado.setText("Error: La cédula debe contener exactamente 10 dígitos numéricos.");
                    return;
                }

                if (!codigoAnimal.matches("^ANI-\\d+$")) {
                    txtResultado.setText("Error: El código de animal debe tener exactamente el formato ANI-(número), por ejemplo: ANI-0001.");
                    return;
                }

                if (gestor.buscarSolicitudPorId(id) != null) {
                    txtResultado.setText("Error: El ID de solicitud (" + id + ") ya existe y no puede registrarse nuevamente.");
                    return;
                }

                Adoptante ad = gestorAdoptantes.buscarAdoptantePorCedula(cedula);
                if (ad == null) {
                    txtResultado.setText("Error: No se encontró ningún adoptante registrado con la cédula: " + cedula);
                    return;
                }

                Animal an = gestorAnimales.buscarAnimalPorCodigo(codigoAnimal);
                if (an == null) {
                    txtResultado.setText("Error: No se encontró ningún animal registrado con el código: " + codigoAnimal);
                    return;
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(baos);
                PrintStream old = System.out;
                System.setOut(ps);

                gestor.crearSolicitud(ad, an, id, "17/11/2026");

                System.out.flush();
                System.setOut(old);
                txtResultado.setText(baos.toString());
            }
        });

        btnEvaluar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = txtId.getText().trim();
                String codigoAnimal = txtCodigoAnimal.getText().trim();

                if (id.isEmpty()) {
                    txtResultado.setText("Error: Ingrese el ID de la solicitud que desea evaluar.");
                    return;
                }

                if (!id.matches("^SOL-\\d+$")) {
                    txtResultado.setText("Error: El ID de solicitud debe tener exactamente el formato SOL-(número), por ejemplo: SOL-001.");
                    return;
                }

                if (!codigoAnimal.isEmpty()) {
                    if (!codigoAnimal.matches("^ANI-\\d+$")) {
                        txtResultado.setText("Error: El código de animal debe tener exactamente el formato ANI-(número), por ejemplo: ANI-0001.");
                        return;
                    }
                    Animal anSeleccionado = gestorAnimales.buscarAnimalPorCodigo(codigoAnimal);
                    if (anSeleccionado != null && !anSeleccionado.getEstado().equalsIgnoreCase("Disponible")) {
                        txtResultado.setText("Error: El animal seleccionado (" + anSeleccionado.getNombre() + ") no está disponible para adopción, no se puede evaluar.");
                        return;
                    }
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(baos);
                PrintStream old = System.out;
                System.setOut(ps);

                gestor.procesarEvaluacionAdopcion(id);

                System.out.flush();
                System.setOut(old);
                txtResultado.setText(baos.toString());
            }
        });

        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtId.setText("SOL-001");
                txtCedulaAdoptante.setText("");
                txtCodigoAnimal.setText("");
                txtResultado.setText("");
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaSolicitudes().setVisible(true));
    }
}