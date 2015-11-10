/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.portaltrading.jpa;

import br.com.portaltrading.entidades.RepMaritimo;
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
import br.com.portaltrading.entidades.Container;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Willian
 */
public class RepMaritimoJpaController implements Serializable {

    public RepMaritimoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RepMaritimo repMaritimo) {
        if (repMaritimo.getContaineres() == null) {
            repMaritimo.setContaineres(new ArrayList<Container>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empresa empresa = repMaritimo.getEmpresa();
            if (empresa != null) {
                empresa = em.getReference(empresa.getClass(), empresa.getIdEmpresa());
                repMaritimo.setEmpresa(empresa);
            }
            List<Container> attachedContaineres = new ArrayList<Container>();
            for (Container containeresContainerToAttach : repMaritimo.getContaineres()) {
                containeresContainerToAttach = em.getReference(containeresContainerToAttach.getClass(), containeresContainerToAttach.getIdContainer());
                attachedContaineres.add(containeresContainerToAttach);
            }
            repMaritimo.setContaineres(attachedContaineres);
            em.persist(repMaritimo);
            if (empresa != null) {
                empresa.getRepmaritimoList().add(repMaritimo);
                empresa = em.merge(empresa);
            }
            for (Container containeresContainer : repMaritimo.getContaineres()) {
                RepMaritimo oldRepMaritimoOfContaineresContainer = containeresContainer.getRepMaritimo();
                containeresContainer.setRepMaritimo(repMaritimo);
                containeresContainer = em.merge(containeresContainer);
                if (oldRepMaritimoOfContaineresContainer != null) {
                    oldRepMaritimoOfContaineresContainer.getContaineres().remove(containeresContainer);
                    oldRepMaritimoOfContaineresContainer = em.merge(oldRepMaritimoOfContaineresContainer);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RepMaritimo repMaritimo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RepMaritimo persistentRepMaritimo = em.find(RepMaritimo.class, repMaritimo.getIdRepMaritimo());
            Empresa empresaOld = persistentRepMaritimo.getEmpresa();
            Empresa empresaNew = repMaritimo.getEmpresa();
            List<Container> containeresOld = persistentRepMaritimo.getContaineres();
            List<Container> containeresNew = repMaritimo.getContaineres();
            List<String> illegalOrphanMessages = null;
            for (Container containeresOldContainer : containeresOld) {
                if (!containeresNew.contains(containeresOldContainer)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Container " + containeresOldContainer + " since its repMaritimo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (empresaNew != null) {
                empresaNew = em.getReference(empresaNew.getClass(), empresaNew.getIdEmpresa());
                repMaritimo.setEmpresa(empresaNew);
            }
            List<Container> attachedContaineresNew = new ArrayList<Container>();
            for (Container containeresNewContainerToAttach : containeresNew) {
                containeresNewContainerToAttach = em.getReference(containeresNewContainerToAttach.getClass(), containeresNewContainerToAttach.getIdContainer());
                attachedContaineresNew.add(containeresNewContainerToAttach);
            }
            containeresNew = attachedContaineresNew;
            repMaritimo.setContaineres(containeresNew);
            repMaritimo = em.merge(repMaritimo);
            if (empresaOld != null && !empresaOld.equals(empresaNew)) {
                empresaOld.getRepmaritimoList().remove(repMaritimo);
                empresaOld = em.merge(empresaOld);
            }
            if (empresaNew != null && !empresaNew.equals(empresaOld)) {
                empresaNew.getRepmaritimoList().add(repMaritimo);
                empresaNew = em.merge(empresaNew);
            }
            for (Container containeresNewContainer : containeresNew) {
                if (!containeresOld.contains(containeresNewContainer)) {
                    RepMaritimo oldRepMaritimoOfContaineresNewContainer = containeresNewContainer.getRepMaritimo();
                    containeresNewContainer.setRepMaritimo(repMaritimo);
                    containeresNewContainer = em.merge(containeresNewContainer);
                    if (oldRepMaritimoOfContaineresNewContainer != null && !oldRepMaritimoOfContaineresNewContainer.equals(repMaritimo)) {
                        oldRepMaritimoOfContaineresNewContainer.getContaineres().remove(containeresNewContainer);
                        oldRepMaritimoOfContaineresNewContainer = em.merge(oldRepMaritimoOfContaineresNewContainer);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = repMaritimo.getIdRepMaritimo();
                if (findRepMaritimo(id) == null) {
                    throw new NonexistentEntityException("The repMaritimo with id " + id + " no longer exists.");
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
            RepMaritimo repMaritimo;
            try {
                repMaritimo = em.getReference(RepMaritimo.class, id);
                repMaritimo.getIdRepMaritimo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The repMaritimo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Container> containeresOrphanCheck = repMaritimo.getContaineres();
            for (Container containeresOrphanCheckContainer : containeresOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This RepMaritimo (" + repMaritimo + ") cannot be destroyed since the Container " + containeresOrphanCheckContainer + " in its containeres field has a non-nullable repMaritimo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Empresa empresa = repMaritimo.getEmpresa();
            if (empresa != null) {
                empresa.getRepmaritimoList().remove(repMaritimo);
                empresa = em.merge(empresa);
            }
            em.remove(repMaritimo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<RepMaritimo> findRepMaritimoEntities() {
        return findRepMaritimoEntities(true, -1, -1);
    }

    public List<RepMaritimo> findRepMaritimoEntities(int maxResults, int firstResult) {
        return findRepMaritimoEntities(false, maxResults, firstResult);
    }

    private List<RepMaritimo> findRepMaritimoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RepMaritimo.class));
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

    public RepMaritimo findRepMaritimo(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RepMaritimo.class, id);
        } finally {
            em.close();
        }
    }

    public int getRepMaritimoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RepMaritimo> rt = cq.from(RepMaritimo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
