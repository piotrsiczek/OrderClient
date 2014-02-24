package orders;


import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;


/**
 * Klasa renderujaca dane z biezacego zamowienia
 * @author Piotr Siczek
 */
public class CurrentOrderCellRenderer extends JPanel implements ListCellRenderer {
	 // See Serialization for more info, this is to remove a warning.
	 private static final long serialVersionUID = 1L;

	 private JLabel[] labels; // Stores the labels for each column.
	 private Color[] fgs; // Stores the foreground colours for each column.
	 private Color[] bgs; // Stores the background colours for each column.
	 private Color[] sfgs; // Stores the foreground colours for each column when selected.
	 private Color[] sbgs; // Stores the background colours for each column when selected.
	 private int columns;

	 public CurrentOrderCellRenderer() 
	 {
		 columns = 3;
	     setLayout(new GridLayout(1, columns));
	     
	     labels = new JLabel[columns];
	     fgs = new Color[columns];
	     bgs = new Color[columns];
	     sfgs = new Color[columns];
	     sbgs = new Color[columns];

	     for(int i = 0; i < columns; i++) 
	     {
	    	 labels[i] = new JLabel();
	    	 labels[i].setOpaque(true);
	    	 add(labels[i]);
	     }
	 }
	 
	 public void setTextPosition(int column, int position) 
	 {		 
	     labels[column].setHorizontalAlignment(position);
	 }
	 
	 private String round(double digit)
	 {
	        DecimalFormat df = new DecimalFormat("#.##");
	        return df.format(digit);
	 }

	 public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
 
		 Row data = (Row)value;
		 
		 labels[0].setText(data.getName());
		 labels[1].setText(Integer.toString(data.getQty()));
		 
		 
		 labels[2].setText(round(data.getPrice()));

	     for(int i = 0; i < columns; i++) {


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
