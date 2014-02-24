package orders;

/**
 * Struktura przetrzymujaca rekord
 * @author Piotr Siczek
 *
 */
class Row
{
	private String name;
	private int qty;
	private double price;
	
	public Row(String name, int qty, double price)
	{
		this.name = name;
		this.qty = qty;
		this.price = price;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getQty()
	{
		return qty;
	}
	
	public double getPrice()
	{
		return price;
	}
}
