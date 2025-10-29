package org.example;

import funciones.FuncionApp;
import javax.persistence.EntityManager;
import java.util.Date;

public class Main {
    public static void poblarDB(EntityManager em) {
        try {
            em.getTransaction().begin();

            UnidadMedida unidadMedida = UnidadMedida.builder().denominacion("Kilogramo").build();
            UnidadMedida unidadMedidapote = UnidadMedida.builder().denominacion("pote").build();
            em.persist(unidadMedida);
            em.persist(unidadMedidapote);

            Categoria categoria = Categoria.builder().denominacion("Frutas").esInsumo(true).build();
            Categoria categoriaPostre = Categoria.builder().denominacion("Postre").esInsumo(false).build();
            em.persist(categoria);
            em.persist(categoriaPostre);

            ArticuloInsumo articuloInsumo = ArticuloInsumo.builder()
                    .denominacion("Manzana").codigo(Long.toString(new Date().getTime()) + "-M")
                    .precioCompra(1.5).precioVenta(5d).stockActual(100).stockMaximo(200)
                    .esParaElaborar(true).unidadMedida(unidadMedida).build();
            em.persist(articuloInsumo);

            try { Thread.sleep(10); } catch (InterruptedException e) {}

            ArticuloInsumo articuloInsumoPera = ArticuloInsumo.builder()
                    .denominacion("Pera").codigo(Long.toString(new Date().getTime()) + "-P")
                    .precioCompra(2.5).precioVenta(10d).stockActual(130).stockMaximo(200)
                    .esParaElaborar(true).unidadMedida(unidadMedida).build();
            em.persist(articuloInsumoPera);

            categoria.getArticulos().add(articuloInsumo);
            categoria.getArticulos().add(articuloInsumoPera);

            ArticuloManufacturadoDetalle detalleManzana = ArticuloManufacturadoDetalle.builder().cantidad(2).articuloInsumo(articuloInsumo).build();
            ArticuloManufacturadoDetalle detallePera = ArticuloManufacturadoDetalle.builder().cantidad(2).articuloInsumo(articuloInsumoPera).build();

            ArticuloManufacturado articuloManufacturado = ArticuloManufacturado.builder()
                    .denominacion("Ensalada de frutas").descripcion("Ensalada de manzanas y peras")
                    .precioVenta(150d).tiempoEstimadoMinutos(10).preparacion("Cortar y mezclar")
                    .unidadMedida(unidadMedidapote).build();

            articuloManufacturado.getDetalles().add(detalleManzana);
            articuloManufacturado.getDetalles().add(detallePera);
            categoriaPostre.getArticulos().add(articuloManufacturado);
            em.persist(articuloManufacturado);

            Cliente cliente = Cliente.builder().cuit(FuncionApp.generateRandomCUIT()).razonSocial("Juan Perez").build();
            em.persist(cliente);

            FacturaDetalle detalle1 = new FacturaDetalle(3, articuloInsumo);
            detalle1.calcularSubTotal();
            FacturaDetalle detalle2 = new FacturaDetalle(3, articuloInsumoPera);
            detalle2.calcularSubTotal();

            Factura factura = Factura.builder()
                    .puntoVenta(2024).fechaAlta(new Date()).fechaComprobante(FuncionApp.generateRandomDate())
                    .cliente(cliente).nroComprobante(FuncionApp.generateRandomNumber()).build();

            factura.addDetalleFactura(detalle1);
            factura.addDetalleFactura(detalle2);
            factura.calcularTotal();
            em.persist(factura);

            em.getTransaction().commit();

        } catch (Exception ex) {
            if (em.getTransaction() != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al poblar la base de datos", ex);
        }
    }

    public static void main(String[] args) {
        System.out.println("Esta es una clase de utilidad. Ejecute 'MainConsultasJPQL' para ver los resultados.");
    }
}