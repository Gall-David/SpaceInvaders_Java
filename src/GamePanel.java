import java.awt.Color;
import java.awt.Font;
import java.io.*;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements KeyListener {
	private static final long serialVersionUID = -1036125108881682872L;
	private List<Asteroid> asteroids;
	private List<Projectile> projectiles;
	private RocketShip rocketShip;
	private Timer timer;
	private int score;

	private int end; // 0 - the game is still in progress
						// 1 - the game has ended

	public GamePanel() {
		setBackground(Color.BLACK);

		asteroids = new ArrayList<>();
		projectiles = new ArrayList<>();
		rocketShip = new RocketShip(Globals.WINDOW_WIDTH / 2, Globals.SHIP_HEIGHT);

		this.end = 0;
		this.score = 1000;

		addKeyListener(this);
		setFocusable(true);

		timer = new Timer(10, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				updateGameState("");
				repaint();
			}
		});
		timer.start();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		rocketShip.paintComponent(g);
		for (Asteroid asteroid : asteroids) {
			asteroid.paintComponent(g);
		}
		for (Projectile projectile : projectiles) {
			projectile.paintComponent(g);
		}
		// start and end screen, also score
		if (end == 0) {
			g.setColor(Color.GREEN);
			g.setFont(new Font("TimesRoman", 1, 20));
			g.drawString("Score: " + score, 10, 20);
		}
		if (end == 1) {
			int highScore = checkHighScore();
			
			g.setColor(Color.GREEN);
			g.setFont(new Font("TimesRoman", 1, 20));
			g.drawString("Score: " + score, 10, 20);
			g.setColor(Color.RED);
			g.setFont(new Font("TimesRoman", Font.BOLD, 60));
			g.drawString("GAME OVER!", 150, 200);
			
			if(this.score >= highScore) {
				//update the highScore file
				updateHighScore(this.score);
				g.setColor(Color.GREEN);
				g.setFont(new Font("TimesRoman", Font.BOLD, 40));
				g.drawString("NEW HIGHSCORE: " + this.score, 120, 300);
			}
			else {
				g.setColor(Color.GREEN);
				g.setFont(new Font("TimesRoman", Font.BOLD, 40));
				g.drawString("HIGHSCORE: " + highScore, 165, 300);
			}
		}
	}

	public int checkHighScore() {
		File file = new File("highScore.txt");
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			
			//convert the highScore from string to int using java8 streams
			int num = Arrays.stream(line.split(""))
	                .mapToInt(Integer::parseInt)
	                .reduce(0, (x, y) -> x * 10 + y);
			
			reader.close();
			return num;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR: Highscore file open");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR: line Reader at Highscore");
		}
		return 0;
	}
	
	public void updateHighScore(int newScore) {
		//empty the text file to get rid of its current contents
		try {
			FileWriter fw = new FileWriter("highScore.txt");
			BufferedWriter bw = new BufferedWriter(fw);
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR: opening fileWriter1 at highscore update");
		}
		
		//convert the newScore to string using java8 streams
		 String newScoreString = IntStream.of(newScore)
                 .mapToObj(String::valueOf)
                 .findFirst()
                 .get();
		 
		 //write the newScore into the now empty highScore.txt file
		 FileWriter fw;
		try {
			fw = new FileWriter("highScore.txt");
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(newScoreString);
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR: opening fileWriter2 at highscore update");
		}
	}
	
	public boolean isColliding(RocketShip object1, Asteroid object2) {
		int object1X = object1.getX();
		int object1Y = object1.getY();
		int object1Width = object1.getWidth() - 50;
		int object1Height = object1.getHeight() - 50;

		int object2X = object2.getX();
		int object2Y = object2.getY();
		int object2Width = object2.getWidth() - 50;
		int object2Height = object2.getHeight() - 50;

		if (object1X < object2X + object2Width && object1X + object1Width > object2X
				&& object1Y < object2Y + object2Height && object1Y + object1Height > object2Y) {
			return true;
		}

		return false;
	}

	public boolean isColliding(Asteroid object1, Projectile object2) {
		int object1X = object1.getX();
		int object1Y = object1.getY();
		int object1Width = object1.getWidth() - 30;
		int object1Height = object1.getHeight() - 30;

		int object2X = object2.getX();
		int object2Y = object2.getY();
		int object2Width = object2.getWidth() - 5;
		int object2Height = object2.getHeight() - 5;

		if (object1X < object2X + object2Width && object1X + object1Width > object2X
				&& object1Y < object2Y + object2Height && object1Y + object1Height > object2Y) {
			return true;
		}

		return false;
	}

	public void updateGameState(String direction) {
		System.out.println("Score: " + score + "\n");

		rocketShip.move(direction);
		while (asteroids.size() < Globals.NUM_OF_ASTEROIDS) {
			asteroids.add(new Asteroid());
			// System.out.println("asteroid added\n");
		}

		for (int i = 0; i < asteroids.size(); i++) {
			Asteroid asteroid = asteroids.get(i);
			asteroid.move();
			if (asteroid.isOutOfBounds()) {
				asteroids.remove(i);
				i--;
			}
		}

		for (Projectile projectile : projectiles) {
			projectile.move();
			// projectile is out of bounds
			if (projectile.getX() - (projectile.getHeight() / 2) > Globals.WINDOW_HEIGHT) {
				projectiles.remove(projectile);
			}
		}
		// check for collisions
		// asteoids with projectiles
		for (int i = 0; i < asteroids.size(); i++) {
			for (int j = 0; j < projectiles.size(); j++) {
				if (isColliding(asteroids.get(i), projectiles.get(j))) {
					playAsteroidDestroyedSound();
					asteroids.remove(asteroids.get(i));
					projectiles.remove(projectiles.get(j));
					// score increase
					score += 350;
				}
			}
		}

		// asteroids with rocketship
		for (int i = 0; i < asteroids.size(); i++) {
			if (isColliding(rocketShip, asteroids.get(i))) {
				// rocketShip and asteroid have collided
				asteroids.remove(asteroids.get(i));
				// game over logic
				this.end = 1;
				asteroids.clear();
				projectiles.clear();
				timer.stop();
				System.out.println("You died " + score);
			}
		}

	}

	public void playShootingSound() {
		try {
			AudioInputStream in = AudioSystem.getAudioInputStream(new File("audio/shooting.wav"));
			Clip clip = AudioSystem.getClip();
			clip.open(in);
			clip.start();
		} catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
			System.out.println("ERROR: playShootingSound");
		}
	}

	public void playAsteroidDestroyedSound() {
		try {
			AudioInputStream in = AudioSystem.getAudioInputStream(new File("audio/asterDestroid.wav"));
			Clip clip = AudioSystem.getClip();
			clip.open(in);
			clip.start();
		} catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
			System.out.println("ERROR: playAsterDestroSound");
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (end != 1) {
			if (e.getKeyChar() == ' ') {
				if (score > 299) {
					playShootingSound();
					Projectile projectile = new Projectile(rocketShip.getX() + rocketShip.getWidth() / 2,
							rocketShip.getY());
					projectiles.add(projectile);
					score -= 300;
				}
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (end != 1) {
			// TODO Auto-generated method stub
			int keyCode = e.getKeyCode();

			if (keyCode == KeyEvent.VK_RIGHT) {
				updateGameState("right");
			}
			if (keyCode == KeyEvent.VK_LEFT) {
				updateGameState("left");
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

}
