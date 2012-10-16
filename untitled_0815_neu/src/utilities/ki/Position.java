package utilities.ki;
/**
 * 
 * @author Johannes Riedel
 * kapselt x und y-Koordinate eines Chips
 */
public class Position {
	private byte x,y;
	
	public Position(byte x, byte y)
		{
		this.x = x;
		this.y = y;
		}

	public byte getX() {
		return x;
	}

	public void setX(byte x) {
		this.x = x;
	}

	public byte getY() {
		return y;
	}

	public void setY(byte y) {
		this.y = y;
	}

}
