package negocio;

import modelo.Insumo;

public class MainPrueba {
    public static void main(String[] args) {
        GestorInventario gestor = new GestorInventario();

        // 1. Creamos dos insumos de prueba
        Insumo insumo1 = new Insumo("INS-01", "Croquetas Perro", "Alimento", "Juan Pérez", 10, "2026-12-30", "");
        Insumo insumo2 = new Insumo("INS-02", "Antibiótico", "Medicina", "Ana Gómez", 2, "2026-11-18", ""); // Stock bajo (< 5)

        // 2. Los registramos usando tu gestor
        System.out.println(gestor.registrarInsumo(insumo1));
        System.out.println(gestor.registrarInsumo(insumo2));

        // 3. Verificamos el reporte de stock
        System.out.println("\n" + gestor.verificarAlertasStock());

        System.out.println("\n=== LISTA DE TODOS LOS INSUMOS ===");
        for (int i = 0; i < gestor.getListaInsumos().size(); i++) {
            System.out.println(gestor.getListaInsumos().get(i));
        }
    }
}
