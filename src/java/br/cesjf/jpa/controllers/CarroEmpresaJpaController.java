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
import sistemacontrolepedagio.carros.CarroEmpresa;

/**
 *
 * @author heitormaffra
 */
public class CarroEmpresaJpaController implements Serializable {

    public CarroEmpresaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CarroEmpresa carroEmpresa) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            em.persist(carroEmpresa);
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

    public void edit(CarroEmpresa carroEmpresa) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            carroEmpresa = em.merge(carroEmpresa);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = carroEmpresa.getId();
                if (findCarroEmpresa(id) == null) {
                    throw new NonexistentEntityException("The carroEmpresa with id " + id + " no longer exists.");
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
            CarroEmpresa carroEmpresa;
            try {
                carroEmpresa = em.getReference(CarroEmpresa.class, id);
                carroEmpresa.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The carroEmpresa with id " + id + " no longer exists.", enfe);
            }
            em.remove(carroEmpresa);
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

    public List<CarroEmpresa> findCarroEmpresaEntities() {
        return findCarroEmpresaEntities(true, -1, -1);
    }

    public List<CarroEmpresa> findCarroEmpresaEntities(int maxResults, int firstResult) {
        return findCarroEmpresaEntities(false, maxResults, firstResult);
    }

    private List<CarroEmpresa> findCarroEmpresaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CarroEmpresa.class));
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

    public CarroEmpresa findCarroEmpresa(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CarroEmpresa.class, id);
        } finally {
            em.close();
        }
    }

    public int getCarroEmpresaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CarroEmpresa> rt = cq.from(CarroEmpresa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
