package net.sf.jcommon.spring;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class JPUserDetailsService implements UserDetailsService {

	private EntityManagerFactory entityManagerFactory;
	private Class<? extends UserDetails> userClass;
	private String usernameProperty;

	public JPUserDetailsService(EntityManagerFactory entityManagerFactory,
			Class<? extends UserDetails> userClass, String usernameProperty) {
		this.entityManagerFactory = entityManagerFactory;
		this.userClass = userClass;
		this.usernameProperty = usernameProperty;
	}

	public JPUserDetailsService(EntityManagerFactory entityManagerFactory,
			Class<? extends UserDetails> userClass) {
		this(entityManagerFactory, userClass, "username");
	}
	
	public JPUserDetailsService(Class<? extends UserDetails> userClass) {
		this(null, userClass);
	}

	public EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}

	public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	public Class<? extends UserDetails> getUserClass() {
		return userClass;
	}

	public void setUserClass(Class<? extends UserDetails> userClass) {
		this.userClass = userClass;
	}

	public String getUsernameProperty() {
		return usernameProperty;
	}

	public void setUsernameProperty(String usernameProperty) {
		this.usernameProperty = usernameProperty;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	EntityManager em = entityManagerFactory.createEntityManager();
    	CriteriaBuilder cb = em.getCriteriaBuilder();
    	CriteriaQuery<? extends UserDetails> c = cb.createQuery(userClass);
    	c.where(cb.equal(c.from(userClass).get(usernameProperty), username));
    	try {
    		return em.createQuery(c).getSingleResult();
    	} catch (NoResultException exc) {
    		throw new UsernameNotFoundException("Username " + username + " not found", exc); 
    	} catch (NonUniqueResultException exc) {
    		throw new UsernameNotFoundException("More than one user with username " + username 
    				+ ". Database may be corrupted", exc);
    	}
	}

}
