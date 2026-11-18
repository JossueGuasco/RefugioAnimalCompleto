package Interfaz;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import modelo.Animal;
import modelo.Gato;
import modelo.Perro;
import negocio.GestorAnimales;

public class VentanaAnimales extends JFrame implements ActionListener {

    private GestorAnimales gestor;

    private JTextField txtNombre, txtRaza, txtPeso, txtEdad, txtHistorial, txtUltimaAtencion, txtIngreso, txtCodigo;
    private JComboBox<String> cmbEspecie, cmbSexo, cmbTamanio, cmbEnergia, cmbSalud, cmbUnidadEdad;
    private JButton btnRegistrar, btnEvaluar, btnTodos, btnDisponibles;
    private JTextArea areaListado;

    // Constructor que recibe el gestor compartido desde la VentanaPrincipal
    public VentanaAnimales(GestorAnimales gestor) {
        this.gestor = gestor;

        setTitle("Gestión de Animales");
        setSize(800, 620);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        agregarEtiqueta("Nombre o alias:", 20, 20);
        txtNombre = new JTextField();
        txtNombre.setBounds(180, 20, 180, 25);
        add(txtNombre);

        agregarEtiqueta("Especie:", 20, 55);
        cmbEspecie = new JComboBox<>(new String[]{"Perro", "Gato"});
        cmbEspecie.setBounds(180, 55, 180, 25);
        add(cmbEspecie);

        agregarEtiqueta("Raza:", 20, 90);
        txtRaza = new JTextField();
        txtRaza.setBounds(180, 90, 180, 25);
        add(txtRaza);

        agregarEtiqueta("Sexo:", 20, 125);
        cmbSexo = new JComboBox<>(new String[]{"Macho", "Hembra"});
        cmbSexo.setBounds(180, 125, 180, 25);
        add(cmbSexo);

        agregarEtiqueta("Peso (kg):", 20, 160);
        txtPeso = new JTextField();
        txtPeso.setBounds(180, 160, 180, 25);
        add(txtPeso);

        agregarEtiqueta("Edad al ingreso:", 20, 195);
        txtEdad = new JTextField();
        txtEdad.setBounds(180, 195, 100, 25);
        add(txtEdad);
        // La unidad se elige a mano para que no haya confusión entre meses y años
        cmbUnidadEdad = new JComboBox<>(new String[]{"Meses", "Años"});
        cmbUnidadEdad.setBounds(290, 195, 70, 25);
        add(cmbUnidadEdad);

        agregarEtiqueta("Tamaño:", 400, 20);
        cmbTamanio = new JComboBox<>(new String[]{"Pequeño", "Mediano", "Grande"});
        cmbTamanio.setBounds(580, 20, 180, 25);
        add(cmbTamanio);

        agregarEtiqueta("Nivel de energía:", 400, 55);
        cmbEnergia = new JComboBox<>(new String[]{"Bajo", "Medio", "Alto"});
        cmbEnergia.setBounds(580, 55, 180, 25);
        add(cmbEnergia);

        agregarEtiqueta("Estado de salud:", 400, 90);
        cmbSalud = new JComboBox<>(new String[]{"Sano", "Herida leve", "Desnutrido", "Enfermo", "Crítico"});
        cmbSalud.setBounds(580, 90, 180, 25);
        add(cmbSalud);

        agregarEtiqueta("Historial médico:", 400, 125);
        txtHistorial = new JTextField();
        txtHistorial.setBounds(580, 125, 180, 25);
        add(txtHistorial);

        agregarEtiqueta("Última atención:", 400, 160);
        txtUltimaAtencion = new JTextField();
        txtUltimaAtencion.setBounds(580, 160, 180, 25);
        add(txtUltimaAtencion);

        agregarEtiqueta("Fecha de ingreso:", 400, 195);
        agregarEtiqueta("Fechas: dd/MM/yyyy", 580, 220);
        txtIngreso = new JTextField();
        txtIngreso.setBounds(580, 195, 180, 25);
        add(txtIngreso);

        btnRegistrar = new JButton("Registrar animal");
        btnRegistrar.setBounds(20, 240, 170, 30);
        btnRegistrar.addActionListener(this);
        add(btnRegistrar);

        agregarEtiqueta("Código (ej. ANI-0001):", 220, 245);
        txtCodigo = new JTextField();
        txtCodigo.setBounds(370, 245, 100, 25);
        add(txtCodigo);

        btnEvaluar = new JButton("Evaluar urgencia");
        btnEvaluar.setBounds(490, 240, 150, 30);
        btnEvaluar.addActionListener(this);
        add(btnEvaluar);

        btnTodos = new JButton("Ver todos");
        btnTodos.setBounds(20, 285, 170, 30);
        btnTodos.addActionListener(this);
        add(btnTodos);

        btnDisponibles = new JButton("Ver disponibles");
        btnDisponibles.setBounds(210, 285, 170, 30);
        btnDisponibles.addActionListener(this);
        add(btnDisponibles);

        // Área donde se muestran los animales (con salto de línea automático integrado)
        areaListado = new JTextArea();
        areaListado.setEditable(false);
        areaListado.setLineWrap(true);          // Salto de línea automático para que no se desborde
        areaListado.setWrapStyleWord(true);     // Respeta las palabras completas al bajar de línea

        JScrollPane scroll = new JScrollPane(areaListado);
        scroll.setBounds(20, 330, 740, 240);
        add(scroll);

        // Muestra la lista actual al abrir la ventana si ya había registros previos
        mostrarAnimales(gestor.getListaAnimales());
    }

    private void agregarEtiqueta(String texto, int x, int y) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setBounds(x, y, 200, 25);
        add(etiqueta);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnRegistrar) {
            registrarAnimal();
        } else if (e.getSource() == btnEvaluar) {
            String codigoBuscado = txtCodigo.getText().trim();
            if (codigoBuscado.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor ingrese el código del animal para evaluar su urgencia.");
                return;
            }
            if (!codigoBuscado.matches("(?i)ANI-[0-9]{4,}")) {
                JOptionPane.showMessageDialog(this, "Error: El código debe tener el formato ANI-0001");
                return;
            }
            String resultado = gestor.evaluarUrgenciaAnimal(codigoBuscado);
            JOptionPane.showMessageDialog(this, resultado);
            mostrarAnimales(gestor.getListaAnimales()); // Actualiza la vista por si cambió de estado
        } else if (e.getSource() == btnTodos) {
            mostrarAnimales(gestor.getListaAnimales());
        } else if (e.getSource() == btnDisponibles) {
            mostrarAnimales(gestor.listarAnimalesDisponibles());
        }
    }

    private void registrarAnimal() {
        // Primero se validan los campos del formulario
        String error = validarCampos();
        if (!error.isEmpty()) {
            JOptionPane.showMessageDialog(this, error);
            return;
        }

        double peso = -1;
        int edadMeses = -1;
        try {
            peso = Double.parseDouble(txtPeso.getText().trim().replace(',', '.'));

            // La edad siempre se guarda en meses (si eligieron Años se multiplica por 12)
            double valorEdad = Double.parseDouble(txtEdad.getText().trim().replace(',', '.'));
            if (cmbUnidadEdad.getSelectedItem().equals("Años")) {
                edadMeses = (int) Math.round(valorEdad * 12);
            } else {
                edadMeses = (int) Math.round(valorEdad);
            }
        } catch (NumberFormatException ex) {
            // Si hay error de formato, se mantiene en -1 para que el gestor valide el error
        }

        String especie = (String) cmbEspecie.getSelectedItem();
        String codigo = gestor.generarCodigo();
        String sexo = (String) cmbSexo.getSelectedItem();
        String tamanio = (String) cmbTamanio.getSelectedItem();
        String energia = (String) cmbEnergia.getSelectedItem();
        String salud = (String) cmbSalud.getSelectedItem();

        Animal nuevo;
        if (especie.equals("Gato")) {
            nuevo = new Gato(codigo, txtNombre.getText().trim(), txtRaza.getText().trim(), sexo, peso, edadMeses,
                    tamanio, energia, salud, txtHistorial.getText().trim(), txtIngreso.getText().trim());
        } else {
            nuevo = new Perro(codigo, txtNombre.getText().trim(), txtRaza.getText().trim(), sexo, peso, edadMeses,
                    tamanio, energia, salud, txtHistorial.getText().trim(), txtIngreso.getText().trim());
        }
        nuevo.setFechaUltimaAtencion(txtUltimaAtencion.getText().trim());

        String mensaje = gestor.registrarAnimal(nuevo);
        if (mensaje.startsWith("Error")) {
            JOptionPane.showMessageDialog(this, mensaje);
        } else {
            JOptionPane.showMessageDialog(this, mensaje + "\nCódigo asignado: " + nuevo.getCodigo());
            limpiarCampos();
            mostrarAnimales(gestor.getListaAnimales());
        }
    }

    // Devuelve el mensaje de error del primer campo inválido, o "" si todo está bien
    private String validarCampos() {
        String nombre = txtNombre.getText().trim();
        String raza = txtRaza.getText().trim();
        String peso = txtPeso.getText().trim();
        String edad = txtEdad.getText().trim();
        String historial = txtHistorial.getText().trim();
        String ultimaAtencion = txtUltimaAtencion.getText().trim();
        String ingreso = txtIngreso.getText().trim();

        //1. Nombre: solo letras y espacios (sin números)
        if (nombre.isEmpty()) {
            return "Error: Ingrese el nombre o alias del animal";
        }
        if (!soloLetras(nombre) || nombre.length() < 2 || nombre.length() > 30) {
            return "Error: El nombre solo debe tener letras (entre 2 y 30 caracteres), sin números ni símbolos";
        }

        //2. Raza: solo letras y espacios (sin números)
        if (raza.isEmpty()) {
            return "Error: Ingrese la raza del animal";
        }
        if (!soloLetras(raza) || raza.length() < 3 || raza.length() > 30) {
            return "Error: La raza solo debe tener letras (entre 3 y 30 caracteres), sin números ni símbolos";
        }

        //3. Peso: número positivo y razonable (máximo 100 kg)
        if (peso.isEmpty()) {
            return "Error: Ingrese el peso del animal";
        }
        if (!esNumero(peso)) {
            return "Error: El peso debe ser un número positivo (ejemplo: 12.5), sin letras ni signos";
        }
        double pesoNumero = Double.parseDouble(peso.replace(',', '.'));
        if (pesoNumero <= 0 || pesoNumero > 100) {
            return "Error: El peso debe ser mayor a 0 y máximo 100 kg";
        }

        //4. Edad: número positivo; el máximo es 25 años (300 meses)
        if (edad.isEmpty()) {
            return "Error: Ingrese la edad del animal";
        }
        if (!esNumero(edad)) {
            return "Error: La edad debe ser un número positivo, sin letras ni signos";
        }
        double edadNumero = Double.parseDouble(edad.replace(',', '.'));
        if (cmbUnidadEdad.getSelectedItem().equals("Años")) {
            edadNumero = edadNumero * 12; // se pasa a meses, igual que al registrar
        }
        if (edadNumero < 0.5 || edadNumero > 300) {
            return "Error: La edad debe ser de al menos 1 mes y máximo 25 años (300 meses)";
        }

        //5. Historial médico: debe tener letras (no solo números) y no ser muy largo
        if (historial.isEmpty()) {
            return "Error: Ingrese el historial médico (diagnóstico)";
        }
        if (!historial.matches(".*[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ].*") || historial.length() > 150) {
            return "Error: El historial médico debe describirse con letras (máximo 150 caracteres)";
        }

        //6. Fechas: dd/MM/yyyy, que existan y que no sean futuras
        if (ingreso.isEmpty()) {
            return "Error: Ingrese la fecha de ingreso";
        }
        if (!esFechaReal(ingreso)) {
            return "Error: La fecha de ingreso no existe, use el formato dd/MM/yyyy (ejemplo: 15/11/2026)";
        }
        if (esFechaFutura(ingreso)) {
            return "Error: La fecha de ingreso no puede ser posterior a hoy";
        }
        if (!ultimaAtencion.isEmpty()) {
            if (!esFechaReal(ultimaAtencion)) {
                return "Error: La fecha de la última atención no existe, use el formato dd/MM/yyyy";
            }
            if (esFechaFutura(ultimaAtencion)) {
                return "Error: La fecha de la última atención no puede ser posterior a hoy";
            }
        }
        return "";
    }

    // Solo letras (con tildes y ñ) separadas por espacios
    private boolean soloLetras(String texto) {
        return texto.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ]+( [a-zA-ZáéíóúÁÉÍÓÚñÑüÜ]+)*");
    }

    // Número positivo con máximo 2 decimales (acepta punto o coma) y hasta 6 dígitos enteros
    private boolean esNumero(String texto) {
        return texto.matches("[0-9]{1,6}([.,][0-9]{1,2})?");
    }

    // Comprueba que la fecha dd/MM/yyyy exista de verdad (por ejemplo, no acepta 31/02/2026)
    private boolean esFechaReal(String fecha) {
        if (!fecha.matches("[0-9]{2}/[0-9]{2}/[0-9]{4}")) {
            return false;
        }
        int dia = Integer.parseInt(fecha.substring(0, 2));
        int mes = Integer.parseInt(fecha.substring(3, 5));
        int anio = Integer.parseInt(fecha.substring(6, 10));
        if (anio < 2000 || mes < 1 || mes > 12 || dia < 1) {
            return false;
        }
        int diasDelMes = 31;
        if (mes == 4 || mes == 6 || mes == 9 || mes == 11) {
            diasDelMes = 30;
        } else if (mes == 2) {
            boolean bisiesto = (anio % 4 == 0 && anio % 100 != 0) || anio % 400 == 0;
            diasDelMes = bisiesto ? 29 : 28;
        }
        return dia <= diasDelMes;
    }

    // Convierte la fecha a un número yyyyMMdd y la compara con la de hoy
    private boolean esFechaFutura(String fecha) {
        int numeroFecha = Integer.parseInt(fecha.substring(6, 10)) * 10000
                + Integer.parseInt(fecha.substring(3, 5)) * 100
                + Integer.parseInt(fecha.substring(0, 2));
        int hoy = Integer.parseInt(new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date()));
        return numeroFecha > hoy;
    }

    private void mostrarAnimales(ArrayList<Animal> animales) {
        areaListado.setText("");
        if (animales.isEmpty()) {
            areaListado.setText("No hay animales para mostrar en esta lista.");
            return;
        }
        for (Animal a : animales) {
            areaListado.append(a.toString() + "\n----------------------------------------------------\n");
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtRaza.setText("");
        txtPeso.setText("");
        txtEdad.setText("");
        txtHistorial.setText("");
        txtUltimaAtencion.setText("");
        txtIngreso.setText("");
        txtCodigo.setText("");
    }

    public static void main(String[] args) {
        GestorAnimales gestor = new GestorAnimales();
        VentanaAnimales ventana = new VentanaAnimales(gestor);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setVisible(true);
    }
}