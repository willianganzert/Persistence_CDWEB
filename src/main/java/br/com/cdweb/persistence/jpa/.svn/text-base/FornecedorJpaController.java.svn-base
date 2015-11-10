/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.portaltrading.jpa;

import br.com.portaltrading.entidades.Fornecedor;
import br.com.portaltrading.jpa.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.com.portaltrading.entidades.Empresa;

/**
 *
 * @author Willian
 */
public class FornecedorJpaController implements Serializable {

    public FornecedorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Fornecedor fornecedor) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empresa empresa = fornecedor.getEmpresa();
            if (empresa != null) {
                empresa = em.getReference(empresa.getClass(), empresa.getIdEmpresa());
                fornecedor.setEmpresa(empresa);
            }
            em.persist(fornecedor);
            if (empresa != null) {
                empresa.getFornecedorList().add(fornecedor);
                empresa = em.merge(empresa);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Fornecedor fornecedor) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Fornecedor persistentFornecedor = em.find(Fornecedor.class, fornecedor.getIdFornecedor());
            Empresa empresaOld = persistentFornecedor.getEmpresa();
            Empresa empresaNew = fornecedor.getEmpresa();
            if (empresaNew != null) {
                empresaNew = em.getReference(empresaNew.getClass(), empresaNew.getIdEmpresa());
                fornecedor.setEmpresa(empresaNew);
            }
            fornecedor = em.merge(fornecedor);
            if (empresaOld != null && !empresaOld.equals(empresaNew)) {
                empresaOld.getFornecedorList().remove(fornecedor);
                empresaOld = em.merge(empresaOld);
            }
            if (empresaNew != null && !empresaNew.equals(empresaOld)) {
                empresaNew.getFornecedorList().add(fornecedor);
                empresaNew = em.merge(empresaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = fornecedor.getIdFornecedor();
                if (findFornecedor(id) == null) {
                    throw new NonexistentEntityException("The fornecedor with id " + id + " no longer exists.");
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
            Fornecedor fornecedor;
            try {
                fornecedor = em.getReference(Fornecedor.class, id);
                fornecedor.getIdFornecedor();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The fornecedor with id " + id + " no longer exists.", enfe);
            }
            Empresa empresa = fornecedor.getEmpresa();
            if (empresa != null) {
                empresa.getFornecedorList().remove(fornecedor);
                empresa = em.merge(empresa);
            }
            em.remove(fornecedor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Fornecedor> findFornecedorEntities() {
        return findFornecedorEntities(true, -1, -1);
    }

    public List<Fornecedor> findFornecedorEntities(int maxResults, int firstResult) {
        return findFornecedorEntities(false, maxResults, firstResult);
    }

    private List<Fornecedor> findFornecedorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Fornecedor.class));
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

    public Fornecedor findFornecedor(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Fornecedor.class, id);
        } finally {
            em.close();
        }
    }

    public int getFornecedorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Fornecedor> rt = cq.from(Fornecedor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
