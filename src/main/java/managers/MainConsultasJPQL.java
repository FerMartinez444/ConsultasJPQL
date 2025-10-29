package managers;

import org.example.Main;
import org.example.Articulo;
import org.example.Cliente;
import org.example.Factura;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class MainConsultasJPQL {

    public static void main(String[] args) {
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            emf = Persistence.createEntityManagerFactory("example-unit");
            em = emf.createEntityManager();

            Main.poblarDB(em);

            ClienteManager clienteManager = new ClienteManager(em);
            FacturaManager facturaManager = new FacturaManager(em);
            ArticuloManager articuloManager = new ArticuloManager(em);

            System.out.println("\n--- Ejecutando consultas del Trabajo Práctico ---");

            ejercicio1(clienteManager);
            ejercicio2(facturaManager);
            ejercicio3(clienteManager);
            ejercicio4(articuloManager);
            ejercicio5(facturaManager, clienteManager);
            ejercicio6(facturaManager, clienteManager);
            ejercicio7(articuloManager, facturaManager);
            ejercicio8(articuloManager, facturaManager);
            ejercicio9(facturaManager);
            ejercicio10(facturaManager);
            ejercicio11(facturaManager);
            ejercicio12(articuloManager);
            ejercicio13(articuloManager);
            ejercicio14(clienteManager);

        } catch (Exception e) {
            System.err.println("Ha ocurrido un error principal: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
            if (emf != null) emf.close();
            System.out.println("\n--- Ejecución finalizada. ---");
        }
    }

    public static void ejercicio1(ClienteManager mCliente) {
        System.out.println("\n--- Ejercicio 1: Listar todos los clientes ---");
        List<Cliente> clientes = mCliente.getClientes();
        for (Cliente cli : clientes) {
            System.out.println("ID: " + cli.getId() + ", Razón Social: " + cli.getRazonSocial());
        }
    }

    public static void ejercicio2(FacturaManager mFactura) {
        System.out.println("\n--- Ejercicio 2: Listar facturas del último mes ---");
        List<Factura> facturas = mFactura.getFacturasUltimoMes();
        if (facturas.isEmpty()) System.out.println("No se encontraron facturas.");
        else facturas.forEach(f -> System.out.println("Factura N°: " + f.getNroComprobante()));
    }

    public static void ejercicio3(ClienteManager mCliente) {
        System.out.println("\n--- Ejercicio 3: Cliente con más facturas ---");
        try {
            Cliente cliente = mCliente.getClienteConMasFacturas();
            System.out.println("El cliente con más facturas es: " + cliente.getRazonSocial());
        } catch (Exception e) {
            System.out.println("No se encontraron datos para la consulta.");
        }
    }

    public static void ejercicio4(ArticuloManager mArticulo) {
        System.out.println("\n--- Ejercicio 4: Artículos más vendidos ---");
        List<Object[]> resultados = mArticulo.getArticulosMasVendidos();
        for (Object[] resultado : resultados) {
            Articulo art = (Articulo) resultado[0];
            System.out.println("Artículo: " + art.getDenominacion() + ", Cantidad Vendida: " + resultado[1]);
        }
    }

    public static void ejercicio5(FacturaManager mFactura, ClienteManager mCliente) {
        System.out.println("\n--- Ejercicio 5: Facturas de un cliente en los últimos 3 meses ---");
        Cliente cliente = mCliente.getClientes().get(0);
        System.out.println("Buscando facturas para el cliente: " + cliente.getRazonSocial());
        List<Factura> facturas = mFactura.getFacturasUltimosTresMesesPorCliente(cliente.getId());
        if (facturas.isEmpty()) System.out.println("No se encontraron facturas.");
        else facturas.forEach(f -> System.out.println("Factura N°: " + f.getNroComprobante()));
    }

    public static void ejercicio6(FacturaManager mFactura, ClienteManager mCliente) {
        System.out.println("\n--- Ejercicio 6: Total facturado por un cliente ---");
        Cliente cliente = mCliente.getClientes().get(0);
        System.out.println("Calculando total para el cliente: " + cliente.getRazonSocial());
        Double total = mFactura.getTotalFacturadoPorCliente(cliente.getId());
        System.out.println("El total facturado es: $" + (total != null ? String.format("%.2f", total) : "0.00"));
    }

    public static void ejercicio7(ArticuloManager mArticulo, FacturaManager mFactura) {
        System.out.println("\n--- Ejercicio 7: Artículos de una factura ---");
        Factura factura = mFactura.getFacturas().get(0);
        System.out.println("Buscando artículos para la factura N°: " + factura.getNroComprobante());
        List<Articulo> articulos = mArticulo.getArticulosPorFactura(factura.getId());
        for (Articulo art : articulos) {
            System.out.println("- " + art.getDenominacion());
        }
    }

    public static void ejercicio8(ArticuloManager mArticulo, FacturaManager mFactura) {
        System.out.println("\n--- Ejercicio 8: Artículo más caro de una factura ---");
        Factura factura = mFactura.getFacturas().get(0);
        System.out.println("Buscando en factura N°: " + factura.getNroComprobante());
        try {
            Articulo articulo = mArticulo.getArticuloMasCaroPorFactura(factura.getId());
            System.out.println("El artículo más caro es: " + articulo.getDenominacion() + " con un precio de $" + articulo.getPrecioVenta());
        } catch (Exception e) {
            System.out.println("No se encontraron datos para la consulta.");
        }
    }

    public static void ejercicio9(FacturaManager mFactura) {
        System.out.println("\n--- Ejercicio 9: Contar total de facturas ---");
        Long total = mFactura.contarTotalFacturas();
        System.out.println("Cantidad total de facturas en el sistema: " + total);
    }

    public static void ejercicio10(FacturaManager mFactura) {
        System.out.println("\n--- Ejercicio 10: Facturas con total mayor a $20 ---");
        List<Factura> facturas = mFactura.getFacturasConTotalMayorA(20.0);
        if (facturas.isEmpty()) System.out.println("No se encontraron facturas.");
        else facturas.forEach(f -> System.out.println("Factura N°: " + f.getNroComprobante() + ", Total: " + f.getTotal()));
    }

    public static void ejercicio11(FacturaManager mFactura) {
        System.out.println("\n--- Ejercicio 11: Facturas que contienen 'Pera' ---");
        List<Factura> facturas = mFactura.getFacturasPorNombreArticulo("Pera");
        if (facturas.isEmpty()) System.out.println("No se encontraron facturas.");
        else facturas.forEach(f -> System.out.println("Factura N°: " + f.getNroComprobante()));
    }

    public static void ejercicio12(ArticuloManager mArticulo) {
        System.out.println("\n--- Ejercicio 12: Artículos por código parcial ('-P') ---");
        List<Articulo> articulos = mArticulo.getArticulosPorCodigoParcial("-P");
        for(Articulo art : articulos) {
            System.out.println("- " + art.getDenominacion() + " (Código: " + art.getCodigo() + ")");
        }
    }

    public static void ejercicio13(ArticuloManager mArticulo) {
        System.out.println("\n--- Ejercicio 13: Artículos con precio mayor al promedio ---");
        List<Articulo> articulos = mArticulo.getArticulosConPrecioMayorAlPromedio();
        for(Articulo art : articulos) {
            System.out.println("- " + art.getDenominacion() + " ($" + String.format("%.2f", art.getPrecioVenta()) + ")");
        }
    }

    public static void ejercicio14(ClienteManager mCliente) {
        System.out.println("\n--- Ejercicio 14: Clientes que han realizado compras (EXISTS) ---");
        List<Cliente> clientes = mCliente.getClientesConFacturas();
        for(Cliente cli : clientes) {
            System.out.println("- " + cli.getRazonSocial());
        }
    }
}