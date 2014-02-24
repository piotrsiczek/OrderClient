/**
 * 
 */
package orders;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


/**
 * Klasa obslugujaca akcje uruchamiane przez urzytkownika
 * @author Piotr Siczek
 */
public class Controler implements Runnable
{
	private View view;
	private Model model;
	private Thread t;
	
	/**
	 * Konstruktor
	 * @param model obiekt zawierajacy dane aplikacji
	 */
	public Controler(Model model)
	{
		this.model = model;
		//obsluga odbierania danych
		t = new Thread(this);
		t.start();
	}
	
	/**
	 * Funkcja ustwiajaca widok
	 * @param view obiekt reprezentujacy widok
	 */
	public void setView(View view)
	{
		this.view = view;
	}
	
	/**
	 * Klasa obslugujaca usuwanie pojedynczego elementu
	 */
	public class DeleteSingleAction implements ActionListener 
	{
	        public void actionPerformed(ActionEvent e)
	        {
	            if (view.isCurrentOrderNotSelected()) return;
	            
	            int size;
	            int selected = view.getCurrentOrderListSelection();
	            
	            size = model.removeSingleItem(selected);
	            view.drawCurrentOrderList(size, selected);
	              
					if (size == 0)
					{
						view.setDeleteAllButton(false);
						view.setDeleteButton(false);
						view.setSetOrderButton(false);
					} 
					
					model.test();
	            
	        }
	}
	
	/**
	 * Klasa obslugujaca usuwanie wszystkich elementow w liscie badz w grupie
	 */
	public class DeleteAllAction implements ActionListener 
	{
	        public void actionPerformed(ActionEvent e)
	        {
	        	int size;
	        	
	            if (view.isCurrentOrderNotSelected())
	            {
	            		model.removeAllItems();
	            		view.setDeleteAllButton(false);
	            		view.setDeleteButton(false);
	            		view.setSetOrderButton(false);
	            }
	            else
	            {
	            	int selected = view.getCurrentOrderListSelection();
	  
	            	size = model.removeAllItems(selected);
	            	
	            	view.drawCurrentOrderList(size, selected);	
	            }
	            
	            
	            
	            
	            
	        }
	}
	
	/**
	 * Klasa obslugujaca zaznaczenie elementu w liscie
	 */
	public class ItemSelect implements ListSelectionListener
	{
		
		private JList list;
				
	        @Override
			public void valueChanged(ListSelectionEvent e) 
			{
	        	
					 if (!e.getValueIsAdjusting()) 
					 {
						 int id;
						 int state = 0;
						 list = (JList) e.getSource();
						 
						 	
						 	
						 	if (list.getSelectedIndex() != -1)
						 	{
								id = Integer.parseInt(list.getName().substring(12));					
								String values[] = (String[])list.getSelectedValue();
								
								state = model.refreshCurrentOrder(values[0], Double.parseDouble(values[1]), id);
							
								
								if (state == 1)
								{
									view.setDeleteButton(true);
									view.setDeleteAllButton(true);
									view.setSetOrderButton(true);
								}
							   
							   	list.clearSelection();
						 	}
					 }		 
	        }										
	}
	
	
	/**
	 * Klasa obslugujaca zapis do bazy danych
	 */
	public class SetOrderAction implements ActionListener 
	{
	        public void actionPerformed(ActionEvent e)
	        {   
	        	model.setOrder();
	        	
	        	String data = model.createMsg();
	        	//System.out.println(data);
	        	
	        	model.sendOrder(data);
	        	      	
	        	model.refreshPreparingOrder(data, view.getPreparingCols());
	        	
	        	  
	        	//System.out.println(model.createMsg());
	        	
	        	
	        	
	            
        		model.removeAllItems();
        		view.setDeleteAllButton(false);
        		view.setDeleteButton(false);
        		view.setSetOrderButton(false);            
	            
	        }
	}


	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		int i = 0;
		String data = null;
		

			while (true)
			{
				System.out.println("Nowy watek" + i);
				i++;
				
				
				data = model.getData();
				
				if (data.charAt(0) == 'd')
				{
					data = data.substring(1);
					model.removePreparedItem(Integer.parseInt(data));
					view.removePreparingOrderSelection(Integer.parseInt(data));
					
				}
				else view.setPreparingOrderSelection(Integer.parseInt(data));
			}
		
	}
	
}
