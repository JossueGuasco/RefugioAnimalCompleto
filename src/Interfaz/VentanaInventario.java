package Interfaz;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import modelo.Insumo;
import negocio.GestorInventario;

public class VentanaInventario extends JFrame {
    private GestorInventario gestor;
    private Map<String, String> unidadesPorId;

    // Componentes Pestaña 1: Registro
    private JTextField txtId;
    private JTextField txtNombre;
    private JComboBox<String> cmbTipo;
    private JTextField txtDonante;
    private JTextField txtCantidad;
    private JComboBox<String> cmbUnidad;
    private JTextField txtFechaCaducidad;

    // Componentes Pestaña 2: Inventario y Consultas
    private JComboBox<String> cmbIdsRegistrados;
    private JTextArea areaResultados;

    public VentanaInventario() {
        this.gestor = new GestorInventario();
        this.unidadesPorId = new HashMap<>();
        inicializarComponentes();
    }

    public VentanaInventario(GestorInventario gestorExterno) {
        this.gestor = (gestorExterno != null) ? gestorExterno : new GestorInventario();
        this.unidadesPorId = new HashMap<>();
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setTitle("Módulo 4 - Gestión de Insumos y Donaciones");
        setSize(780, 560);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.setFont(new Font("SansSerif", Font.BOLD, 13));

        // =========================================================================
        // PESTAÑA 1: REGISTRO DE INSUMOS
        // =========================================================================
        JPanel tabRegistro = new JPanel(new BorderLayout(15, 15));
        tabRegistro.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel panelFormulario = new JPanel(new GridLayout(6, 2, 10, 14));
        panelFormulario.setBorder(BorderFactory.createTitledBorder(" Datos del Insumo y Donación "));

        panelFormulario.add(new JLabel("ID de insumo:"));
        txtId = new JTextField();
        panelFormulario.add(txtId);

        panelFormulario.add(new JLabel("Nombre del producto:"));
        txtNombre = new JTextField();
        panelFormulario.add(txtNombre);

        panelFormulario.add(new JLabel("Tipo de insumo:"));
        cmbTipo = new JComboBox<>(new String[]{"Alimento", "Medicina", "Limpieza", "Accesorios"});
        panelFormulario.add(cmbTipo);

        panelFormulario.add(new JLabel("Donante:"));
        txtDonante = new JTextField();
        panelFormulario.add(txtDonante);

        panelFormulario.add(new JLabel("Cantidad y Unidad:"));
        JPanel panelCantidadUnidad = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        txtCantidad = new JTextField(8);
        cmbUnidad = new JComboBox<>();
        panelCantidadUnidad.add(txtCantidad);
        panelCantidadUnidad.add(cmbUnidad);
        panelFormulario.add(panelCantidadUnidad);

        panelFormulario.add(new JLabel("Fecha de caducidad (AAAA-MM-DD):"));
        txtFechaCaducidad = new JTextField();
        panelFormulario.add(txtFechaCaducidad);

        actualizarUnidadesYSaltarCaducidad();

        tabRegistro.add(panelFormulario, BorderLayout.CENTER);

        JPanel panelBotonRegistro = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnRegistrar = new JButton("Registrar Insumo");
        btnRegistrar.setPreferredSize(new Dimension(180, 35));
        panelBotonRegistro.add(btnRegistrar);
        tabRegistro.add(panelBotonRegistro, BorderLayout.SOUTH);

        cmbTipo.addActionListener(e -> actualizarUnidadesYSaltarCaducidad());

        // =========================================================================
        // PESTAÑA 2: INVENTARIO Y CONSULTAS
        // =========================================================================
        JPanel tabInventario = new JPanel(new BorderLayout(10, 10));
        tabInventario.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel panelControlesSuperior = new JPanel(new BorderLayout(5, 8));

        JPanel panelSelectorId = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panelSelectorId.setBorder(BorderFactory.createTitledBorder(" Gestión Individual por ID "));
        panelSelectorId.add(new JLabel("Seleccione ID:"));
        cmbIdsRegistrados = new JComboBox<>();
        cmbIdsRegistrados.setPreferredSize(new Dimension(130, 26));

        JButton btnVerDetalle = new JButton("Desplegar Info");
        JButton btnRestock = new JButton("➕ Añadir Stock (Restock)");
        btnRestock.setBackground(new Color(230, 245, 230));

        panelSelectorId.add(cmbIdsRegistrados);
        panelSelectorId.add(btnVerDetalle);
        panelSelectorId.add(btnRestock);
        panelControlesSuperior.add(panelSelectorId, BorderLayout.NORTH);

        JPanel panelBotonesInventario = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        JButton btnListarTodo = new JButton("Listar Todo el Inventario");
        JButton btnAlertas = new JButton("Verificar Alertas de Stock");
        panelBotonesInventario.add(btnListarTodo);
        panelBotonesInventario.add(btnAlertas);
        panelControlesSuperior.add(panelBotonesInventario, BorderLayout.SOUTH);

        tabInventario.add(panelControlesSuperior, BorderLayout.NORTH);

        areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        areaResultados.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaResultados.setLineWrap(true);
        areaResultados.setWrapStyleWord(true);

        JScrollPane scrollResultados = new JScrollPane(areaResultados);
        scrollResultados.setBorder(BorderFactory.createTitledBorder(" Resultados y Reportes del Inventario "));
        tabInventario.add(scrollResultados, BorderLayout.CENTER);

        pestanas.addTab("Registrar Insumo", tabRegistro);
        pestanas.addTab("Inventario y Consultas", tabInventario);
        add(pestanas);

        actualizarComboIds();

        btnRegistrar.addActionListener(e -> registrarInsumo());
        btnVerDetalle.addActionListener(e -> desplegarInsumoPorIdSeleccionado());
        btnRestock.addActionListener(e -> realizarRestock());
        btnListarTodo.addActionListener(e -> listarInventario());
        btnAlertas.addActionListener(e -> verificarAlertas());
    }

    private void actualizarUnidadesYSaltarCaducidad() {
        cmbUnidad.removeAllItems();
        String tipoSeleccionado = (String) cmbTipo.getSelectedItem();
        if (tipoSeleccionado == null) return;

        switch (tipoSeleccionado) {
            case "Alimento":
                cmbUnidad.addItem("Quintales");
                cmbUnidad.addItem("Kilogramos (kg)");
                cmbUnidad.addItem("Libras");
                cmbUnidad.addItem("Latas");
                cmbUnidad.addItem("Unidades");
                txtFechaCaducidad.setText("");
                txtFechaCaducidad.setEnabled(true);
                break;
            case "Medicina":
                cmbUnidad.addItem("Tabletas");
                cmbUnidad.addItem("Frascos");
                cmbUnidad.addItem("Ampollas");
                cmbUnidad.addItem("Tubos");
                cmbUnidad.addItem("Cápsulas");
                cmbUnidad.addItem("Sobres");
                txtFechaCaducidad.setText("");
                txtFechaCaducidad.setEnabled(true);
                break;
            case "Limpieza":
                cmbUnidad.addItem("Litros");
                cmbUnidad.addItem("Galones");
                cmbUnidad.addItem("Envases");
                cmbUnidad.addItem("Kilos");
                cmbUnidad.addItem("Unidades");
                txtFechaCaducidad.setText("");
                txtFechaCaducidad.setEnabled(true);
                break;
            case "Accesorios":
                cmbUnidad.addItem("Unidades");
                txtFechaCaducidad.setText("N/A");
                txtFechaCaducidad.setEnabled(false);
                break;
        }
    }

    private boolean contieneNumeros(String texto) {
        return texto.matches(".*\\d.*");
    }

    private void actualizarComboIds() {
        cmbIdsRegistrados.removeAllItems();
        for (Insumo ins : gestor.getListaInsumos()) {
            cmbIdsRegistrados.addItem(ins.getIdInsumo());
        }
    }

    private void registrarInsumo() {
        try {
            String id = txtId.getText().trim();
            String nombre = txtNombre.getText().trim();
            String tipoBase = (String) cmbTipo.getSelectedItem();
            String donante = txtDonante.getText().trim();
            String unidadSeleccionada = (String) cmbUnidad.getSelectedItem();
            String caducidad = txtFechaCaducidad.getText().trim();

            if (id.isEmpty() || nombre.isEmpty() || donante.isEmpty() || txtCantidad.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Error: Todos los campos obligatorios deben estar completos.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (tipoBase.equals("Alimento") || tipoBase.equals("Medicina") || tipoBase.equals("Limpieza")) {
                if (caducidad.isEmpty() || caducidad.equalsIgnoreCase("N/A")) {
                    JOptionPane.showMessageDialog(this, "Error: Los insumos de tipo '" + tipoBase + "' exigen una fecha de caducidad obligatoria (AAAA-MM-DD).", "Fecha requerida", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    LocalDate fechaIngresada = LocalDate.parse(caducidad.replace('/', '-'));
                    LocalDate fechaHoy = LocalDate.now();

                    if (fechaIngresada.isBefore(fechaHoy)) {
                        JOptionPane.showMessageDialog(this, "Error: No se puede registrar un insumo caducado. La fecha ingresada ya pasó.", "Fecha inválida", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(this, "Error: Formato de fecha inválido. Utilice el formato AAAA-MM-DD (ej. 2026-12-31).", "Error de formato", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            if (contieneNumeros(nombre)) {
                JOptionPane.showMessageDialog(this, "Error: El 'Nombre del producto' no debe contener números.", "Formato inválido", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (contieneNumeros(donante)) {
                JOptionPane.showMessageDialog(this, "Error: El nombre del 'Donante' solo debe contener letras, no números.", "Formato inválido", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int cantidadNumerica = Integer.parseInt(txtCantidad.getText().trim());
            if (cantidadNumerica <= 0) {
                JOptionPane.showMessageDialog(this, "Error: La cantidad debe ser un número entero estrictamente positivo mayor a cero.", "Cantidad inválida", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Insumo nuevo = new Insumo(id, nombre, tipoBase, donante, cantidadNumerica, caducidad, "");
            String mensaje = gestor.registrarInsumo(nuevo);

            JOptionPane.showMessageDialog(this, mensaje);
            if (mensaje.startsWith("Insumo registrado")) {
                unidadesPorId.put(id, unidadSeleccionada);
                actualizarComboIds();
                limpiarCampos();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error: Ingrese únicamente un número entero válido en el campo de cantidad.", "Error de formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método de Restock con lógica inteligente para proteger la fecha más próxima (FEFO)
    private void realizarRestock() {
        String idSeleccionado = (String) cmbIdsRegistrados.getSelectedItem();
        if (idSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "No hay ningún ID seleccionado para hacer restock.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Insumo ins = gestor.buscarInsumoPorId(idSeleccionado);
        if (ins != null) {
            JPanel panelRestock = new JPanel(new GridLayout(6, 2, 5, 5));
            JTextField txtCantExtra = new JTextField(8);
            JTextField txtNuevoDonante = new JTextField(15);
            JTextField txtNuevaFecha = new JTextField(10);

            txtNuevoDonante.setText(ins.getDonante());
            txtNuevaFecha.setText(ins.getFechaCaducidad());

            panelRestock.add(new JLabel("Stock actual:"));
            panelRestock.add(new JLabel(String.valueOf(ins.getCantidad())));
            panelRestock.add(new JLabel("Cantidad a añadir:"));
            panelRestock.add(txtCantExtra);
            panelRestock.add(new JLabel("Donante del lote:"));
            panelRestock.add(txtNuevoDonante);
            panelRestock.add(new JLabel("Fecha Caducidad (AAAA-MM-DD):"));
            panelRestock.add(txtNuevaFecha);

            int opcion = JOptionPane.showConfirmDialog(this, panelRestock,
                    "Añadir Stock (Restock) - " + ins.getNombre(),
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (opcion == JOptionPane.OK_OPTION) {
                try {
                    String strCantidad = txtCantExtra.getText().trim();
                    String nuevoDonante = txtNuevoDonante.getText().trim();
                    String nuevaFechaStr = txtNuevaFecha.getText().trim();

                    if (strCantidad.isEmpty() || nuevoDonante.isEmpty() || nuevaFechaStr.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Error: Todos los campos son obligatorios.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    if (contieneNumeros(nuevoDonante)) {
                        JOptionPane.showMessageDialog(this, "Error: El nombre del 'Donante' solo debe contener letras.", "Formato inválido", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    int cantidadExtra = Integer.parseInt(strCantidad);
                    if (cantidadExtra <= 0) {
                        JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a cero.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (!nuevaFechaStr.equalsIgnoreCase("N/A")) {
                        try {
                            LocalDate fechaIngresada = LocalDate.parse(nuevaFechaStr.replace('/', '-'));
                            LocalDate fechaHoy = LocalDate.now();

                            if (fechaIngresada.isBefore(fechaHoy)) {
                                JOptionPane.showMessageDialog(this, "Error: No se puede ingresar un lote con una fecha de caducidad que ya pasó.", "Fecha inválida", JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            // LÓGICA INTELIGENTE DE FECHAS:
                            // Si el insumo ya tenía una fecha registrada, comparamos cuál vence primero.
                            String fechaAntiguaStr = ins.getFechaCaducidad();
                            if (fechaAntiguaStr != null && !fechaAntiguaStr.equalsIgnoreCase("N/A") && !fechaAntiguaStr.isEmpty()) {
                                LocalDate fechaAntigua = LocalDate.parse(fechaAntiguaStr.replace('/', '-'));

                                // Si la fecha nueva es ANTES que la antigua (vence más pronto), la reemplazamos.
                                // Si la fecha nueva es DESPUÉS (vence el próximo año), conservamos la antigua para no perder la alerta.
                                if (fechaIngresada.isBefore(fechaAntigua)) {
                                    ins.setFechaCaducidad(nuevaFechaStr);
                                }
                            } else {
                                ins.setFechaCaducidad(nuevaFechaStr);
                            }

                        } catch (DateTimeParseException ex) {
                            JOptionPane.showMessageDialog(this, "Error: Formato de fecha inválido. Use AAAA-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }

                    int nuevoStock = ins.getCantidad() + cantidadExtra;
                    ins.setCantidad(nuevoStock);
                    ins.setDonante(nuevoDonante);

                    JOptionPane.showMessageDialog(this, "¡Restock realizado con éxito!\nNuevo stock: " + nuevoStock, "Éxito", JOptionPane.INFORMATION_MESSAGE);

                    desplegarInsumoPorIdSeleccionado();

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Error: Ingrese un número entero válido en la cantidad.", "Error de formato", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void desplegarInsumoPorIdSeleccionado() {
        String idSeleccionado = (String) cmbIdsRegistrados.getSelectedItem();
        if (idSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "No hay ningún ID seleccionado o disponible.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Insumo ins = gestor.buscarInsumoPorId(idSeleccionado);
        if (ins != null) {
            String unidad = unidadesPorId.getOrDefault(ins.getIdInsumo(), "Unidades");

            StringBuilder detalle = new StringBuilder("=== DETALLE DEL INSUMO SELECCIONADO ===\n\n");
            detalle.append("ID: ").append(ins.getIdInsumo()).append("\n")
                    .append("Producto: ").append(ins.getNombre()).append("\n")
                    .append("Tipo: ").append(ins.getTipoInsumo()).append("\n")
                    .append("Cantidad: ").append(ins.getCantidad()).append(" [").append(unidad).append("]\n")
                    .append("Donante: ").append(ins.getDonante()).append("\n")
                    .append("Caducidad: ").append(ins.getFechaCaducidad()).append("\n");

            areaResultados.setText(detalle.toString());
        } else {
            JOptionPane.showMessageDialog(this, "Insumo no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listarInventario() {
        StringBuilder textoLista = new StringBuilder("=== INVENTARIO ACTUAL DEL REFUGIO ===\n\n");
        if (gestor.getListaInsumos().isEmpty()) {
            textoLista.append("No hay insumos registrados en el inventario actualmente.");
        } else {
            for (Insumo ins : gestor.getListaInsumos()) {
                String unidad = unidadesPorId.getOrDefault(ins.getIdInsumo(), "Unidades");
                textoLista.append("--------------------------------------------------\n")
                        .append("ID: ").append(ins.getIdInsumo()).append("\n")
                        .append("Producto: ").append(ins.getNombre()).append("\n")
                        .append("Tipo: ").append(ins.getTipoInsumo()).append("\n")
                        .append("Cantidad: ").append(ins.getCantidad()).append(" [").append(unidad).append("]\n")
                        .append("Donante: ").append(ins.getDonante()).append("\n")
                        .append("Caducidad: ").append(ins.getFechaCaducidad()).append("\n");
            }
            textoLista.append("--------------------------------------------------");
        }
        areaResultados.setText(textoLista.toString());
    }

    private void verificarAlertas() {
        StringBuilder reporte = new StringBuilder("=== REPORTE DE ALERTAS Y VENCIMIENTOS ===\n\n");
        if (gestor.getListaInsumos().isEmpty()) {
            reporte.append("No hay insumos registrados para verificar.");
        } else {
            boolean hayAlertas = false;
            LocalDate hoy = LocalDate.now();

            for (Insumo ins : gestor.getListaInsumos()) {
                String unidad = unidadesPorId.getOrDefault(ins.getIdInsumo(), "Unidades");

                if (ins.getCantidad() <= 5) {
                    reporte.append("• [STOCK BAJO] El producto '").append(ins.getNombre())
                            .append("' (ID: ").append(ins.getIdInsumo())
                            .append(") tiene solo ").append(ins.getCantidad()).append(" [").append(unidad)
                            .append("]. Se requiere reabastecimiento.\n");
                    hayAlertas = true;
                }

                String fechaStr = ins.getFechaCaducidad();
                if (fechaStr != null && !fechaStr.equalsIgnoreCase("N/A") && !fechaStr.isEmpty()) {
                    try {
                        LocalDate fechaVenc = LocalDate.parse(fechaStr.replace('/', '-'));
                        long diasRestantes = ChronoUnit.DAYS.between(hoy, fechaVenc);

                        if (diasRestantes >= 0 && diasRestantes <= 30) {
                            reporte.append("• [⚠️ PRÓXIMO A VENCER] El producto '").append(ins.getNombre())
                                    .append("' (ID: ").append(ins.getIdInsumo())
                                    .append(") caduca pronto: ").append(fechaStr)
                                    .append(" (Faltan ").append(diasRestantes).append(" días).\n");
                            hayAlertas = true;
                        } else if (diasRestantes < 0) {
                            reporte.append("• [❌ CADUCADO] El producto '").append(ins.getNombre())
                                    .append("' (ID: ").append(ins.getIdInsumo())
                                    .append(") YA VENCIÓ el ").append(fechaStr).append(".\n");
                            hayAlertas = true;
                        }
                    } catch (Exception e) {
                        // Ignorar formato inválido
                    }
                }
            }

            if (!hayAlertas) {
                reporte.append("¡Excelente! No hay alertas de stock bajo ni de productos próximos a caducar.");
            }
        }
        areaResultados.setText(reporte.toString());
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtDonante.setText("");
        txtCantidad.setText("");
        cmbTipo.setSelectedIndex(0);
        actualizarUnidadesYSaltarCaducidad();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaInventario ventana = new VentanaInventario();
            ventana.setVisible(true);
        });
    }
}