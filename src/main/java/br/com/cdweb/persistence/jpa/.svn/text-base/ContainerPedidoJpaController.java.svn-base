/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.portaltrading.jpa;

import br.com.portaltrading.entidades.ContainerPedido;
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
import br.com.portaltrading.entidades.Container;

/**
 *
 * @author Willian
 */
public class ContainerPedidoJpaController implements Serializable {

    public ContainerPedidoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ContainerPedido containerPedido) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pedido pedido = containerPedido.getPedido();
            if (pedido != null) {
                pedido = em.getReference(pedido.getClass(), pedido.getIdPedido());
                containerPedido.setPedido(pedido);
            }
            Container container = containerPedido.getContainer();
            if (container != null) {
                container = em.getReference(container.getClass(), container.getIdContainer());
                containerPedido.setContainer(container);
            }
            em.persist(containerPedido);
            if (pedido != null) {
                pedido.getContaineresPedido().add(containerPedido);
                pedido = em.merge(pedido);
            }
            if (container != null) {
                container.getContainerPedidoList().add(containerPedido);
                container = em.merge(container);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ContainerPedido containerPedido) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ContainerPedido persistentContainerPedido = em.find(ContainerPedido.class, containerPedido.getIdContainerPedido());
            Pedido pedidoOld = persistentContainerPedido.getPedido();
            Pedido pedidoNew = containerPedido.getPedido();
            Container containerOld = persistentContainerPedido.getContainer();
            Container containerNew = containerPedido.getContainer();
            if (pedidoNew != null) {
                pedidoNew = em.getReference(pedidoNew.getClass(), pedidoNew.getIdPedido());
                containerPedido.setPedido(pedidoNew);
            }
            if (containerNew != null) {
                containerNew = em.getReference(containerNew.getClass(), containerNew.getIdContainer());
                containerPedido.setContainer(containerNew);
            }
            containerPedido = em.merge(containerPedido);
            if (pedidoOld != null && !pedidoOld.equals(pedidoNew)) {
                pedidoOld.getContaineresPedido().remove(containerPedido);
                pedidoOld = em.merge(pedidoOld);
            }
            if (pedidoNew != null && !pedidoNew.equals(pedidoOld)) {
                pedidoNew.getContaineresPedido().add(containerPedido);
                pedidoNew = em.merge(pedidoNew);
            }
            if (containerOld != null && !containerOld.equals(containerNew)) {
                containerOld.getContainerPedidoList().remove(containerPedido);
                containerOld = em.merge(containerOld);
            }
            if (containerNew != null && !containerNew.equals(containerOld)) {
                containerNew.getContainerPedidoList().add(containerPedido);
                containerNew = em.merge(containerNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = containerPedido.getIdContainerPedido();
                if (findContainerPedido(id) == null) {
                    throw new NonexistentEntityException("The containerPedido with id " + id + " no longer exists.");
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
            ContainerPedido containerPedido;
            try {
                containerPedido = em.getReference(ContainerPedido.class, id);
                containerPedido.getIdContainerPedido();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The containerPedido with id " + id + " no longer exists.", enfe);
            }
            Pedido pedido = containerPedido.getPedido();
            if (pedido != null) {
                pedido.getContaineresPedido().remove(containerPedido);
                pedido = em.merge(pedido);
            }
            Container container = containerPedido.getContainer();
            if (container != null) {
                container.getContainerPedidoList().remove(containerPedido);
                container = em.merge(container);
            }
            em.remove(containerPedido);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ContainerPedido> findContainerPedidoEntities() {
        return findContainerPedidoEntities(true, -1, -1);
    }

    public List<ContainerPedido> findContainerPedidoEntities(int maxResults, int firstResult) {
        return findContainerPedidoEntities(false, maxResults, firstResult);
    }

    private List<ContainerPedido> findContainerPedidoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ContainerPedido.class));
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

    public ContainerPedido findContainerPedido(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ContainerPedido.class, id);
        } finally {
            em.close();
        }
    }

    public int getContainerPedidoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ContainerPedido> rt = cq.from(ContainerPedido.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
