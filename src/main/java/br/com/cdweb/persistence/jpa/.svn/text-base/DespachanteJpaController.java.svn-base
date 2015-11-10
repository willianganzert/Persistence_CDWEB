/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.portaltrading.jpa;

import br.com.portaltrading.entidades.Despachante;
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
import br.com.portaltrading.entidades.Pedido;
import java.util.ArrayList;
import java.util.List;
import br.com.portaltrading.entidades.ValorDespachante;

/**
 *
 * @author Willian
 */
public class DespachanteJpaController implements Serializable {

    public DespachanteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Despachante despachante) {
        if (despachante.getPedidos() == null) {
            despachante.setPedidos(new ArrayList<Pedido>());
        }
        if (despachante.getValoresDespachante() == null) {
            despachante.setValoresDespachante(new ArrayList<ValorDespachante>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empresa empresa = despachante.getEmpresa();
            if (empresa != null) {
                empresa = em.getReference(empresa.getClass(), empresa.getIdEmpresa());
                despachante.setEmpresa(empresa);
            }
            List<Pedido> attachedPedidos = new ArrayList<Pedido>();
            for (Pedido pedidosPedidoToAttach : despachante.getPedidos()) {
                pedidosPedidoToAttach = em.getReference(pedidosPedidoToAttach.getClass(), pedidosPedidoToAttach.getIdPedido());
                attachedPedidos.add(pedidosPedidoToAttach);
            }
            despachante.setPedidos(attachedPedidos);
            List<ValorDespachante> attachedValoresDespachante = new ArrayList<ValorDespachante>();
            for (ValorDespachante valoresDespachanteValorDespachanteToAttach : despachante.getValoresDespachante()) {
                valoresDespachanteValorDespachanteToAttach = em.getReference(valoresDespachanteValorDespachanteToAttach.getClass(), valoresDespachanteValorDespachanteToAttach.getIdValorDespachante());
                attachedValoresDespachante.add(valoresDespachanteValorDespachanteToAttach);
            }
            despachante.setValoresDespachante(attachedValoresDespachante);
            em.persist(despachante);
            if (empresa != null) {
                empresa.getDespachanteList().add(despachante);
                empresa = em.merge(empresa);
            }
            for (Pedido pedidosPedido : despachante.getPedidos()) {
                Despachante oldDespachanteOfPedidosPedido = pedidosPedido.getDespachante();
                pedidosPedido.setDespachante(despachante);
                pedidosPedido = em.merge(pedidosPedido);
                if (oldDespachanteOfPedidosPedido != null) {
                    oldDespachanteOfPedidosPedido.getPedidos().remove(pedidosPedido);
                    oldDespachanteOfPedidosPedido = em.merge(oldDespachanteOfPedidosPedido);
                }
            }
            for (ValorDespachante valoresDespachanteValorDespachante : despachante.getValoresDespachante()) {
                Despachante oldDespachanteOfValoresDespachanteValorDespachante = valoresDespachanteValorDespachante.getDespachante();
                valoresDespachanteValorDespachante.setDespachante(despachante);
                valoresDespachanteValorDespachante = em.merge(valoresDespachanteValorDespachante);
                if (oldDespachanteOfValoresDespachanteValorDespachante != null) {
                    oldDespachanteOfValoresDespachanteValorDespachante.getValoresDespachante().remove(valoresDespachanteValorDespachante);
                    oldDespachanteOfValoresDespachanteValorDespachante = em.merge(oldDespachanteOfValoresDespachanteValorDespachante);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Despachante despachante) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Despachante persistentDespachante = em.find(Despachante.class, despachante.getIdDespachante());
            Empresa empresaOld = persistentDespachante.getEmpresa();
            Empresa empresaNew = despachante.getEmpresa();
            List<Pedido> pedidosOld = persistentDespachante.getPedidos();
            List<Pedido> pedidosNew = despachante.getPedidos();
            List<ValorDespachante> valoresDespachanteOld = persistentDespachante.getValoresDespachante();
            List<ValorDespachante> valoresDespachanteNew = despachante.getValoresDespachante();
            List<String> illegalOrphanMessages = null;
            for (Pedido pedidosOldPedido : pedidosOld) {
                if (!pedidosNew.contains(pedidosOldPedido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Pedido " + pedidosOldPedido + " since its despachante field is not nullable.");
                }
            }
            for (ValorDespachante valoresDespachanteOldValorDespachante : valoresDespachanteOld) {
                if (!valoresDespachanteNew.contains(valoresDespachanteOldValorDespachante)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ValorDespachante " + valoresDespachanteOldValorDespachante + " since its despachante field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (empresaNew != null) {
                empresaNew = em.getReference(empresaNew.getClass(), empresaNew.getIdEmpresa());
                despachante.setEmpresa(empresaNew);
            }
            List<Pedido> attachedPedidosNew = new ArrayList<Pedido>();
            for (Pedido pedidosNewPedidoToAttach : pedidosNew) {
                pedidosNewPedidoToAttach = em.getReference(pedidosNewPedidoToAttach.getClass(), pedidosNewPedidoToAttach.getIdPedido());
                attachedPedidosNew.add(pedidosNewPedidoToAttach);
            }
            pedidosNew = attachedPedidosNew;
            despachante.setPedidos(pedidosNew);
            List<ValorDespachante> attachedValoresDespachanteNew = new ArrayList<ValorDespachante>();
            for (ValorDespachante valoresDespachanteNewValorDespachanteToAttach : valoresDespachanteNew) {
                valoresDespachanteNewValorDespachanteToAttach = em.getReference(valoresDespachanteNewValorDespachanteToAttach.getClass(), valoresDespachanteNewValorDespachanteToAttach.getIdValorDespachante());
                attachedValoresDespachanteNew.add(valoresDespachanteNewValorDespachanteToAttach);
            }
            valoresDespachanteNew = attachedValoresDespachanteNew;
            despachante.setValoresDespachante(valoresDespachanteNew);
            despachante = em.merge(despachante);
            if (empresaOld != null && !empresaOld.equals(empresaNew)) {
                empresaOld.getDespachanteList().remove(despachante);
                empresaOld = em.merge(empresaOld);
            }
            if (empresaNew != null && !empresaNew.equals(empresaOld)) {
                empresaNew.getDespachanteList().add(despachante);
                empresaNew = em.merge(empresaNew);
            }
            for (Pedido pedidosNewPedido : pedidosNew) {
                if (!pedidosOld.contains(pedidosNewPedido)) {
                    Despachante oldDespachanteOfPedidosNewPedido = pedidosNewPedido.getDespachante();
                    pedidosNewPedido.setDespachante(despachante);
                    pedidosNewPedido = em.merge(pedidosNewPedido);
                    if (oldDespachanteOfPedidosNewPedido != null && !oldDespachanteOfPedidosNewPedido.equals(despachante)) {
                        oldDespachanteOfPedidosNewPedido.getPedidos().remove(pedidosNewPedido);
                        oldDespachanteOfPedidosNewPedido = em.merge(oldDespachanteOfPedidosNewPedido);
                    }
                }
            }
            for (ValorDespachante valoresDespachanteNewValorDespachante : valoresDespachanteNew) {
                if (!valoresDespachanteOld.contains(valoresDespachanteNewValorDespachante)) {
                    Despachante oldDespachanteOfValoresDespachanteNewValorDespachante = valoresDespachanteNewValorDespachante.getDespachante();
                    valoresDespachanteNewValorDespachante.setDespachante(despachante);
                    valoresDespachanteNewValorDespachante = em.merge(valoresDespachanteNewValorDespachante);
                    if (oldDespachanteOfValoresDespachanteNewValorDespachante != null && !oldDespachanteOfValoresDespachanteNewValorDespachante.equals(despachante)) {
                        oldDespachanteOfValoresDespachanteNewValorDespachante.getValoresDespachante().remove(valoresDespachanteNewValorDespachante);
                        oldDespachanteOfValoresDespachanteNewValorDespachante = em.merge(oldDespachanteOfValoresDespachanteNewValorDespachante);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = despachante.getIdDespachante();
                if (findDespachante(id) == null) {
                    throw new NonexistentEntityException("The despachante with id " + id + " no longer exists.");
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
            Despachante despachante;
            try {
                despachante = em.getReference(Despachante.class, id);
                despachante.getIdDespachante();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The despachante with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Pedido> pedidosOrphanCheck = despachante.getPedidos();
            for (Pedido pedidosOrphanCheckPedido : pedidosOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Despachante (" + despachante + ") cannot be destroyed since the Pedido " + pedidosOrphanCheckPedido + " in its pedidos field has a non-nullable despachante field.");
            }
            List<ValorDespachante> valoresDespachanteOrphanCheck = despachante.getValoresDespachante();
            for (ValorDespachante valoresDespachanteOrphanCheckValorDespachante : valoresDespachanteOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Despachante (" + despachante + ") cannot be destroyed since the ValorDespachante " + valoresDespachanteOrphanCheckValorDespachante + " in its valoresDespachante field has a non-nullable despachante field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Empresa empresa = despachante.getEmpresa();
            if (empresa != null) {
                empresa.getDespachanteList().remove(despachante);
                empresa = em.merge(empresa);
            }
            em.remove(despachante);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Despachante> findDespachanteEntities() {
        return findDespachanteEntities(true, -1, -1);
    }

    public List<Despachante> findDespachanteEntities(int maxResults, int firstResult) {
        return findDespachanteEntities(false, maxResults, firstResult);
    }

    private List<Despachante> findDespachanteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Despachante.class));
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

    public Despachante findDespachante(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Despachante.class, id);
        } finally {
            em.close();
        }
    }

    public int getDespachanteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Despachante> rt = cq.from(Despachante.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
