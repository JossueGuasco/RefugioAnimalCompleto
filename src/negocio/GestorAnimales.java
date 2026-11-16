package negocio;
import java.util.ArrayList;
import modelo.Animal;

public class GestorAnimales {

    private ArrayList<Animal> listaAnimales;
    private Animal animalActivo; // Animal con el que se está trabajando

    public GestorAnimales() {
        listaAnimales = new ArrayList<>();
        animalActivo = null;
    }

    public String registrarAnimal(Animal nuevoAnimal) {

        //1. Campos obligatorios vacíos
        if (estaVacio(nuevoAnimal.getNombre())
                || estaVacio(nuevoAnimal.getEspecie())
                || estaVacio(nuevoAnimal.getRaza())
                || estaVacio(nuevoAnimal.getSexo())
                || estaVacio(nuevoAnimal.getTamanio())
                || estaVacio(nuevoAnimal.getNivelEnergia())
                || estaVacio(nuevoAnimal.getEstadoSalud())
                || estaVacio(nuevoAnimal.getHistorialMedico())
                || estaVacio(nuevoAnimal.getFechaIngreso())) {
            return "Error: Faltan datos obligatorios, el animal no fue registrado";
        }

        //2. La especie debe ser Perro o Gato
        if (!nuevoAnimal.getEspecie().equalsIgnoreCase("Perro") && !nuevoAnimal.getEspecie().equalsIgnoreCase("Gato")) {
            return "Error: La especie debe ser Perro o Gato";
        }

        //3. El tamaño, la energía y la salud deben tener un valor válido
        String tamanio = nuevoAnimal.getTamanio();
        if (!tamanio.equalsIgnoreCase("Pequeño") && !tamanio.equalsIgnoreCase("Pequeno")
                && !tamanio.equalsIgnoreCase("Mediano") && !tamanio.equalsIgnoreCase("Grande")) {
            return "Error: El tamaño debe ser Pequeño, Mediano o Grande";
        }

        String energia = nuevoAnimal.getNivelEnergia();
        if (!energia.equalsIgnoreCase("Bajo") && !energia.equalsIgnoreCase("Medio") && !energia.equalsIgnoreCase("Alto")) {
            return "Error: El nivel de energía debe ser Bajo, Medio o Alto";
        }

        String salud = nuevoAnimal.getEstadoSalud();
        if (!salud.equalsIgnoreCase("Sano") && !salud.equalsIgnoreCase("Herida leve")
                && !salud.equalsIgnoreCase("Desnutrido") && !salud.equalsIgnoreCase("Enfermo")
                && !salud.equalsIgnoreCase("Crítico") && !salud.equalsIgnoreCase("Critico")) {
            return "Error: El estado de salud debe ser Sano, Herida leve, Desnutrido, Enfermo o Crítico";
        }

        //4. Peso y edad positivos
        if (nuevoAnimal.getPeso() <= 0 || nuevoAnimal.getEdadMeses() <= 0) {
            return "Error: El peso y la edad deben ser valores positivos";
        }

        //5. Las fechas deben tener el formato dd/MM/yyyy
        if (!esFechaValida(nuevoAnimal.getFechaIngreso())) {
            return "Error: La fecha de ingreso no es válida (use dd/MM/yyyy)";
        }
        if (!estaVacio(nuevoAnimal.getFechaUltimaAtencion()) && !esFechaValida(nuevoAnimal.getFechaUltimaAtencion())) {
            return "Error: La fecha de la última atención no es válida (use dd/MM/yyyy)";
        }

        //6. El código no puede estar repetido (si no tiene, se le asigna uno)
        if (estaVacio(nuevoAnimal.getCodigo())) {
            nuevoAnimal.setCodigo(generarCodigo());
        } else if (!validarCodigoUnico(nuevoAnimal.getCodigo())) {
            return "Error: Ya existe un animal con el código " + nuevoAnimal.getCodigo();
        }

        //Si pasó todas las validaciones, se guarda
        nuevoAnimal.actualizarEstadoAnimal("En evaluación");
        listaAnimales.add(nuevoAnimal);
        animalActivo = nuevoAnimal;
        return "Animal registrado con estado EN EVALUACION";
    }

    public String generarCodigo() {
        int numero = listaAnimales.size() + 1;
        if (numero < 10) {
            return "ANI-000" + numero;
        } else if (numero < 100) {
            return "ANI-00" + numero;
        } else if (numero < 1000) {
            return "ANI-0" + numero;
        }
        return "ANI-" + numero;
    }

    public boolean validarCodigoUnico(String codigo) {
        for (Animal animal : listaAnimales) {
            if (animal.getCodigo().equalsIgnoreCase(codigo)) {
                return false; // ya existe
            }
        }
        return true; // no existe, se puede usar
    }

    public Animal buscarAnimalPorCodigo(String codigo) {
        for (Animal animal : listaAnimales) {
            if (animal.getCodigo().equalsIgnoreCase(codigo)) {
                return animal; // se encontró el animal
            }
        }
        return null; // no se encontró el animal
    }

    public ArrayList<Animal> listarAnimalesDisponibles() {
        ArrayList<Animal> disponibles = new ArrayList<>();
        for (Animal animal : listaAnimales) {
            if (animal.estaDisponible()) {
                disponibles.add(animal);
            }
        }
        return disponibles;
    }

    public String evaluarUrgenciaAnimal(String codigo) {
        Animal animal = buscarAnimalPorCodigo(codigo);
        if (animal == null) {
            return "Error: No se encontró ningún animal con el código " + codigo;
        }
        animalActivo = animal;

        String nivel = animal.clasificarNivelUrgencia();
        int diasEnRefugio = animal.calcularDiasEnRefugio();
        int tiempoEstimado = animal.calcularTiempoEstimadoRefugio();

        String resultado = "Evaluación de " + animal.getNombre() + " (" + animal.getCodigo() + ")\n";
        resultado = resultado + animal.detallePuntaje() + "\n";
        resultado = resultado + "Nivel de urgencia: " + nivel + "\n";
        resultado = resultado + "Edad aproximada actual: " + animal.calcularEdadActualMeses() + " meses\n";
        resultado = resultado + "Días en el refugio: " + diasEnRefugio + "\n";
        resultado = resultado + "Tiempo estimado de permanencia: " + tiempoEstimado + " días\n";

        // Si ya pasó el tiempo estimado y está sano, pasa a Disponible
        if (animal.getEstado().equalsIgnoreCase("En evaluación") && animal.getEstadoSalud().equalsIgnoreCase("Sano") && diasEnRefugio > tiempoEstimado) {
            animal.actualizarEstadoAnimal("Disponible");
            resultado = resultado + "El animal pasó a estado DISPONIBLE y ya puede ser seleccionado para una solicitud de adopción";
        } else {
            resultado = resultado + "Estado actual: " + animal.getEstado();
        }
        return resultado;
    }

    private boolean estaVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    // La fecha debe ser dd/MM/yyyy, por ejemplo 15/11/2026
    private boolean esFechaValida(String fecha) {
        if (fecha.length() != 10 || fecha.charAt(2) != '/' || fecha.charAt(5) != '/') {
            return false;
        }
        for (int i = 0; i < fecha.length(); i++) {
            if (i != 2 && i != 5 && !Character.isDigit(fecha.charAt(i))) {
                return false;
            }
        }
        int dia = Integer.parseInt(fecha.substring(0, 2));
        int mes = Integer.parseInt(fecha.substring(3, 5));
        int anio = Integer.parseInt(fecha.substring(6, 10));
        if (dia < 1 || dia > 31 || mes < 1 || mes > 12 || anio < 2000) {
            return false;
        }
        return true;
    }

    public ArrayList<Animal> getListaAnimales() {
        return listaAnimales;
    }

    public Animal getAnimalActivo() {
        return animalActivo;
    }

    public void setAnimalActivo(Animal animalActivo) {
        this.animalActivo = animalActivo;
    }
}
