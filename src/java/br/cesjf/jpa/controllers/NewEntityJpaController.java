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
import sistemacontrolepedagio.carros.NewEntity;

/**
 *
 * @author heitormaffra
 */
public class NewEntityJpaController implements Serializable {

    public NewEntityJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(NewEntity newEntity) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            em.persist(newEntity);
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

    public void edit(NewEntity newEntity) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            newEntity = em.merge(newEntity);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = newEntity.getId();
                if (findNewEntity(id) == null) {
                    throw new NonexistentEntityException("The newEntity with id " + id + " no longer exists.");
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
            NewEntity newEntity;
            try {
                newEntity = em.getReference(NewEntity.class, id);
                newEntity.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The newEntity with id " + id + " no longer exists.", enfe);
            }
            em.remove(newEntity);
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

    public List<NewEntity> findNewEntityEntities() {
        return findNewEntityEntities(true, -1, -1);
    }

    public List<NewEntity> findNewEntityEntities(int maxResults, int firstResult) {
        return findNewEntityEntities(false, maxResults, firstResult);
    }

    private List<NewEntity> findNewEntityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(NewEntity.class));
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

    public NewEntity findNewEntity(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(NewEntity.class, id);
        } finally {
            em.close();
        }
    }

    public int getNewEntityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<NewEntity> rt = cq.from(NewEntity.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
