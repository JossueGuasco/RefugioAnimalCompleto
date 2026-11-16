package modelo;

public class Gato extends Animal {

    public Gato(String codigo, String nombre, String raza, String sexo, double peso, int edadMeses, String tamanio, String nivelEnergia, String estadoSalud, String historialMedico, String fechaIngreso) {
        super(codigo, nombre, "Gato", raza, sexo, peso, edadMeses, tamanio, nivelEnergia, estadoSalud, historialMedico, fechaIngreso);
    }

    // El peso mínimo normal de un gato es el mismo sin importar el tamaño
    @Override
    public double obtenerPesoMinimo() {
        return 2.5;
    }
}
