package negocio;

import modelo.*;
import java.util.ArrayList;

public class GestorSolicitudes {
    private ArrayList<SolicitudAdopcion> listaSolicitudes;

    public GestorSolicitudes() {
        this.listaSolicitudes = new ArrayList<>();
    }

    public boolean crearSolicitud(Adoptante adoptante, Animal animal, String idSolicitud, String fecha) {
        if (!adoptante.esMayorDeEdad()) {
            System.out.println("Error: El solicitante debe ser mayor de edad para iniciar un proceso de adopción.");
            return false;
        }

        if (!animal.getEstado().equalsIgnoreCase("Disponible")) {
            System.out.println("Alerta de Error: El animal seleccionado (" + animal.getNombre() + ") no está disponible para adopción. Estado actual: " + animal.getEstado());
            return false;
        }

        SolicitudAdopcion nuevaSolicitud = new SolicitudAdopcion(idSolicitud, fecha, adoptante, animal);
        listaSolicitudes.add(nuevaSolicitud);
        System.out.println("Solicitud registrada con éxito con estado PENDIENTE de revisión.");
        return true;
    }

    public SolicitudAdopcion buscarSolicitudPorId(String idSolicitud) {
        for (SolicitudAdopcion sol : listaSolicitudes) {
            if (sol.getIdSolicitud().equalsIgnoreCase(idSolicitud)) {
                return sol;
            }
        }
        return null;
    }

    public void procesarEvaluacionAdopcion(String idSolicitud) {
        SolicitudAdopcion sol = buscarSolicitudPorId(idSolicitud);
        if (sol != null) {
            double puntaje = sol.calcularCompatibilidad();
            System.out.println("Puntaje de compatibilidad calculado: " + puntaje + "%");

            if (puntaje >= 70.0) {
                sol.actualizarEstadoSolicitud("APROBADA");
                System.out.println("Solicitud APROBADA automáticamente.");
            } else {
                sol.actualizarEstadoSolicitud("RECHAZADA");
                System.out.println("Solicitud clasificada como RECHAZADA / EN OBSERVACIÓN.");
            }
        } else {
            System.out.println("Error: No se encontró ninguna solicitud con el ID: " + idSolicitud);
        }
    }
}