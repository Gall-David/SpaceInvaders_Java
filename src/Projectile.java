import java.awt.Color;
import java.awt.Graphics;

public class Projectile implements GameElement{
	private int x;
	private int y;
	private int velocity;
	
	public Projectile(int x, int y) {
		this.x = x;
		this.y = y;
		this.velocity = Globals.PROJECTILE_SPEED;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public void paintComponent(Graphics g) {
		int size = Globals.PROJECTILE_SIZE;
		g.setColor(Color.RED);
		g.fillOval(this.x, this.y, size, size);
	}
	
	public void move() {
		this.y -= velocity;
	}
	
	@Override
	public int getHeight() {
		return Globals.PROJECTILE_SIZE;
	}

	@Override
	public int getWidth() {
		return Globals.PROJECTILE_SIZE;
	}
}
