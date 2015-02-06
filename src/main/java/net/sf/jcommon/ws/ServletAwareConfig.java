package net.sf.jcommon.ws;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/**
 * This enables you access from websockets endpoint the HttpSession. Below is an example on how to use it.
 * <blockquote><code><pre>@ServerEndpoint(value="/path_to", configurator=ServletAwareConfig.class)
public class YourWebSocketEndpoint {

    private EndpointConfig config;

    @OnOpen
    public void onOpen(Session websocketSession, EndpointConfig config) {
        this.config = config;
    }

    @OnMessage
    public void onMessage(String message) {
        HttpSession httpSession = (HttpSession) config.getUserProperties().get("httpSession");
        ServletContext servletContext = httpSession.getServletContext();
        // ...
    }

}</pre></code></blockquote>
 */
public class ServletAwareConfig extends ServerEndpointConfig.Configurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        config.getUserProperties().put("httpSession", httpSession);
    }

}