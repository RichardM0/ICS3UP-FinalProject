package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {



	public boolean upPressed;
	public boolean enterPressed;
	public boolean escPressed;
	
	@Override
	public void keyTyped(KeyEvent e) {
		// blank function (required to use)
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		int code = e.getKeyCode();

		if(code == KeyEvent.VK_SPACE || code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
			upPressed = true;
		}
		if(code == KeyEvent.VK_ENTER){
			enterPressed = true;

		}
		if(code == KeyEvent.VK_ESCAPE){
			escPressed = true;

		}


	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		
		if(code == KeyEvent.VK_SPACE || code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
			upPressed = false;
		}
		if(code == KeyEvent.VK_ENTER){
			enterPressed = false;
		}
		if(code == KeyEvent.VK_ESCAPE){
			escPressed = false;
		}
		
	}

}