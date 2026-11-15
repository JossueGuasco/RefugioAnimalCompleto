package modelo;

public class Adoptante {

    private String nombreCompleto;
    private String identificacion; //cedula 10 dígitos
    private int edad;
    private String telefono;
    private String correoElectronico;
    private String tipoVivienda; // Casa o Departamento
    private double tamanioVivienda; //Metros cuadrados
    private boolean tienePatio;
    private int numeroMascotas; //mascotas que ya tiene
    private String preferenciaTamanio; //Pequeño, Mediano, Grande
    private String clasificacionAptitud; // APTO / RESTRINGIDO / NO APTO


    public Adoptante(String nombreCompleto, String identificacion, int edad, String telefono, String correoElectronico, String tipoVivienda, double tamanioVivienda, boolean tienePatio, int numeroMascotas, String preferenciaTamanio) {
        this.nombreCompleto = nombreCompleto;
        this.identificacion = identificacion;
        this.edad = edad;
        this.telefono = telefono;
        this.correoElectronico = correoElectronico;
        this.tipoVivienda = tipoVivienda;
        this.tamanioVivienda = tamanioVivienda;
        this.tienePatio = tienePatio;
        this.numeroMascotas = numeroMascotas;
        this.preferenciaTamanio = preferenciaTamanio;
        this.clasificacionAptitud = "SIN EVALUAR";
    }

    public boolean esMayorDeEdad() {
        return edad >= 18;
    }

    public String evaluarAptitudVivienda() {
        String mensaje;
        if (tamanioVivienda < 20 || numeroMascotas >= 4) {
            clasificacionAptitud = "NO APTO";
            mensaje = "Evaluación no aprobada. Clasificación: NO APTO para adopción";
        } else if (preferenciaTamanio.equalsIgnoreCase("Pequeño") || preferenciaTamanio.equalsIgnoreCase("Pequeno")) {
            clasificacionAptitud = "APTO";
            mensaje = "Evaluación exitosa. Clasificación: APTO para adopción";
        } else if (preferenciaTamanio.equalsIgnoreCase("Mediano")) {
            if(tamanioVivienda >= 40) {
                clasificacionAptitud = "APTO";
                mensaje = "Evaluación exitosa. Clasificación: APTO para adoptar";
            } else {
                clasificacionAptitud = "RESTRINGIDO";
                mensaje = "Advertencia: Espacio no recomendado para animales medianos. Clasificación: RESTRINGIDO";
            }
        } else if (preferenciaTamanio.equalsIgnoreCase("Grande")) {
            if (tienePatio || tamanioVivienda >= 80) {
                clasificacionAptitud = "APTO";
                mensaje = "Evaluación exitosa. Clasificación: APTO para adopción";
            } else {
                clasificacionAptitud = "RESTRINGIDO";
                mensaje = "Advertencia: Espacio no recomendado para razas grandes. Clasificación: RESTRINGIDO";
            }
        } else {
            mensaje = "Error: La preferencia de tamaño debe ser Pequeño, Mediano o Grande";
        }
        return mensaje;
    }


    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getTipoVivienda() {
        return tipoVivienda;
    }

    public void setTipoVivienda(String tipoVivienda) {
        this.tipoVivienda = tipoVivienda;
    }

    public double getTamanioVivienda() {
        return tamanioVivienda;
    }

    public void setTamanioVivienda(double tamanioVivienda) {
        this.tamanioVivienda = tamanioVivienda;
    }

    public boolean isTienePatio() {
        return tienePatio;
    }

    public void setTienePatio(boolean tienePatio) {
        this.tienePatio = tienePatio;
    }

    public int getNumeroMascotas() {
        return numeroMascotas;
    }

    public void setNumeroMascotas(int numeroMascotas) {
        this.numeroMascotas = numeroMascotas;
    }

    public String getPreferenciaTamanio() {
        return preferenciaTamanio;
    }

    public void setPreferenciaTamanio(String preferenciaTamanio) {
        this.preferenciaTamanio = preferenciaTamanio;
    }

    public String getClasificacionAptitud() {
        return clasificacionAptitud;
    }

    public void setClasificacionAptitud(String clasificacionAptitud) {
        this.clasificacionAptitud = clasificacionAptitud;
    }


    @Override
    public String toString() {
        String patio;
        if (tienePatio) {
            patio = "Sí";
        } else {
            patio = "No";
        }
        return "Adoptante \n"
                + "Nombre: " + nombreCompleto + "\n"
                + "Cédula: " + identificacion + "\n"
                + "Edad: " + edad + " años\n"
                + "Teléfono: " + telefono + "\n"
                + "Correo: " + correoElectronico + "\n"
                + "Tipo de vivienda: " + tipoVivienda + "\n"
                + "Tamaño de vivienda: " + tamanioVivienda + " m²\n"
                + "Tiene patio: " + patio + "\n"
                + "Mascotas actuales: " + numeroMascotas + "\n"
                + "Preferencia de tamaño: " + preferenciaTamanio + "\n"
                + "Clasificación de aptitud: " + clasificacionAptitud;
    }
}

