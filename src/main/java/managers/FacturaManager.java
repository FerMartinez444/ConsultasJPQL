package managers;

import org.example.Factura;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.List;

public class FacturaManager {

    private EntityManager em;

    public FacturaManager(EntityManager em) {
        this.em = em;
    }

    public List<Factura> getFacturas(){
        String jpql = "FROM Factura";
        Query query = em.createQuery(jpql);
        List<Factura> facturas = query.getResultList();
        return facturas;
    }

    public List<Factura> getFacturasActivas(){
        String jpql = "FROM Factura WHERE fechaBaja IS NULL ORDER BY fechaComprobante DESC";
        Query query = em.createQuery(jpql);
        List<Factura> facturas = query.getResultList();
        return facturas;
    }

    public List<Factura> getFacturasXNroComprobante(Long nroComprobante){
        String jpql = "FROM Factura WHERE nroComprobante = :nroComprobante";
        Query query = em.createQuery(jpql);
        query.setParameter("nroComprobante", nroComprobante);
        List<Factura> facturas = query.getResultList();
        return facturas;
    }

    public List<Factura> buscarFacturasXRangoFechas(LocalDate fechaInicio, LocalDate fechaFin){
        String jpql = "FROM Factura WHERE fechaComprobante >= :fechaInicio AND fechaComprobante <= :fechaFin";
        Query query = em.createQuery(jpql);
        query.setParameter("fechaInicio", fechaInicio);
        query.setParameter("fechaFin", fechaFin);
        List<Factura> facturas = query.getResultList();
        return facturas;
    }

    public Factura getFacturaXPtoVentaXNroComprobante(Integer puntoVenta, Long nroComprobante){
        String jpql = "FROM Factura WHERE puntoVenta = :puntoVenta AND nroComprobante = :nroComprobante";
        Query query = em.createQuery(jpql);
        query.setMaxResults(1);
        query.setParameter("puntoVenta", puntoVenta);
        query.setParameter("nroComprobante", nroComprobante);
        Factura factura = (Factura) query.getSingleResult();
        return factura;
    }

    public List<Factura> getFacturasXCliente(Long idCliente){
        String jpql = "FROM Factura WHERE cliente.id = :idCliente";
        Query query = em.createQuery(jpql);
        query.setParameter("idCliente", idCliente);
        List<Factura> facturas = query.getResultList();
        return facturas;
    }

    public List<Factura> getFacturasXCuitCliente(String cuitCliente){
        String jpql = "FROM Factura WHERE cliente.cuit = :cuitCliente";
        Query query = em.createQuery(jpql);
        query.setParameter("cuitCliente", cuitCliente);
        List<Factura> facturas = query.getResultList();
        return facturas;
    }

    public List<Factura> getFacturasXArticulo(Long idArticulo){
        StringBuilder jpql = new StringBuilder("SELECT fact FROM Factura AS fact LEFT OUTER JOIN fact.detallesFactura AS detalle");
        jpql.append(" WHERE detalle.id = :idArticulo");
        Query query = em.createQuery(jpql.toString());
        query.setParameter("idArticulo", idArticulo);
        List<Factura> facturas = query.getResultList();
        return facturas;
    }

    public Long getMaxNroComprobanteFactura(){
        StringBuilder jpql = new StringBuilder("SELECT MAX(nroComprobante) FROM Factura WHERE fechaBaja IS NULL");
        Query query = em.createQuery(jpql.toString());
        Long maxNroFactura = (Long) query.getSingleResult();
        return maxNroFactura;
    }

    public List<Factura> getFacturasUltimoMes() {
        LocalDate fechaFin = LocalDate.now();
        LocalDate fechaInicio = fechaFin.minusMonths(1);
        String jpql = "FROM Factura f WHERE f.fechaComprobante BETWEEN :fechaInicio AND :fechaFin";
        Query query = em.createQuery(jpql);
        query.setParameter("fechaInicio", fechaInicio);
        query.setParameter("fechaFin", fechaFin);
        return query.getResultList();
    }

    public List<Factura> getFacturasUltimosTresMesesPorCliente(Long idCliente) {
        LocalDate fechaFin = LocalDate.now();
        LocalDate fechaInicio = fechaFin.minusMonths(3);
        String jpql = "FROM Factura f WHERE f.cliente.id = :idCliente AND f.fechaComprobante BETWEEN :fechaInicio AND :fechaFin";
        Query query = em.createQuery(jpql);
        query.setParameter("idCliente", idCliente);
        query.setParameter("fechaInicio", fechaInicio);
        query.setParameter("fechaFin", fechaFin);
        return query.getResultList();
    }

    public Double getTotalFacturadoPorCliente(Long idCliente) {
        String jpql = "SELECT SUM(f.total) FROM Factura f WHERE f.cliente.id = :idCliente";
        Query query = em.createQuery(jpql);
        query.setParameter("idCliente", idCliente);
        return (Double) query.getSingleResult();
    }

    public Long contarTotalFacturas() {
        String jpql = "SELECT COUNT(f) FROM Factura f";
        Query query = em.createQuery(jpql);
        return (Long) query.getSingleResult();
    }

    public List<Factura> getFacturasConTotalMayorA(double monto) {
        String jpql = "FROM Factura f WHERE f.total > :monto";
        Query query = em.createQuery(jpql);
        query.setParameter("monto", monto);
        return query.getResultList();
    }

    public List<Factura> getFacturasPorNombreArticulo(String nombreArticulo) {
        String jpql = "SELECT DISTINCT f FROM Factura f JOIN f.detallesFactura fd WHERE fd.articulo.denominacion = :nombreArticulo";
        Query query = em.createQuery(jpql);
        query.setParameter("nombreArticulo", nombreArticulo);
        return query.getResultList();
    }
}