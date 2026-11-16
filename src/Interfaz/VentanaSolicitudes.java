package Interfaz;

import modelo.Adoptante;
import modelo.Animal;
import modelo.Perro;
import modelo.SolicitudAdopcion;
import negocio.GestorSolicitudes;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class VentanaSolicitudes extends JFrame {
    private GestorSolicitudes gestor;
    private boolean actualizando = false;

    private Adoptante adoptanteValido = new Adoptante("1723456789", "María López", 28, "0987654321", "maria.lopez@gmail.com", "Casa", 90.0, true, 1, "Grande");
    private Adoptante adoptanteRestringido = new Adoptante("1709876543", "Carlos Ruíz", 30, "0998887766", "carlos.ruiz@gmail.com", "Departamento", 45.0, false, 0, "Grande");

    private Animal animalDisponible = crearAnimalDemo("ANI-0001", "Firulais", "Grande", "Medio", "Disponible");
    private Animal animalEnEvaluacion = crearAnimalDemo("ANI-0003", "Max", "Mediano", "Alto", "En evaluación");

    private static Animal crearAnimalDemo(String codigo, String nombre, String tamanio, String energia, String estado) {
        Animal animal = new Perro(codigo, nombre, "Mestizo", "Macho", 10, 24, tamanio, energia,
                "Sano", "Sin antecedentes", "15/11/2026");
        animal.actualizarEstadoAnimal(estado);
        return animal;
    }

    public VentanaSolicitudes() {
        gestor = new GestorSolicitudes();

        setTitle("Gestión de Solicitudes de Adopción - Módulo 3");
        setSize(525, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblTitulo = new JLabel("Módulo 3: Gestión de Solicitudes");
        lblTitulo.setBounds(150, 20, 250, 25);
        add(lblTitulo);

        JLabel lblId = new JLabel("ID Solicitud:");
        lblId.setBounds(50, 60, 120, 25);
        add(lblId);

        JTextField txtId = new JTextField("SOL-001");
        txtId.setBounds(180, 60, 250, 25);
        add(txtId);

        JLabel lblAdoptante = new JLabel("Adoptante:");
        lblAdoptante.setBounds(50, 100, 120, 25);
        add(lblAdoptante);

        String[] adoptantesOpciones = {"María López", "Carlos Ruíz"};
        JComboBox<String> cmbAdoptante = new JComboBox<>(adoptantesOpciones);
        cmbAdoptante.setBounds(180, 100, 250, 25);
        add(cmbAdoptante);

        JLabel lblAnimalAsignado = new JLabel("Animal Asignado:");
        lblAnimalAsignado.setBounds(50, 140, 120, 25);
        add(lblAnimalAsignado);

        String[] animalesOpciones = {"Firulais [Disponible]", "Max [En evaluación]"};
        JComboBox<String> cmbAnimal = new JComboBox<>(animalesOpciones);
        cmbAnimal.setBounds(180, 140, 250, 25);
        add(cmbAnimal);

        JButton btnRegistrar = new JButton("Registrar Solicitud");
        btnRegistrar.setBounds(140, 190, 240, 30);
        add(btnRegistrar);

        JButton btnEvaluar = new JButton("Procesar Evaluación");
        btnEvaluar.setBounds(140, 235, 240, 30);
        add(btnEvaluar);

        JTextArea txtResultado = new JTextArea();
        txtResultado.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtResultado);
        scrollPane.setBounds(50, 280, 410, 130);
        add(scrollPane);

        cmbAdoptante.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (actualizando) return;
                actualizando = true;
                if (cmbAdoptante.getSelectedIndex() == 0) {
                    txtId.setText("SOL-001");
                    cmbAnimal.setSelectedIndex(0);
                } else {
                    txtId.setText("SOL-002");
                    cmbAnimal.setSelectedIndex(1);
                }
                actualizando = false;
            }
        });

        txtId.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { actualizarPorId(); }
            @Override
            public void removeUpdate(DocumentEvent e) { actualizarPorId(); }
            @Override
            public void changedUpdate(DocumentEvent e) { actualizarPorId(); }

            private void actualizarPorId() {
                if (actualizando) return;
                actualizando = true;
                String idTexto = txtId.getText().trim();
                if (idTexto.equalsIgnoreCase("SOL-001")) {
                    cmbAdoptante.setSelectedIndex(0);
                    cmbAnimal.setSelectedIndex(0);
                } else if (idTexto.equalsIgnoreCase("SOL-002")) {
                    cmbAdoptante.setSelectedIndex(1);
                    cmbAnimal.setSelectedIndex(1);
                }
                actualizando = false;
            }
        });

        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = txtId.getText();
                Adoptante ad;
                Animal an;

                if (cmbAdoptante.getSelectedIndex() == 0) {
                    ad = adoptanteValido;
                } else {
                    ad = adoptanteRestringido;
                }

                if (cmbAnimal.getSelectedIndex() == 0) {
                    an = animalDisponible;
                } else {
                    an = animalEnEvaluacion;
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(baos);
                PrintStream old = System.out;
                System.setOut(ps);

                gestor.crearSolicitud(ad, an, id, "12/11/2026");

                System.out.flush();
                System.setOut(old);
                txtResultado.setText(baos.toString());
            }
        });

        btnEvaluar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = txtId.getText();
                Animal anSeleccionado = (cmbAnimal.getSelectedIndex() == 0) ? animalDisponible : animalEnEvaluacion;

                if (!anSeleccionado.getEstado().equalsIgnoreCase("Disponible")) {
                    txtResultado.setText("Error: El animal seleccionado (" + anSeleccionado.getNombre() + ") no está disponible para adopción, no se puede evaluar.");
                    return;
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
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VentanaSolicitudes().setVisible(true);
            }
        });
    }
}