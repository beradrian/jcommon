package net.sf.jcommon.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jcommon.xml.Elements;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;

/**
 * Converts HTML to PDF. It can act as a filter and it will simply convert the response, supposedly in HTML, to PDF.
 * 
 * It can also act as a servlet and then it will receive the URL to be converted as a request parameter. 
 * The parameter name can be configured through a servlet initialization parameter called {@code uriParameter}.
 * If not specified the default value is {@code q} (from query).
 * 
 *  Configuration examples
 *  - as a filter
 *  <blockquote><code><pre>
 *  &lt;filter&gt;
 *      &lt;filter-name&gt;html2PdfConverter&lt;/filter-name&gt;
 *      &lt;filter-class&gt;net.sf.jcommon.web.Html2PdfConverter&lt;/filter-class&gt;
 *  &lt;/filter&gt;
 *
 *  &lt;filter-mapping&gt;
 *      &lt;filter-name&gt;html2PdfConverter&lt;/filter-name&gt;
 *      &lt;url-pattern&gt;*.pdf&lt;/url-pattern&gt;
 *  &lt;/filter-mapping&gt;
 *  </pre></code></blockquote>
 *  
 *  Then a request to http://server/webapp/justhtml.pdf will result in the response getting automatically 
 *  converted from HTML to PDF.
 *  
 *  If you're using it with Spring it is enough to map it to a @Controller method @RequestMapping({"info.html", "info.pdf"}) 
 *  and then return a view that will return HTML code. This filter will automatically transform the result into PDF and
 *  you can have both HTML and PDF with the same code. 
 *
 *  - as a servlet
 *  <blockquote><code><pre>
 *  &lt;servlet&gt;
 *      &lt;servlet-name&gt;html2PdfConverter&lt;/servlet-name&gt;
 *      &lt;servlet-class&gt;net.sf.jcommon.web.Html2PdfConverter&lt;/servlet-class&gt;
 *  &lt;/servlet&gt;
 *
 *  &lt;servlet-mapping&gt;
 *      &lt;servlet-name&gt;html2PdfConverter&lt;/servlet-name&gt;
 *      &lt;url-pattern&gt;/gimmepdf&lt;/url-pattern&gt;
 *  &lt;/servlet-mapping&gt;
 *  </pre></code></blockquote>
 *  
 *  Then a request to http://server/webapp/gimmepdf?q=/justhtml will result in a server-side request to
 *  http://server/webapp/justhtml, which will then get translated to PDF and returned as a response.
 */
public class Html2PdfConverter implements Filter, Servlet {

	private FilterConfig filterConfig;
	private ServletConfig servletConfig;
	
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

	@Override
	public String getServletInfo() {
		return Html2PdfConverter.class.getName();
	}
	
	@Override	
	public ServletConfig getServletConfig() {
		return servletConfig;
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		this.servletConfig = config;
	}
	
	private String getInitParameter(String name, String defaultValue) {
		String value = (servletConfig != null ? servletConfig.getInitParameter(name) : filterConfig.getInitParameter(name));
		return value != null ? value :  defaultValue;
	}

	@Override
	public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
			return;
		}
		convert((HttpServletRequest)request, (HttpServletResponse)response, null);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
			return;
		}
		convert((HttpServletRequest)request, (HttpServletResponse)response, chain);
	}
	
	private void convert(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ServletResponse newResponse = new RedirectedHttpServletResponse(response, baos);
		if (chain != null) {
			chain.doFilter(request, newResponse);
		} else {
			RequestDispatcher rd = request.getRequestDispatcher(request.getParameter(getInitParameter("uriParameter", "q")));
			rd.include(request, newResponse);
		}
		newResponse.flushBuffer();
		
 		// Build PDF document.
        final ITextRenderer iTextRenderer = new ITextRenderer();
        iTextRenderer.setDocumentFromString(baos.toString(), request.getRequestURL().toString());
        
        // set the PDF version from config parameters
        String v = getMeta(iTextRenderer.getDocument(), "pdf.version");
        if (v != null) {
        	iTextRenderer.setPDFVersion(v.charAt(0));
        }
        
        iTextRenderer.layout();
        
        try {
        	baos = new ByteArrayOutputStream();
			iTextRenderer.createPDF(baos);
			response.setContentType("application/pdf");
			writeToResponse(response, baos);
			baos.close();
		} catch (DocumentException e) {
			throw new ServletException(e);
		}
	}

	@Override
	public void destroy() {
	}
	
	private static void writeToResponse(HttpServletResponse response, ByteArrayOutputStream baos) throws IOException {
		// Write content type and also length (determined via byte array).
		response.setContentLength(baos.size());

		// Flush byte array to servlet output stream.
		ServletOutputStream out = response.getOutputStream();
		baos.writeTo(out);
		out.flush();
	}
	
	private static String getMeta(Document doc, String name) {
		for (Node x : Elements.asIterable(doc.getDocumentElement().getElementsByTagName("meta"))) {
			if (x instanceof Element && name.equals(((Element)x).getAttribute("name"))) {
				return ((Element)x).getAttribute("content");
			}
		}
		return null;
	}



}
