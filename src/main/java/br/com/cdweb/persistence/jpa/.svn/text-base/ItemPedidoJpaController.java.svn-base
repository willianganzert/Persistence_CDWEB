/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.portaltrading.jpa;

import br.com.portaltrading.entidades.ItemPedido;
import br.com.portaltrading.jpa.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.com.portaltrading.entidades.Pedido;
import br.com.portaltrading.entidades.Manufaturado;

/**
 *
 * @author Willian
 */
public class ItemPedidoJpaController implements Serializable {

    public ItemPedidoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ItemPedido itemPedido) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pedido pedido = itemPedido.getPedido();
            if (pedido != null) {
                pedido = em.getReference(pedido.getClass(), pedido.getIdPedido());
                itemPedido.setPedido(pedido);
            }
            Manufaturado manufaturado = itemPedido.getManufaturado();
            if (manufaturado != null) {
                manufaturado = em.getReference(manufaturado.getClass(), manufaturado.getIdManufaturado());
                itemPedido.setManufaturado(manufaturado);
            }
            em.persist(itemPedido);
            if (pedido != null) {
                pedido.getItensPedido().add(itemPedido);
                pedido = em.merge(pedido);
            }
            if (manufaturado != null) {
                manufaturado.getItemPedidoList().add(itemPedido);
                manufaturado = em.merge(manufaturado);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ItemPedido itemPedido) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ItemPedido persistentItemPedido = em.find(ItemPedido.class, itemPedido.getIdItemPedido());
            Pedido pedidoOld = persistentItemPedido.getPedido();
            Pedido pedidoNew = itemPedido.getPedido();
            Manufaturado manufaturadoOld = persistentItemPedido.getManufaturado();
            Manufaturado manufaturadoNew = itemPedido.getManufaturado();
            if (pedidoNew != null) {
                pedidoNew = em.getReference(pedidoNew.getClass(), pedidoNew.getIdPedido());
                itemPedido.setPedido(pedidoNew);
            }
            if (manufaturadoNew != null) {
                manufaturadoNew = em.getReference(manufaturadoNew.getClass(), manufaturadoNew.getIdManufaturado());
                itemPedido.setManufaturado(manufaturadoNew);
            }
            itemPedido = em.merge(itemPedido);
            if (pedidoOld != null && !pedidoOld.equals(pedidoNew)) {
                pedidoOld.getItensPedido().remove(itemPedido);
                pedidoOld = em.merge(pedidoOld);
            }
            if (pedidoNew != null && !pedidoNew.equals(pedidoOld)) {
                pedidoNew.getItensPedido().add(itemPedido);
                pedidoNew = em.merge(pedidoNew);
            }
            if (manufaturadoOld != null && !manufaturadoOld.equals(manufaturadoNew)) {
                manufaturadoOld.getItemPedidoList().remove(itemPedido);
                manufaturadoOld = em.merge(manufaturadoOld);
            }
            if (manufaturadoNew != null && !manufaturadoNew.equals(manufaturadoOld)) {
                manufaturadoNew.getItemPedidoList().add(itemPedido);
                manufaturadoNew = em.merge(manufaturadoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = itemPedido.getIdItemPedido();
                if (findItemPedido(id) == null) {
                    throw new NonexistentEntityException("The itemPedido with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ItemPedido itemPedido;
            try {
                itemPedido = em.getReference(ItemPedido.class, id);
                itemPedido.getIdItemPedido();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The itemPedido with id " + id + " no longer exists.", enfe);
            }
            Pedido pedido = itemPedido.getPedido();
            if (pedido != null) {
                pedido.getItensPedido().remove(itemPedido);
                pedido = em.merge(pedido);
            }
            Manufaturado manufaturado = itemPedido.getManufaturado();
            if (manufaturado != null) {
                manufaturado.getItemPedidoList().remove(itemPedido);
                manufaturado = em.merge(manufaturado);
            }
            em.remove(itemPedido);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ItemPedido> findItemPedidoEntities() {
        return findItemPedidoEntities(true, -1, -1);
    }

    public List<ItemPedido> findItemPedidoEntities(int maxResults, int firstResult) {
        return findItemPedidoEntities(false, maxResults, firstResult);
    }

    private List<ItemPedido> findItemPedidoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ItemPedido.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public ItemPedido findItemPedido(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ItemPedido.class, id);
        } finally {
            em.close();
        }
    }

    public int getItemPedidoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ItemPedido> rt = cq.from(ItemPedido.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
