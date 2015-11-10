/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.portaltrading.jpa;

import br.com.portaltrading.entidades.Permissao;
import br.com.portaltrading.jpa.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.com.portaltrading.entidades.Cargo;

/**
 *
 * @author Willian
 */
public class PermissaoJpaController implements Serializable {

    public PermissaoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Permissao permissao) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cargo cargo = permissao.getCargo();
            if (cargo != null) {
                cargo = em.getReference(cargo.getClass(), cargo.getIdCargo());
                permissao.setCargo(cargo);
            }
            em.persist(permissao);
            if (cargo != null) {
                cargo.getPermissoes().add(permissao);
                cargo = em.merge(cargo);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Permissao permissao) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Permissao persistentPermissao = em.find(Permissao.class, permissao.getIdPermissao());
            Cargo cargoOld = persistentPermissao.getCargo();
            Cargo cargoNew = permissao.getCargo();
            if (cargoNew != null) {
                cargoNew = em.getReference(cargoNew.getClass(), cargoNew.getIdCargo());
                permissao.setCargo(cargoNew);
            }
            permissao = em.merge(permissao);
            if (cargoOld != null && !cargoOld.equals(cargoNew)) {
                cargoOld.getPermissoes().remove(permissao);
                cargoOld = em.merge(cargoOld);
            }
            if (cargoNew != null && !cargoNew.equals(cargoOld)) {
                cargoNew.getPermissoes().add(permissao);
                cargoNew = em.merge(cargoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = permissao.getIdPermissao();
                if (findPermissao(id) == null) {
                    throw new NonexistentEntityException("The permissao with id " + id + " no longer exists.");
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
            Permissao permissao;
            try {
                permissao = em.getReference(Permissao.class, id);
                permissao.getIdPermissao();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The permissao with id " + id + " no longer exists.", enfe);
            }
            Cargo cargo = permissao.getCargo();
            if (cargo != null) {
                cargo.getPermissoes().remove(permissao);
                cargo = em.merge(cargo);
            }
            em.remove(permissao);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Permissao> findPermissaoEntities() {
        return findPermissaoEntities(true, -1, -1);
    }

    public List<Permissao> findPermissaoEntities(int maxResults, int firstResult) {
        return findPermissaoEntities(false, maxResults, firstResult);
    }

    private List<Permissao> findPermissaoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Permissao.class));
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

    public Permissao findPermissao(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Permissao.class, id);
        } finally {
            em.close();
        }
    }

    public int getPermissaoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Permissao> rt = cq.from(Permissao.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
