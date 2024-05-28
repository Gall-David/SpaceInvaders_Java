import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Asteroid implements GameElement {
	private int x;
	private int y;
	private int velocity;
	private BufferedImage img;

	public Asteroid() {
		Random RANDOM = new Random();

		// random x
		this.x = RANDOM.nextInt(Globals.WINDOW_WIDTH - 20);

		// y
		this.y = 0;

		// velocity
		this.velocity = Globals.ASTEROID_VELOCITY;

		// image opening:
		try {
			img = ImageIO.read(new File("img/asteroid.png"));
		} catch (IOException e) {
			System.out.println("Error: Asteroid Image.png\n");
		}
	}

	public void paintComponent(Graphics g) {
		g.drawImage(img, this.x, this.y, null);
	}

	public void move() {
		this.y = this.y + this.velocity;
	}

	public boolean isOutOfBounds() {
		return x < 0 || x > Globals.WINDOW_WIDTH || y < 0 || y > Globals.WINDOW_HEIGHT;
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
