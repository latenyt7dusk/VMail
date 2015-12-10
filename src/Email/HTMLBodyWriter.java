/*
 * 
 * 
 * 
 */
package Email;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.*;

/**
 *
 * @author HERU
 */
public class HTMLBodyWriter extends HTMLWriter {

    public HTMLBodyWriter(Writer w, HTMLDocument doc) {
        super(w, doc);
    }

    private boolean inBody = false;

    private boolean isBody(Element elem) {
        // copied from HTMLWriter.startTag()
        AttributeSet attr = elem.getAttributes();
        Object nameAttribute = attr
                .getAttribute(StyleConstants.NameAttribute);
        HTML.Tag name = null;
        if (nameAttribute instanceof HTML.Tag) {
            name = (HTML.Tag) nameAttribute;
        }
        return name == HTML.Tag.BODY;
    }

    @Override
    protected void startTag(Element elem) throws IOException,
            BadLocationException {
        if (inBody) {
            super.startTag(elem);
        }
        if (isBody(elem)) {
            inBody = true;
        }
    }

    @Override
    protected void endTag(Element elem) throws IOException {
        if (isBody(elem)) {
            inBody = false;
        }
        if (inBody) {
            super.endTag(elem);
        }
    }

    public static String parse(JTextPane t) {
        try {
            StringWriter writer = new StringWriter();
            HTMLDocument doc = (HTMLDocument) t.getStyledDocument();

            HTMLWriter htmlWriter = new HTMLBodyWriter(writer, doc);
            htmlWriter.write();
            return writer.toString();
        } catch (Exception er) {
            return "";
        }
    }

}
