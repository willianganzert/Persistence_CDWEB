/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.portaltrading.jpa;

import br.com.portaltrading.entidades.ProdMan;
import br.com.portaltrading.jpa.exceptions.IllegalOrphanException;
import br.com.portaltrading.jpa.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.com.portaltrading.entidades.Manufaturado;
import br.com.portaltrading.entidades.ProdutoValidado;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Willian
 */
public class ProdManJpaController implements Serializable {

    public ProdManJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ProdMan prodMan) {
        if (prodMan.getProdutosValidados() == null) {
            prodMan.setProdutosValidados(new ArrayList<ProdutoValidado>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Manufaturado manufaturado = prodMan.getManufaturado();
            if (manufaturado != null) {
                manufaturado = em.getReference(manufaturado.getClass(), manufaturado.getIdManufaturado());
                prodMan.setManufaturado(manufaturado);
            }
            List<ProdutoValidado> attachedProdutosValidados = new ArrayList<ProdutoValidado>();
            for (ProdutoValidado produtosValidadosProdutoValidadoToAttach : prodMan.getProdutosValidados()) {
                produtosValidadosProdutoValidadoToAttach = em.getReference(produtosValidadosProdutoValidadoToAttach.getClass(), produtosValidadosProdutoValidadoToAttach.getIdProdutoValidado());
                attachedProdutosValidados.add(produtosValidadosProdutoValidadoToAttach);
            }
            prodMan.setProdutosValidados(attachedProdutosValidados);
            em.persist(prodMan);
            if (manufaturado != null) {
                manufaturado.getProdManList().add(prodMan);
                manufaturado = em.merge(manufaturado);
            }
            for (ProdutoValidado produtosValidadosProdutoValidado : prodMan.getProdutosValidados()) {
                ProdMan oldProdManOfProdutosValidadosProdutoValidado = produtosValidadosProdutoValidado.getProdMan();
                produtosValidadosProdutoValidado.setProdMan(prodMan);
                produtosValidadosProdutoValidado = em.merge(produtosValidadosProdutoValidado);
                if (oldProdManOfProdutosValidadosProdutoValidado != null) {
                    oldProdManOfProdutosValidadosProdutoValidado.getProdutosValidados().remove(produtosValidadosProdutoValidado);
                    oldProdManOfProdutosValidadosProdutoValidado = em.merge(oldProdManOfProdutosValidadosProdutoValidado);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ProdMan prodMan) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ProdMan persistentProdMan = em.find(ProdMan.class, prodMan.getIdProdMan());
            Manufaturado manufaturadoOld = persistentProdMan.getManufaturado();
            Manufaturado manufaturadoNew = prodMan.getManufaturado();
            List<ProdutoValidado> produtosValidadosOld = persistentProdMan.getProdutosValidados();
            List<ProdutoValidado> produtosValidadosNew = prodMan.getProdutosValidados();
            List<String> illegalOrphanMessages = null;
            for (ProdutoValidado produtosValidadosOldProdutoValidado : produtosValidadosOld) {
                if (!produtosValidadosNew.contains(produtosValidadosOldProdutoValidado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ProdutoValidado " + produtosValidadosOldProdutoValidado + " since its prodMan field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (manufaturadoNew != null) {
                manufaturadoNew = em.getReference(manufaturadoNew.getClass(), manufaturadoNew.getIdManufaturado());
                prodMan.setManufaturado(manufaturadoNew);
            }
            List<ProdutoValidado> attachedProdutosValidadosNew = new ArrayList<ProdutoValidado>();
            for (ProdutoValidado produtosValidadosNewProdutoValidadoToAttach : produtosValidadosNew) {
                produtosValidadosNewProdutoValidadoToAttach = em.getReference(produtosValidadosNewProdutoValidadoToAttach.getClass(), produtosValidadosNewProdutoValidadoToAttach.getIdProdutoValidado());
                attachedProdutosValidadosNew.add(produtosValidadosNewProdutoValidadoToAttach);
            }
            produtosValidadosNew = attachedProdutosValidadosNew;
            prodMan.setProdutosValidados(produtosValidadosNew);
            prodMan = em.merge(prodMan);
            if (manufaturadoOld != null && !manufaturadoOld.equals(manufaturadoNew)) {
                manufaturadoOld.getProdManList().remove(prodMan);
                manufaturadoOld = em.merge(manufaturadoOld);
            }
            if (manufaturadoNew != null && !manufaturadoNew.equals(manufaturadoOld)) {
                manufaturadoNew.getProdManList().add(prodMan);
                manufaturadoNew = em.merge(manufaturadoNew);
            }
            for (ProdutoValidado produtosValidadosNewProdutoValidado : produtosValidadosNew) {
                if (!produtosValidadosOld.contains(produtosValidadosNewProdutoValidado)) {
                    ProdMan oldProdManOfProdutosValidadosNewProdutoValidado = produtosValidadosNewProdutoValidado.getProdMan();
                    produtosValidadosNewProdutoValidado.setProdMan(prodMan);
                    produtosValidadosNewProdutoValidado = em.merge(produtosValidadosNewProdutoValidado);
                    if (oldProdManOfProdutosValidadosNewProdutoValidado != null && !oldProdManOfProdutosValidadosNewProdutoValidado.equals(prodMan)) {
                        oldProdManOfProdutosValidadosNewProdutoValidado.getProdutosValidados().remove(produtosValidadosNewProdutoValidado);
                        oldProdManOfProdutosValidadosNewProdutoValidado = em.merge(oldProdManOfProdutosValidadosNewProdutoValidado);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = prodMan.getIdProdMan();
                if (findProdMan(id) == null) {
                    throw new NonexistentEntityException("The prodMan with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ProdMan prodMan;
            try {
                prodMan = em.getReference(ProdMan.class, id);
                prodMan.getIdProdMan();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The prodMan with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ProdutoValidado> produtosValidadosOrphanCheck = prodMan.getProdutosValidados();
            for (ProdutoValidado produtosValidadosOrphanCheckProdutoValidado : produtosValidadosOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ProdMan (" + prodMan + ") cannot be destroyed since the ProdutoValidado " + produtosValidadosOrphanCheckProdutoValidado + " in its produtosValidados field has a non-nullable prodMan field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Manufaturado manufaturado = prodMan.getManufaturado();
            if (manufaturado != null) {
                manufaturado.getProdManList().remove(prodMan);
                manufaturado = em.merge(manufaturado);
            }
            em.remove(prodMan);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ProdMan> findProdManEntities() {
        return findProdManEntities(true, -1, -1);
    }

    public List<ProdMan> findProdManEntities(int maxResults, int firstResult) {
        return findProdManEntities(false, maxResults, firstResult);
    }

    private List<ProdMan> findProdManEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ProdMan.class));
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

    public ProdMan findProdMan(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ProdMan.class, id);
        } finally {
            em.close();
        }
    }

    public int getProdManCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ProdMan> rt = cq.from(ProdMan.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
