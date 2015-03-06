package net.sf.jcommon.spring;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.sf.jcommon.web.RedirectedHttpServletResponse;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;

/**
 * A Spring view resolver that decorates another view resolver and transforms the output to PDF.
 * The output must be well-formed XHTML.
 */
public class Xhtml2PdfViewResolver implements ViewResolver {
	
	private static final String PDF_MIME_TYPE = "application/pdf";
	
	private ViewResolver decoratedViewResolver;
	
	public Xhtml2PdfViewResolver(ViewResolver decoratedViewResolver) {
		this.decoratedViewResolver = decoratedViewResolver;
	}

	@Override
	public View resolveViewName(String viewName, Locale locale)
			throws Exception {
		return decorate(decoratedViewResolver.resolveViewName(viewName, locale));
	}

	/**
	 * Decorates another view in order to transform the output to PDF.
	 */
	private View decorate(View view) {
		if (view == null)
			return null;
		return new Xhtml2PdfView(view);
	}

	/**
	 * View decorator that renders the decorated view and then transforms the output to PDF.
	 */
	private static class Xhtml2PdfView implements View {
		private View decoratedView;

		public Xhtml2PdfView(View decoratedView) {
			this.decoratedView = decoratedView;
		}

		@Override
		public String getContentType() {
			return PDF_MIME_TYPE;
		}

		@Override
		public void render(Map<String, ?> model, HttpServletRequest request,
				HttpServletResponse response) throws Exception {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			decoratedView.render(model, request, new RedirectedHttpServletResponse(response, out));
			
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	        Document doc = builder.parse(new ByteArrayInputStream(out.toByteArray()));
	        ITextRenderer renderer = new ITextRenderer();
	        renderer.setDocument(doc, null);
	        renderer.layout();
	        OutputStream os = response.getOutputStream();
	        renderer.createPDF(os);
		}
	}
	
}
