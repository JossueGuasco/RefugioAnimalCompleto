package Interfaz;

import modelo.Adoptante;
import modelo.Animal;
import modelo.SolicitudAdopcion;
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
import java.util.ArrayList;
import java.util.List;

public class VentanaSolicitudes extends JFrame {
    private GestorSolicitudes gestor;
    private GestorAdoptantes gestorAdoptantes;
    private GestorAnimales gestorAnimales;

    private JTextField txtId;
    private JTextField txtCedulaAdoptante;
    private JComboBox<String> cmbAnimalesDisponibles;
    private List<Animal> listaAnimalesFiltrados;
    private JTextArea txtResultado;
    private JLabel lblUltimoRegistro;

    public VentanaSolicitudes(GestorSolicitudes gestor, GestorAdoptantes gestorAdoptantes, GestorAnimales gestorAnimales) {
        this.gestor = gestor;
        this.gestorAdoptantes = gestorAdoptantes;
        this.gestorAnimales = gestorAnimales;
        this.listaAnimalesFiltrados = new ArrayList<>();
        inicializarComponentes();
    }

    public VentanaSolicitudes() {
        this(new GestorSolicitudes(), new GestorAdoptantes(), new GestorAnimales());
    }

    private void inicializarComponentes() {
        setTitle("Gestión de Solicitudes de Adopción - Módulo 3");
        setSize(780, 580);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        lblUltimoRegistro = new JLabel("Último registro: Ninguno");
        lblUltimoRegistro.setBounds(40, 20, 700, 25);
        lblUltimoRegistro.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblUltimoRegistro);

        JLabel lblId = new JLabel("ID Solicitud (ej: SOL-001):");
        lblId.setBounds(40, 65, 210, 25);
        add(lblId);

        txtId = new JTextField("SOL-001");
        txtId.setBounds(260, 65, 460, 25);
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
        lblAdoptante.setBounds(40, 105, 210, 25);
        add(lblAdoptante);

        txtCedulaAdoptante = new JTextField();
        txtCedulaAdoptante.setBounds(260, 105, 460, 25);
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

        JLabel lblAnimalAsignado = new JLabel("Animales Disponibles:");
        lblAnimalAsignado.setBounds(40, 145, 210, 25);
        add(lblAnimalAsignado);

        cmbAnimalesDisponibles = new JComboBox<>();
        cmbAnimalesDisponibles.setBounds(260, 145, 350, 25);
        add(cmbAnimalesDisponibles);

        JButton btnRefrescarAnimales = new JButton("Actualizar");
        btnRefrescarAnimales.setBounds(620, 145, 100, 25);
        add(btnRefrescarAnimales);

        cargarAnimalesDisponibles();

        JButton btnRegistrar = new JButton("Registrar Solicitud");
        btnRegistrar.setBounds(265, 190, 240, 30);
        add(btnRegistrar);

        JButton btnEvaluar = new JButton("Procesar Evaluación");
        btnEvaluar.setBounds(265, 230, 240, 30);
        add(btnEvaluar);

        JButton btnLimpiar = new JButton("Limpiar Campos");
        btnLimpiar.setBounds(265, 270, 240, 30);
        add(btnLimpiar);

        txtResultado = new JTextArea();
        txtResultado.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtResultado);
        scrollPane.setBounds(40, 315, 680, 195);
        add(scrollPane);

        btnRefrescarAnimales.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarAnimalesDisponibles();
            }
        });

        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = txtId.getText().trim();
                String cedula = txtCedulaAdoptante.getText().trim();

                if (id.isEmpty() || cedula.isEmpty()) {
                    txtResultado.setText("Error: Los campos ID de Solicitud y Cédula del Adoptante son obligatorios.");
                    return;
                }

                if (!id.matches("^SOL-\\d+$")) {
                    txtResultado.setText("Error: El ID de solicitud debe tener exactamente el formato SOL-número, por ejemplo: SOL-001.");
                    return;
                }

                if (!cedula.matches("^\\d{10}$")) {
                    txtResultado.setText("Error: La cédula debe contener exactamente 10 dígitos numéricos.");
                    return;
                }

                int indiceSeleccionado = cmbAnimalesDisponibles.getSelectedIndex();
                if (listaAnimalesFiltrados.isEmpty() || indiceSeleccionado < 0) {
                    txtResultado.setText("Error: No hay ningún animal disponible seleccionado para asignar a la solicitud.");
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

                Animal an = listaAnimalesFiltrados.get(indiceSeleccionado);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(baos);
                PrintStream old = System.out;
                System.setOut(ps);

                gestor.crearSolicitud(ad, an, id, "17/11/2026");

                System.out.flush();
                System.setOut(old);
                txtResultado.setText(baos.toString());

                lblUltimoRegistro.setText("Último registro: " + ad.getNombreCompleto() + " | " + id);
                cargarAnimalesDisponibles();
            }
        });

        btnEvaluar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = txtId.getText().trim();

                if (id.isEmpty()) {
                    txtResultado.setText("Error: Ingrese el ID de la solicitud que desea evaluar.");
                    return;
                }

                if (!id.matches("^SOL-\\d+$")) {
                    txtResultado.setText("Error: El ID de solicitud debe tener exactamente el formato SOL-número, por ejemplo: SOL-001.");
                    return;
                }

                if (!listaAnimalesFiltrados.isEmpty() && cmbAnimalesDisponibles.getSelectedIndex() >= 0) {
                    Animal anSeleccionado = listaAnimalesFiltrados.get(cmbAnimalesDisponibles.getSelectedIndex());
                    if (anSeleccionado.getEstado() != null && !anSeleccionado.getEstado().equalsIgnoreCase("Disponible")) {
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
                String resultadoEval = baos.toString();
                txtResultado.setText(resultadoEval);

                SolicitudAdopcion sol = gestor.buscarSolicitudPorId(id);
                if (sol != null && sol.getAdoptante() != null && sol.getAnimal() != null) {
                    String nombreAdoptante = sol.getAdoptante().getNombreCompleto();
                    String nombreAnimal = sol.getAnimal().getNombre();

                    if (resultadoEval.toLowerCase().contains("rechazad") || resultadoEval.toLowerCase().contains("no apto") || resultadoEval.toLowerCase().contains("negativ") || resultadoEval.toLowerCase().contains("error")) {
                        lblUltimoRegistro.setText("Último registro: " + nombreAdoptante + " | " + id);
                    } else {
                        lblUltimoRegistro.setText("Último registro exitoso: Se llevó al animal " + nombreAnimal + " (Adoptante: " + nombreAdoptante + " | " + id + ")");
                    }
                }

                cargarAnimalesDisponibles();
            }
        });

        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtId.setText("SOL-001");
                txtCedulaAdoptante.setText("");
                cargarAnimalesDisponibles();
                txtResultado.setText("");
            }
        });
    }

    private void cargarAnimalesDisponibles() {
        cmbAnimalesDisponibles.removeAllItems();
        listaAnimalesFiltrados.clear();

        if (gestorAnimales != null && gestorAnimales.getListaAnimales() != null) {
            for (Animal a : gestorAnimales.getListaAnimales()) {
                if (a != null && a.getEstado() != null && a.getEstado().equalsIgnoreCase("Disponible")) {
                    listaAnimalesFiltrados.add(a);
                    String codigo = (a.getCodigo() != null) ? a.getCodigo() : "SIN-COD";
                    String nombre = (a.getNombre() != null) ? a.getNombre() : "Sin nombre";
                    String especie = (a.getEspecie() != null) ? a.getEspecie() : "Animal";
                    String raza = (a.getRaza() != null) ? a.getRaza() : "N/D";
                    String tamanio = (a.getTamanio() != null) ? a.getTamanio() : "N/D";

                    String resumen = especie + " [" + codigo + "] - " + nombre + " | Raza: " + raza + " | Tam: " + tamanio;
                    cmbAnimalesDisponibles.addItem(resumen);
                }
            }
        }

        if (listaAnimalesFiltrados.isEmpty()) {
            cmbAnimalesDisponibles.addItem("No hay animales disponibles actualmente");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaSolicitudes().setVisible(true));
    }
}