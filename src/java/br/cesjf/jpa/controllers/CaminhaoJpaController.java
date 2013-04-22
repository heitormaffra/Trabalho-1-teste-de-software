/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cesjf.jpa.controllers;

import br.cesjf.jpa.controllers.exceptions.NonexistentEntityException;
import br.cesjf.jpa.controllers.exceptions.RollbackFailureException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import sistemacontrolepedagio.carros.Caminhao;

/**
 *
 * @author heitormaffra
 */
public class CaminhaoJpaController implements Serializable {

    public CaminhaoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Caminhao caminhao) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            em.persist(caminhao);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Caminhao caminhao) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            caminhao = em.merge(caminhao);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = caminhao.getId();
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

    public void destroy(Long id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Caminhao caminhao;
            try {
                caminhao = em.getReference(Caminhao.class, id);
                caminhao.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The caminhao with id " + id + " no longer exists.", enfe);
            }
            em.remove(caminhao);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
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
