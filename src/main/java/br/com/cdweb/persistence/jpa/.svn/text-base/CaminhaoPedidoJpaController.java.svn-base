/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.portaltrading.jpa;

import br.com.portaltrading.entidades.CaminhaoPedido;
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
import br.com.portaltrading.entidades.Caminhao;

/**
 *
 * @author Willian
 */
public class CaminhaoPedidoJpaController implements Serializable {

    public CaminhaoPedidoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CaminhaoPedido caminhaoPedido) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pedido pedido = caminhaoPedido.getPedido();
            if (pedido != null) {
                pedido = em.getReference(pedido.getClass(), pedido.getIdPedido());
                caminhaoPedido.setPedido(pedido);
            }
            Caminhao caminhao = caminhaoPedido.getCaminhao();
            if (caminhao != null) {
                caminhao = em.getReference(caminhao.getClass(), caminhao.getIdCaminhao());
                caminhaoPedido.setCaminhao(caminhao);
            }
            em.persist(caminhaoPedido);
            if (pedido != null) {
                pedido.getCaminhoesPedido().add(caminhaoPedido);
                pedido = em.merge(pedido);
            }
            if (caminhao != null) {
                caminhao.getCaminhaoPedidoList().add(caminhaoPedido);
                caminhao = em.merge(caminhao);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CaminhaoPedido caminhaoPedido) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CaminhaoPedido persistentCaminhaoPedido = em.find(CaminhaoPedido.class, caminhaoPedido.getIdCaminhaoPedido());
            Pedido pedidoOld = persistentCaminhaoPedido.getPedido();
            Pedido pedidoNew = caminhaoPedido.getPedido();
            Caminhao caminhaoOld = persistentCaminhaoPedido.getCaminhao();
            Caminhao caminhaoNew = caminhaoPedido.getCaminhao();
            if (pedidoNew != null) {
                pedidoNew = em.getReference(pedidoNew.getClass(), pedidoNew.getIdPedido());
                caminhaoPedido.setPedido(pedidoNew);
            }
            if (caminhaoNew != null) {
                caminhaoNew = em.getReference(caminhaoNew.getClass(), caminhaoNew.getIdCaminhao());
                caminhaoPedido.setCaminhao(caminhaoNew);
            }
            caminhaoPedido = em.merge(caminhaoPedido);
            if (pedidoOld != null && !pedidoOld.equals(pedidoNew)) {
                pedidoOld.getCaminhoesPedido().remove(caminhaoPedido);
                pedidoOld = em.merge(pedidoOld);
            }
            if (pedidoNew != null && !pedidoNew.equals(pedidoOld)) {
                pedidoNew.getCaminhoesPedido().add(caminhaoPedido);
                pedidoNew = em.merge(pedidoNew);
            }
            if (caminhaoOld != null && !caminhaoOld.equals(caminhaoNew)) {
                caminhaoOld.getCaminhaoPedidoList().remove(caminhaoPedido);
                caminhaoOld = em.merge(caminhaoOld);
            }
            if (caminhaoNew != null && !caminhaoNew.equals(caminhaoOld)) {
                caminhaoNew.getCaminhaoPedidoList().add(caminhaoPedido);
                caminhaoNew = em.merge(caminhaoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = caminhaoPedido.getIdCaminhaoPedido();
                if (findCaminhaoPedido(id) == null) {
                    throw new NonexistentEntityException("The caminhaoPedido with id " + id + " no longer exists.");
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
            CaminhaoPedido caminhaoPedido;
            try {
                caminhaoPedido = em.getReference(CaminhaoPedido.class, id);
                caminhaoPedido.getIdCaminhaoPedido();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The caminhaoPedido with id " + id + " no longer exists.", enfe);
            }
            Pedido pedido = caminhaoPedido.getPedido();
            if (pedido != null) {
                pedido.getCaminhoesPedido().remove(caminhaoPedido);
                pedido = em.merge(pedido);
            }
            Caminhao caminhao = caminhaoPedido.getCaminhao();
            if (caminhao != null) {
                caminhao.getCaminhaoPedidoList().remove(caminhaoPedido);
                caminhao = em.merge(caminhao);
            }
            em.remove(caminhaoPedido);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CaminhaoPedido> findCaminhaoPedidoEntities() {
        return findCaminhaoPedidoEntities(true, -1, -1);
    }

    public List<CaminhaoPedido> findCaminhaoPedidoEntities(int maxResults, int firstResult) {
        return findCaminhaoPedidoEntities(false, maxResults, firstResult);
    }

    private List<CaminhaoPedido> findCaminhaoPedidoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CaminhaoPedido.class));
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

    public CaminhaoPedido findCaminhaoPedido(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CaminhaoPedido.class, id);
        } finally {
            em.close();
        }
    }

    public int getCaminhaoPedidoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CaminhaoPedido> rt = cq.from(CaminhaoPedido.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
