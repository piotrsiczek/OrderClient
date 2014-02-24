/**
 * 
 */
package orders;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;


/**
 * Klasa renderujaca dane wyswietlane w listach
 * @author Piotr Siczek
 *
 */
public class MultiColumnCellRenderer extends JPanel implements ListCellRenderer {
	 // See Serialization for more info, this is to remove a warning.
	 private static final long serialVersionUID = 1L;

	 private JLabel[] labels; // Stores the labels for each column.
	 private Color[] fgs; // Stores the foreground colours for each column.
	 private Color[] bgs; // Stores the background colours for each column.
	 private Color[] sfgs; // Stores the foreground colours for each column when selected.
	 private Color[] sbgs; // Stores the background colours for each column when selected.
	 private int size;
	 /**
	  * @param columns The amount of columns that the JList stores.
	  */
	 public MultiColumnCellRenderer(int columns) 
	 {
	     setLayout(new GridLayout(1, columns));
	     
	     size=0;
	     
	     labels = new JLabel[columns];
	     fgs = new Color[columns];
	     bgs = new Color[columns];
	     sfgs = new Color[columns];
	     sbgs = new Color[columns];

	     for(int i = 0; i < columns; i++) 
	     {
	    	 labels[i] = new JLabel();
	    	 labels[i].setOpaque(true);
	    	 labels[i].setBorder(BorderFactory.createEmptyBorder(2, 5, 0, 0));
	    	 add(labels[i]);
	     }
	 }

	 
	 public int length()
	 {
		 return size;
	 }
	 
	 public void add(String[] data)
	 {
		 size++;
		 
		 for(int i = 0; i < labels.length; i++)
		 {
			 labels[i].setText(data[i]);
			 
		 }
	 }
	 
	 public void setTextPosition(int column, int position) 
	 {		 
	     labels[column].setHorizontalAlignment(position);
	 }
	 
	 public void test(int column, int position) 
	 {		 
	     System.out.println("elo test");
	 }

	 public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
	     // You might want to change this line in case you don't use String[]'s.

		 
		 String[] celldata = (String[])value;

	     for(int i = 0; i < celldata.length; i++) {
	  labels[i].setText(celldata[i]);

	  // Colour logic, highly configurable.
	  if(isSelected) {
	      if(sbgs[i] != null) {
	   this.labels[i].setBackground(sbgs[i]);
	      }
	      else {
	   this.labels[i].setBackground(list.getSelectionBackground());
	      }

	      if(sfgs[i] != null) {
	   this.labels[i].setForeground(sfgs[i]);
	      }
	      else {
	   this.labels[i].setForeground(list.getSelectionForeground());
	      }
	  }
	  else {
	      if(bgs[i] != null) {
	   this.labels[i].setBackground(bgs[i]);
	      }
	      else {
	   this.labels[i].setBackground(list.getBackground());
	      }

	      if(fgs[i] != null) {
	   this.labels[i].setForeground(fgs[i]);
	      }
	      else {
	   this.labels[i].setForeground(list.getForeground());
	      }
	  }
	     }

	     super.setEnabled(list.isEnabled());
	     super.setFont(list.getFont());
	     return this;
	 }
	   }
