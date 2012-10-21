package net.sf.jcommon.web;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class GZIPFilter implements Filter {

    public static final String ACCEPT_ENCODING_HEADER = "accept-encoding";
	public static final String GZIP_ENCODING = "gzip";

	public void init(FilterConfig filterConfig) {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            String acceptEncoding = httpRequest.getHeader(ACCEPT_ENCODING_HEADER);
            if (acceptEncoding != null && acceptEncoding.indexOf(GZIPFilter.GZIP_ENCODING) != -1) {
                GZIPResponseWrapper wrappedResponse = new GZIPResponseWrapper(httpResponse);
                chain.doFilter(request, wrappedResponse);
                wrappedResponse.finishResponse();
                return;
            }
        }
        chain.doFilter(request, response);
    }

  public void destroy() {
  }
}
