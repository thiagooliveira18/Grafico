package graficos;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;


public class Game extends Canvas implements Runnable{
	
	public static JFrame frame;
	
	private boolean isRunning;
	private Thread thread;
	
	//Dimens�o da janela.
	private final int WIDTH = 160;
	private final int HEIGHT = 120;
	private final int SCALE = 5;
	
	//Importando o BufferedImage.
	private BufferedImage image;
	
	private Spritesheet sheet;
	private BufferedImage[] player;
	private int frames = 0;
	private int maxFrames = 20;
	private int curAnimation = 0, maxAnimation = 3;
	
	private int x = 0;
	
	public Game() {
		sheet = new Spritesheet("/spritesheet.png"); //pegando o spritesheet.png
		//player = sheet.getSprite(0, 0, 75, 75);//Colocando o player com sprite.
		player = new BufferedImage[3];
		player[0] = sheet.getSprite(0, 0, 75, 75);
		player[1] = sheet.getSprite(75, 0, 75, 75);
		player[2] = sheet.getSprite(150, 0, 75, 75);
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		initFrame();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);//instanciando o BufferedImage.
	}
	
	public void initFrame() {
		frame = new JFrame("�lion Online");
		frame.add(this);//implementando todas as fun��es do JFrame.
		frame.setResizable(false);//n�o disponibiliza altera��o da dimens�o para o usu�rio.
		frame.pack();//m�todo do frame para calcular certas dimens�es e mostrar.
		frame.setLocationRelativeTo(null);//para a janela aparecer no centroda tela.
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//para quando clicar em fechar finalizar tamb�m o programa.
		frame.setVisible(true);//para quando inicializar ja estar visiv�l.
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) {
		Game game = new Game();
		game.start();
	}
	
	public void Tick() {
		//x++;
		frames++;
		if(frames > maxFrames) {
			frames = 0;
			curAnimation++;
			if(curAnimation >= maxAnimation) {
				curAnimation = 0;
			}
		}
	}
	
	public void Render() {
		BufferStrategy bs = this.getBufferStrategy();
		
		//para caso n�o houver um BufferStrategy ele ir� criar.
		if(bs == null) {
			this.createBufferStrategy(3);
			return;// Para quebrar o if.
		}
		
		Graphics g = image.getGraphics();//Para come�ar a renderizar graficos.
		g.setColor(new Color(121,213,229));//Para setar cor que ir� mostrar na tela.
		g.fillRect(0, 0, WIDTH, HEIGHT);//Para renderizar um ret�ngulo.
		/*
		g.setColor(Color.CYAN);
		g.fillRect(20, 20, 20, 20);
		
		g.setColor(Color.RED);
		g.fillOval(50, 50, 20, 20);
		*/
		
		//Para Mostrar String.
		g.setFont(new Font("Arial", Font.BOLD, 9));
		g.setColor(Color.white);
		g.drawString("Bem Vindo ao Jogo!", 40, 20);
		
		/* Renderiza��o do Player */
		
		/*Rota��o do Player
		Graphics2D g2 = (Graphics2D)esse parenteses � o cast g;
		g2.rotate(Math.toRadians(45),20+37,20+37);
		*/
		
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(player[curAnimation], x, 20, null);
		/***/
		
		g.dispose();
		g = bs.getDrawGraphics();//Pegando os graficos principal.
		g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);//Desenhando na tela.
		bs.show();//Para mostrar os gr�ficos.
	}
	
	@Override
	public void run() {
		long lastTime = System.nanoTime();//Pega o tempo atual do PC em nanosegundo.
		double amountOfTicks = 60.0;//para determinar quantos Frames por segundo.
		double ns = 1000000000 / amountOfTicks;//pega 1 segundo em formato de nano segundo e divide pelos frames p/ seconds.
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		while(isRunning) {
			long now = System.nanoTime();// pega o tempo atual em nano segundo
			delta += (now - lastTime) / ns;
			lastTime = now;
			
			if(delta >= 1) {
				Tick();
				Render();
				frames++;
				delta--;
			}
			if(System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: " + frames);
				frames = 0;
				timer += 1000;
			}
		}
		
	}
	
	
	
}
