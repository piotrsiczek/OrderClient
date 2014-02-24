/**
 * 
 */
package orders;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;



/**
 * Klasa zawierajaca dane listy z przygotowywanymi zamowieniami
 * @author Piotr Siczek
 *
 */
public class PreparingOrderModel extends AbstractListModel {
	
	private static final long serialVersionUID = 1L;
	private List<String> items;
	//private List<Integer> id;
	private int size;
	
	public PreparingOrderModel()
	{
		items = new ArrayList<String>();
		size = 0;
	}
	
	public void addElement(String data)
	{
		items.add(data);		
		size++;	
		fireIntervalAdded(this, size-1, size-1);

	}
	
	public void removeElementAt(int index)
	{
		items.remove(index);
		size--;
		fireIntervalAdded(this, size+1, size+1);
	}

	@Override
	public Object getElementAt(int index) {
		
		return items.get(index);

	}

	@Override
	public int getSize() {
		
		return size;
	}

}
