/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.portaltrading.jpa;

import br.com.portaltrading.entidades.ValorContainer;
import br.com.portaltrading.jpa.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.com.portaltrading.entidades.Container;

/**
 *
 * @author Willian
 */
public class ValorContainerJpaController implements Serializable {

    public ValorContainerJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ValorContainer valorContainer) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Container container = valorContainer.getContainer();
            if (container != null) {
                container = em.getReference(container.getClass(), container.getIdContainer());
                valorContainer.setContainer(container);
            }
            em.persist(valorContainer);
            if (container != null) {
                container.getValoresContainer().add(valorContainer);
                container = em.merge(container);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ValorContainer valorContainer) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ValorContainer persistentValorContainer = em.find(ValorContainer.class, valorContainer.getIdValorContainer());
            Container containerOld = persistentValorContainer.getContainer();
            Container containerNew = valorContainer.getContainer();
            if (containerNew != null) {
                containerNew = em.getReference(containerNew.getClass(), containerNew.getIdContainer());
                valorContainer.setContainer(containerNew);
            }
            valorContainer = em.merge(valorContainer);
            if (containerOld != null && !containerOld.equals(containerNew)) {
                containerOld.getValoresContainer().remove(valorContainer);
                containerOld = em.merge(containerOld);
            }
            if (containerNew != null && !containerNew.equals(containerOld)) {
                containerNew.getValoresContainer().add(valorContainer);
                containerNew = em.merge(containerNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = valorContainer.getIdValorContainer();
                if (findValorContainer(id) == null) {
                    throw new NonexistentEntityException("The valorContainer with id " + id + " no longer exists.");
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
            ValorContainer valorContainer;
            try {
                valorContainer = em.getReference(ValorContainer.class, id);
                valorContainer.getIdValorContainer();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The valorContainer with id " + id + " no longer exists.", enfe);
            }
            Container container = valorContainer.getContainer();
            if (container != null) {
                container.getValoresContainer().remove(valorContainer);
                container = em.merge(container);
            }
            em.remove(valorContainer);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ValorContainer> findValorContainerEntities() {
        return findValorContainerEntities(true, -1, -1);
    }

    public List<ValorContainer> findValorContainerEntities(int maxResults, int firstResult) {
        return findValorContainerEntities(false, maxResults, firstResult);
    }

    private List<ValorContainer> findValorContainerEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ValorContainer.class));
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

    public ValorContainer findValorContainer(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ValorContainer.class, id);
        } finally {
            em.close();
        }
    }

    public int getValorContainerCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ValorContainer> rt = cq.from(ValorContainer.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
