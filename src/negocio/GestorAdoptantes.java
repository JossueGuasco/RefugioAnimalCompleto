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

        //1. Campos obligatorios vacíos
        if (estaVacio(nuevoAdoptante.getNombreCompleto())
                || estaVacio(nuevoAdoptante.getIdentificacion())
                || estaVacio(nuevoAdoptante.getTelefono())
                || estaVacio(nuevoAdoptante.getCorreoElectronico())
                || estaVacio(nuevoAdoptante.getTipoVivienda())
                || estaVacio(nuevoAdoptante.getPreferenciaTamanio())) {
            return "Error: Todos los campos son obligatorios,";
        }

        //2. La cédula debe tener 10 dígitos
        if (!esCedulaValida(nuevoAdoptante.getIdentificacion())) {
            return "Error: La cédula debe tener exactamente 10 dígitos numéricos";
        }

        //3. La cedula no puede estar repetida
        if(!validarIdentificacionUnica(nuevoAdoptante.getIdentificacion())) {
            return "Error: Ya existe un adoptante registrado con esa cédula";
        }

        //4. Debe ser mayor de edad
        if (!nuevoAdoptante.esMayorDeEdad()) {
            return "Error: El solicitante debe ser mayor de edad para iniciar un proceso de adopción";
        }

        //5. Valores númericps válidos
        if (nuevoAdoptante.getTamanioVivienda() <= 0) {
            return "Error: El tamaño de la vivienda debe ser mayor a 0";
        }

        if (nuevoAdoptante.getNumeroMascotas() < 0) {
            return "Error: El número de mascotas no puede ser negativo";
        }

        //Si pasó todas las validaciones, se guarda
        listaAdoptantes.add(nuevoAdoptante);
        return "Habilitado para adoptar";
    }

    public boolean validarIdentificacionUnica(String identificacion) {
        for (Adoptante adoptante : listaAdoptantes) {
            if (adoptante.getIdentificacion().equals(identificacion)) {
                return false; // ya existe
            }
        }
        return true; // no existe, se puede usar
    }
    public Adoptante buscarAdoptantePorCedula(String identificacion) {
        for (Adoptante adoptante : listaAdoptantes) {
            if (adoptante.getIdentificacion().equals(identificacion)) {
                return adoptante; // se encontró el adoptante
            }
        }
        return null; // no se encontró el adoptante
    }


    private boolean estaVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    private boolean esCedulaValida(String cedula) {
        if (cedula.length() != 10) {
            return false;
        }
        for(int i = 0; i < cedula.length(); i++) {
            if(!Character.isDigit(cedula.charAt(i))) {
                return false;
            }
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
