/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.portaltrading.jpa;

import br.com.portaltrading.entidades.TipoDocumento;
import br.com.portaltrading.jpa.exceptions.IllegalOrphanException;
import br.com.portaltrading.jpa.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.com.portaltrading.entidades.Documento;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Willian
 */
public class TipoDocumentoJpaController implements Serializable {

    public TipoDocumentoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoDocumento tipoDocumento) {
        if (tipoDocumento.getDocumentos() == null) {
            tipoDocumento.setDocumentos(new ArrayList<Documento>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Documento> attachedDocumentos = new ArrayList<Documento>();
            for (Documento documentosDocumentoToAttach : tipoDocumento.getDocumentos()) {
                documentosDocumentoToAttach = em.getReference(documentosDocumentoToAttach.getClass(), documentosDocumentoToAttach.getIdDocumento());
                attachedDocumentos.add(documentosDocumentoToAttach);
            }
            tipoDocumento.setDocumentos(attachedDocumentos);
            em.persist(tipoDocumento);
            for (Documento documentosDocumento : tipoDocumento.getDocumentos()) {
                TipoDocumento oldTipoDocumentoOfDocumentosDocumento = documentosDocumento.getTipoDocumento();
                documentosDocumento.setTipoDocumento(tipoDocumento);
                documentosDocumento = em.merge(documentosDocumento);
                if (oldTipoDocumentoOfDocumentosDocumento != null) {
                    oldTipoDocumentoOfDocumentosDocumento.getDocumentos().remove(documentosDocumento);
                    oldTipoDocumentoOfDocumentosDocumento = em.merge(oldTipoDocumentoOfDocumentosDocumento);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoDocumento tipoDocumento) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoDocumento persistentTipoDocumento = em.find(TipoDocumento.class, tipoDocumento.getIdTipoDocumento());
            List<Documento> documentosOld = persistentTipoDocumento.getDocumentos();
            List<Documento> documentosNew = tipoDocumento.getDocumentos();
            List<String> illegalOrphanMessages = null;
            for (Documento documentosOldDocumento : documentosOld) {
                if (!documentosNew.contains(documentosOldDocumento)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Documento " + documentosOldDocumento + " since its tipoDocumento field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Documento> attachedDocumentosNew = new ArrayList<Documento>();
            for (Documento documentosNewDocumentoToAttach : documentosNew) {
                documentosNewDocumentoToAttach = em.getReference(documentosNewDocumentoToAttach.getClass(), documentosNewDocumentoToAttach.getIdDocumento());
                attachedDocumentosNew.add(documentosNewDocumentoToAttach);
            }
            documentosNew = attachedDocumentosNew;
            tipoDocumento.setDocumentos(documentosNew);
            tipoDocumento = em.merge(tipoDocumento);
            for (Documento documentosNewDocumento : documentosNew) {
                if (!documentosOld.contains(documentosNewDocumento)) {
                    TipoDocumento oldTipoDocumentoOfDocumentosNewDocumento = documentosNewDocumento.getTipoDocumento();
                    documentosNewDocumento.setTipoDocumento(tipoDocumento);
                    documentosNewDocumento = em.merge(documentosNewDocumento);
                    if (oldTipoDocumentoOfDocumentosNewDocumento != null && !oldTipoDocumentoOfDocumentosNewDocumento.equals(tipoDocumento)) {
                        oldTipoDocumentoOfDocumentosNewDocumento.getDocumentos().remove(documentosNewDocumento);
                        oldTipoDocumentoOfDocumentosNewDocumento = em.merge(oldTipoDocumentoOfDocumentosNewDocumento);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = tipoDocumento.getIdTipoDocumento();
                if (findTipoDocumento(id) == null) {
                    throw new NonexistentEntityException("The tipoDocumento with id " + id + " no longer exists.");
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
            TipoDocumento tipoDocumento;
            try {
                tipoDocumento = em.getReference(TipoDocumento.class, id);
                tipoDocumento.getIdTipoDocumento();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoDocumento with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Documento> documentosOrphanCheck = tipoDocumento.getDocumentos();
            for (Documento documentosOrphanCheckDocumento : documentosOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TipoDocumento (" + tipoDocumento + ") cannot be destroyed since the Documento " + documentosOrphanCheckDocumento + " in its documentos field has a non-nullable tipoDocumento field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipoDocumento);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoDocumento> findTipoDocumentoEntities() {
        return findTipoDocumentoEntities(true, -1, -1);
    }

    public List<TipoDocumento> findTipoDocumentoEntities(int maxResults, int firstResult) {
        return findTipoDocumentoEntities(false, maxResults, firstResult);
    }

    private List<TipoDocumento> findTipoDocumentoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoDocumento.class));
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

    public TipoDocumento findTipoDocumento(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoDocumento.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoDocumentoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoDocumento> rt = cq.from(TipoDocumento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
