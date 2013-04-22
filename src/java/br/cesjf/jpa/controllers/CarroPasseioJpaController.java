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
import sistemacontrolepedagio.carros.CarroPasseio;

/**
 *
 * @author heitormaffra
 */
public class CarroPasseioJpaController implements Serializable {

    public CarroPasseioJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CarroPasseio carroPasseio) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            em.persist(carroPasseio);
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

    public void edit(CarroPasseio carroPasseio) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            carroPasseio = em.merge(carroPasseio);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = carroPasseio.getId();
                if (findCarroPasseio(id) == null) {
                    throw new NonexistentEntityException("The carroPasseio with id " + id + " no longer exists.");
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
            CarroPasseio carroPasseio;
            try {
                carroPasseio = em.getReference(CarroPasseio.class, id);
                carroPasseio.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The carroPasseio with id " + id + " no longer exists.", enfe);
            }
            em.remove(carroPasseio);
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

    public List<CarroPasseio> findCarroPasseioEntities() {
        return findCarroPasseioEntities(true, -1, -1);
    }

    public List<CarroPasseio> findCarroPasseioEntities(int maxResults, int firstResult) {
        return findCarroPasseioEntities(false, maxResults, firstResult);
    }

    private List<CarroPasseio> findCarroPasseioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CarroPasseio.class));
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

    public CarroPasseio findCarroPasseio(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CarroPasseio.class, id);
        } finally {
            em.close();
        }
    }

    public int getCarroPasseioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CarroPasseio> rt = cq.from(CarroPasseio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
