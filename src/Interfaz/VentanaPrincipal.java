package Interfaz;

import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import negocio.GestorAdoptantes;
import negocio.GestorAnimales;

public class VentanaPrincipal {

    private final GestorAnimales gestorAnimales = new GestorAnimales();
    private final GestorAdoptantes gestorAdoptantes = new GestorAdoptantes();

    private JFrame ventanaAnimales;
    private JFrame ventanaAdoptantes;
    private JFrame ventanaSolicitudes;
    private JFrame ventanaInventario;

    public VentanaPrincipal() {
        JFrame ventana = new JFrame("Refugio Animal");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(480, 440);
        ventana.setLocationRelativeTo(null);
        ventana.setContentPane(crearContenido());
        ventana.setVisible(true);
    }

    private JPanel crearContenido() {
        JPanel contenido = new JPanel(new BorderLayout());
        contenido.setBackground(new Color(245, 248, 245));

        JPanel encabezado = new JPanel(new GridLayout(2, 1, 0, 5));
        encabezado.setBackground(new Color(47, 105, 78));
        encabezado.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        JLabel titulo = new JLabel("Refugio Animal", SwingConstants.CENTER);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        encabezado.add(titulo);

        JLabel subtitulo = new JLabel("Sistema de gestion", SwingConstants.CENTER);
        subtitulo.setForeground(new Color(230, 240, 232));
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        encabezado.add(subtitulo);
        contenido.add(encabezado, BorderLayout.NORTH);

        JPanel botones = new JPanel(new GridLayout(4, 1, 10, 10));
        botones.setBackground(new Color(245, 248, 245));
        botones.setBorder(BorderFactory.createEmptyBorder(25, 45, 20, 45));
        botones.add(crearBoton("Gestion de animales", this::abrirAnimales));
        botones.add(crearBoton("Gestion de adoptantes", this::abrirAdoptantes));
        botones.add(crearBoton("Solicitudes de adopcion", this::abrirSolicitudes));
        botones.add(crearBoton("Inventario", this::abrirInventario));
        contenido.add(botones, BorderLayout.CENTER);

        JLabel pie = new JLabel("Seleccione una opcion para comenzar", SwingConstants.CENTER);
        pie.setForeground(new Color(90, 105, 95));
        pie.setBorder(BorderFactory.createEmptyBorder(0, 10, 15, 10));
        contenido.add(pie, BorderLayout.SOUTH);
        return contenido;
    }

    private JButton crearBoton(String texto, Runnable accion) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setForeground(new Color(40, 75, 55));
        boton.addActionListener(evento -> accion.run());
        return boton;
    }

    private void abrirAnimales() {
        if (ventanaAnimales == null || !ventanaAnimales.isDisplayable()) {
            ventanaAnimales = new VentanaAnimales(gestorAnimales);
        }
        mostrarVentanaModulo(ventanaAnimales);
    }

    private void abrirAdoptantes() {
        if (ventanaAdoptantes == null || !ventanaAdoptantes.isDisplayable()) {
            ventanaAdoptantes = new VentanaAdoptantes(gestorAdoptantes);
        }
        mostrarVentanaModulo(ventanaAdoptantes);
    }

    private void abrirSolicitudes() {
        if (ventanaSolicitudes == null || !ventanaSolicitudes.isDisplayable()) {
            ventanaSolicitudes = new VentanaSolicitudes();
        }
        mostrarVentanaModulo(ventanaSolicitudes);
    }

    private void abrirInventario() {
        if (ventanaInventario == null || !ventanaInventario.isDisplayable()) {
            ventanaInventario = new VentanaInventario();
        }
        mostrarVentanaModulo(ventanaInventario);
    }

    private void mostrarVentanaModulo(JFrame ventana) {
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.setVisible(true);
        ventana.toFront();
        ventana.requestFocus();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaPrincipal());
    }
}
