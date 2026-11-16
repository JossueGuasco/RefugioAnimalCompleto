package modelo;

public class Perro extends Animal {

    public Perro(String codigo, String nombre, String raza, String sexo, double peso, int edadMeses, String tamanio, String nivelEnergia, String estadoSalud, String historialMedico, String fechaIngreso) {
        super(codigo, nombre, "Perro", raza, sexo, peso, edadMeses, tamanio, nivelEnergia, estadoSalud, historialMedico, fechaIngreso);
    }

    // El peso mínimo normal de un perro depende de su tamaño
    @Override
    public double obtenerPesoMinimo() {
        if (getTamanio().equalsIgnoreCase("Pequeño") || getTamanio().equalsIgnoreCase("Pequeno")) {
            return 3;
        } else if (getTamanio().equalsIgnoreCase("Mediano")) {
            return 10;
        } else if (getTamanio().equalsIgnoreCase("Grande")) {
            return 20;
        }
        return 0;
    }
}