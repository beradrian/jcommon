package net.sf.jcommon.ui;

import java.applet.Applet;
import java.awt.*;

import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.*;

import netscape.javascript.JSObject;
import netscape.javascript.JSException;

/**
 * Used in an editor pane enclosed in an applet to call javascript in a browser.
 * The link href must start with <i>browser:</i>.
 * The browser will execute as javascript the string after <i>browser:</i>.
 * If the editor pane isn't enclosed in an applet this listener has no effect.
 * for example <code>browser:alert("Hello!")<code> will display an alert box in the browser
 * with the message <code>Hello!</code>.
 * This is the code to add such a listener to an editor pane:
 * <blockquote><code><pre>
 * ...
 * JEditorPane panel = new JEditorPane("text/html", "");
 * panel.addHyperlinkListener(new BrowserHyperlinkListener());
 * ...
 * </pre></code></blockquote>
 * @author Adrian BER
 */
public class BrowserHyperlinkListener implements HyperlinkListener {

    public void hyperlinkUpdate(HyperlinkEvent e) {
        Object source = e.getSource();
        if ( source != null && source instanceof Component) {
            // get the enclosing applet
            Applet applet = (Applet)SwingUtilities
                .getAncestorOfClass(Applet.class, (Component)source);
            if (applet != null) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED
                        && e.getDescription() != null
                        && e.getDescription().startsWith("browser:")) {
                    try {
                        // call the javascript in the browser
                        JSObject win = JSObject.getWindow(applet);
                        Object[] args = new Object[1];
                        args[0] = e.getDescription().substring(8);
                        win.call("eval", args);
                    }
                    catch (JSException exc) {
                        log(exc);
                    }
                    catch (Exception exc) {
                        log(exc);
                    }
                }
            }
        }
    }

    /** Logs an exception into the standard error, which in this case will be the
     * browser JVM console.
     * @param exc the exception to be logged.
     */
    private void log(Exception exc) {
        System.err.println("Browser javascript invocation error: " + exc.getMessage());
        exc.printStackTrace();
    }

}
