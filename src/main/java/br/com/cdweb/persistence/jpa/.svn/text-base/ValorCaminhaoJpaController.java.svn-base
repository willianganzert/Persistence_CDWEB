/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.portaltrading.jpa;

import br.com.portaltrading.entidades.ValorCaminhao;
import br.com.portaltrading.jpa.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.com.portaltrading.entidades.Caminhao;

/**
 *
 * @author Willian
 */
public class ValorCaminhaoJpaController implements Serializable {

    public ValorCaminhaoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ValorCaminhao valorCaminhao) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Caminhao caminhao = valorCaminhao.getCaminhao();
            if (caminhao != null) {
                caminhao = em.getReference(caminhao.getClass(), caminhao.getIdCaminhao());
                valorCaminhao.setCaminhao(caminhao);
            }
            em.persist(valorCaminhao);
            if (caminhao != null) {
                caminhao.getValoresCaminhao().add(valorCaminhao);
                caminhao = em.merge(caminhao);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ValorCaminhao valorCaminhao) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ValorCaminhao persistentValorCaminhao = em.find(ValorCaminhao.class, valorCaminhao.getIdValorCaminhao());
            Caminhao caminhaoOld = persistentValorCaminhao.getCaminhao();
            Caminhao caminhaoNew = valorCaminhao.getCaminhao();
            if (caminhaoNew != null) {
                caminhaoNew = em.getReference(caminhaoNew.getClass(), caminhaoNew.getIdCaminhao());
                valorCaminhao.setCaminhao(caminhaoNew);
            }
            valorCaminhao = em.merge(valorCaminhao);
            if (caminhaoOld != null && !caminhaoOld.equals(caminhaoNew)) {
                caminhaoOld.getValoresCaminhao().remove(valorCaminhao);
                caminhaoOld = em.merge(caminhaoOld);
            }
            if (caminhaoNew != null && !caminhaoNew.equals(caminhaoOld)) {
                caminhaoNew.getValoresCaminhao().add(valorCaminhao);
                caminhaoNew = em.merge(caminhaoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = valorCaminhao.getIdValorCaminhao();
                if (findValorCaminhao(id) == null) {
                    throw new NonexistentEntityException("The valorCaminhao with id " + id + " no longer exists.");
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
            ValorCaminhao valorCaminhao;
            try {
                valorCaminhao = em.getReference(ValorCaminhao.class, id);
                valorCaminhao.getIdValorCaminhao();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The valorCaminhao with id " + id + " no longer exists.", enfe);
            }
            Caminhao caminhao = valorCaminhao.getCaminhao();
            if (caminhao != null) {
                caminhao.getValoresCaminhao().remove(valorCaminhao);
                caminhao = em.merge(caminhao);
            }
            em.remove(valorCaminhao);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ValorCaminhao> findValorCaminhaoEntities() {
        return findValorCaminhaoEntities(true, -1, -1);
    }

    public List<ValorCaminhao> findValorCaminhaoEntities(int maxResults, int firstResult) {
        return findValorCaminhaoEntities(false, maxResults, firstResult);
    }

    private List<ValorCaminhao> findValorCaminhaoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ValorCaminhao.class));
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

    public ValorCaminhao findValorCaminhao(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ValorCaminhao.class, id);
        } finally {
            em.close();
        }
    }

    public int getValorCaminhaoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ValorCaminhao> rt = cq.from(ValorCaminhao.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
