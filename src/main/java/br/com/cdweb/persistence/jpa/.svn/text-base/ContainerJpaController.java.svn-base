/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.portaltrading.jpa;

import br.com.portaltrading.entidades.Container;
import br.com.portaltrading.jpa.exceptions.IllegalOrphanException;
import br.com.portaltrading.jpa.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.com.portaltrading.entidades.RepMaritimo;
import br.com.portaltrading.entidades.ContainerPedido;
import java.util.ArrayList;
import java.util.List;
import br.com.portaltrading.entidades.ValorContainer;

/**
 *
 * @author Willian
 */
public class ContainerJpaController implements Serializable {

    public ContainerJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Container container) {
        if (container.getContainerPedidoList() == null) {
            container.setContainerPedidoList(new ArrayList<ContainerPedido>());
        }
        if (container.getValoresContainer() == null) {
            container.setValoresContainer(new ArrayList<ValorContainer>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RepMaritimo repMaritimo = container.getRepMaritimo();
            if (repMaritimo != null) {
                repMaritimo = em.getReference(repMaritimo.getClass(), repMaritimo.getIdRepMaritimo());
                container.setRepMaritimo(repMaritimo);
            }
            List<ContainerPedido> attachedContainerPedidoList = new ArrayList<ContainerPedido>();
            for (ContainerPedido containerPedidoListContainerPedidoToAttach : container.getContainerPedidoList()) {
                containerPedidoListContainerPedidoToAttach = em.getReference(containerPedidoListContainerPedidoToAttach.getClass(), containerPedidoListContainerPedidoToAttach.getIdContainerPedido());
                attachedContainerPedidoList.add(containerPedidoListContainerPedidoToAttach);
            }
            container.setContainerPedidoList(attachedContainerPedidoList);
            List<ValorContainer> attachedValoresContainer = new ArrayList<ValorContainer>();
            for (ValorContainer valoresContainerValorContainerToAttach : container.getValoresContainer()) {
                valoresContainerValorContainerToAttach = em.merge(valoresContainerValorContainerToAttach);
                attachedValoresContainer.add(valoresContainerValorContainerToAttach);
            }
            container.setValoresContainer(attachedValoresContainer);
            em.persist(container);
            if (repMaritimo != null) {
                repMaritimo.getContaineres().add(container);
                repMaritimo = em.merge(repMaritimo);
            }
            for (ContainerPedido containerPedidoListContainerPedido : container.getContainerPedidoList()) {
                Container oldContainerOfContainerPedidoListContainerPedido = containerPedidoListContainerPedido.getContainer();
                containerPedidoListContainerPedido.setContainer(container);
                containerPedidoListContainerPedido = em.merge(containerPedidoListContainerPedido);
                if (oldContainerOfContainerPedidoListContainerPedido != null) {
                    oldContainerOfContainerPedidoListContainerPedido.getContainerPedidoList().remove(containerPedidoListContainerPedido);
                    oldContainerOfContainerPedidoListContainerPedido = em.merge(oldContainerOfContainerPedidoListContainerPedido);
                }
            }
            for (ValorContainer valoresContainerValorContainer : container.getValoresContainer()) {
                Container oldContainerOfValoresContainerValorContainer = valoresContainerValorContainer.getContainer();
                valoresContainerValorContainer.setContainer(container);
                valoresContainerValorContainer = em.merge(valoresContainerValorContainer);
                if (oldContainerOfValoresContainerValorContainer != null) {
                    oldContainerOfValoresContainerValorContainer.getValoresContainer().remove(valoresContainerValorContainer);
                    oldContainerOfValoresContainerValorContainer = em.merge(oldContainerOfValoresContainerValorContainer);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Container container) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Container persistentContainer = em.find(Container.class, container.getIdContainer());
            RepMaritimo repMaritimoOld = persistentContainer.getRepMaritimo();
            RepMaritimo repMaritimoNew = container.getRepMaritimo();
            List<ContainerPedido> containerPedidoListOld = persistentContainer.getContainerPedidoList();
            List<ContainerPedido> containerPedidoListNew = container.getContainerPedidoList();
            List<ValorContainer> valoresContainerOld = persistentContainer.getValoresContainer();
            List<ValorContainer> valoresContainerNew = container.getValoresContainer();
            List<String> illegalOrphanMessages = null;
            for (ContainerPedido containerPedidoListOldContainerPedido : containerPedidoListOld) {
                if (!containerPedidoListNew.contains(containerPedidoListOldContainerPedido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ContainerPedido " + containerPedidoListOldContainerPedido + " since its container field is not nullable.");
                }
            }
            for (ValorContainer valoresContainerOldValorContainer : valoresContainerOld) {
                if (!valoresContainerNew.contains(valoresContainerOldValorContainer)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ValorContainer " + valoresContainerOldValorContainer + " since its container field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (repMaritimoNew != null) {
                repMaritimoNew = em.getReference(repMaritimoNew.getClass(), repMaritimoNew.getIdRepMaritimo());
                container.setRepMaritimo(repMaritimoNew);
            }
            List<ContainerPedido> attachedContainerPedidoListNew = new ArrayList<ContainerPedido>();
            for (ContainerPedido containerPedidoListNewContainerPedidoToAttach : containerPedidoListNew) {
                containerPedidoListNewContainerPedidoToAttach = em.getReference(containerPedidoListNewContainerPedidoToAttach.getClass(), containerPedidoListNewContainerPedidoToAttach.getIdContainerPedido());
                attachedContainerPedidoListNew.add(containerPedidoListNewContainerPedidoToAttach);
            }
            containerPedidoListNew = attachedContainerPedidoListNew;
            container.setContainerPedidoList(containerPedidoListNew);
            List<ValorContainer> attachedValoresContainerNew = new ArrayList<ValorContainer>();
            for (ValorContainer valoresContainerNewValorContainerToAttach : valoresContainerNew) {
                valoresContainerNewValorContainerToAttach = em.merge(valoresContainerNewValorContainerToAttach);
                attachedValoresContainerNew.add(valoresContainerNewValorContainerToAttach);
            }
            valoresContainerNew = attachedValoresContainerNew;
            container.setValoresContainer(valoresContainerNew);
            container = em.merge(container);
            if (repMaritimoOld != null && !repMaritimoOld.equals(repMaritimoNew)) {
                repMaritimoOld.getContaineres().remove(container);
                repMaritimoOld = em.merge(repMaritimoOld);
            }
            if (repMaritimoNew != null && !repMaritimoNew.equals(repMaritimoOld)) {
                repMaritimoNew.getContaineres().add(container);
                repMaritimoNew = em.merge(repMaritimoNew);
            }
            for (ContainerPedido containerPedidoListNewContainerPedido : containerPedidoListNew) {
                if (!containerPedidoListOld.contains(containerPedidoListNewContainerPedido)) {
                    Container oldContainerOfContainerPedidoListNewContainerPedido = containerPedidoListNewContainerPedido.getContainer();
                    containerPedidoListNewContainerPedido.setContainer(container);
                    containerPedidoListNewContainerPedido = em.merge(containerPedidoListNewContainerPedido);
                    if (oldContainerOfContainerPedidoListNewContainerPedido != null && !oldContainerOfContainerPedidoListNewContainerPedido.equals(container)) {
                        oldContainerOfContainerPedidoListNewContainerPedido.getContainerPedidoList().remove(containerPedidoListNewContainerPedido);
                        oldContainerOfContainerPedidoListNewContainerPedido = em.merge(oldContainerOfContainerPedidoListNewContainerPedido);
                    }
                }
            }
            for (ValorContainer valoresContainerNewValorContainer : valoresContainerNew) {
                if (!valoresContainerOld.contains(valoresContainerNewValorContainer)) {
                    Container oldContainerOfValoresContainerNewValorContainer = valoresContainerNewValorContainer.getContainer();
                    valoresContainerNewValorContainer.setContainer(container);
                    valoresContainerNewValorContainer = em.merge(valoresContainerNewValorContainer);
                    if (oldContainerOfValoresContainerNewValorContainer != null && !oldContainerOfValoresContainerNewValorContainer.equals(container)) {
                        oldContainerOfValoresContainerNewValorContainer.getValoresContainer().remove(valoresContainerNewValorContainer);
                        oldContainerOfValoresContainerNewValorContainer = em.merge(oldContainerOfValoresContainerNewValorContainer);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = container.getIdContainer();
                if (findContainer(id) == null) {
                    throw new NonexistentEntityException("The container with id " + id + " no longer exists.");
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
            Container container;
            try {
                container = em.getReference(Container.class, id);
                container.getIdContainer();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The container with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ContainerPedido> containerPedidoListOrphanCheck = container.getContainerPedidoList();
            for (ContainerPedido containerPedidoListOrphanCheckContainerPedido : containerPedidoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Container (" + container + ") cannot be destroyed since the ContainerPedido " + containerPedidoListOrphanCheckContainerPedido + " in its containerPedidoList field has a non-nullable container field.");
            }
            List<ValorContainer> valoresContainerOrphanCheck = container.getValoresContainer();
            for (ValorContainer valoresContainerOrphanCheckValorContainer : valoresContainerOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Container (" + container + ") cannot be destroyed since the ValorContainer " + valoresContainerOrphanCheckValorContainer + " in its valoresContainer field has a non-nullable container field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            RepMaritimo repMaritimo = container.getRepMaritimo();
            if (repMaritimo != null) {
                repMaritimo.getContaineres().remove(container);
                repMaritimo = em.merge(repMaritimo);
            }
            em.remove(container);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Container> findContainerEntities() {
        return findContainerEntities(true, -1, -1);
    }

    public List<Container> findContainerEntities(int maxResults, int firstResult) {
        return findContainerEntities(false, maxResults, firstResult);
    }

    private List<Container> findContainerEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Container.class));
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

    public Container findContainer(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Container.class, id);
        } finally {
            em.close();
        }
    }

    public int getContainerCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Container> rt = cq.from(Container.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
