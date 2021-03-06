/*
 * Friends in Global Health - FGH © 2016
 */
package org.openmrs.module.pharmacyapi.api.dao;

import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.api.db.hibernate.DbSessionFactory;

/**
 * @author Stélio Moiane
 */
public abstract class GenericDAOImpl<T> implements GenericDAO<T> {
	
	private DbSessionFactory sessionFactory;
	
	@Override
	public DbSession getSession() {
		return this.sessionFactory.getCurrentSession();
	}
	
	@Override
	public void setSession(final DbSessionFactory dbSessionFactory) {
		this.sessionFactory = dbSessionFactory;
	}
	
	@Override
	public T save(final T entity) {
		this.getSession().saveOrUpdate(entity);
		return entity;
	}
}
