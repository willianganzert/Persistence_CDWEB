/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.portaltrading.jpa;

import br.com.portaltrading.entidades.ProdutoValidado;
import br.com.portaltrading.jpa.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.com.portaltrading.entidades.ProdMan;
import br.com.portaltrading.entidades.Pedido;

/**
 *
 * @author Willian
 */
public class ProdutoValidadoJpaController implements Serializable {

    public ProdutoValidadoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ProdutoValidado produtoValidado) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ProdMan prodMan = produtoValidado.getProdMan();
            if (prodMan != null) {
                prodMan = em.getReference(prodMan.getClass(), prodMan.getIdProdMan());
                produtoValidado.setProdMan(prodMan);
            }
            Pedido pedido = produtoValidado.getPedido();
            if (pedido != null) {
                pedido = em.getReference(pedido.getClass(), pedido.getIdPedido());
                produtoValidado.setPedido(pedido);
            }
            em.persist(produtoValidado);
            if (prodMan != null) {
                prodMan.getProdutosValidados().add(produtoValidado);
                prodMan = em.merge(prodMan);
            }
            if (pedido != null) {
                pedido.getProdutosValidados().add(produtoValidado);
                pedido = em.merge(pedido);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ProdutoValidado produtoValidado) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ProdutoValidado persistentProdutoValidado = em.find(ProdutoValidado.class, produtoValidado.getIdProdutoValidado());
            ProdMan prodManOld = persistentProdutoValidado.getProdMan();
            ProdMan prodManNew = produtoValidado.getProdMan();
            Pedido pedidoOld = persistentProdutoValidado.getPedido();
            Pedido pedidoNew = produtoValidado.getPedido();
            if (prodManNew != null) {
                prodManNew = em.getReference(prodManNew.getClass(), prodManNew.getIdProdMan());
                produtoValidado.setProdMan(prodManNew);
            }
            if (pedidoNew != null) {
                pedidoNew = em.getReference(pedidoNew.getClass(), pedidoNew.getIdPedido());
                produtoValidado.setPedido(pedidoNew);
            }
            produtoValidado = em.merge(produtoValidado);
            if (prodManOld != null && !prodManOld.equals(prodManNew)) {
                prodManOld.getProdutosValidados().remove(produtoValidado);
                prodManOld = em.merge(prodManOld);
            }
            if (prodManNew != null && !prodManNew.equals(prodManOld)) {
                prodManNew.getProdutosValidados().add(produtoValidado);
                prodManNew = em.merge(prodManNew);
            }
            if (pedidoOld != null && !pedidoOld.equals(pedidoNew)) {
                pedidoOld.getProdutosValidados().remove(produtoValidado);
                pedidoOld = em.merge(pedidoOld);
            }
            if (pedidoNew != null && !pedidoNew.equals(pedidoOld)) {
                pedidoNew.getProdutosValidados().add(produtoValidado);
                pedidoNew = em.merge(pedidoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = produtoValidado.getIdProdutoValidado();
                if (findProdutoValidado(id) == null) {
                    throw new NonexistentEntityException("The produtoValidado with id " + id + " no longer exists.");
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
            ProdutoValidado produtoValidado;
            try {
                produtoValidado = em.getReference(ProdutoValidado.class, id);
                produtoValidado.getIdProdutoValidado();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The produtoValidado with id " + id + " no longer exists.", enfe);
            }
            ProdMan prodMan = produtoValidado.getProdMan();
            if (prodMan != null) {
                prodMan.getProdutosValidados().remove(produtoValidado);
                prodMan = em.merge(prodMan);
            }
            Pedido pedido = produtoValidado.getPedido();
            if (pedido != null) {
                pedido.getProdutosValidados().remove(produtoValidado);
                pedido = em.merge(pedido);
            }
            em.remove(produtoValidado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ProdutoValidado> findProdutoValidadoEntities() {
        return findProdutoValidadoEntities(true, -1, -1);
    }

    public List<ProdutoValidado> findProdutoValidadoEntities(int maxResults, int firstResult) {
        return findProdutoValidadoEntities(false, maxResults, firstResult);
    }

    private List<ProdutoValidado> findProdutoValidadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ProdutoValidado.class));
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

    public ProdutoValidado findProdutoValidado(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ProdutoValidado.class, id);
        } finally {
            em.close();
        }
    }

    public int getProdutoValidadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ProdutoValidado> rt = cq.from(ProdutoValidado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
