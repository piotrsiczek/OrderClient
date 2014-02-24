/**
 * 
 */
package orders;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;


/**
 * Klasa tworzaca interfejs graficzny aplikacji
 * @author Piotr Siczek
 */
public class View {
	
	private Model model;
	private Controler controler;
	private JFrame frame;
	private JList currentOrderList;
	private PreparingOrderCellRenderer preparingOrdersRenderer;
	private JButton deleteButton;
	private JButton deleteAllButton;
	private JButton setOrderButton;
	private JPanel mainPanel;
	private JList right;
	
	public View(Model model, Controler controler) throws SQLException 
	{
		this.model = model;
		this.controler = controler;		
		
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screensize = kit.getScreenSize();
		
		int width = screensize.width;
		int height = screensize.height;
		
		
			createUI();
		
	        
			mainPanel.setOpaque(true); //content panes must be opaque
			
	        frame.setContentPane(mainPanel);
	        
			frame.pack();
			
			frame.setSize(width/2+200, height/2+200);
			//frame.setLocation(width/2 - frame.getSize().width / 2, height/2 - frame.getSize().height / 2);
			
			frame.setVisible(true);
					
	}
	
	/**
	 * Funkcja tworzaca interfejs urzytkownika
	 * @throws SQLException
	 */
	private void createUI() throws SQLException 
	{
		 mainPanel = new JPanel();
		 mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
	     mainPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
	        //intro.setAlignmentX(JComponent.CENTER_ALIGNMENT);
	        //name.setAlignmentX(JComponent.CENTER_ALIGNMENT);
	        //button.setAlignmentX(JComponent.CENTER_ALIGNMENT);
	
				
		mainPanel.add( createCenterPanel() );
		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		mainPanel.add( createBottomPanel() );

        
       
    }
	
	
	/**
	 * Funkcja tworzaca pojedyncza liste produktow
	 * @param value id kategorii produktow
	 * @return stworzona liste 
	 * @throws SQLException
	 */
	private JScrollPane createSingleList(int value) throws SQLException
	{
		ResultSet data = model.getContent(value);	
		Vector<String[]> items = new Vector<String[]>();		

		while (data.next())  
		{  
			String[] table = {data.getString(3), data.getString(4)};
			items.add(table);
		} 
		
		JList list = new JList(items);
		
			MultiColumnCellRenderer cellRenderer = new MultiColumnCellRenderer(2);
			cellRenderer.setTextPosition(1, SwingConstants.CENTER);
		
		list.setCellRenderer(cellRenderer);
		
		list.setBorder(BorderFactory.createLineBorder(Color.black));
		list.setName("categoryList" + value);		
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


			Controler.ItemSelect selectAction = controler.new ItemSelect();
		list.addListSelectionListener(selectAction);
		
		JScrollPane scrollPane = new JScrollPane(list);  
		scrollPane.setPreferredSize(new Dimension(190, 235));
		scrollPane.setMinimumSize(new Dimension(190, 235));
		scrollPane.setMaximumSize(new Dimension(190, 235));
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setViewportView(list);
				
		return scrollPane;
	}
	
	/**
	 * Funkcja tworzy srodkowy panel
	 * @return srodkowy panel
	 * @throws SQLException
	 */
	private JPanel createCenterPanel() throws SQLException
	{
		ResultSet data = model.getCategories();
				
		JPanel centerPanel= new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));
		centerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JPanel topPanel= new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JPanel captionPanel= new JPanel();
		captionPanel.setLayout(new BoxLayout(captionPanel, BoxLayout.X_AXIS));
		captionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		
		while (data.next())  
		{  
		    JLabel label = new JLabel(" " + data.getString(2), SwingConstants.LEFT); 

		    label.setMinimumSize(new Dimension(180, 15));
		    label.setMaximumSize(new Dimension(180, 15));
		    
		    captionPanel.add(label);
		    captionPanel.add(Box.createRigidArea(new Dimension(20,0)));
		    
			JScrollPane scroll = createSingleList(data.getInt(1));
			
			centerPanel.add(scroll);
			centerPanel.add(Box.createRigidArea(new Dimension(10,0)));
			
		}
		
		topPanel.add(captionPanel);
		topPanel.add(Box.createRigidArea(new Dimension(0,5)));
		topPanel.add(centerPanel);
		
		return topPanel;
	}
	
	
	
	/**
	 * Funkcja tworzy dolny panel
	 * @return dolny panel
	 */
	public JPanel createBottomPanel()
	{
		JPanel southPanel= new JPanel();
		
		southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.X_AXIS));

		currentOrderList = new JList();
		
		currentOrderList.setAlignmentX(Component.LEFT_ALIGNMENT);
		currentOrderList.setAlignmentY(Component.TOP_ALIGNMENT);
		currentOrderList.setModel(model.getCurrentOrderModel());

		CurrentOrderCellRenderer leftRenderer = new CurrentOrderCellRenderer();
		leftRenderer.setTextPosition(1, SwingConstants.CENTER);
		
		currentOrderList.setCellRenderer(leftRenderer);
		currentOrderList.setPreferredSize(new Dimension(300, 200));
		currentOrderList.setMinimumSize(new Dimension(300, 200));
		currentOrderList.setMaximumSize(new Dimension(400, 200));
		
		JPanel middlePanel= new JPanel();
		middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.X_AXIS));
		middlePanel.setAlignmentY(Component.TOP_ALIGNMENT);
		middlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		deleteButton = new JButton("delete");
		deleteButton.setEnabled(false);
		deleteButton.setAlignmentY(Component.TOP_ALIGNMENT);
		deleteButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		middlePanel.add(deleteButton);
		middlePanel.add(Box.createRigidArea(new Dimension(5, 1)));
		
		deleteAllButton = new JButton("delete all");
		deleteAllButton.setEnabled(false);
		deleteAllButton.setAlignmentY(Component.TOP_ALIGNMENT);
		deleteAllButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		middlePanel.add(deleteAllButton);
		middlePanel.add(Box.createRigidArea(new Dimension(5, 1)));
		
		setOrderButton = new JButton("save order");
		setOrderButton.setEnabled(false);
		
		setOrderButton.setAlignmentY(Component.TOP_ALIGNMENT);
		setOrderButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		middlePanel.add(setOrderButton);
		middlePanel.add(Box.createRigidArea(new Dimension(5, 1)));
		
		Controler.DeleteSingleAction deleteSingleAction = controler.new DeleteSingleAction();
		deleteButton.addActionListener(deleteSingleAction);
		
		Controler.DeleteAllAction deleteAllAction = controler.new DeleteAllAction();
		deleteAllButton.addActionListener(deleteAllAction);
		
		Controler.SetOrderAction setOrderAction = controler.new SetOrderAction();
		setOrderButton.addActionListener(setOrderAction);
		
		
		
		JPanel leftPanel= new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		leftPanel.setAlignmentY(Component.TOP_ALIGNMENT);
		leftPanel.add(currentOrderList);
		leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		leftPanel.add(middlePanel);

		
		    preparingOrdersRenderer = new PreparingOrderCellRenderer(2);
		
			right = new JList();
			right.setCellRenderer(preparingOrdersRenderer);
			right.setModel(model.getPreparingOrderModel());
			
			
		right.setAlignmentY(Component.TOP_ALIGNMENT);	
		//right.setPreferredSize(new Dimension(300, 200));
		//right.setMinimumSize(new Dimension(300, 200));
		//right.setMaximumSize(new Dimension(400, 200));
		
		
		
		JScrollPane rightScrollPane = new JScrollPane(right);
		rightScrollPane.setAlignmentY(Component.TOP_ALIGNMENT);
		rightScrollPane.setPreferredSize(new Dimension(300, 200));
		rightScrollPane.setMinimumSize(new Dimension(300, 200));
		rightScrollPane.setMaximumSize(new Dimension(400, 200));
	
		
		rightScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		rightScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		rightScrollPane.setViewportView(right);
		
		
		southPanel.add(leftPanel);
		southPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		southPanel.add(rightScrollPane);
		
		
		return southPanel;		
	}
	
	/**
	 * Funkcja sprawdzajaca czy jest zaznaczony element na liscie biezacych zamowien
	 * @return tak / nie 
	 */
	public boolean isCurrentOrderNotSelected()
	{
		return currentOrderList.isSelectionEmpty();
	}
	
	/**
	 * Funkcja ustawiajaca guzik usuwajacy pojedynczy element
	 */
	public void setDeleteButton(boolean b)
	{
		deleteButton.setEnabled(b);
	}
	
	/**
	 * Funkcja ustawiajaca guzik usuwajacy wszystkie zamowienia
	 */
	public void setDeleteAllButton(boolean b)
	{
		deleteAllButton.setEnabled(b);
	}
	
	/**
	 * Funkcja ustawiajaca guzik potwierdzajacy zamowienie
	 */
	public void setSetOrderButton(boolean b)
	{
		setOrderButton.setEnabled(b);
	}
	

	
	/**
	 * Funkcja wyswietlajaca biezace zamowienia
	 * @param size ilosc elementow na liscie
	 * @param index numer elementu
	 */
	public void drawCurrentOrderList(int size, int index)
	{                            
              if (size != 0)
              {
                  	if (size == index)
                  	{
                  		currentOrderList.setSelectedIndex(index-1);
                  	}
      	            	else
      	            	{
      	            		currentOrderList.setSelectedIndex(index);
      	            	}
              }
	              else
	              {
	          			setDeleteAllButton(false);
	          			setDeleteButton(false);
	          			setSetOrderButton(false);
	              }      
	}
	
	/**
	 * Funkcja zwracajaca numer zaznaczonego indeksu
	 * @return numer zaznaczonego indeksu
	 */
	public int getCurrentOrderListSelection()
	{
		return currentOrderList.getSelectedIndex();
	}
	
	
	/**
	 * Funkcja zwracajaca ilosc w ktorej trzeba wyswietlic dane
	 * @return ilosc kolumn
	 */
	public int getPreparingCols()
	{
		return preparingOrdersRenderer.getCols();
	}
	
	/**
	 * Funkcja dodajaca dodatkowe zaznaczenie elementu
	 * @param index numer elementu do zaznaczenia
	 */
	public void setPreparingOrderSelection(int index)
	{
		preparingOrdersRenderer.setBackground(index);
		right.setSelectedIndex(index);
		right.clearSelection();
		
	}
	
	/**
	 * Funkcja usuwajaca dadatkowe zaznaczenie elementu
	 * @param index numer elementu do odznaczenia
	 */
	public void removePreparingOrderSelection(int index)
	{
		preparingOrdersRenderer.removeSpecial(index);
	}
	

}
