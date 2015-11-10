/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.portaltrading.jpa;

import br.com.portaltrading.entidades.Manufaturado;
import br.com.portaltrading.jpa.exceptions.IllegalOrphanException;
import br.com.portaltrading.jpa.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.com.portaltrading.entidades.ItemPedido;
import java.util.ArrayList;
import java.util.List;
import br.com.portaltrading.entidades.ProdMan;

/**
 *
 * @author Willian
 */
public class ManufaturadoJpaController implements Serializable {

    public ManufaturadoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Manufaturado manufaturado) {
        if (manufaturado.getItemPedidoList() == null) {
            manufaturado.setItemPedidoList(new ArrayList<ItemPedido>());
        }
        if (manufaturado.getProdManList() == null) {
            manufaturado.setProdManList(new ArrayList<ProdMan>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<ItemPedido> attachedItemPedidoList = new ArrayList<ItemPedido>();
            for (ItemPedido itemPedidoListItemPedidoToAttach : manufaturado.getItemPedidoList()) {
                itemPedidoListItemPedidoToAttach = em.getReference(itemPedidoListItemPedidoToAttach.getClass(), itemPedidoListItemPedidoToAttach.getIdItemPedido());
                attachedItemPedidoList.add(itemPedidoListItemPedidoToAttach);
            }
            manufaturado.setItemPedidoList(attachedItemPedidoList);
            List<ProdMan> attachedProdManList = new ArrayList<ProdMan>();
            for (ProdMan prodManListProdManToAttach : manufaturado.getProdManList()) {
                prodManListProdManToAttach = em.getReference(prodManListProdManToAttach.getClass(), prodManListProdManToAttach.getIdProdMan());
                attachedProdManList.add(prodManListProdManToAttach);
            }
            manufaturado.setProdManList(attachedProdManList);
            em.persist(manufaturado);
            for (ItemPedido itemPedidoListItemPedido : manufaturado.getItemPedidoList()) {
                Manufaturado oldManufaturadoOfItemPedidoListItemPedido = itemPedidoListItemPedido.getManufaturado();
                itemPedidoListItemPedido.setManufaturado(manufaturado);
                itemPedidoListItemPedido = em.merge(itemPedidoListItemPedido);
                if (oldManufaturadoOfItemPedidoListItemPedido != null) {
                    oldManufaturadoOfItemPedidoListItemPedido.getItemPedidoList().remove(itemPedidoListItemPedido);
                    oldManufaturadoOfItemPedidoListItemPedido = em.merge(oldManufaturadoOfItemPedidoListItemPedido);
                }
            }
            for (ProdMan prodManListProdMan : manufaturado.getProdManList()) {
                Manufaturado oldManufaturadoOfProdManListProdMan = prodManListProdMan.getManufaturado();
                prodManListProdMan.setManufaturado(manufaturado);
                prodManListProdMan = em.merge(prodManListProdMan);
                if (oldManufaturadoOfProdManListProdMan != null) {
                    oldManufaturadoOfProdManListProdMan.getProdManList().remove(prodManListProdMan);
                    oldManufaturadoOfProdManListProdMan = em.merge(oldManufaturadoOfProdManListProdMan);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Manufaturado manufaturado) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Manufaturado persistentManufaturado = em.find(Manufaturado.class, manufaturado.getIdManufaturado());
            List<ItemPedido> itemPedidoListOld = persistentManufaturado.getItemPedidoList();
            List<ItemPedido> itemPedidoListNew = manufaturado.getItemPedidoList();
            List<ProdMan> prodManListOld = persistentManufaturado.getProdManList();
            List<ProdMan> prodManListNew = manufaturado.getProdManList();
            List<String> illegalOrphanMessages = null;
            for (ItemPedido itemPedidoListOldItemPedido : itemPedidoListOld) {
                if (!itemPedidoListNew.contains(itemPedidoListOldItemPedido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ItemPedido " + itemPedidoListOldItemPedido + " since its manufaturado field is not nullable.");
                }
            }
            for (ProdMan prodManListOldProdMan : prodManListOld) {
                if (!prodManListNew.contains(prodManListOldProdMan)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ProdMan " + prodManListOldProdMan + " since its manufaturado field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<ItemPedido> attachedItemPedidoListNew = new ArrayList<ItemPedido>();
            for (ItemPedido itemPedidoListNewItemPedidoToAttach : itemPedidoListNew) {
                itemPedidoListNewItemPedidoToAttach = em.getReference(itemPedidoListNewItemPedidoToAttach.getClass(), itemPedidoListNewItemPedidoToAttach.getIdItemPedido());
                attachedItemPedidoListNew.add(itemPedidoListNewItemPedidoToAttach);
            }
            itemPedidoListNew = attachedItemPedidoListNew;
            manufaturado.setItemPedidoList(itemPedidoListNew);
            List<ProdMan> attachedProdManListNew = new ArrayList<ProdMan>();
            for (ProdMan prodManListNewProdManToAttach : prodManListNew) {
                prodManListNewProdManToAttach = em.getReference(prodManListNewProdManToAttach.getClass(), prodManListNewProdManToAttach.getIdProdMan());
                attachedProdManListNew.add(prodManListNewProdManToAttach);
            }
            prodManListNew = attachedProdManListNew;
            manufaturado.setProdManList(prodManListNew);
            manufaturado = em.merge(manufaturado);
            for (ItemPedido itemPedidoListNewItemPedido : itemPedidoListNew) {
                if (!itemPedidoListOld.contains(itemPedidoListNewItemPedido)) {
                    Manufaturado oldManufaturadoOfItemPedidoListNewItemPedido = itemPedidoListNewItemPedido.getManufaturado();
                    itemPedidoListNewItemPedido.setManufaturado(manufaturado);
                    itemPedidoListNewItemPedido = em.merge(itemPedidoListNewItemPedido);
                    if (oldManufaturadoOfItemPedidoListNewItemPedido != null && !oldManufaturadoOfItemPedidoListNewItemPedido.equals(manufaturado)) {
                        oldManufaturadoOfItemPedidoListNewItemPedido.getItemPedidoList().remove(itemPedidoListNewItemPedido);
                        oldManufaturadoOfItemPedidoListNewItemPedido = em.merge(oldManufaturadoOfItemPedidoListNewItemPedido);
                    }
                }
            }
            for (ProdMan prodManListNewProdMan : prodManListNew) {
                if (!prodManListOld.contains(prodManListNewProdMan)) {
                    Manufaturado oldManufaturadoOfProdManListNewProdMan = prodManListNewProdMan.getManufaturado();
                    prodManListNewProdMan.setManufaturado(manufaturado);
                    prodManListNewProdMan = em.merge(prodManListNewProdMan);
                    if (oldManufaturadoOfProdManListNewProdMan != null && !oldManufaturadoOfProdManListNewProdMan.equals(manufaturado)) {
                        oldManufaturadoOfProdManListNewProdMan.getProdManList().remove(prodManListNewProdMan);
                        oldManufaturadoOfProdManListNewProdMan = em.merge(oldManufaturadoOfProdManListNewProdMan);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = manufaturado.getIdManufaturado();
                if (findManufaturado(id) == null) {
                    throw new NonexistentEntityException("The manufaturado with id " + id + " no longer exists.");
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
            Manufaturado manufaturado;
            try {
                manufaturado = em.getReference(Manufaturado.class, id);
                manufaturado.getIdManufaturado();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The manufaturado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ItemPedido> itemPedidoListOrphanCheck = manufaturado.getItemPedidoList();
            for (ItemPedido itemPedidoListOrphanCheckItemPedido : itemPedidoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Manufaturado (" + manufaturado + ") cannot be destroyed since the ItemPedido " + itemPedidoListOrphanCheckItemPedido + " in its itemPedidoList field has a non-nullable manufaturado field.");
            }
            List<ProdMan> prodManListOrphanCheck = manufaturado.getProdManList();
            for (ProdMan prodManListOrphanCheckProdMan : prodManListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Manufaturado (" + manufaturado + ") cannot be destroyed since the ProdMan " + prodManListOrphanCheckProdMan + " in its prodManList field has a non-nullable manufaturado field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(manufaturado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Manufaturado> findManufaturadoEntities() {
        return findManufaturadoEntities(true, -1, -1);
    }

    public List<Manufaturado> findManufaturadoEntities(int maxResults, int firstResult) {
        return findManufaturadoEntities(false, maxResults, firstResult);
    }

    private List<Manufaturado> findManufaturadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Manufaturado.class));
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

    public Manufaturado findManufaturado(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Manufaturado.class, id);
        } finally {
            em.close();
        }
    }

    public int getManufaturadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Manufaturado> rt = cq.from(Manufaturado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
