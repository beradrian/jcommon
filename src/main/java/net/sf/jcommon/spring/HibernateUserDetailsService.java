package net.sf.jcommon.spring;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class HibernateUserDetailsService implements UserDetailsService {

	private SessionFactory sessionFactory;
	private Class<? extends UserDetails> userClass;
	private String usernameProperty;
	
	public HibernateUserDetailsService(SessionFactory sessionFactory,
			Class<? extends UserDetails> userClass, String usernameProperty) {
		this.sessionFactory = sessionFactory;
		this.userClass = userClass;
		this.usernameProperty = usernameProperty;
	}

	public HibernateUserDetailsService(SessionFactory sessionFactory,
			Class<? extends UserDetails> userClass) {
		this(sessionFactory, userClass, "username");
	}
	
	public HibernateUserDetailsService(Class<? extends UserDetails> userClass) {
		this(null, userClass);
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
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
		return (UserDetails) sessionFactory.getCurrentSession().createCriteria(userClass)
			.add(Restrictions.eq(usernameProperty, username)).uniqueResult();
	}

}
