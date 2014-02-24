/**
 * 
 */
package orders;

import java.sql.SQLException;


/**
 * Main
 * @author Piotr Siczek
 */
public class Orders {

	public static void main(String[] args) 
	{	
		try {
		
			Model model = new Model();
			Controler controler = new Controler(model);
			View orderView = new View(model, controler);
			controler.setView(orderView);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
