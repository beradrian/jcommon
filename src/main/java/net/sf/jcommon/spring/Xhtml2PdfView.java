package net.sf.jcommon.spring;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.sf.jcommon.web.RedirectedHttpServletResponse;

import org.springframework.web.servlet.view.InternalResourceView;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;

/**
 * A Spring view that transforms an internal resource (like JSP) into a PDF.
 */
public class Xhtml2PdfView extends InternalResourceView {
	
	private static final String PDF_MIME_TYPE = "application/pdf";

	@Override
	public String getContentType() {
		return PDF_MIME_TYPE;
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		super.renderMergedOutputModel(model, request, new RedirectedHttpServletResponse(response, out));
		
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(out.toByteArray()));
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocument(doc, null);
        renderer.layout();
        OutputStream os = response.getOutputStream();
        renderer.createPDF(os);
	}

}
