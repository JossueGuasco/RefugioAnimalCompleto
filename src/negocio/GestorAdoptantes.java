package negocio;

import java.util.ArrayList;
import modelo.Adoptante;

public class GestorAdoptantes {

    private ArrayList<Adoptante> listaAdoptantes;
    private Adoptante adoptanteActivo; // Adoptante con el que se está trabajando

    public GestorAdoptantes() {
        listaAdoptantes = new ArrayList<>();
        adoptanteActivo = null;
    }

    public String registrarAdoptante(Adoptante nuevoAdoptante) {

        if (estaVacio(nuevoAdoptante.getNombreCompleto())
                || estaVacio(nuevoAdoptante.getIdentificacion())
                || estaVacio(nuevoAdoptante.getTelefono())
                || estaVacio(nuevoAdoptante.getCorreoElectronico())
                || estaVacio(nuevoAdoptante.getTipoVivienda())
                || estaVacio(nuevoAdoptante.getPreferenciaTamanio())) {
            return "Error: Todos los campos son obligatorios";
        }

        String nombre = nuevoAdoptante.getNombreCompleto().trim();
        if (!esSoloLetras(nombre)) {
            return "Error: El nombre solo puede contener letras y espacios";
        }
        if (nombre.length() < 3 || nombre.length() > 60) {
            return "Error: El nombre debe tener entre 3 y 60 caracteres";
        }

        String cedula = nuevoAdoptante.getIdentificacion().trim();
        if (cedula.length() != 10 || !esSoloNumeros(cedula)) {
            return "Error: La cédula debe tener exactamente 10 dígitos numéricos";
        }


        if (!validarIdentificacionUnica(cedula)) {
            return "Error: Ya existe un adoptante registrado con esa cédula";
        }


        int edad = nuevoAdoptante.getEdad();
        if (edad <= 0) {
            return "Error: La edad debe ser un número positivo";
        }
        if (!nuevoAdoptante.esMayorDeEdad()) {
            return "Error: El solicitante debe ser mayor de edad para iniciar un proceso de adopción";
        }
        if (edad > 100) {
            return "Error: La edad ingresada no es válida (máximo 100 años)";
        }


        String telefono = nuevoAdoptante.getTelefono().trim();
        if (telefono.length() != 10 || !esSoloNumeros(telefono)) {
            return "Error: El teléfono debe tener exactamente 10 dígitos numéricos";
        }


        if (!esCorreoValido(nuevoAdoptante.getCorreoElectronico().trim())) {
            return "Error: El correo electrónico no tiene un formato válido (ejemplo: nombre@gmail.com)";
        }

        double tamanio = nuevoAdoptante.getTamanioVivienda();
        if (tamanio <= 0) {
            return "Error: El tamaño de la vivienda debe ser mayor a 0";
        }
        if (tamanio > 1000) {
            return "Error: El tamaño de la vivienda no es válido (máximo 1000 m²)";
        }

        int mascotas = nuevoAdoptante.getNumeroMascotas();
        if (mascotas < 0) {
            return "Error: El número de mascotas no puede ser negativo";
        }
        if (mascotas > 20) {
            return "Error: El número de mascotas no es válido (máximo 20)";
        }

        listaAdoptantes.add(nuevoAdoptante);
        return "Habilitado para adoptar";
    }


    public boolean validarIdentificacionUnica(String identificacion) {
        for (Adoptante adoptante : listaAdoptantes) {
            if (adoptante.getIdentificacion().equals(identificacion)) {
                return false; // Ya existe
            }
        }
        return true; // No existe, se puede usar
    }


    public Adoptante buscarAdoptantePorCedula(String identificacion) {
        for (Adoptante adoptante : listaAdoptantes) {
            if (adoptante.getIdentificacion().equals(identificacion)) {
                return adoptante;
            }
        }
        return null;
    }


    private boolean estaVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    private boolean esSoloNumeros(String texto) {
        for (int i = 0; i < texto.length(); i++) {
            if (!Character.isDigit(texto.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean esSoloLetras(String texto) {
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (!Character.isLetter(c) && c != ' ') {
                return false;
            }
        }
        return true;
    }

    private boolean esCorreoValido(String correo) {
        if (correo.contains(" ")) {
            return false;
        }
        int posicionArroba = correo.indexOf("@");
        int posicionPunto = correo.lastIndexOf(".");

        if (posicionArroba <= 0) {
            return false;
        }
        if (posicionPunto < posicionArroba + 2) {
            return false;
        }
        if (posicionPunto == correo.length() - 1) {
            return false;
        }
        return true;
    }


    public ArrayList<Adoptante> getListaAdoptantes() {
        return listaAdoptantes;
    }

    public Adoptante getAdoptanteActivo() {
        return adoptanteActivo;
    }

    public void setAdoptanteActivo(Adoptante adoptanteActivo) {
        this.adoptanteActivo = adoptanteActivo;
    }
}
