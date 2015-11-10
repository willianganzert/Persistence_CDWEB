/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.portaltrading.jpa;

import br.com.portaltrading.entidades.Usuario;
import br.com.portaltrading.jpa.exceptions.IllegalOrphanException;
import br.com.portaltrading.jpa.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.com.portaltrading.entidades.Funcionario;
import java.util.ArrayList;
import java.util.List;
import br.com.portaltrading.entidades.Cliente;

/**
 *
 * @author Willian
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) {
        if (usuario.getFuncionario() == null) {
            usuario.setFuncionario(new ArrayList<Funcionario>());
        }
        if (usuario.getCliente() == null) {
            usuario.setCliente(new ArrayList<Cliente>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Funcionario> attachedFuncionario = new ArrayList<Funcionario>();
            for (Funcionario funcionarioFuncionarioToAttach : usuario.getFuncionario()) {
                funcionarioFuncionarioToAttach = em.getReference(funcionarioFuncionarioToAttach.getClass(), funcionarioFuncionarioToAttach.getIdFuncionario());
                attachedFuncionario.add(funcionarioFuncionarioToAttach);
            }
            usuario.setFuncionario(attachedFuncionario);
            List<Cliente> attachedCliente = new ArrayList<Cliente>();
            for (Cliente clienteClienteToAttach : usuario.getCliente()) {
                clienteClienteToAttach = em.getReference(clienteClienteToAttach.getClass(), clienteClienteToAttach.getIdCliente());
                attachedCliente.add(clienteClienteToAttach);
            }
            usuario.setCliente(attachedCliente);
            em.persist(usuario);
            for (Funcionario funcionarioFuncionario : usuario.getFuncionario()) {
                Usuario oldUsuarioOfFuncionarioFuncionario = funcionarioFuncionario.getUsuario();
                funcionarioFuncionario.setUsuario(usuario);
                funcionarioFuncionario = em.merge(funcionarioFuncionario);
                if (oldUsuarioOfFuncionarioFuncionario != null) {
                    oldUsuarioOfFuncionarioFuncionario.getFuncionario().remove(funcionarioFuncionario);
                    oldUsuarioOfFuncionarioFuncionario = em.merge(oldUsuarioOfFuncionarioFuncionario);
                }
            }
            for (Cliente clienteCliente : usuario.getCliente()) {
                Usuario oldUsuarioOfClienteCliente = clienteCliente.getUsuario();
                clienteCliente.setUsuario(usuario);
                clienteCliente = em.merge(clienteCliente);
                if (oldUsuarioOfClienteCliente != null) {
                    oldUsuarioOfClienteCliente.getCliente().remove(clienteCliente);
                    oldUsuarioOfClienteCliente = em.merge(oldUsuarioOfClienteCliente);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getIdUsuario());
            List<Funcionario> funcionarioOld = persistentUsuario.getFuncionario();
            List<Funcionario> funcionarioNew = usuario.getFuncionario();
            List<Cliente> clienteOld = persistentUsuario.getCliente();
            List<Cliente> clienteNew = usuario.getCliente();
            List<String> illegalOrphanMessages = null;
            for (Funcionario funcionarioOldFuncionario : funcionarioOld) {
                if (!funcionarioNew.contains(funcionarioOldFuncionario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Funcionario " + funcionarioOldFuncionario + " since its usuario field is not nullable.");
                }
            }
            for (Cliente clienteOldCliente : clienteOld) {
                if (!clienteNew.contains(clienteOldCliente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cliente " + clienteOldCliente + " since its usuario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Funcionario> attachedFuncionarioNew = new ArrayList<Funcionario>();
            for (Funcionario funcionarioNewFuncionarioToAttach : funcionarioNew) {
                funcionarioNewFuncionarioToAttach = em.getReference(funcionarioNewFuncionarioToAttach.getClass(), funcionarioNewFuncionarioToAttach.getIdFuncionario());
                attachedFuncionarioNew.add(funcionarioNewFuncionarioToAttach);
            }
            funcionarioNew = attachedFuncionarioNew;
            usuario.setFuncionario(funcionarioNew);
            List<Cliente> attachedClienteNew = new ArrayList<Cliente>();
            for (Cliente clienteNewClienteToAttach : clienteNew) {
                clienteNewClienteToAttach = em.getReference(clienteNewClienteToAttach.getClass(), clienteNewClienteToAttach.getIdCliente());
                attachedClienteNew.add(clienteNewClienteToAttach);
            }
            clienteNew = attachedClienteNew;
            usuario.setCliente(clienteNew);
            usuario = em.merge(usuario);
            for (Funcionario funcionarioNewFuncionario : funcionarioNew) {
                if (!funcionarioOld.contains(funcionarioNewFuncionario)) {
                    Usuario oldUsuarioOfFuncionarioNewFuncionario = funcionarioNewFuncionario.getUsuario();
                    funcionarioNewFuncionario.setUsuario(usuario);
                    funcionarioNewFuncionario = em.merge(funcionarioNewFuncionario);
                    if (oldUsuarioOfFuncionarioNewFuncionario != null && !oldUsuarioOfFuncionarioNewFuncionario.equals(usuario)) {
                        oldUsuarioOfFuncionarioNewFuncionario.getFuncionario().remove(funcionarioNewFuncionario);
                        oldUsuarioOfFuncionarioNewFuncionario = em.merge(oldUsuarioOfFuncionarioNewFuncionario);
                    }
                }
            }
            for (Cliente clienteNewCliente : clienteNew) {
                if (!clienteOld.contains(clienteNewCliente)) {
                    Usuario oldUsuarioOfClienteNewCliente = clienteNewCliente.getUsuario();
                    clienteNewCliente.setUsuario(usuario);
                    clienteNewCliente = em.merge(clienteNewCliente);
                    if (oldUsuarioOfClienteNewCliente != null && !oldUsuarioOfClienteNewCliente.equals(usuario)) {
                        oldUsuarioOfClienteNewCliente.getCliente().remove(clienteNewCliente);
                        oldUsuarioOfClienteNewCliente = em.merge(oldUsuarioOfClienteNewCliente);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = usuario.getIdUsuario();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getIdUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Funcionario> funcionarioOrphanCheck = usuario.getFuncionario();
            for (Funcionario funcionarioOrphanCheckFuncionario : funcionarioOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Funcionario " + funcionarioOrphanCheckFuncionario + " in its funcionario field has a non-nullable usuario field.");
            }
            List<Cliente> clienteOrphanCheck = usuario.getCliente();
            for (Cliente clienteOrphanCheckCliente : clienteOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Cliente " + clienteOrphanCheckCliente + " in its cliente field has a non-nullable usuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
