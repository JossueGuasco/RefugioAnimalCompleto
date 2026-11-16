package modelo;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Animal {

    private String codigo;
    private String nombre;
    private String especie; // Perro o Gato
    private String raza;
    private String sexo;
    private double peso; // en kg
    private int edadMeses; // edad aproximada al ingreso
    private String tamanio; // Pequeño, Mediano, Grande
    private String nivelEnergia; // Bajo, Medio, Alto
    private String estadoSalud; // Sano, Herida leve, Desnutrido, Enfermo, Crítico
    private String historialMedico;
    private String fechaIngreso; // formato dd/MM/yyyy
    private String fechaUltimaAtencion; // formato dd/MM/yyyy
    private String nivelUrgencia; // Baja, Media, Alta, Crítica
    private String estado; // En evaluación, Disponible, Adoptado

    public Animal(String codigo, String nombre, String especie, String raza, String sexo, double peso, int edadMeses, String tamanio, String nivelEnergia, String estadoSalud, String historialMedico, String fechaIngreso) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.sexo = sexo;
        this.peso = peso;
        this.edadMeses = edadMeses;
        this.tamanio = tamanio;
        this.nivelEnergia = nivelEnergia;
        this.estadoSalud = estadoSalud;
        this.historialMedico = historialMedico;
        this.fechaIngreso = fechaIngreso;
        this.fechaUltimaAtencion = "";
        this.nivelUrgencia = "SIN EVALUAR";
        this.estado = "En evaluación";
    }

    //métodos para trabajar con las fechas (dd/MM/yyyy)

    private String obtenerFechaActual() {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        return formato.format(new Date());
    }

    private int obtenerDia(String fecha) {
        return Integer.parseInt(fecha.substring(0, 2));
    }

    private int obtenerMes(String fecha) {
        return Integer.parseInt(fecha.substring(3, 5));
    }

    private int obtenerAnio(String fecha) {
        return Integer.parseInt(fecha.substring(6, 10));
    }

    // Convierte la fecha a días (aproximado: cada mes tiene 30 días y cada año 365)
    private int convertirADias(String fecha) {
        return obtenerAnio(fecha) * 365 + obtenerMes(fecha) * 30 + obtenerDia(fecha);
    }

    private int diasSinAtencion() {
        String fechaBase = fechaUltimaAtencion;
        // Si nunca tuvo atención se cuenta desde el ingreso
        if (fechaBase == null || fechaBase.trim().isEmpty()) {
            fechaBase = fechaIngreso;
        }
        int dias = convertirADias(obtenerFechaActual()) - convertirADias(fechaBase);
        if (dias < 0) {
            dias = 0;
        }
        return dias;
    }

    // metodo para calcular el puntaje de urgencia

    private int puntosPorSalud() {
        if (estadoSalud.equalsIgnoreCase("Sano")) {
            return 0;
        } else if (estadoSalud.equalsIgnoreCase("Herida leve")) {
            return 1;
        } else if (estadoSalud.equalsIgnoreCase("Desnutrido")) {
            return 2;
        } else if (estadoSalud.equalsIgnoreCase("Enfermo")) {
            return 3;
        } else if (estadoSalud.equalsIgnoreCase("Crítico") || estadoSalud.equalsIgnoreCase("Critico")) {
            return 5;
        }
        return 0;
    }

    private int puntosPorEdad() {
        int edadActual = calcularEdadActualMeses();
        // Menos de 6 meses o más de 8 años 96 meses
        if (edadActual < 6 || edadActual > 96) {
            return 2;
        }
        return 0;
    }

    private int puntosPorPeso() {
        double pesoMinimo = 0;
        if (especie.equalsIgnoreCase("Gato")) {
            pesoMinimo = 2.5;
        } else if (tamanio.equalsIgnoreCase("Pequeño") || tamanio.equalsIgnoreCase("Pequeno")) {
            pesoMinimo = 3;
        } else if (tamanio.equalsIgnoreCase("Mediano")) {
            pesoMinimo = 10;
        } else if (tamanio.equalsIgnoreCase("Grande")) {
            pesoMinimo = 20;
        }
        if (peso < pesoMinimo) {
            return 1;
        }
        return 0;
    }

    private int puntosPorAtencion() {
        if (diasSinAtencion() > 3) {
            return 1;
        }
        return 0;
    }

    //Métodos de las reglas de negocio

    public int calcularPuntajeUrgencia() {
        return puntosPorSalud() + puntosPorEdad() + puntosPorPeso() + puntosPorAtencion();
    }

    public String detallePuntaje() {
        return "Estado de salud (" + estadoSalud + "): " + puntosPorSalud() + "\n"
                + "Edad menor a 6 meses o mayor a 8 años: " + puntosPorEdad() + "\n"
                + "Peso bajo de lo normal: " + puntosPorPeso() + "\n"
                + "Más de 3 días sin atención veterinaria: " + puntosPorAtencion() + "\n"
                + "PUNTAJE FINAL: " + calcularPuntajeUrgencia();
    }

    public String clasificarNivelUrgencia() {
        int puntaje = calcularPuntajeUrgencia();
        if (puntaje >= 6) {
            nivelUrgencia = "CRÍTICA";
        } else if (puntaje >= 4) {
            nivelUrgencia = "ALTA";
        } else if (puntaje >= 2) {
            nivelUrgencia = "MEDIA";
        } else {
            nivelUrgencia = "BAJA";
        }
        return nivelUrgencia;
    }

    public int calcularEdadActualMeses() {
        String hoy = obtenerFechaActual();
        int mesesTranscurridos = (obtenerAnio(hoy) - obtenerAnio(fechaIngreso)) * 12 + (obtenerMes(hoy) - obtenerMes(fechaIngreso));
        if (mesesTranscurridos < 0) {
            mesesTranscurridos = 0;
        }
        return edadMeses + mesesTranscurridos;
    }

    public int calcularDiasEnRefugio() {
        int dias = convertirADias(obtenerFechaActual()) - convertirADias(fechaIngreso);
        if (dias < 0) {
            dias = 0;
        }
        return dias;
    }

    public int calcularTiempoEstimadoRefugio() {
        String nivel = clasificarNivelUrgencia();
        if (nivel.equals("CRÍTICA")) {
            return 60;
        } else if (nivel.equals("ALTA")) {
            return 30;
        } else if (nivel.equals("MEDIA")) {
            return 15;
        }
        return 7;
    }

    public boolean estaDisponible() {
        return estado.equalsIgnoreCase("Disponible");
    }

    public void actualizarEstadoAnimal(String nuevoEstado) {
        if (nuevoEstado.equalsIgnoreCase("En evaluación") || nuevoEstado.equalsIgnoreCase("En evaluacion")) {
            estado = "En evaluación";
        } else if (nuevoEstado.equalsIgnoreCase("Disponible")) {
            estado = "Disponible";
        } else if (nuevoEstado.equalsIgnoreCase("Adoptado")) {
            estado = "Adoptado";
        }
    }

    //Getters y setters

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public int getEdadMeses() {
        return edadMeses;
    }

    public void setEdadMeses(int edadMeses) {
        this.edadMeses = edadMeses;
    }

    public String getTamanio() {
        return tamanio;
    }

    public void setTamanio(String tamanio) {
        this.tamanio = tamanio;
    }

    public String getNivelEnergia() {
        return nivelEnergia;
    }

    public void setNivelEnergia(String nivelEnergia) {
        this.nivelEnergia = nivelEnergia;
    }

    public String getEstadoSalud() {
        return estadoSalud;
    }

    public void setEstadoSalud(String estadoSalud) {
        this.estadoSalud = estadoSalud;
    }

    public String getHistorialMedico() {
        return historialMedico;
    }

    public void setHistorialMedico(String historialMedico) {
        this.historialMedico = historialMedico;
    }

    public String getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(String fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getFechaUltimaAtencion() {
        return fechaUltimaAtencion;
    }

    public void setFechaUltimaAtencion(String fechaUltimaAtencion) {
        this.fechaUltimaAtencion = fechaUltimaAtencion;
    }

    public String getNivelUrgencia() {
        return nivelUrgencia;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Animal \n"
                + "Código: " + codigo + "\n"
                + "Nombre: " + nombre + "\n"
                + "Especie: " + especie + "\n"
                + "Raza: " + raza + "\n"
                + "Sexo: " + sexo + "\n"
                + "Peso: " + peso + " kg\n"
                + "Edad: " + calcularEdadActualMeses() + " meses\n"
                + "Tamaño: " + tamanio + "\n"
                + "Nivel de energía: " + nivelEnergia + "\n"
                + "Estado de salud: " + estadoSalud + "\n"
                + "Historial médico: " + historialMedico + "\n"
                + "Fecha de ingreso: " + fechaIngreso + "\n"
                + "Nivel de urgencia: " + nivelUrgencia + "\n"
                + "Estado: " + estado;
    }

    // El peso mínimo normal de un gato es el mismo sin importar el tamaño
    public abstract double obtenerPesoMinimo();
}
