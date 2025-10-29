package managers;

import org.example.Articulo;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class ArticuloManager {
    private EntityManager em;

    public ArticuloManager(EntityManager em) {
        this.em = em;
    }

    public List<Object[]> getArticulosMasVendidos() {
        String jpql = "SELECT fd.articulo, SUM(fd.cantidad) as totalVendido FROM FacturaDetalle fd GROUP BY fd.articulo ORDER BY totalVendido DESC";
        Query query = em.createQuery(jpql);
        return query.getResultList();
    }

    public List<Articulo> getArticulosPorFactura(Long idFactura) {
        String jpql = "SELECT fd.articulo FROM FacturaDetalle fd WHERE fd.factura.id = :idFactura";
        Query query = em.createQuery(jpql);
        query.setParameter("idFactura", idFactura);
        return query.getResultList();
    }

    public Articulo getArticuloMasCaroPorFactura(Long idFactura) {
        String jpql = "SELECT fd.articulo FROM FacturaDetalle fd WHERE fd.factura.id = :idFactura ORDER BY fd.articulo.precioVenta DESC";
        Query query = em.createQuery(jpql);
        query.setMaxResults(1);
        return (Articulo) query.getSingleResult();
    }

    public List<Articulo> getArticulosPorCodigoParcial(String codigoParcial) {
        String jpql = "SELECT a FROM Articulo a WHERE a.codigo LIKE :codigo";
        Query query = em.createQuery(jpql);
        query.setParameter("codigo", "%" + codigoParcial + "%");
        return query.getResultList();
    }

    public List<Articulo> getArticulosConPrecioMayorAlPromedio() {
        String jpql = "SELECT a FROM Articulo a WHERE a.precioVenta > (SELECT AVG(a2.precioVenta) FROM Articulo a2)";
        Query query = em.createQuery(jpql);
        return query.getResultList();
    }
}