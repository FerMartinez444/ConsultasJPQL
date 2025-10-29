package managers;

import funciones.FuncionApp;
import org.example.Cliente;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class ClienteManager {

    private EntityManager em;

    public ClienteManager(EntityManager em) {
        this.em = em;
    }

    public List<Cliente> getClientesXIds(List<Long> idsClientes){
        String jpql = "FROM Cliente WHERE id IN (:idsClientes) ORDER BY razonSocial ASC";
        Query query = em.createQuery(jpql);
        query.setParameter("idsClientes", idsClientes);
        List<Cliente> clientes = query.getResultList();
        return clientes;
    }

    public List<Cliente> getClientesXRazonSocialParcialmente(String razonSocial){
        StringBuilder jpql = new StringBuilder("SELECT DISTINCT(cliente) FROM Cliente cliente WHERE 1=1 ");
        if(!FuncionApp.isEmpty(razonSocial))
            jpql.append(this.parseSearchField("cliente.razonSocial", razonSocial));
        jpql.append(" ORDER BY cliente.razonSocial ASC");
        Query query = em.createQuery(jpql.toString());
        List<Cliente> clientes = query.getResultList();
        return clientes;
    }

    public String parseSearchField(String field, String value) {
        String[] fields = new String[1];
        fields[0] = field;
        return this.parseSearchField(fields, value);
    }

    public String parseSearchField(String field[], String value) {
        if(value != null) {
            String[] values = value.split(" ");
            StringBuffer result = new StringBuffer();
            for(int i = 0; i < values.length; i++) {
                StringBuffer resultFields = new StringBuffer();
                if(!values[i].trim().equals("")){
                    result.append(" AND ");
                    for (int j = 0; j < field.length ; j++){
                        if (j!=0)
                            resultFields.append(" OR ");
                        resultFields.append(" (LOWER(" + field[j].trim() + ") LIKE '" + values[i].trim().toLowerCase() + "%' OR LOWER(" + field[j].trim() + ") LIKE '%" + values[i].trim().toLowerCase() + "%')");
                    }
                    result.append("(" + resultFields.toString() + ")");
                }
            }
            return result.toString();
        }
        return "";
    }

    public List<Cliente> getClientes() {
        String jpql = "SELECT c FROM Cliente c";
        Query query = em.createQuery(jpql);
        return query.getResultList();
    }

    public Cliente getClienteConMasFacturas() {
        String jpql = "SELECT f.cliente FROM Factura f GROUP BY f.cliente ORDER BY COUNT(f) DESC";
        Query query = em.createQuery(jpql);
        query.setMaxResults(1);
        return (Cliente) query.getSingleResult();
    }

    public List<Cliente> getClientesConFacturas() {
        String jpql = "SELECT c FROM Cliente c WHERE EXISTS (SELECT f FROM Factura f WHERE f.cliente = c)";
        Query query = em.createQuery(jpql);
        return query.getResultList();
    }
}