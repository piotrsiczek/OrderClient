/**
 * 
 */
package orders;

import java.util.List;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.AbstractListModel;

/**
 * Klasa zawierajace dane listy zamowien
 * @author Piotr Siczek
 *
 */
public class OrderListModel extends AbstractListModel {

	private static final long serialVersionUID = 1L;
	private List<Row> items;
	private Vector<Integer> id;
	private int size;
	
	public OrderListModel()
	{
		
		items = new ArrayList<Row>();
		id = new Vector<Integer>();
		size=0;
	}
	
	public void addElement(String name, int qty, double price, int id)
	{
		items.add(new Row(name, qty, price));
		this.id.add(id);
		
		size++;	
	
		fireIntervalAdded(this, size-1, size-1);
		
		
	}
	
	public void updateElement(int i, String name, int qty, double price)
	{
		items.set(i, new Row(name, qty, price));
		
		fireContentsChanged(this, i, i);	
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.ListModel#getElementAt(int)
	 */
	
	public void removeElementAt(int index)
	{
		//System.out.println("removed");
		//items.remove(index);
		
		int elements = items.get(index).getQty();
				
		if (elements > 1)
		{	
			double totalPrice = items.get(index).getPrice();
			double price = totalPrice / elements;
		
			items.set(index, new Row(items.get(index).getName(), elements - 1, totalPrice - price));
			fireContentsChanged(this, index, index);	
		}
			else
			{
				System.out.println("usuwa: " + index);
				
				
				
				System.out.println("dotad");
				
				try {
									
					items.remove(index);
					id.remove(index);
					size--;
				

					fireIntervalRemoved(this, index, index);
				}
				catch(Exception e)
				{
					System.out.println(e.toString());
				}
				
				
			}
		
		//
	}
	
	public int removeAll(int index)
	{								
			items.remove(index);
			id.remove(index);
			size--;
			fireIntervalRemoved(this, index, index);
			
			return size;
	}
	
	public void removeAll()
	{						
		int i;

		for (i = 0; i < size; i++)
		{
			items.remove(0);
			id.remove(0);
		}
		
		size = 0;
		fireIntervalRemoved(this, 0, i);		
	}
	
	public void display()
	{
		for (int i=0; i < size; i++)
		{
			System.out.println("display: " + items.get(i).getName() + "\t" + items.get(i).getQty());
		}
		
	}
	
	@Override
	public Object getElementAt(int index) {
		
		return items.get(index);
	}
	
	public int getId(int index)
	{
		return id.get(index);
	}
	


	/* (non-Javadoc)
	 * @see javax.swing.ListModel#getSize()
	 */
	@Override
	public int getSize() {
		
		return size;
	}



}
