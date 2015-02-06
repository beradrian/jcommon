package net.sf.jcommon.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class HttpSessionCollector extends SelfRegisteredAttributeListener implements HttpSessionListener, HttpSessionActivationListener {

	private Map<String, HttpSession> sessions = new HashMap<String, HttpSession>();
	
	@Override
    public void sessionCreated(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        sessions.put(session.getId(), session);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        sessions.remove(event.getSession().getId());
    }

	public Map<String, HttpSession> getSessions() {
		return sessions;
	}

	@Override
	public void sessionWillPassivate(HttpSessionEvent event) {
        sessions.remove(event.getSession().getId());
	}

	@Override
	public void sessionDidActivate(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        sessions.put(session.getId(), session);
	}

}
