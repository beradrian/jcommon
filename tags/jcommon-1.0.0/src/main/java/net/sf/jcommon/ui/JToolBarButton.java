package net.sf.jcommon.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * ToolBar button implementation.
 * @author BER
 * @version 1.0
 */
public class JToolBarButton extends JButton {

  /** a highlight border when mouse is on a tollbar button */
  private Border highlightBorder;
  private Border normalBorder;

  /** Creates a new toolbar button. */
  public JToolBarButton() {
    init();
  }

  /** Creates a new toolbar button.
    * @param icon the icon put on this button
    */
  public JToolBarButton(Icon icon){
    super(icon);
    init();
  }

  /** Creates a new toolbar button for the given action.
    * @param action the action associated with this toolbar button
    */
  public JToolBarButton(Action action){
    super(action);
    init();
  }

  /** Initializes this toolbar button. */
  private void init(){
    setOnMouseOverBorder( BorderFactory.createBevelBorder(BevelBorder.RAISED,
      this.getBackground(),this.getBackground().darker()) );
    setNormalBorder( BorderFactory.createLineBorder(this.getBackground(),
      highlightBorder.getBorderInsets(this).bottom) );
    setBorderNow(normalBorder);

    addMouseListener(new MouseListener(){

      public void mouseClicked(MouseEvent e){}
      public void mouseEntered(MouseEvent e){
        if(JToolBarButton.this.isEnabled()) {
          setBorderNow(highlightBorder);
        }
      }
      public void mouseExited(MouseEvent e){
        if(JToolBarButton.this.isEnabled()) {
          setBorderNow(normalBorder);
        }
      }
      public void mousePressed(MouseEvent e){}
      public void mouseReleased(MouseEvent e){}
    });
  }

  /** Sets the border for this component when the mouse isn't over it.
    * @param border the new normal border
    */
  public void setNormalBorder(Border border) {
    normalBorder=border;
  }

  /** Sets the border for this component when the mouse is over it.
    * @param border the new highlighted border
    */
  public void setOnMouseOverBorder(Border border) {
    highlightBorder=border;
  }

  public void setBorder(Border border) {
  }

  /** Used instead of setBorder which is overwritten so that anybody
    * couldn't change the border
    * @param border the new border
    */
  private void setBorderNow(Border border) {
    super.setBorder(border);
  }

}