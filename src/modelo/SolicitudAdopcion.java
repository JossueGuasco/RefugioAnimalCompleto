package modelo;

public class SolicitudAdopcion {
    private String idSolicitud;
    private String fechaSolicitud;
    private String estado;
    private double porcentajeCompatibilidad;
    private Adoptante adoptante;
    private Animal animal;

    public SolicitudAdopcion(String idSolicitud, String fechaSolicitud, Adoptante adoptante, Animal animal) {
        this.idSolicitud = idSolicitud;
        this.fechaSolicitud = fechaSolicitud;
        this.adoptante = adoptante;
        this.animal = animal;
        this.estado = "Pendiente";
        this.porcentajeCompatibilidad = 0.0;
    }

    public double calcularCompatibilidad() {
        double puntaje = 60.0;

        // Regla de compatibilidad entre vivienda y animal
        if (adoptante.getClasificacionAptitud().equalsIgnoreCase("Apto")) {
            puntaje = 85.0;
        } else if (adoptante.getClasificacionAptitud().equalsIgnoreCase("Restringido")) {
            puntaje = 50.0;
        } else {
            puntaje = 30.0;
        }

        this.porcentajeCompatibilidad = puntaje;
        return this.porcentajeCompatibilidad;
    }

    public void actualizarEstadoSolicitud(String nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public String getIdSolicitud() { return idSolicitud; }
    public String getFechaSolicitud() { return fechaSolicitud; }
    public String getEstado() { return estado; }
    public double getPorcentajeCompatibilidad() { return porcentajeCompatibilidad; }
    public Adoptante getAdoptante() { return adoptante; }
    public Animal getAnimal() { return animal; }

    @Override
    public String toString() {
        return "Solicitud [ID: " + idSolicitud + ", Fecha: " + fechaSolicitud + ", Estado: " + estado +
                ", Compatibilidad: " + porcentajeCompatibilidad + "%" + ", Adoptante: " + adoptante.getNombreCompleto() + ", Animal: " + animal.getNombre() + "]";
    }
}
