package Interfaz;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import modelo.Adoptante;
import negocio.GestorAdoptantes;

public class VentanaAdoptantes extends JFrame {


    private GestorAdoptantes gestor;


    private JTextField txtNombre;
    private JTextField txtCedula;
    private JTextField txtEdad;
    private JTextField txtTelefono;
    private JTextField txtCorreo;
    private JComboBox<String> cmbTipoVivienda;
    private JTextField txtTamanio;
    private JCheckBox chkPatio;
    private JTextField txtMascotas;
    private JComboBox<String> cmbPreferencia;


    private JButton btnRegistrar;
    private JButton btnEvaluar;
    private JButton btnLimpiar;


    private JTextArea areaLista;


    public VentanaAdoptantes(GestorAdoptantes gestor) {
        this.gestor = gestor;


        setTitle("Módulo 2 - Gestión de Adoptantes");
        setSize(750, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        JPanel panelFormulario = new JPanel(new GridLayout(10, 2, 5, 5));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos del adoptante"));

        txtNombre = new JTextField();
        txtCedula = new JTextField();
        txtEdad = new JTextField();
        txtTelefono = new JTextField();
        txtCorreo = new JTextField();
        cmbTipoVivienda = new JComboBox<>(new String[]{"Casa", "Departamento"});
        txtTamanio = new JTextField();
        chkPatio = new JCheckBox("Sí");
        txtMascotas = new JTextField();
        cmbPreferencia = new JComboBox<>(new String[]{"Pequeño", "Mediano", "Grande"});

        panelFormulario.add(new JLabel("Nombre completo:"));
        panelFormulario.add(txtNombre);
        panelFormulario.add(new JLabel("Cédula:"));
        panelFormulario.add(txtCedula);
        panelFormulario.add(new JLabel("Edad:"));
        panelFormulario.add(txtEdad);
        panelFormulario.add(new JLabel("Teléfono:"));
        panelFormulario.add(txtTelefono);
        panelFormulario.add(new JLabel("Correo electrónico:"));
        panelFormulario.add(txtCorreo);
        panelFormulario.add(new JLabel("Tipo de vivienda:"));
        panelFormulario.add(cmbTipoVivienda);
        panelFormulario.add(new JLabel("Tamaño de vivienda (m²):"));
        panelFormulario.add(txtTamanio);
        panelFormulario.add(new JLabel("¿Tiene patio?"));
        panelFormulario.add(chkPatio);
        panelFormulario.add(new JLabel("Mascotas actuales:"));
        panelFormulario.add(txtMascotas);
        panelFormulario.add(new JLabel("Preferencia de tamaño:"));
        panelFormulario.add(cmbPreferencia);


        JPanel panelBotones = new JPanel();
        btnRegistrar = new JButton("Registrar adoptante");
        btnEvaluar = new JButton("Evaluar vivienda");
        btnLimpiar = new JButton("Limpiar");
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnEvaluar);
        panelBotones.add(btnLimpiar);


        areaLista = new JTextArea(20, 28);
        areaLista.setEditable(false); // Solo para mostrar, no para escribir
        JScrollPane scrollLista = new JScrollPane(areaLista);
        scrollLista.setBorder(BorderFactory.createTitledBorder("Adoptantes registrados"));

        setLayout(new BorderLayout(10, 10));
        add(panelFormulario, BorderLayout.CENTER);
        add(scrollLista, BorderLayout.EAST);
        add(panelBotones, BorderLayout.SOUTH);


        btnRegistrar.addActionListener(e -> registrar());
        btnEvaluar.addActionListener(e -> evaluarVivienda());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        actualizarLista();
    }


    private void registrar() {

        int edad;
        double tamanio;
        int mascotas;

        try {
            edad = Integer.parseInt(txtEdad.getText().trim());
            tamanio = Double.parseDouble(txtTamanio.getText().trim());
            mascotas = Integer.parseInt(txtMascotas.getText().trim());
        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(this,
                    "Error: Edad, tamaño de vivienda y mascotas son obligatorios y deben ser números",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Adoptante nuevo = new Adoptante(
                txtNombre.getText().trim(),
                txtCedula.getText().trim(),
                edad,
                txtTelefono.getText().trim(),
                txtCorreo.getText().trim(),
                (String) cmbTipoVivienda.getSelectedItem(),
                tamanio,
                chkPatio.isSelected(),
                mascotas,
                (String) cmbPreferencia.getSelectedItem());


        String mensaje = gestor.registrarAdoptante(nuevo);

        if (mensaje.equals("Habilitado para adoptar")) {
            JOptionPane.showMessageDialog(this, mensaje,
                    "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
            gestor.setAdoptanteActivo(nuevo);
            actualizarLista();
        } else {
            JOptionPane.showMessageDialog(this, mensaje,
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void evaluarVivienda() {
        String cedula = txtCedula.getText().trim();

        if (cedula.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Error: Escriba la cédula del adoptante que desea evaluar",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Adoptante adoptante = gestor.buscarAdoptantePorCedula(cedula);

        if (adoptante == null) {
            JOptionPane.showMessageDialog(this,
                    "Error: No existe un adoptante registrado con esa cédula",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String mensaje = adoptante.evaluarAptitudVivienda();
        String clasificacion = adoptante.getClasificacionAptitud();


        if (clasificacion.equals("APTO")) {
            JOptionPane.showMessageDialog(this, mensaje,
                    "Evaluación de vivienda", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, mensaje,
                    "Evaluación de vivienda", JOptionPane.WARNING_MESSAGE);
        }

        gestor.setAdoptanteActivo(adoptante);
        actualizarLista();
    }


    private void limpiarCampos() {
        txtNombre.setText("");
        txtCedula.setText("");
        txtEdad.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        cmbTipoVivienda.setSelectedIndex(0);
        txtTamanio.setText("");
        chkPatio.setSelected(false);
        txtMascotas.setText("");
        cmbPreferencia.setSelectedIndex(0);
    }


    private void actualizarLista() {
        areaLista.setText("");
        if (gestor.getListaAdoptantes().isEmpty()) {
            areaLista.setText("No hay adoptantes registrados.");
            return;
        }
        for (Adoptante a : gestor.getListaAdoptantes()) {
            areaLista.append(a.toString() + "\n\n");
        }
    }

    public static void main(String[] args) {
        GestorAdoptantes gestor = new GestorAdoptantes();
        VentanaAdoptantes ventana = new VentanaAdoptantes(gestor);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setVisible(true);
    }
}

