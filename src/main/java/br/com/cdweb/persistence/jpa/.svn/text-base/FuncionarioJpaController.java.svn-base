/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.portaltrading.jpa;

import br.com.portaltrading.entidades.Funcionario;
import br.com.portaltrading.jpa.exceptions.IllegalOrphanException;
import br.com.portaltrading.jpa.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.com.portaltrading.entidades.Usuario;
import br.com.portaltrading.entidades.Cargo;
import br.com.portaltrading.entidades.Pedido;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Willian
 */
public class FuncionarioJpaController implements Serializable {

    public FuncionarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Funcionario funcionario) {
        if (funcionario.getPedidos() == null) {
            funcionario.setPedidos(new ArrayList<Pedido>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuario = funcionario.getUsuario();
            if (usuario != null) {
                usuario = em.getReference(usuario.getClass(), usuario.getIdUsuario());
                funcionario.setUsuario(usuario);
            }
            Cargo cargo = funcionario.getCargo();
            if (cargo != null) {
                cargo = em.getReference(cargo.getClass(), cargo.getIdCargo());
                funcionario.setCargo(cargo);
            }
            List<Pedido> attachedPedidos = new ArrayList<Pedido>();
            for (Pedido pedidosPedidoToAttach : funcionario.getPedidos()) {
                pedidosPedidoToAttach = em.getReference(pedidosPedidoToAttach.getClass(), pedidosPedidoToAttach.getIdPedido());
                attachedPedidos.add(pedidosPedidoToAttach);
            }
            funcionario.setPedidos(attachedPedidos);
            em.persist(funcionario);
            if (usuario != null) {
                usuario.getFuncionario().add(funcionario);
                usuario = em.merge(usuario);
            }
            if (cargo != null) {
                cargo.getFuncionarios().add(funcionario);
                cargo = em.merge(cargo);
            }
            for (Pedido pedidosPedido : funcionario.getPedidos()) {
                Funcionario oldFuncionarioOfPedidosPedido = pedidosPedido.getFuncionario();
                pedidosPedido.setFuncionario(funcionario);
                pedidosPedido = em.merge(pedidosPedido);
                if (oldFuncionarioOfPedidosPedido != null) {
                    oldFuncionarioOfPedidosPedido.getPedidos().remove(pedidosPedido);
                    oldFuncionarioOfPedidosPedido = em.merge(oldFuncionarioOfPedidosPedido);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Funcionario funcionario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Funcionario persistentFuncionario = em.find(Funcionario.class, funcionario.getIdFuncionario());
            Usuario usuarioOld = persistentFuncionario.getUsuario();
            Usuario usuarioNew = funcionario.getUsuario();
            Cargo cargoOld = persistentFuncionario.getCargo();
            Cargo cargoNew = funcionario.getCargo();
            List<Pedido> pedidosOld = persistentFuncionario.getPedidos();
            List<Pedido> pedidosNew = funcionario.getPedidos();
            List<String> illegalOrphanMessages = null;
            for (Pedido pedidosOldPedido : pedidosOld) {
                if (!pedidosNew.contains(pedidosOldPedido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Pedido " + pedidosOldPedido + " since its funcionario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (usuarioNew != null) {
                usuarioNew = em.getReference(usuarioNew.getClass(), usuarioNew.getIdUsuario());
                funcionario.setUsuario(usuarioNew);
            }
            if (cargoNew != null) {
                cargoNew = em.getReference(cargoNew.getClass(), cargoNew.getIdCargo());
                funcionario.setCargo(cargoNew);
            }
            List<Pedido> attachedPedidosNew = new ArrayList<Pedido>();
            for (Pedido pedidosNewPedidoToAttach : pedidosNew) {
                pedidosNewPedidoToAttach = em.getReference(pedidosNewPedidoToAttach.getClass(), pedidosNewPedidoToAttach.getIdPedido());
                attachedPedidosNew.add(pedidosNewPedidoToAttach);
            }
            pedidosNew = attachedPedidosNew;
            funcionario.setPedidos(pedidosNew);
            funcionario = em.merge(funcionario);
            if (usuarioOld != null && !usuarioOld.equals(usuarioNew)) {
                usuarioOld.getFuncionario().remove(funcionario);
                usuarioOld = em.merge(usuarioOld);
            }
            if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
                usuarioNew.getFuncionario().add(funcionario);
                usuarioNew = em.merge(usuarioNew);
            }
            if (cargoOld != null && !cargoOld.equals(cargoNew)) {
                cargoOld.getFuncionarios().remove(funcionario);
                cargoOld = em.merge(cargoOld);
            }
            if (cargoNew != null && !cargoNew.equals(cargoOld)) {
                cargoNew.getFuncionarios().add(funcionario);
                cargoNew = em.merge(cargoNew);
            }
            for (Pedido pedidosNewPedido : pedidosNew) {
                if (!pedidosOld.contains(pedidosNewPedido)) {
                    Funcionario oldFuncionarioOfPedidosNewPedido = pedidosNewPedido.getFuncionario();
                    pedidosNewPedido.setFuncionario(funcionario);
                    pedidosNewPedido = em.merge(pedidosNewPedido);
                    if (oldFuncionarioOfPedidosNewPedido != null && !oldFuncionarioOfPedidosNewPedido.equals(funcionario)) {
                        oldFuncionarioOfPedidosNewPedido.getPedidos().remove(pedidosNewPedido);
                        oldFuncionarioOfPedidosNewPedido = em.merge(oldFuncionarioOfPedidosNewPedido);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = funcionario.getIdFuncionario();
                if (findFuncionario(id) == null) {
                    throw new NonexistentEntityException("The funcionario with id " + id + " no longer exists.");
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
            Funcionario funcionario;
            try {
                funcionario = em.getReference(Funcionario.class, id);
                funcionario.getIdFuncionario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The funcionario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Pedido> pedidosOrphanCheck = funcionario.getPedidos();
            for (Pedido pedidosOrphanCheckPedido : pedidosOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Funcionario (" + funcionario + ") cannot be destroyed since the Pedido " + pedidosOrphanCheckPedido + " in its pedidos field has a non-nullable funcionario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Usuario usuario = funcionario.getUsuario();
            if (usuario != null) {
                usuario.getFuncionario().remove(funcionario);
                usuario = em.merge(usuario);
            }
            Cargo cargo = funcionario.getCargo();
            if (cargo != null) {
                cargo.getFuncionarios().remove(funcionario);
                cargo = em.merge(cargo);
            }
            em.remove(funcionario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Funcionario> findFuncionarioEntities() {
        return findFuncionarioEntities(true, -1, -1);
    }

    public List<Funcionario> findFuncionarioEntities(int maxResults, int firstResult) {
        return findFuncionarioEntities(false, maxResults, firstResult);
    }

    private List<Funcionario> findFuncionarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Funcionario.class));
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

    public Funcionario findFuncionario(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Funcionario.class, id);
        } finally {
            em.close();
        }
    }

    public int getFuncionarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Funcionario> rt = cq.from(Funcionario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
