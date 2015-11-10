/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.portaltrading.jpa;

import br.com.portaltrading.entidades.Cliente;
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
import br.com.portaltrading.entidades.Pedido;
import java.util.ArrayList;
import java.util.List;
import br.com.portaltrading.entidades.Documento;

/**
 *
 * @author Willian
 */
public class ClienteJpaController implements Serializable {

    public ClienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cliente cliente) {
        if (cliente.getPedidos() == null) {
            cliente.setPedidos(new ArrayList<Pedido>());
        }
        if (cliente.getDocumentos() == null) {
            cliente.setDocumentos(new ArrayList<Documento>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuario = cliente.getUsuario();
            if (usuario != null) {
                usuario = em.getReference(usuario.getClass(), usuario.getIdUsuario());
                cliente.setUsuario(usuario);
            }
            List<Pedido> attachedPedidos = new ArrayList<Pedido>();
            for (Pedido pedidosPedidoToAttach : cliente.getPedidos()) {
                pedidosPedidoToAttach = em.getReference(pedidosPedidoToAttach.getClass(), pedidosPedidoToAttach.getIdPedido());
                attachedPedidos.add(pedidosPedidoToAttach);
            }
            cliente.setPedidos(attachedPedidos);
            List<Documento> attachedDocumentos = new ArrayList<Documento>();
            for (Documento documentosDocumentoToAttach : cliente.getDocumentos()) {
                documentosDocumentoToAttach = em.getReference(documentosDocumentoToAttach.getClass(), documentosDocumentoToAttach.getIdDocumento());
                attachedDocumentos.add(documentosDocumentoToAttach);
            }
            cliente.setDocumentos(attachedDocumentos);
            em.persist(cliente);
            if (usuario != null) {
                usuario.getCliente().add(cliente);
                usuario = em.merge(usuario);
            }
            for (Pedido pedidosPedido : cliente.getPedidos()) {
                Cliente oldClienteOfPedidosPedido = pedidosPedido.getCliente();
                pedidosPedido.setCliente(cliente);
                pedidosPedido = em.merge(pedidosPedido);
                if (oldClienteOfPedidosPedido != null) {
                    oldClienteOfPedidosPedido.getPedidos().remove(pedidosPedido);
                    oldClienteOfPedidosPedido = em.merge(oldClienteOfPedidosPedido);
                }
            }
            for (Documento documentosDocumento : cliente.getDocumentos()) {
                Cliente oldClienteOfDocumentosDocumento = documentosDocumento.getCliente();
                documentosDocumento.setCliente(cliente);
                documentosDocumento = em.merge(documentosDocumento);
                if (oldClienteOfDocumentosDocumento != null) {
                    oldClienteOfDocumentosDocumento.getDocumentos().remove(documentosDocumento);
                    oldClienteOfDocumentosDocumento = em.merge(oldClienteOfDocumentosDocumento);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cliente cliente) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente persistentCliente = em.find(Cliente.class, cliente.getIdCliente());
            Usuario usuarioOld = persistentCliente.getUsuario();
            Usuario usuarioNew = cliente.getUsuario();
            List<Pedido> pedidosOld = persistentCliente.getPedidos();
            List<Pedido> pedidosNew = cliente.getPedidos();
            List<Documento> documentosOld = persistentCliente.getDocumentos();
            List<Documento> documentosNew = cliente.getDocumentos();
            List<String> illegalOrphanMessages = null;
            for (Pedido pedidosOldPedido : pedidosOld) {
                if (!pedidosNew.contains(pedidosOldPedido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Pedido " + pedidosOldPedido + " since its cliente field is not nullable.");
                }
            }
            for (Documento documentosOldDocumento : documentosOld) {
                if (!documentosNew.contains(documentosOldDocumento)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Documento " + documentosOldDocumento + " since its cliente field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (usuarioNew != null) {
                usuarioNew = em.getReference(usuarioNew.getClass(), usuarioNew.getIdUsuario());
                cliente.setUsuario(usuarioNew);
            }
            List<Pedido> attachedPedidosNew = new ArrayList<Pedido>();
            for (Pedido pedidosNewPedidoToAttach : pedidosNew) {
                pedidosNewPedidoToAttach = em.getReference(pedidosNewPedidoToAttach.getClass(), pedidosNewPedidoToAttach.getIdPedido());
                attachedPedidosNew.add(pedidosNewPedidoToAttach);
            }
            pedidosNew = attachedPedidosNew;
            cliente.setPedidos(pedidosNew);
            List<Documento> attachedDocumentosNew = new ArrayList<Documento>();
            for (Documento documentosNewDocumentoToAttach : documentosNew) {
                documentosNewDocumentoToAttach = em.getReference(documentosNewDocumentoToAttach.getClass(), documentosNewDocumentoToAttach.getIdDocumento());
                attachedDocumentosNew.add(documentosNewDocumentoToAttach);
            }
            documentosNew = attachedDocumentosNew;
            cliente.setDocumentos(documentosNew);
            cliente = em.merge(cliente);
            if (usuarioOld != null && !usuarioOld.equals(usuarioNew)) {
                usuarioOld.getCliente().remove(cliente);
                usuarioOld = em.merge(usuarioOld);
            }
            if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
                usuarioNew.getCliente().add(cliente);
                usuarioNew = em.merge(usuarioNew);
            }
            for (Pedido pedidosNewPedido : pedidosNew) {
                if (!pedidosOld.contains(pedidosNewPedido)) {
                    Cliente oldClienteOfPedidosNewPedido = pedidosNewPedido.getCliente();
                    pedidosNewPedido.setCliente(cliente);
                    pedidosNewPedido = em.merge(pedidosNewPedido);
                    if (oldClienteOfPedidosNewPedido != null && !oldClienteOfPedidosNewPedido.equals(cliente)) {
                        oldClienteOfPedidosNewPedido.getPedidos().remove(pedidosNewPedido);
                        oldClienteOfPedidosNewPedido = em.merge(oldClienteOfPedidosNewPedido);
                    }
                }
            }
            for (Documento documentosNewDocumento : documentosNew) {
                if (!documentosOld.contains(documentosNewDocumento)) {
                    Cliente oldClienteOfDocumentosNewDocumento = documentosNewDocumento.getCliente();
                    documentosNewDocumento.setCliente(cliente);
                    documentosNewDocumento = em.merge(documentosNewDocumento);
                    if (oldClienteOfDocumentosNewDocumento != null && !oldClienteOfDocumentosNewDocumento.equals(cliente)) {
                        oldClienteOfDocumentosNewDocumento.getDocumentos().remove(documentosNewDocumento);
                        oldClienteOfDocumentosNewDocumento = em.merge(oldClienteOfDocumentosNewDocumento);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = cliente.getIdCliente();
                if (findCliente(id) == null) {
                    throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.");
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
            Cliente cliente;
            try {
                cliente = em.getReference(Cliente.class, id);
                cliente.getIdCliente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Pedido> pedidosOrphanCheck = cliente.getPedidos();
            for (Pedido pedidosOrphanCheckPedido : pedidosOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cliente (" + cliente + ") cannot be destroyed since the Pedido " + pedidosOrphanCheckPedido + " in its pedidos field has a non-nullable cliente field.");
            }
            List<Documento> documentosOrphanCheck = cliente.getDocumentos();
            for (Documento documentosOrphanCheckDocumento : documentosOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cliente (" + cliente + ") cannot be destroyed since the Documento " + documentosOrphanCheckDocumento + " in its documentos field has a non-nullable cliente field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Usuario usuario = cliente.getUsuario();
            if (usuario != null) {
                usuario.getCliente().remove(cliente);
                usuario = em.merge(usuario);
            }
            em.remove(cliente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cliente> findClienteEntities() {
        return findClienteEntities(true, -1, -1);
    }

    public List<Cliente> findClienteEntities(int maxResults, int firstResult) {
        return findClienteEntities(false, maxResults, firstResult);
    }

    private List<Cliente> findClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cliente.class));
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

    public Cliente findCliente(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getClienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cliente> rt = cq.from(Cliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
