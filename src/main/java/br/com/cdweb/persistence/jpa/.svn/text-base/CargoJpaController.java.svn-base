/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.portaltrading.jpa;

import br.com.portaltrading.entidades.Cargo;
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

/**
 *
 * @author Willian
 */
public class CargoJpaController implements Serializable {

    public CargoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cargo cargo) {
        if (cargo.getFuncionarios() == null) {
            cargo.setFuncionarios(new ArrayList<Funcionario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Funcionario> attachedFuncionarios = new ArrayList<Funcionario>();
            for (Funcionario funcionariosFuncionarioToAttach : cargo.getFuncionarios()) {
                funcionariosFuncionarioToAttach = em.getReference(funcionariosFuncionarioToAttach.getClass(), funcionariosFuncionarioToAttach.getIdFuncionario());
                attachedFuncionarios.add(funcionariosFuncionarioToAttach);
            }
            cargo.setFuncionarios(attachedFuncionarios);
            em.persist(cargo);
            for (Funcionario funcionariosFuncionario : cargo.getFuncionarios()) {
                Cargo oldCargoOfFuncionariosFuncionario = funcionariosFuncionario.getCargo();
                funcionariosFuncionario.setCargo(cargo);
                funcionariosFuncionario = em.merge(funcionariosFuncionario);
                if (oldCargoOfFuncionariosFuncionario != null) {
                    oldCargoOfFuncionariosFuncionario.getFuncionarios().remove(funcionariosFuncionario);
                    oldCargoOfFuncionariosFuncionario = em.merge(oldCargoOfFuncionariosFuncionario);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cargo cargo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cargo persistentCargo = em.find(Cargo.class, cargo.getIdCargo());
            List<Funcionario> funcionariosOld = persistentCargo.getFuncionarios();
            List<Funcionario> funcionariosNew = cargo.getFuncionarios();
            List<String> illegalOrphanMessages = null;
            for (Funcionario funcionariosOldFuncionario : funcionariosOld) {
                if (!funcionariosNew.contains(funcionariosOldFuncionario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Funcionario " + funcionariosOldFuncionario + " since its cargo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Funcionario> attachedFuncionariosNew = new ArrayList<Funcionario>();
            for (Funcionario funcionariosNewFuncionarioToAttach : funcionariosNew) {
                funcionariosNewFuncionarioToAttach = em.getReference(funcionariosNewFuncionarioToAttach.getClass(), funcionariosNewFuncionarioToAttach.getIdFuncionario());
                attachedFuncionariosNew.add(funcionariosNewFuncionarioToAttach);
            }
            funcionariosNew = attachedFuncionariosNew;
            cargo.setFuncionarios(funcionariosNew);
            cargo = em.merge(cargo);
            for (Funcionario funcionariosNewFuncionario : funcionariosNew) {
                if (!funcionariosOld.contains(funcionariosNewFuncionario)) {
                    Cargo oldCargoOfFuncionariosNewFuncionario = funcionariosNewFuncionario.getCargo();
                    funcionariosNewFuncionario.setCargo(cargo);
                    funcionariosNewFuncionario = em.merge(funcionariosNewFuncionario);
                    if (oldCargoOfFuncionariosNewFuncionario != null && !oldCargoOfFuncionariosNewFuncionario.equals(cargo)) {
                        oldCargoOfFuncionariosNewFuncionario.getFuncionarios().remove(funcionariosNewFuncionario);
                        oldCargoOfFuncionariosNewFuncionario = em.merge(oldCargoOfFuncionariosNewFuncionario);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = cargo.getIdCargo();
                if (findCargo(id) == null) {
                    throw new NonexistentEntityException("The cargo with id " + id + " no longer exists.");
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
            Cargo cargo;
            try {
                cargo = em.getReference(Cargo.class, id);
                cargo.getIdCargo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cargo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Funcionario> funcionariosOrphanCheck = cargo.getFuncionarios();
            for (Funcionario funcionariosOrphanCheckFuncionario : funcionariosOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cargo (" + cargo + ") cannot be destroyed since the Funcionario " + funcionariosOrphanCheckFuncionario + " in its funcionarios field has a non-nullable cargo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(cargo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cargo> findCargoEntities() {
        return findCargoEntities(true, -1, -1);
    }

    public List<Cargo> findCargoEntities(int maxResults, int firstResult) {
        return findCargoEntities(false, maxResults, firstResult);
    }

    private List<Cargo> findCargoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cargo.class));
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

    public Cargo findCargo(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cargo.class, id);
        } finally {
            em.close();
        }
    }

    public int getCargoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cargo> rt = cq.from(Cargo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
