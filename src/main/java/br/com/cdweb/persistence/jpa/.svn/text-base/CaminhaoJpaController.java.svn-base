/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.portaltrading.jpa;

import br.com.portaltrading.entidades.Caminhao;
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
import br.com.portaltrading.entidades.CaminhaoPedido;
import java.util.ArrayList;
import java.util.List;
import br.com.portaltrading.entidades.ValorCaminhao;

/**
 *
 * @author Willian
 */
public class CaminhaoJpaController implements Serializable {

    public CaminhaoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Caminhao caminhao) {
        if (caminhao.getCaminhaoPedidoList() == null) {
            caminhao.setCaminhaoPedidoList(new ArrayList<CaminhaoPedido>());
        }
        if (caminhao.getValoresCaminhao() == null) {
            caminhao.setValoresCaminhao(new ArrayList<ValorCaminhao>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RepRodoviario repRodoviario = caminhao.getRepRodoviario();
            if (repRodoviario != null) {
                repRodoviario = em.getReference(repRodoviario.getClass(), repRodoviario.getIdRepRodoviario());
                caminhao.setRepRodoviario(repRodoviario);
            }
            List<CaminhaoPedido> attachedCaminhaoPedidoList = new ArrayList<CaminhaoPedido>();
            for (CaminhaoPedido caminhaoPedidoListCaminhaoPedidoToAttach : caminhao.getCaminhaoPedidoList()) {
                caminhaoPedidoListCaminhaoPedidoToAttach = em.merge(caminhaoPedidoListCaminhaoPedidoToAttach);
                attachedCaminhaoPedidoList.add(caminhaoPedidoListCaminhaoPedidoToAttach);
            }
            caminhao.setCaminhaoPedidoList(attachedCaminhaoPedidoList);
            List<ValorCaminhao> attachedValoresCaminhao = new ArrayList<ValorCaminhao>();
            for (ValorCaminhao valoresCaminhaoValorCaminhaoToAttach : caminhao.getValoresCaminhao()) {
                valoresCaminhaoValorCaminhaoToAttach = em.getReference(valoresCaminhaoValorCaminhaoToAttach.getClass(), valoresCaminhaoValorCaminhaoToAttach.getIdValorCaminhao());
                attachedValoresCaminhao.add(valoresCaminhaoValorCaminhaoToAttach);
            }
            caminhao.setValoresCaminhao(attachedValoresCaminhao);
            em.persist(caminhao);
            if (repRodoviario != null) {
                repRodoviario.getCaminhoes().add(caminhao);
                repRodoviario = em.merge(repRodoviario);
            }
            for (CaminhaoPedido caminhaoPedidoListCaminhaoPedido : caminhao.getCaminhaoPedidoList()) {
                Caminhao oldCaminhaoOfCaminhaoPedidoListCaminhaoPedido = caminhaoPedidoListCaminhaoPedido.getCaminhao();
                caminhaoPedidoListCaminhaoPedido.setCaminhao(caminhao);
                caminhaoPedidoListCaminhaoPedido = em.merge(caminhaoPedidoListCaminhaoPedido);
                if (oldCaminhaoOfCaminhaoPedidoListCaminhaoPedido != null) {
                    oldCaminhaoOfCaminhaoPedidoListCaminhaoPedido.getCaminhaoPedidoList().remove(caminhaoPedidoListCaminhaoPedido);
                    oldCaminhaoOfCaminhaoPedidoListCaminhaoPedido = em.merge(oldCaminhaoOfCaminhaoPedidoListCaminhaoPedido);
                }
            }
            for (ValorCaminhao valoresCaminhaoValorCaminhao : caminhao.getValoresCaminhao()) {
                Caminhao oldCaminhaoOfValoresCaminhaoValorCaminhao = valoresCaminhaoValorCaminhao.getCaminhao();
                valoresCaminhaoValorCaminhao.setCaminhao(caminhao);
                valoresCaminhaoValorCaminhao = em.merge(valoresCaminhaoValorCaminhao);
                if (oldCaminhaoOfValoresCaminhaoValorCaminhao != null) {
                    oldCaminhaoOfValoresCaminhaoValorCaminhao.getValoresCaminhao().remove(valoresCaminhaoValorCaminhao);
                    oldCaminhaoOfValoresCaminhaoValorCaminhao = em.merge(oldCaminhaoOfValoresCaminhaoValorCaminhao);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Caminhao caminhao) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Caminhao persistentCaminhao = em.find(Caminhao.class, caminhao.getIdCaminhao());
            RepRodoviario repRodoviarioOld = persistentCaminhao.getRepRodoviario();
            RepRodoviario repRodoviarioNew = caminhao.getRepRodoviario();
            List<CaminhaoPedido> caminhaoPedidoListOld = persistentCaminhao.getCaminhaoPedidoList();
            List<CaminhaoPedido> caminhaoPedidoListNew = caminhao.getCaminhaoPedidoList();
            List<ValorCaminhao> valoresCaminhaoOld = persistentCaminhao.getValoresCaminhao();
            List<ValorCaminhao> valoresCaminhaoNew = caminhao.getValoresCaminhao();
            List<String> illegalOrphanMessages = null;
            for (CaminhaoPedido caminhaoPedidoListOldCaminhaoPedido : caminhaoPedidoListOld) {
                if (!caminhaoPedidoListNew.contains(caminhaoPedidoListOldCaminhaoPedido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CaminhaoPedido " + caminhaoPedidoListOldCaminhaoPedido + " since its caminhao field is not nullable.");
                }
            }
            for (ValorCaminhao valoresCaminhaoOldValorCaminhao : valoresCaminhaoOld) {
                if (!valoresCaminhaoNew.contains(valoresCaminhaoOldValorCaminhao)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ValorCaminhao " + valoresCaminhaoOldValorCaminhao + " since its caminhao field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (repRodoviarioNew != null) {
                repRodoviarioNew = em.getReference(repRodoviarioNew.getClass(), repRodoviarioNew.getIdRepRodoviario());
                caminhao.setRepRodoviario(repRodoviarioNew);
            }
            List<CaminhaoPedido> attachedCaminhaoPedidoListNew = new ArrayList<CaminhaoPedido>();
            for (CaminhaoPedido caminhaoPedidoListNewCaminhaoPedidoToAttach : caminhaoPedidoListNew) {
                caminhaoPedidoListNewCaminhaoPedidoToAttach = em.merge(caminhaoPedidoListNewCaminhaoPedidoToAttach);
                attachedCaminhaoPedidoListNew.add(caminhaoPedidoListNewCaminhaoPedidoToAttach);
            }
            caminhaoPedidoListNew = attachedCaminhaoPedidoListNew;
            caminhao.setCaminhaoPedidoList(caminhaoPedidoListNew);
            List<ValorCaminhao> attachedValoresCaminhaoNew = new ArrayList<ValorCaminhao>();
            for (ValorCaminhao valoresCaminhaoNewValorCaminhaoToAttach : valoresCaminhaoNew) {
                valoresCaminhaoNewValorCaminhaoToAttach = em.getReference(valoresCaminhaoNewValorCaminhaoToAttach.getClass(), valoresCaminhaoNewValorCaminhaoToAttach.getIdValorCaminhao());
                attachedValoresCaminhaoNew.add(valoresCaminhaoNewValorCaminhaoToAttach);
            }
            valoresCaminhaoNew = attachedValoresCaminhaoNew;
            caminhao.setValoresCaminhao(valoresCaminhaoNew);
            caminhao = em.merge(caminhao);
            if (repRodoviarioOld != null && !repRodoviarioOld.equals(repRodoviarioNew)) {
                repRodoviarioOld.getCaminhoes().remove(caminhao);
                repRodoviarioOld = em.merge(repRodoviarioOld);
            }
            if (repRodoviarioNew != null && !repRodoviarioNew.equals(repRodoviarioOld)) {
                repRodoviarioNew.getCaminhoes().add(caminhao);
                repRodoviarioNew = em.merge(repRodoviarioNew);
            }
            for (CaminhaoPedido caminhaoPedidoListNewCaminhaoPedido : caminhaoPedidoListNew) {
                if (!caminhaoPedidoListOld.contains(caminhaoPedidoListNewCaminhaoPedido)) {
                    Caminhao oldCaminhaoOfCaminhaoPedidoListNewCaminhaoPedido = caminhaoPedidoListNewCaminhaoPedido.getCaminhao();
                    caminhaoPedidoListNewCaminhaoPedido.setCaminhao(caminhao);
                    caminhaoPedidoListNewCaminhaoPedido = em.merge(caminhaoPedidoListNewCaminhaoPedido);
                    if (oldCaminhaoOfCaminhaoPedidoListNewCaminhaoPedido != null && !oldCaminhaoOfCaminhaoPedidoListNewCaminhaoPedido.equals(caminhao)) {
                        oldCaminhaoOfCaminhaoPedidoListNewCaminhaoPedido.getCaminhaoPedidoList().remove(caminhaoPedidoListNewCaminhaoPedido);
                        oldCaminhaoOfCaminhaoPedidoListNewCaminhaoPedido = em.merge(oldCaminhaoOfCaminhaoPedidoListNewCaminhaoPedido);
                    }
                }
            }
            for (ValorCaminhao valoresCaminhaoNewValorCaminhao : valoresCaminhaoNew) {
                if (!valoresCaminhaoOld.contains(valoresCaminhaoNewValorCaminhao)) {
                    Caminhao oldCaminhaoOfValoresCaminhaoNewValorCaminhao = valoresCaminhaoNewValorCaminhao.getCaminhao();
                    valoresCaminhaoNewValorCaminhao.setCaminhao(caminhao);
                    valoresCaminhaoNewValorCaminhao = em.merge(valoresCaminhaoNewValorCaminhao);
                    if (oldCaminhaoOfValoresCaminhaoNewValorCaminhao != null && !oldCaminhaoOfValoresCaminhaoNewValorCaminhao.equals(caminhao)) {
                        oldCaminhaoOfValoresCaminhaoNewValorCaminhao.getValoresCaminhao().remove(valoresCaminhaoNewValorCaminhao);
                        oldCaminhaoOfValoresCaminhaoNewValorCaminhao = em.merge(oldCaminhaoOfValoresCaminhaoNewValorCaminhao);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = caminhao.getIdCaminhao();
                if (findCaminhao(id) == null) {
                    throw new NonexistentEntityException("The caminhao with id " + id + " no longer exists.");
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
            Caminhao caminhao;
            try {
                caminhao = em.getReference(Caminhao.class, id);
                caminhao.getIdCaminhao();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The caminhao with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<CaminhaoPedido> caminhaoPedidoListOrphanCheck = caminhao.getCaminhaoPedidoList();
            for (CaminhaoPedido caminhaoPedidoListOrphanCheckCaminhaoPedido : caminhaoPedidoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Caminhao (" + caminhao + ") cannot be destroyed since the CaminhaoPedido " + caminhaoPedidoListOrphanCheckCaminhaoPedido + " in its caminhaoPedidoList field has a non-nullable caminhao field.");
            }
            List<ValorCaminhao> valoresCaminhaoOrphanCheck = caminhao.getValoresCaminhao();
            for (ValorCaminhao valoresCaminhaoOrphanCheckValorCaminhao : valoresCaminhaoOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Caminhao (" + caminhao + ") cannot be destroyed since the ValorCaminhao " + valoresCaminhaoOrphanCheckValorCaminhao + " in its valoresCaminhao field has a non-nullable caminhao field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            RepRodoviario repRodoviario = caminhao.getRepRodoviario();
            if (repRodoviario != null) {
                repRodoviario.getCaminhoes().remove(caminhao);
                repRodoviario = em.merge(repRodoviario);
            }
            em.remove(caminhao);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Caminhao> findCaminhaoEntities() {
        return findCaminhaoEntities(true, -1, -1);
    }

    public List<Caminhao> findCaminhaoEntities(int maxResults, int firstResult) {
        return findCaminhaoEntities(false, maxResults, firstResult);
    }

    private List<Caminhao> findCaminhaoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Caminhao.class));
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

    public Caminhao findCaminhao(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Caminhao.class, id);
        } finally {
            em.close();
        }
    }

    public int getCaminhaoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Caminhao> rt = cq.from(Caminhao.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
