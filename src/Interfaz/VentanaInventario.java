package Interfaz;

import java.awt.*;
import javax.swing.*;
import modelo.Insumo;
import negocio.GestorInventario;

public class VentanaInventario extends JFrame {
    private GestorInventario gestor;

    // Componentes del formulario
    private JTextField txtId;
    private JTextField txtNombre;
    private JComboBox<String> cmbTipo;
    private JTextField txtDonante;
    private JTextField txtCantidad;
    private JTextField txtFechaCaducidad;
    private JTextArea areaResultados;

    public VentanaInventario() {
        this.gestor = new GestorInventario();
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setTitle("Módulo 4 - Gestión de Insumos y Donaciones");
        setSize(750, 600); // Ventana más amplia y cómoda
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        // Panel Principal con márgenes internos (Padding)
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 1. Formulario superior con etiquetas claras ("ID de insumo" y "Nombre del producto")
        JPanel panelFormulario = new JPanel(new GridLayout(6, 2, 10, 12));
        panelFormulario.setBorder(BorderFactory.createTitledBorder(" Datos del Insumo y Donación "));

        panelFormulario.add(new JLabel("ID de insumo:"));
        txtId = new JTextField();
        panelFormulario.add(txtId);

        panelFormulario.add(new JLabel("Nombre del producto:"));
        txtNombre = new JTextField();
        panelFormulario.add(txtNombre);

        panelFormulario.add(new JLabel("Tipo de insumo:"));
        cmbTipo = new JComboBox<>(new String[]{"Alimento", "Medicina", "Limpieza", "Cobijas / Accesorios"});
        panelFormulario.add(cmbTipo);

        panelFormulario.add(new JLabel("Donante:"));
        txtDonante = new JTextField();
        panelFormulario.add(txtDonante);

        panelFormulario.add(new JLabel("Cantidad:"));
        txtCantidad = new JTextField();
        panelFormulario.add(txtCantidad);

        panelFormulario.add(new JLabel("Fecha de caducidad (AAAA-MM-DD o N/A):"));
        txtFechaCaducidad = new JTextField("N/A");
        panelFormulario.add(txtFechaCaducidad);

        panelPrincipal.add(panelFormulario, BorderLayout.NORTH);

        // 2. Panel central con botones centrados y bien espaciados
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton btnRegistrar = new JButton("Registrar Insumo");
        JButton btnListar = new JButton("Listar Inventario");
        JButton btnAlertas = new JButton("Verificar Alertas de Stock");

        // Dando un toque más estético a los botones
        btnRegistrar.setPreferredSize(new Dimension(150, 30));
        btnListar.setPreferredSize(new Dimension(140, 30));
        btnAlertas.setPreferredSize(new Dimension(180, 30));

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnListar);
        panelBotones.add(btnAlertas);

        panelPrincipal.add(panelBotones, BorderLayout.CENTER);

        // 3. Área de resultados inferior mucho más grande, legible y con salto de línea automático
        areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        areaResultados.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Fuente clara tipo consola
        areaResultados.setLineWrap(true);       // Evita que se salga horizontalmente
        areaResultados.setWrapStyleWord(true);  // Corta por palabras de forma limpia

        JScrollPane scrollResultados = new JScrollPane(areaResultados);
        scrollResultados.setPreferredSize(new Dimension(700, 200)); // Altura generosa para ver listas enteras
        scrollResultados.setBorder(BorderFactory.createTitledBorder(" Resultados y Reportes del Inventario "));

        panelPrincipal.add(scrollResultados, BorderLayout.SOUTH);

        add(panelPrincipal);

        // Eventos de los botones (conectados a tu lógica intacta)
        btnRegistrar.addActionListener(e -> registrarInsumo());
        btnListar.addActionListener(e -> listarInventario());
        btnAlertas.addActionListener(e -> verificarAlertas());
    }

    private void registrarInsumo() {
        try {
            String id = txtId.getText();
            String nombre = txtNombre.getText();
            String tipo = (String) cmbTipo.getSelectedItem();
            String donante = txtDonante.getText();
            int cantidad = Integer.parseInt(txtCantidad.getText());
            String caducidad = txtFechaCaducidad.getText();

            // Mantiene tu constructor y lógica original exactamente igual
            Insumo nuevo = new Insumo(id, nombre, tipo, donante, cantidad, caducidad, "");
            String mensaje = gestor.registrarInsumo(nuevo);

            JOptionPane.showMessageDialog(this, mensaje);
            if (mensaje.startsWith("Insumo registrado")) {
                limpiarCampos();
                listarInventario();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error: Ingrese un número válido en la cantidad.", "Error de formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listarInventario() {
        StringBuilder textoLista = new StringBuilder("=== INVENTARIO ACTUAL DEL REFUGIO ===\n\n");
        if (gestor.getListaInsumos().isEmpty()) {
            textoLista.append("No hay insumos registrados en el inventario actualmente.");
        } else {
            for (int i = 0; i < gestor.getListaInsumos().size(); i++) {
                Insumo ins = gestor.getListaInsumos().get(i);
                textoLista.append("- ").append(ins.toString()).append("\n");
            }
        }
        areaResultados.setText(textoLista.toString());
    }

    private void verificarAlertas() {
        String reporte = gestor.verificarAlertasStock();
        areaResultados.setText("=== REPORTE DE ALERTAS ===\n\n" + reporte);
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtDonante.setText("");
        txtCantidad.setText("");
        txtFechaCaducidad.setText("N/A");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaInventario ventana = new VentanaInventario();
            ventana.setVisible(true);
        });
    }
}