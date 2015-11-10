/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.portaltrading.jpa;

import br.com.portaltrading.entidades.RepRodoviario;
import br.com.portaltrading.jpa.exceptions.IllegalOrphanException;
import br.com.portaltrading.jpa.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.com.portaltrading.entidades.Empresa;
import br.com.portaltrading.entidades.Caminhao;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Willian
 */
public class RepRodoviarioJpaController implements Serializable {

    public RepRodoviarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RepRodoviario repRodoviario) {
        if (repRodoviario.getCaminhoes() == null) {
            repRodoviario.setCaminhoes(new ArrayList<Caminhao>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empresa empresa = repRodoviario.getEmpresa();
            if (empresa != null) {
                empresa = em.getReference(empresa.getClass(), empresa.getIdEmpresa());
                repRodoviario.setEmpresa(empresa);
            }
            List<Caminhao> attachedCaminhoes = new ArrayList<Caminhao>();
            for (Caminhao caminhoesCaminhaoToAttach : repRodoviario.getCaminhoes()) {
                caminhoesCaminhaoToAttach = em.getReference(caminhoesCaminhaoToAttach.getClass(), caminhoesCaminhaoToAttach.getIdCaminhao());
                attachedCaminhoes.add(caminhoesCaminhaoToAttach);
            }
            repRodoviario.setCaminhoes(attachedCaminhoes);
            em.persist(repRodoviario);
            if (empresa != null) {
                empresa.getRepRodoviarioList().add(repRodoviario);
                empresa = em.merge(empresa);
            }
            for (Caminhao caminhoesCaminhao : repRodoviario.getCaminhoes()) {
                RepRodoviario oldRepRodoviarioOfCaminhoesCaminhao = caminhoesCaminhao.getRepRodoviario();
                caminhoesCaminhao.setRepRodoviario(repRodoviario);
                caminhoesCaminhao = em.merge(caminhoesCaminhao);
                if (oldRepRodoviarioOfCaminhoesCaminhao != null) {
                    oldRepRodoviarioOfCaminhoesCaminhao.getCaminhoes().remove(caminhoesCaminhao);
                    oldRepRodoviarioOfCaminhoesCaminhao = em.merge(oldRepRodoviarioOfCaminhoesCaminhao);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RepRodoviario repRodoviario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RepRodoviario persistentRepRodoviario = em.find(RepRodoviario.class, repRodoviario.getIdRepRodoviario());
            Empresa empresaOld = persistentRepRodoviario.getEmpresa();
            Empresa empresaNew = repRodoviario.getEmpresa();
            List<Caminhao> caminhoesOld = persistentRepRodoviario.getCaminhoes();
            List<Caminhao> caminhoesNew = repRodoviario.getCaminhoes();
            List<String> illegalOrphanMessages = null;
            for (Caminhao caminhoesOldCaminhao : caminhoesOld) {
                if (!caminhoesNew.contains(caminhoesOldCaminhao)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Caminhao " + caminhoesOldCaminhao + " since its repRodoviario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (empresaNew != null) {
                empresaNew = em.getReference(empresaNew.getClass(), empresaNew.getIdEmpresa());
                repRodoviario.setEmpresa(empresaNew);
            }
            List<Caminhao> attachedCaminhoesNew = new ArrayList<Caminhao>();
            for (Caminhao caminhoesNewCaminhaoToAttach : caminhoesNew) {
                caminhoesNewCaminhaoToAttach = em.getReference(caminhoesNewCaminhaoToAttach.getClass(), caminhoesNewCaminhaoToAttach.getIdCaminhao());
                attachedCaminhoesNew.add(caminhoesNewCaminhaoToAttach);
            }
            caminhoesNew = attachedCaminhoesNew;
            repRodoviario.setCaminhoes(caminhoesNew);
            repRodoviario = em.merge(repRodoviario);
            if (empresaOld != null && !empresaOld.equals(empresaNew)) {
                empresaOld.getRepRodoviarioList().remove(repRodoviario);
                empresaOld = em.merge(empresaOld);
            }
            if (empresaNew != null && !empresaNew.equals(empresaOld)) {
                empresaNew.getRepRodoviarioList().add(repRodoviario);
                empresaNew = em.merge(empresaNew);
            }
            for (Caminhao caminhoesNewCaminhao : caminhoesNew) {
                if (!caminhoesOld.contains(caminhoesNewCaminhao)) {
                    RepRodoviario oldRepRodoviarioOfCaminhoesNewCaminhao = caminhoesNewCaminhao.getRepRodoviario();
                    caminhoesNewCaminhao.setRepRodoviario(repRodoviario);
                    caminhoesNewCaminhao = em.merge(caminhoesNewCaminhao);
                    if (oldRepRodoviarioOfCaminhoesNewCaminhao != null && !oldRepRodoviarioOfCaminhoesNewCaminhao.equals(repRodoviario)) {
                        oldRepRodoviarioOfCaminhoesNewCaminhao.getCaminhoes().remove(caminhoesNewCaminhao);
                        oldRepRodoviarioOfCaminhoesNewCaminhao = em.merge(oldRepRodoviarioOfCaminhoesNewCaminhao);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = repRodoviario.getIdRepRodoviario();
                if (findRepRodoviario(id) == null) {
                    throw new NonexistentEntityException("The repRodoviario with id " + id + " no longer exists.");
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
            RepRodoviario repRodoviario;
            try {
                repRodoviario = em.getReference(RepRodoviario.class, id);
                repRodoviario.getIdRepRodoviario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The repRodoviario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Caminhao> caminhoesOrphanCheck = repRodoviario.getCaminhoes();
            for (Caminhao caminhoesOrphanCheckCaminhao : caminhoesOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This RepRodoviario (" + repRodoviario + ") cannot be destroyed since the Caminhao " + caminhoesOrphanCheckCaminhao + " in its caminhoes field has a non-nullable repRodoviario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Empresa empresa = repRodoviario.getEmpresa();
            if (empresa != null) {
                empresa.getRepRodoviarioList().remove(repRodoviario);
                empresa = em.merge(empresa);
            }
            em.remove(repRodoviario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<RepRodoviario> findRepRodoviarioEntities() {
        return findRepRodoviarioEntities(true, -1, -1);
    }

    public List<RepRodoviario> findRepRodoviarioEntities(int maxResults, int firstResult) {
        return findRepRodoviarioEntities(false, maxResults, firstResult);
    }

    private List<RepRodoviario> findRepRodoviarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RepRodoviario.class));
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

    public RepRodoviario findRepRodoviario(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RepRodoviario.class, id);
        } finally {
            em.close();
        }
    }

    public int getRepRodoviarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RepRodoviario> rt = cq.from(RepRodoviario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
