/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.portaltrading.jpa;

import br.com.portaltrading.entidades.Pedido;
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
import br.com.portaltrading.entidades.Despachante;
import br.com.portaltrading.entidades.Cliente;
import br.com.portaltrading.entidades.CaminhaoPedido;
import java.util.ArrayList;
import java.util.List;
import br.com.portaltrading.entidades.ItemPedido;
import br.com.portaltrading.entidades.ContainerPedido;
import br.com.portaltrading.entidades.ProdutoValidado;

/**
 *
 * @author Willian
 */
public class PedidoJpaController implements Serializable {

    public PedidoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pedido pedido) {
        if (pedido.getCaminhoesPedido() == null) {
            pedido.setCaminhoesPedido(new ArrayList<CaminhaoPedido>());
        }
        if (pedido.getItensPedido() == null) {
            pedido.setItensPedido(new ArrayList<ItemPedido>());
        }
        if (pedido.getContaineresPedido() == null) {
            pedido.setContaineresPedido(new ArrayList<ContainerPedido>());
        }
        if (pedido.getProdutosValidados() == null) {
            pedido.setProdutosValidados(new ArrayList<ProdutoValidado>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Funcionario funcionario = pedido.getFuncionario();
            if (funcionario != null) {
                funcionario = em.getReference(funcionario.getClass(), funcionario.getIdFuncionario());
                pedido.setFuncionario(funcionario);
            }
            Despachante despachante = pedido.getDespachante();
            if (despachante != null) {
                despachante = em.getReference(despachante.getClass(), despachante.getIdDespachante());
                pedido.setDespachante(despachante);
            }
            Cliente cliente = pedido.getCliente();
            if (cliente != null) {
                cliente = em.getReference(cliente.getClass(), cliente.getIdCliente());
                pedido.setCliente(cliente);
            }
            List<CaminhaoPedido> attachedCaminhoesPedido = new ArrayList<CaminhaoPedido>();
            for (CaminhaoPedido caminhoesPedidoCaminhaoPedidoToAttach : pedido.getCaminhoesPedido()) {
                caminhoesPedidoCaminhaoPedidoToAttach = em.getReference(caminhoesPedidoCaminhaoPedidoToAttach.getClass(), caminhoesPedidoCaminhaoPedidoToAttach.getIdCaminhaoPedido());
                attachedCaminhoesPedido.add(caminhoesPedidoCaminhaoPedidoToAttach);
            }
            pedido.setCaminhoesPedido(attachedCaminhoesPedido);
            List<ItemPedido> attachedItensPedido = new ArrayList<ItemPedido>();
            for (ItemPedido itensPedidoItemPedidoToAttach : pedido.getItensPedido()) {
                itensPedidoItemPedidoToAttach = em.getReference(itensPedidoItemPedidoToAttach.getClass(), itensPedidoItemPedidoToAttach.getIdItemPedido());
                attachedItensPedido.add(itensPedidoItemPedidoToAttach);
            }
            pedido.setItensPedido(attachedItensPedido);
            List<ContainerPedido> attachedContaineresPedido = new ArrayList<ContainerPedido>();
            for (ContainerPedido containeresPedidoContainerPedidoToAttach : pedido.getContaineresPedido()) {
                containeresPedidoContainerPedidoToAttach = em.getReference(containeresPedidoContainerPedidoToAttach.getClass(), containeresPedidoContainerPedidoToAttach.getIdContainerPedido());
                attachedContaineresPedido.add(containeresPedidoContainerPedidoToAttach);
            }
            pedido.setContaineresPedido(attachedContaineresPedido);
            List<ProdutoValidado> attachedProdutosValidados = new ArrayList<ProdutoValidado>();
            for (ProdutoValidado produtosValidadosProdutoValidadoToAttach : pedido.getProdutosValidados()) {
                produtosValidadosProdutoValidadoToAttach = em.getReference(produtosValidadosProdutoValidadoToAttach.getClass(), produtosValidadosProdutoValidadoToAttach.getIdProdutoValidado());
                attachedProdutosValidados.add(produtosValidadosProdutoValidadoToAttach);
            }
            pedido.setProdutosValidados(attachedProdutosValidados);
            em.persist(pedido);
            if (funcionario != null) {
                funcionario.getPedidos().add(pedido);
                funcionario = em.merge(funcionario);
            }
            if (despachante != null) {
                despachante.getPedidos().add(pedido);
                despachante = em.merge(despachante);
            }
            if (cliente != null) {
                cliente.getPedidos().add(pedido);
                cliente = em.merge(cliente);
            }
            for (CaminhaoPedido caminhoesPedidoCaminhaoPedido : pedido.getCaminhoesPedido()) {
                Pedido oldPedidoOfCaminhoesPedidoCaminhaoPedido = caminhoesPedidoCaminhaoPedido.getPedido();
                caminhoesPedidoCaminhaoPedido.setPedido(pedido);
                caminhoesPedidoCaminhaoPedido = em.merge(caminhoesPedidoCaminhaoPedido);
                if (oldPedidoOfCaminhoesPedidoCaminhaoPedido != null) {
                    oldPedidoOfCaminhoesPedidoCaminhaoPedido.getCaminhoesPedido().remove(caminhoesPedidoCaminhaoPedido);
                    oldPedidoOfCaminhoesPedidoCaminhaoPedido = em.merge(oldPedidoOfCaminhoesPedidoCaminhaoPedido);
                }
            }
            for (ItemPedido itensPedidoItemPedido : pedido.getItensPedido()) {
                Pedido oldPedidoOfItensPedidoItemPedido = itensPedidoItemPedido.getPedido();
                itensPedidoItemPedido.setPedido(pedido);
                itensPedidoItemPedido = em.merge(itensPedidoItemPedido);
                if (oldPedidoOfItensPedidoItemPedido != null) {
                    oldPedidoOfItensPedidoItemPedido.getItensPedido().remove(itensPedidoItemPedido);
                    oldPedidoOfItensPedidoItemPedido = em.merge(oldPedidoOfItensPedidoItemPedido);
                }
            }
            for (ContainerPedido containeresPedidoContainerPedido : pedido.getContaineresPedido()) {
                Pedido oldPedidoOfContaineresPedidoContainerPedido = containeresPedidoContainerPedido.getPedido();
                containeresPedidoContainerPedido.setPedido(pedido);
                containeresPedidoContainerPedido = em.merge(containeresPedidoContainerPedido);
                if (oldPedidoOfContaineresPedidoContainerPedido != null) {
                    oldPedidoOfContaineresPedidoContainerPedido.getContaineresPedido().remove(containeresPedidoContainerPedido);
                    oldPedidoOfContaineresPedidoContainerPedido = em.merge(oldPedidoOfContaineresPedidoContainerPedido);
                }
            }
            for (ProdutoValidado produtosValidadosProdutoValidado : pedido.getProdutosValidados()) {
                Pedido oldPedidoOfProdutosValidadosProdutoValidado = produtosValidadosProdutoValidado.getPedido();
                produtosValidadosProdutoValidado.setPedido(pedido);
                produtosValidadosProdutoValidado = em.merge(produtosValidadosProdutoValidado);
                if (oldPedidoOfProdutosValidadosProdutoValidado != null) {
                    oldPedidoOfProdutosValidadosProdutoValidado.getProdutosValidados().remove(produtosValidadosProdutoValidado);
                    oldPedidoOfProdutosValidadosProdutoValidado = em.merge(oldPedidoOfProdutosValidadosProdutoValidado);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pedido pedido) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pedido persistentPedido = em.find(Pedido.class, pedido.getIdPedido());
            Funcionario funcionarioOld = persistentPedido.getFuncionario();
            Funcionario funcionarioNew = pedido.getFuncionario();
            Despachante despachanteOld = persistentPedido.getDespachante();
            Despachante despachanteNew = pedido.getDespachante();
            Cliente clienteOld = persistentPedido.getCliente();
            Cliente clienteNew = pedido.getCliente();
            List<CaminhaoPedido> caminhoesPedidoOld = persistentPedido.getCaminhoesPedido();
            List<CaminhaoPedido> caminhoesPedidoNew = pedido.getCaminhoesPedido();
            List<ItemPedido> itensPedidoOld = persistentPedido.getItensPedido();
            List<ItemPedido> itensPedidoNew = pedido.getItensPedido();
            List<ContainerPedido> containeresPedidoOld = persistentPedido.getContaineresPedido();
            List<ContainerPedido> containeresPedidoNew = pedido.getContaineresPedido();
            List<ProdutoValidado> produtosValidadosOld = persistentPedido.getProdutosValidados();
            List<ProdutoValidado> produtosValidadosNew = pedido.getProdutosValidados();
            List<String> illegalOrphanMessages = null;
            for (CaminhaoPedido caminhoesPedidoOldCaminhaoPedido : caminhoesPedidoOld) {
                if (!caminhoesPedidoNew.contains(caminhoesPedidoOldCaminhaoPedido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CaminhaoPedido " + caminhoesPedidoOldCaminhaoPedido + " since its pedido field is not nullable.");
                }
            }
            for (ItemPedido itensPedidoOldItemPedido : itensPedidoOld) {
                if (!itensPedidoNew.contains(itensPedidoOldItemPedido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ItemPedido " + itensPedidoOldItemPedido + " since its pedido field is not nullable.");
                }
            }
            for (ContainerPedido containeresPedidoOldContainerPedido : containeresPedidoOld) {
                if (!containeresPedidoNew.contains(containeresPedidoOldContainerPedido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ContainerPedido " + containeresPedidoOldContainerPedido + " since its pedido field is not nullable.");
                }
            }
            for (ProdutoValidado produtosValidadosOldProdutoValidado : produtosValidadosOld) {
                if (!produtosValidadosNew.contains(produtosValidadosOldProdutoValidado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ProdutoValidado " + produtosValidadosOldProdutoValidado + " since its pedido field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (funcionarioNew != null) {
                funcionarioNew = em.getReference(funcionarioNew.getClass(), funcionarioNew.getIdFuncionario());
                pedido.setFuncionario(funcionarioNew);
            }
            if (despachanteNew != null) {
                despachanteNew = em.getReference(despachanteNew.getClass(), despachanteNew.getIdDespachante());
                pedido.setDespachante(despachanteNew);
            }
            if (clienteNew != null) {
                clienteNew = em.getReference(clienteNew.getClass(), clienteNew.getIdCliente());
                pedido.setCliente(clienteNew);
            }
            List<CaminhaoPedido> attachedCaminhoesPedidoNew = new ArrayList<CaminhaoPedido>();
            for (CaminhaoPedido caminhoesPedidoNewCaminhaoPedidoToAttach : caminhoesPedidoNew) {
                caminhoesPedidoNewCaminhaoPedidoToAttach = em.getReference(caminhoesPedidoNewCaminhaoPedidoToAttach.getClass(), caminhoesPedidoNewCaminhaoPedidoToAttach.getIdCaminhaoPedido());
                attachedCaminhoesPedidoNew.add(caminhoesPedidoNewCaminhaoPedidoToAttach);
            }
            caminhoesPedidoNew = attachedCaminhoesPedidoNew;
            pedido.setCaminhoesPedido(caminhoesPedidoNew);
            List<ItemPedido> attachedItensPedidoNew = new ArrayList<ItemPedido>();
            for (ItemPedido itensPedidoNewItemPedidoToAttach : itensPedidoNew) {
                itensPedidoNewItemPedidoToAttach = em.getReference(itensPedidoNewItemPedidoToAttach.getClass(), itensPedidoNewItemPedidoToAttach.getIdItemPedido());
                attachedItensPedidoNew.add(itensPedidoNewItemPedidoToAttach);
            }
            itensPedidoNew = attachedItensPedidoNew;
            pedido.setItensPedido(itensPedidoNew);
            List<ContainerPedido> attachedContaineresPedidoNew = new ArrayList<ContainerPedido>();
            for (ContainerPedido containeresPedidoNewContainerPedidoToAttach : containeresPedidoNew) {
                containeresPedidoNewContainerPedidoToAttach = em.getReference(containeresPedidoNewContainerPedidoToAttach.getClass(), containeresPedidoNewContainerPedidoToAttach.getIdContainerPedido());
                attachedContaineresPedidoNew.add(containeresPedidoNewContainerPedidoToAttach);
            }
            containeresPedidoNew = attachedContaineresPedidoNew;
            pedido.setContaineresPedido(containeresPedidoNew);
            List<ProdutoValidado> attachedProdutosValidadosNew = new ArrayList<ProdutoValidado>();
            for (ProdutoValidado produtosValidadosNewProdutoValidadoToAttach : produtosValidadosNew) {
                produtosValidadosNewProdutoValidadoToAttach = em.getReference(produtosValidadosNewProdutoValidadoToAttach.getClass(), produtosValidadosNewProdutoValidadoToAttach.getIdProdutoValidado());
                attachedProdutosValidadosNew.add(produtosValidadosNewProdutoValidadoToAttach);
            }
            produtosValidadosNew = attachedProdutosValidadosNew;
            pedido.setProdutosValidados(produtosValidadosNew);
            pedido = em.merge(pedido);
            if (funcionarioOld != null && !funcionarioOld.equals(funcionarioNew)) {
                funcionarioOld.getPedidos().remove(pedido);
                funcionarioOld = em.merge(funcionarioOld);
            }
            if (funcionarioNew != null && !funcionarioNew.equals(funcionarioOld)) {
                funcionarioNew.getPedidos().add(pedido);
                funcionarioNew = em.merge(funcionarioNew);
            }
            if (despachanteOld != null && !despachanteOld.equals(despachanteNew)) {
                despachanteOld.getPedidos().remove(pedido);
                despachanteOld = em.merge(despachanteOld);
            }
            if (despachanteNew != null && !despachanteNew.equals(despachanteOld)) {
                despachanteNew.getPedidos().add(pedido);
                despachanteNew = em.merge(despachanteNew);
            }
            if (clienteOld != null && !clienteOld.equals(clienteNew)) {
                clienteOld.getPedidos().remove(pedido);
                clienteOld = em.merge(clienteOld);
            }
            if (clienteNew != null && !clienteNew.equals(clienteOld)) {
                clienteNew.getPedidos().add(pedido);
                clienteNew = em.merge(clienteNew);
            }
            for (CaminhaoPedido caminhoesPedidoNewCaminhaoPedido : caminhoesPedidoNew) {
                if (!caminhoesPedidoOld.contains(caminhoesPedidoNewCaminhaoPedido)) {
                    Pedido oldPedidoOfCaminhoesPedidoNewCaminhaoPedido = caminhoesPedidoNewCaminhaoPedido.getPedido();
                    caminhoesPedidoNewCaminhaoPedido.setPedido(pedido);
                    caminhoesPedidoNewCaminhaoPedido = em.merge(caminhoesPedidoNewCaminhaoPedido);
                    if (oldPedidoOfCaminhoesPedidoNewCaminhaoPedido != null && !oldPedidoOfCaminhoesPedidoNewCaminhaoPedido.equals(pedido)) {
                        oldPedidoOfCaminhoesPedidoNewCaminhaoPedido.getCaminhoesPedido().remove(caminhoesPedidoNewCaminhaoPedido);
                        oldPedidoOfCaminhoesPedidoNewCaminhaoPedido = em.merge(oldPedidoOfCaminhoesPedidoNewCaminhaoPedido);
                    }
                }
            }
            for (ItemPedido itensPedidoNewItemPedido : itensPedidoNew) {
                if (!itensPedidoOld.contains(itensPedidoNewItemPedido)) {
                    Pedido oldPedidoOfItensPedidoNewItemPedido = itensPedidoNewItemPedido.getPedido();
                    itensPedidoNewItemPedido.setPedido(pedido);
                    itensPedidoNewItemPedido = em.merge(itensPedidoNewItemPedido);
                    if (oldPedidoOfItensPedidoNewItemPedido != null && !oldPedidoOfItensPedidoNewItemPedido.equals(pedido)) {
                        oldPedidoOfItensPedidoNewItemPedido.getItensPedido().remove(itensPedidoNewItemPedido);
                        oldPedidoOfItensPedidoNewItemPedido = em.merge(oldPedidoOfItensPedidoNewItemPedido);
                    }
                }
            }
            for (ContainerPedido containeresPedidoNewContainerPedido : containeresPedidoNew) {
                if (!containeresPedidoOld.contains(containeresPedidoNewContainerPedido)) {
                    Pedido oldPedidoOfContaineresPedidoNewContainerPedido = containeresPedidoNewContainerPedido.getPedido();
                    containeresPedidoNewContainerPedido.setPedido(pedido);
                    containeresPedidoNewContainerPedido = em.merge(containeresPedidoNewContainerPedido);
                    if (oldPedidoOfContaineresPedidoNewContainerPedido != null && !oldPedidoOfContaineresPedidoNewContainerPedido.equals(pedido)) {
                        oldPedidoOfContaineresPedidoNewContainerPedido.getContaineresPedido().remove(containeresPedidoNewContainerPedido);
                        oldPedidoOfContaineresPedidoNewContainerPedido = em.merge(oldPedidoOfContaineresPedidoNewContainerPedido);
                    }
                }
            }
            for (ProdutoValidado produtosValidadosNewProdutoValidado : produtosValidadosNew) {
                if (!produtosValidadosOld.contains(produtosValidadosNewProdutoValidado)) {
                    Pedido oldPedidoOfProdutosValidadosNewProdutoValidado = produtosValidadosNewProdutoValidado.getPedido();
                    produtosValidadosNewProdutoValidado.setPedido(pedido);
                    produtosValidadosNewProdutoValidado = em.merge(produtosValidadosNewProdutoValidado);
                    if (oldPedidoOfProdutosValidadosNewProdutoValidado != null && !oldPedidoOfProdutosValidadosNewProdutoValidado.equals(pedido)) {
                        oldPedidoOfProdutosValidadosNewProdutoValidado.getProdutosValidados().remove(produtosValidadosNewProdutoValidado);
                        oldPedidoOfProdutosValidadosNewProdutoValidado = em.merge(oldPedidoOfProdutosValidadosNewProdutoValidado);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = pedido.getIdPedido();
                if (findPedido(id) == null) {
                    throw new NonexistentEntityException("The pedido with id " + id + " no longer exists.");
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
            Pedido pedido;
            try {
                pedido = em.getReference(Pedido.class, id);
                pedido.getIdPedido();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pedido with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<CaminhaoPedido> caminhoesPedidoOrphanCheck = pedido.getCaminhoesPedido();
            for (CaminhaoPedido caminhoesPedidoOrphanCheckCaminhaoPedido : caminhoesPedidoOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pedido (" + pedido + ") cannot be destroyed since the CaminhaoPedido " + caminhoesPedidoOrphanCheckCaminhaoPedido + " in its caminhoesPedido field has a non-nullable pedido field.");
            }
            List<ItemPedido> itensPedidoOrphanCheck = pedido.getItensPedido();
            for (ItemPedido itensPedidoOrphanCheckItemPedido : itensPedidoOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pedido (" + pedido + ") cannot be destroyed since the ItemPedido " + itensPedidoOrphanCheckItemPedido + " in its itensPedido field has a non-nullable pedido field.");
            }
            List<ContainerPedido> containeresPedidoOrphanCheck = pedido.getContaineresPedido();
            for (ContainerPedido containeresPedidoOrphanCheckContainerPedido : containeresPedidoOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pedido (" + pedido + ") cannot be destroyed since the ContainerPedido " + containeresPedidoOrphanCheckContainerPedido + " in its containeresPedido field has a non-nullable pedido field.");
            }
            List<ProdutoValidado> produtosValidadosOrphanCheck = pedido.getProdutosValidados();
            for (ProdutoValidado produtosValidadosOrphanCheckProdutoValidado : produtosValidadosOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pedido (" + pedido + ") cannot be destroyed since the ProdutoValidado " + produtosValidadosOrphanCheckProdutoValidado + " in its produtosValidados field has a non-nullable pedido field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Funcionario funcionario = pedido.getFuncionario();
            if (funcionario != null) {
                funcionario.getPedidos().remove(pedido);
                funcionario = em.merge(funcionario);
            }
            Despachante despachante = pedido.getDespachante();
            if (despachante != null) {
                despachante.getPedidos().remove(pedido);
                despachante = em.merge(despachante);
            }
            Cliente cliente = pedido.getCliente();
            if (cliente != null) {
                cliente.getPedidos().remove(pedido);
                cliente = em.merge(cliente);
            }
            em.remove(pedido);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pedido> findPedidoEntities() {
        return findPedidoEntities(true, -1, -1);
    }

    public List<Pedido> findPedidoEntities(int maxResults, int firstResult) {
        return findPedidoEntities(false, maxResults, firstResult);
    }

    private List<Pedido> findPedidoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pedido.class));
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

    public Pedido findPedido(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pedido.class, id);
        } finally {
            em.close();
        }
    }

    public int getPedidoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pedido> rt = cq.from(Pedido.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
