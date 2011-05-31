package net.sf.jcommon.ui.table;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.*;
import java.text.DateFormat;
import java.text.Format;

/**
 * A table cell renderer capable of displaying data in a given {@link Format}.
 * To use it simply set it as the renderer for the needed cell/column.
 * @author Adrian BER
 */
public class FormatRenderer extends DefaultTableCellRenderer {

    private Format formatter;

    public FormatRenderer() {
	    setHorizontalAlignment(JLabel.RIGHT);
	}

    public FormatRenderer(DateFormat formatter) {
        this();
        this.formatter = formatter;
    }

    public Format getFormatter() {
        return formatter;
    }

    public void setFormatter(DateFormat formatter) {
        this.formatter = formatter;
    }

    public void setValue(Object value) {
	    setText(value == null ? "" : formatter.format(value));
	}
}
