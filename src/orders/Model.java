/**
 * 
 */
package orders;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Klasa gromadzaca dane aplikacji
 * @author Piotr Siczek
 */
public class Model 
{
    private static String USER_NAME = "root";
    private static String PASSWORD = "root";
	private OrderListModel currentOrderModel;
	private PreparingOrderModel preparingOrderModel;

	
	public Model()
	{
		System.out.println("model konstruktor");
		currentOrderModel = new OrderListModel();
		
		preparingOrderModel = new PreparingOrderModel();
		//preparingOrderModel.addElement("Colombian,1;golden tips,1;sernik,1;szarlotka,1;silver tips,1;");
	}
	
	/**
	 * Funkcja pobierajaca kategorie produktow
	 * @return wynik zapytania do bazy zawierajacy kategorie produktow
	 * @throws SQLException
	 */
	public ResultSet getCategories() throws SQLException
	{

        //hp lap connection object
        //Connection con = DriverManager.getConnection("jdbc:mysql://localhost/cafeteria", "root", "");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cafeteria", USER_NAME, PASSWORD);
	 	Statement stm = null;
	    String query = "SELECT * FROM categories";
	
	    
	      //Class.forName("com.mysql.jdbc.Driver").newInstance();
	      	
	         stm = con.createStatement();
	
	            ResultSet rs = stm.executeQuery(query);
	            
			return rs;	
	}
	
	/**
	 * Funkcja pobierajaca z bazy danych produkty o szukanym id
	 * @param value id kategorii produktow ktore szukamy
	 * @return wynik zapytania do bazy danych zawierajacy produkty o podanym id
	 * @throws SQLException
	 */
	public ResultSet getContent(int value) throws SQLException
	{

        //hp lap connection object
        //Connection con = DriverManager.getConnection("jdbc:mysql://localhost/cafeteria", "root", "");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cafeteria", USER_NAME, PASSWORD);
	 	Statement stm = null;
	    String query = "SELECT * FROM products WHERE category_id=" + value;
	
	    
	      //Class.forName("com.mysql.jdbc.Driver").newInstance();
	      	
	         stm = con.createStatement();
	
	            ResultSet rs = stm.executeQuery(query);
	            
			return rs;	
	}
	
	/**
	 * Funkcja zapisujaca zamowienie do bazy danych
	 */
	public void setOrder()
	{
		 Statement stmt = null;
		 Statement stm = null;
		 double totalPrice = 0;
		    try 
		    {
                //hp lap connection object
                //Connection con = DriverManager.getConnection("jdbc:mysql://localhost/cafeteria", "root", "");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cafeteria", USER_NAME, PASSWORD);
		    	
		        stmt = con.createStatement();
		        //pstmt = con.prepareStatement("INSERT INTO orders VALUES(NULL, CURDATE(), CURTIME(), '0')", stmt.RETURN_GENERATED_KEYS);
		      /*  
		       pstmt.executeUpdate();
		       ResultSet keys = pstmt.getGeneratedKeys();
		       keys.next();
		       int key = keys.getInt(1);
		        */       
		        stmt.executeUpdate("INSERT INTO orders VALUES(NULL, CURDATE(), CURTIME(), '0')", Statement.RETURN_GENERATED_KEYS);
		        
		        ResultSet keys = stmt.getGeneratedKeys(); // wskazuje przed pierwszym wierszem
		        keys.next();
		        int key = keys.getInt(1);
		        
		        currentOrderModel.display();
		        		     
		        System.out.println("size" + currentOrderModel.getSize());
		        
		        
				for (int i=0; i < currentOrderModel.getSize(); i++)
				{
					System.out.println("iteration" + i);
					
					Row row = (Row)currentOrderModel.getElementAt(i);
					int id = currentOrderModel.getId(i);
					
					System.out.println("id: ..........." + id);
					
						String query = "SELECT * FROM products WHERE category_id='" + id +"' AND name='" + row.getName() +"'";
			      	
			         	stm = con.createStatement();
			
			            ResultSet rs = stm.executeQuery(query);
			            rs.next();
					
					totalPrice += row.getPrice();
					
					stmt.executeUpdate("INSERT INTO ingredients VALUES(NULL, " + key + ",  '" + rs.getInt(1) + "', '" + row.getQty() + "', '" + row.getPrice() + "')");
					//rs.close();
				}
				
				
				
				stmt.executeUpdate("UPDATE orders SET price='" + totalPrice + "' WHERE id=' " + key + "'");			
		        
		        
				stm.close();
		        keys.close();
		        stmt.close();
		        con.close();
		    } catch (SQLException e) {
		        System.out.println(e);
		    	
		    	//e.printStackTrace();
		    }
		
	}
	
	
	/**
	 * Funkcja tworzaca wiadomosc do przeslania
	 * @return widomosc do przeslania
	 */
	public String createMsg()
	{

		System.out.println("msg");
		String msg = "";
		
		for (int i=0; i < currentOrderModel.getSize(); i++)
		{
			Row row = (Row)currentOrderModel.getElementAt(i); 
			
			msg += row.getName() + "," + row.getQty() + ";";		
		}

		return msg;
	}
	
	/**
	 * Funkcja wysylajaca wiadomosc
	 * @param msg wiadomosc do wyslania
	 */
	public void sendOrder(String msg)
	{
		
		System.out.println("wysylam...");
		
		Socket socket = null;
        OutputStream out = null;
 
        try {
            socket = new Socket("localhost", 4444);
            
            out = socket.getOutputStream();
            out.write(msg.getBytes());

            socket.close();
            
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("No message received");
            System.exit(1);
        }
        
    }
	
	/**
	 * Funkcja pobierajaca wiadomosc
	 * @return pobrana wiadomosc
	 */
	public String getData()
	{
        String order = "";
    	ServerSocket serverSocket = null;
    	InputStream in = null;
		
        try {
        	
            serverSocket = new ServerSocket(4445);
                
        } catch (IOException e) {
            System.err.println("Could not listen on port: 4444.");
            System.exit(1);
        }
 
        Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();
            
            in = clientSocket.getInputStream();           
            
            int c;
            
            while ((c = in.read()) != -1)
            {       	
            	order += (char)c;
            }
                        
            serverSocket.close();
            clientSocket.close();
           
            
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        
         return order;
	}
		

	/**
	 * Funkcja pobierajaca model listy z zamowieniami
	 * @see OrderListModel
	 * @return model listy z zamowieniami
	 */
	public OrderListModel getCurrentOrderModel()
	{
		return currentOrderModel;
	}
	
	public PreparingOrderModel getPreparingOrderModel()
	{
		return preparingOrderModel;
	}
	
	/**
	 * Funkcj usuwajaca pojedynczy element
	 * @param index numer elementu do usuniecia
	 * @return ilosc pozostalych elementow w liscie
	 */
	public int removeSingleItem(int index)
	{  		
        currentOrderModel.removeElementAt(index);     
       
        	return currentOrderModel.getSize();
	}
	
	/**
	 * Funkcja usuwajaca wszystkie elementy w grupie
	 * @param index numer elementu do usuniecia
	 * @return ilosc pozostalych elementow w liscie
	 */
	public int removeAllItems(int index)
	{
		return currentOrderModel.removeAll(index);
      		
	}
	
	/**
	 * Funkcja usuwajaca wszystkie elementy na liscie
	 */
	public void removeAllItems()
	{
		currentOrderModel.removeAll();
	}
	
	public void test()
	{
		currentOrderModel.display();
	}

	
	/**
	 * Funkcja aktualizujaca model biezacych zamowien
	 * @param name nazwa produktu
	 * @param price cena produktu
	 * @param id id kategorii produktu
	 * @return flage 1 gdy dodano nowy element, 0 gdy zaktualizowano ilosc elementow
	 */
	public int refreshCurrentOrder(String name, double price, int id)
	{

		for (int i=0; i < currentOrderModel.getSize(); i++)
		{
			Row row = (Row)currentOrderModel.getElementAt(i); 
			
			System.out.println("asdf" + row.getPrice());
			
			if (row.getName() == name)
			{			
				currentOrderModel.updateElement(i, name, row.getQty()+1, row.getPrice()+price);
				return 0;				
			}
		}
		
			currentOrderModel.addElement(name, 1, price, id);
			return 1;
	}
	
	/**
	 * Funkcja aktualizujaca model zlozonych zamowien
	 * @param data dane
	 * @param colsNumber ilosc wyswietlanych kolumn
	 */
	public void refreshPreparingOrder(String data, int colsNumber)
	{
		 int j = 0;
		 String result = "";
		
		 String[] cols = new String[colsNumber];
		 String[] ingredients = data.split(";");
    	 
    	 
		 for (int i = 0; i < colsNumber; i++)
		 {
			 cols[i] = "<html>";
		 }
    		 
		
    	 
		for (int i=0; i < ingredients.length; i++)
		{
			String[] caption = ingredients[i].split(",");

			cols[j%colsNumber] += caption[0] + "<br>";
			cols[(j+1)%colsNumber] += caption[1] + "<br>";
			
			j+=2;			
		}

		for (int i = 0; i < colsNumber; i++)
	   	{
			if (cols[i].length() > 6)
	   		 cols[i] = cols[i].substring(0, cols[i].length()-4);
	   		 cols[i] += "</html>";
	   		 
	   		 result += cols[i] + ";";
	   	}

		preparingOrderModel.addElement(result);
	
	}
	
	/**
	 * Funkcja usuwajaca element z modelu elementow przygotowywanych
	 * @param index numer elementu do usuniecia
	 */
	public void removePreparedItem(int index)
	{
		preparingOrderModel.removeElementAt(index);	
	}

}
