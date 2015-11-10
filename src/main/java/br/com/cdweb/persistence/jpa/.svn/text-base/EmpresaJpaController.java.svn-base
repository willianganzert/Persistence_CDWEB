/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.portaltrading.jpa;

import br.com.portaltrading.entidades.Empresa;
import br.com.portaltrading.jpa.exceptions.IllegalOrphanException;
import br.com.portaltrading.jpa.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.com.portaltrading.entidades.RepRodoviario;
import java.util.ArrayList;
import java.util.List;
import br.com.portaltrading.entidades.Fornecedor;
import br.com.portaltrading.entidades.Despachante;

/**
 *
 * @author Willian
 */
public class EmpresaJpaController implements Serializable {

    public EmpresaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Empresa empresa) {
        if (empresa.getRepRodoviarioList() == null) {
            empresa.setRepRodoviarioList(new ArrayList<RepRodoviario>());
        }
        if (empresa.getFornecedorList() == null) {
            empresa.setFornecedorList(new ArrayList<Fornecedor>());
        }
        if (empresa.getDespachanteList() == null) {
            empresa.setDespachanteList(new ArrayList<Despachante>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<RepRodoviario> attachedRepRodoviarioList = new ArrayList<RepRodoviario>();
            for (RepRodoviario repRodoviarioListRepRodoviarioToAttach : empresa.getRepRodoviarioList()) {
                repRodoviarioListRepRodoviarioToAttach = em.getReference(repRodoviarioListRepRodoviarioToAttach.getClass(), repRodoviarioListRepRodoviarioToAttach.getIdRepRodoviario());
                attachedRepRodoviarioList.add(repRodoviarioListRepRodoviarioToAttach);
            }
            empresa.setRepRodoviarioList(attachedRepRodoviarioList);
            List<Fornecedor> attachedFornecedorList = new ArrayList<Fornecedor>();
            for (Fornecedor fornecedorListFornecedorToAttach : empresa.getFornecedorList()) {
                fornecedorListFornecedorToAttach = em.getReference(fornecedorListFornecedorToAttach.getClass(), fornecedorListFornecedorToAttach.getIdFornecedor());
                attachedFornecedorList.add(fornecedorListFornecedorToAttach);
            }
            empresa.setFornecedorList(attachedFornecedorList);
            List<Despachante> attachedDespachanteList = new ArrayList<Despachante>();
            for (Despachante despachanteListDespachanteToAttach : empresa.getDespachanteList()) {
                despachanteListDespachanteToAttach = em.getReference(despachanteListDespachanteToAttach.getClass(), despachanteListDespachanteToAttach.getIdDespachante());
                attachedDespachanteList.add(despachanteListDespachanteToAttach);
            }
            empresa.setDespachanteList(attachedDespachanteList);
            em.persist(empresa);
            for (RepRodoviario repRodoviarioListRepRodoviario : empresa.getRepRodoviarioList()) {
                Empresa oldEmpresaOfRepRodoviarioListRepRodoviario = repRodoviarioListRepRodoviario.getEmpresa();
                repRodoviarioListRepRodoviario.setEmpresa(empresa);
                repRodoviarioListRepRodoviario = em.merge(repRodoviarioListRepRodoviario);
                if (oldEmpresaOfRepRodoviarioListRepRodoviario != null) {
                    oldEmpresaOfRepRodoviarioListRepRodoviario.getRepRodoviarioList().remove(repRodoviarioListRepRodoviario);
                    oldEmpresaOfRepRodoviarioListRepRodoviario = em.merge(oldEmpresaOfRepRodoviarioListRepRodoviario);
                }
            }
            for (Fornecedor fornecedorListFornecedor : empresa.getFornecedorList()) {
                Empresa oldEmpresaOfFornecedorListFornecedor = fornecedorListFornecedor.getEmpresa();
                fornecedorListFornecedor.setEmpresa(empresa);
                fornecedorListFornecedor = em.merge(fornecedorListFornecedor);
                if (oldEmpresaOfFornecedorListFornecedor != null) {
                    oldEmpresaOfFornecedorListFornecedor.getFornecedorList().remove(fornecedorListFornecedor);
                    oldEmpresaOfFornecedorListFornecedor = em.merge(oldEmpresaOfFornecedorListFornecedor);
                }
            }
            for (Despachante despachanteListDespachante : empresa.getDespachanteList()) {
                Empresa oldEmpresaOfDespachanteListDespachante = despachanteListDespachante.getEmpresa();
                despachanteListDespachante.setEmpresa(empresa);
                despachanteListDespachante = em.merge(despachanteListDespachante);
                if (oldEmpresaOfDespachanteListDespachante != null) {
                    oldEmpresaOfDespachanteListDespachante.getDespachanteList().remove(despachanteListDespachante);
                    oldEmpresaOfDespachanteListDespachante = em.merge(oldEmpresaOfDespachanteListDespachante);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Empresa empresa) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empresa persistentEmpresa = em.find(Empresa.class, empresa.getIdEmpresa());
            List<RepRodoviario> repRodoviarioListOld = persistentEmpresa.getRepRodoviarioList();
            List<RepRodoviario> repRodoviarioListNew = empresa.getRepRodoviarioList();
            List<Fornecedor> fornecedorListOld = persistentEmpresa.getFornecedorList();
            List<Fornecedor> fornecedorListNew = empresa.getFornecedorList();
            List<Despachante> despachanteListOld = persistentEmpresa.getDespachanteList();
            List<Despachante> despachanteListNew = empresa.getDespachanteList();
            List<String> illegalOrphanMessages = null;
            for (RepRodoviario repRodoviarioListOldRepRodoviario : repRodoviarioListOld) {
                if (!repRodoviarioListNew.contains(repRodoviarioListOldRepRodoviario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RepRodoviario " + repRodoviarioListOldRepRodoviario + " since its empresa field is not nullable.");
                }
            }
            for (Fornecedor fornecedorListOldFornecedor : fornecedorListOld) {
                if (!fornecedorListNew.contains(fornecedorListOldFornecedor)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Fornecedor " + fornecedorListOldFornecedor + " since its empresa field is not nullable.");
                }
            }
            for (Despachante despachanteListOldDespachante : despachanteListOld) {
                if (!despachanteListNew.contains(despachanteListOldDespachante)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Despachante " + despachanteListOldDespachante + " since its empresa field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<RepRodoviario> attachedRepRodoviarioListNew = new ArrayList<RepRodoviario>();
            for (RepRodoviario repRodoviarioListNewRepRodoviarioToAttach : repRodoviarioListNew) {
                repRodoviarioListNewRepRodoviarioToAttach = em.getReference(repRodoviarioListNewRepRodoviarioToAttach.getClass(), repRodoviarioListNewRepRodoviarioToAttach.getIdRepRodoviario());
                attachedRepRodoviarioListNew.add(repRodoviarioListNewRepRodoviarioToAttach);
            }
            repRodoviarioListNew = attachedRepRodoviarioListNew;
            empresa.setRepRodoviarioList(repRodoviarioListNew);
            List<Fornecedor> attachedFornecedorListNew = new ArrayList<Fornecedor>();
            for (Fornecedor fornecedorListNewFornecedorToAttach : fornecedorListNew) {
                fornecedorListNewFornecedorToAttach = em.getReference(fornecedorListNewFornecedorToAttach.getClass(), fornecedorListNewFornecedorToAttach.getIdFornecedor());
                attachedFornecedorListNew.add(fornecedorListNewFornecedorToAttach);
            }
            fornecedorListNew = attachedFornecedorListNew;
            empresa.setFornecedorList(fornecedorListNew);
            List<Despachante> attachedDespachanteListNew = new ArrayList<Despachante>();
            for (Despachante despachanteListNewDespachanteToAttach : despachanteListNew) {
                despachanteListNewDespachanteToAttach = em.getReference(despachanteListNewDespachanteToAttach.getClass(), despachanteListNewDespachanteToAttach.getIdDespachante());
                attachedDespachanteListNew.add(despachanteListNewDespachanteToAttach);
            }
            despachanteListNew = attachedDespachanteListNew;
            empresa.setDespachanteList(despachanteListNew);
            empresa = em.merge(empresa);
            for (RepRodoviario repRodoviarioListNewRepRodoviario : repRodoviarioListNew) {
                if (!repRodoviarioListOld.contains(repRodoviarioListNewRepRodoviario)) {
                    Empresa oldEmpresaOfRepRodoviarioListNewRepRodoviario = repRodoviarioListNewRepRodoviario.getEmpresa();
                    repRodoviarioListNewRepRodoviario.setEmpresa(empresa);
                    repRodoviarioListNewRepRodoviario = em.merge(repRodoviarioListNewRepRodoviario);
                    if (oldEmpresaOfRepRodoviarioListNewRepRodoviario != null && !oldEmpresaOfRepRodoviarioListNewRepRodoviario.equals(empresa)) {
                        oldEmpresaOfRepRodoviarioListNewRepRodoviario.getRepRodoviarioList().remove(repRodoviarioListNewRepRodoviario);
                        oldEmpresaOfRepRodoviarioListNewRepRodoviario = em.merge(oldEmpresaOfRepRodoviarioListNewRepRodoviario);
                    }
                }
            }
            for (Fornecedor fornecedorListNewFornecedor : fornecedorListNew) {
                if (!fornecedorListOld.contains(fornecedorListNewFornecedor)) {
                    Empresa oldEmpresaOfFornecedorListNewFornecedor = fornecedorListNewFornecedor.getEmpresa();
                    fornecedorListNewFornecedor.setEmpresa(empresa);
                    fornecedorListNewFornecedor = em.merge(fornecedorListNewFornecedor);
                    if (oldEmpresaOfFornecedorListNewFornecedor != null && !oldEmpresaOfFornecedorListNewFornecedor.equals(empresa)) {
                        oldEmpresaOfFornecedorListNewFornecedor.getFornecedorList().remove(fornecedorListNewFornecedor);
                        oldEmpresaOfFornecedorListNewFornecedor = em.merge(oldEmpresaOfFornecedorListNewFornecedor);
                    }
                }
            }
            for (Despachante despachanteListNewDespachante : despachanteListNew) {
                if (!despachanteListOld.contains(despachanteListNewDespachante)) {
                    Empresa oldEmpresaOfDespachanteListNewDespachante = despachanteListNewDespachante.getEmpresa();
                    despachanteListNewDespachante.setEmpresa(empresa);
                    despachanteListNewDespachante = em.merge(despachanteListNewDespachante);
                    if (oldEmpresaOfDespachanteListNewDespachante != null && !oldEmpresaOfDespachanteListNewDespachante.equals(empresa)) {
                        oldEmpresaOfDespachanteListNewDespachante.getDespachanteList().remove(despachanteListNewDespachante);
                        oldEmpresaOfDespachanteListNewDespachante = em.merge(oldEmpresaOfDespachanteListNewDespachante);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = empresa.getIdEmpresa();
                if (findEmpresa(id) == null) {
                    throw new NonexistentEntityException("The empresa with id " + id + " no longer exists.");
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
            Empresa empresa;
            try {
                empresa = em.getReference(Empresa.class, id);
                empresa.getIdEmpresa();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empresa with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<RepRodoviario> repRodoviarioListOrphanCheck = empresa.getRepRodoviarioList();
            for (RepRodoviario repRodoviarioListOrphanCheckRepRodoviario : repRodoviarioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empresa (" + empresa + ") cannot be destroyed since the RepRodoviario " + repRodoviarioListOrphanCheckRepRodoviario + " in its repRodoviarioList field has a non-nullable empresa field.");
            }
            List<Fornecedor> fornecedorListOrphanCheck = empresa.getFornecedorList();
            for (Fornecedor fornecedorListOrphanCheckFornecedor : fornecedorListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empresa (" + empresa + ") cannot be destroyed since the Fornecedor " + fornecedorListOrphanCheckFornecedor + " in its fornecedorList field has a non-nullable empresa field.");
            }
            List<Despachante> despachanteListOrphanCheck = empresa.getDespachanteList();
            for (Despachante despachanteListOrphanCheckDespachante : despachanteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empresa (" + empresa + ") cannot be destroyed since the Despachante " + despachanteListOrphanCheckDespachante + " in its despachanteList field has a non-nullable empresa field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(empresa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Empresa> findEmpresaEntities() {
        return findEmpresaEntities(true, -1, -1);
    }

    public List<Empresa> findEmpresaEntities(int maxResults, int firstResult) {
        return findEmpresaEntities(false, maxResults, firstResult);
    }

    private List<Empresa> findEmpresaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Empresa.class));
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

    public Empresa findEmpresa(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Empresa.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmpresaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Empresa> rt = cq.from(Empresa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
