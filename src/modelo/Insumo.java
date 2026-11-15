package modelo;

public class Insumo {
    private String idInsumo;
    private String nombre;
    private String tipoInsumo;
    private String donante;
    private int cantidad;
    private String fechaCaducidad;
    private String fechaRegistro;

    public Insumo(String idInsumo, String nombre, String tipoInsumo, String donante, int cantidad, String fechaCaducidad, String fechaRegistro) {
        this.idInsumo = idInsumo;
        this.nombre = nombre;
        this.tipoInsumo = tipoInsumo;
        this.donante = donante;
        this.cantidad = cantidad;
        this.fechaCaducidad = fechaCaducidad;
        this.fechaRegistro = fechaRegistro;
    }

    public String getIdInsumo() {
        return idInsumo;
    }

    public void setIdInsumo(String idInsumo) {
        this.idInsumo = idInsumo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoInsumo() {
        return tipoInsumo;
    }

    public void setTipoInsumo(String tipoInsumo) {
        this.tipoInsumo = tipoInsumo;
    }

    public String getDonante() {
        return donante;
    }

    public void setDonante(String donante) {
        this.donante = donante;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(String fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    @Override
    public String toString() {
        return "ID: " + idInsumo +
                " | Producto: " + nombre +
                " (" + tipoInsumo + ")" +
                " | Cantidad: " + cantidad +
                " | Donante: " + donante +
                " | Caducidad: " + fechaCaducidad +
                " | Registro: " + fechaRegistro;
    }
}
