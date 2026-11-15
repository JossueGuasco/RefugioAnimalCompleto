package negocio;

import java.time.LocalDate;
import java.util.ArrayList;
import modelo.Insumo;

public class GestorInventario {
    private ArrayList<Insumo> listaInsumos;
    private Insumo insumoActivo;
    private int stockMinimo;

    public GestorInventario() {
        this.listaInsumos = new ArrayList<>();
        this.insumoActivo = null;
        this.stockMinimo = 5;
    }

    public Insumo getInsumoActivo() {
        return insumoActivo;
    }

    public void setInsumoActivo(Insumo insumoActivo) {
        this.insumoActivo = insumoActivo;
    }

    public ArrayList<Insumo> getListaInsumos() {
        return listaInsumos;
    }

    public String registrarInsumo(Insumo nuevoInsumo) {
        if (nuevoInsumo == null ||
                nuevoInsumo.getIdInsumo().equals("") ||
                nuevoInsumo.getNombre().equals("") ||
                nuevoInsumo.getDonante().equals("")) {
            return "Error: Todos los campos obligatorios deben estar completos.";
        }

        if (nuevoInsumo.getCantidad() <= 0) {
            return "Error: La cantidad debe ser mayor a cero.";
        }

        if (buscarInsumoPorId(nuevoInsumo.getIdInsumo()) != null) {
            return "Error: Ya existe un insumo registrado con el ID " + nuevoInsumo.getIdInsumo();
        }

        LocalDate fechaActual = LocalDate.now();
        int dia = fechaActual.getDayOfMonth();
        int mes = fechaActual.getMonthValue();
        int anio = fechaActual.getYear();


        String fechaRegistroFormateada = dia + "/" + mes + "/" + anio;
        nuevoInsumo.setFechaRegistro(fechaRegistroFormateada);

        listaInsumos.add(nuevoInsumo);
        this.insumoActivo = nuevoInsumo;
        return "Insumo registrado exitosamente el " + fechaRegistroFormateada;
    }

    public Insumo buscarInsumoPorId(String idInsumo) {
        for (int i = 0; i < listaInsumos.size(); i++) {
            Insumo insumo = listaInsumos.get(i);
            if (insumo.getIdInsumo().equalsIgnoreCase(idInsumo)) {
                return insumo;
            }
        }
        return null;
    }


    public String verificarAlertasStock() {
        if (listaInsumos.isEmpty()) {
            return "No hay insumos registrados en el inventario.";
        }

        String reporte = " Reporte de STOCK y ALERTAS\n";
        boolean hayAlertas = false;

        for (int i = 0; i < listaInsumos.size(); i++) {
            Insumo insumo = listaInsumos.get(i);

            if (insumo.getCantidad() < stockMinimo) {
                reporte += "• [STOCK BAJO] " + insumo.getNombre() +
                        " tiene solo " + insumo.getCantidad() +
                        " unidades. Se requiere reabastecimiento.\n";
                hayAlertas = true;
            }
        }

        if (!hayAlertas) {
            reporte += "Todo el inventario cuenta con stock suficiente.";
        }

        return reporte;
    }
}