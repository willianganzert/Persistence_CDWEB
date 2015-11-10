/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.portaltrading.jpa;

import br.com.portaltrading.entidades.ValorDespachante;
import br.com.portaltrading.jpa.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.com.portaltrading.entidades.Despachante;

/**
 *
 * @author Willian
 */
public class ValorDespachanteJpaController implements Serializable {

    public ValorDespachanteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ValorDespachante valorDespachante) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Despachante despachante = valorDespachante.getDespachante();
            if (despachante != null) {
                despachante = em.getReference(despachante.getClass(), despachante.getIdDespachante());
                valorDespachante.setDespachante(despachante);
            }
            em.persist(valorDespachante);
            if (despachante != null) {
                despachante.getValoresDespachante().add(valorDespachante);
                despachante = em.merge(despachante);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ValorDespachante valorDespachante) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ValorDespachante persistentValorDespachante = em.find(ValorDespachante.class, valorDespachante.getIdValorDespachante());
            Despachante despachanteOld = persistentValorDespachante.getDespachante();
            Despachante despachanteNew = valorDespachante.getDespachante();
            if (despachanteNew != null) {
                despachanteNew = em.getReference(despachanteNew.getClass(), despachanteNew.getIdDespachante());
                valorDespachante.setDespachante(despachanteNew);
            }
            valorDespachante = em.merge(valorDespachante);
            if (despachanteOld != null && !despachanteOld.equals(despachanteNew)) {
                despachanteOld.getValoresDespachante().remove(valorDespachante);
                despachanteOld = em.merge(despachanteOld);
            }
            if (despachanteNew != null && !despachanteNew.equals(despachanteOld)) {
                despachanteNew.getValoresDespachante().add(valorDespachante);
                despachanteNew = em.merge(despachanteNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = valorDespachante.getIdValorDespachante();
                if (findValorDespachante(id) == null) {
                    throw new NonexistentEntityException("The valorDespachante with id " + id + " no longer exists.");
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
            ValorDespachante valorDespachante;
            try {
                valorDespachante = em.getReference(ValorDespachante.class, id);
                valorDespachante.getIdValorDespachante();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The valorDespachante with id " + id + " no longer exists.", enfe);
            }
            Despachante despachante = valorDespachante.getDespachante();
            if (despachante != null) {
                despachante.getValoresDespachante().remove(valorDespachante);
                despachante = em.merge(despachante);
            }
            em.remove(valorDespachante);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ValorDespachante> findValorDespachanteEntities() {
        return findValorDespachanteEntities(true, -1, -1);
    }

    public List<ValorDespachante> findValorDespachanteEntities(int maxResults, int firstResult) {
        return findValorDespachanteEntities(false, maxResults, firstResult);
    }

    private List<ValorDespachante> findValorDespachanteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ValorDespachante.class));
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

    public ValorDespachante findValorDespachante(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ValorDespachante.class, id);
        } finally {
            em.close();
        }
    }

    public int getValorDespachanteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ValorDespachante> rt = cq.from(ValorDespachante.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
