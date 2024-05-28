import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class RocketShip implements GameElement{
	private int x;
	private int y;
	private int velocity;
	private BufferedImage img;
	
	public RocketShip(int x, int y) {
		this.x = x;
		this.y = y;
		
		this.velocity = Globals.SHIP_VELOCITY;
		
		//img
		try {
			img = ImageIO.read(new File("img/rocketShip.png"));
		}catch(IOException e) {
			System.out.println("Error: RocketShip Image.png\n");
		}
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(img, this.x, this.y, null);
	}
	
	public void moveLeft() {
		if(this.x - this.velocity <= 0 ) {
			this.x = 0;
		}
		else {
			this.x -= this.velocity;
		}
	}
	
	public void moveRight() {
		if(this.x + this.velocity >= Globals.WINDOW_WIDTH - 100 ) {
			this.x = Globals.WINDOW_WIDTH - 100;
		}
		else {
			this.x += this.velocity;
		}
	}
	
	public void move(String direction) {
		if(direction == "left") {
			moveLeft();
		}
		else if(direction == "right") {
			moveRight();
		}
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

	public int getHeight() {
		return img.getHeight();
	}
	
	public int getWidth() {
		return img.getWidth();	
	}
}
