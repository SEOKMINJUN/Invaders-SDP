package screen;

import java.awt.event.KeyEvent;
import java.io.IOException;

import engine.Cooldown;
import engine.Core;
// Sound Operator
import Sound_Operator.SoundManager;

/**
 * Implements the title screen.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
public class TitleScreen extends Screen {

	/** Milliseconds between changes in user selection. */
	private static final int SELECTION_TIME = 200;

	/** Time between changes in user selection. */
	private Cooldown selectionCooldown;

	// CtrlS
	private int coin;
	private int gem;
	// select One player or Two player
	private int pnumSelectionCode; //produced by Starter
	private int merchantState;

	/**
	 * Constructor, establishes the properties of the screen.
	 *
	 * @param width
	 *            Screen width.
	 * @param height
	 *            Screen height.
	 * @param fps
	 *            Frames per second, frame rate at which the game is run.
	 */
	public TitleScreen(final int width, final int height, final int fps) {
		super(width, height, fps);

		// Defaults to play.
		this.merchantState = 0;
		this.pnumSelectionCode = 0;
		this.returnCode = 2;
		this.selectionCooldown = Core.getCooldown(SELECTION_TIME);
		this.selectionCooldown.reset();

		// CtrlS: Set user's coin, gem
        try {
            this.coin = Core.getCurrencyManager().getCoin();
			this.gem = Core.getCurrencyManager().getGem();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Sound Operator
		SoundManager.getInstance().playBGM("mainMenu_bgm");

	}

	/**
	 * Starts the action.
	 *
	 * @return Next screen code.
	 */
	public final int run() {
		super.run();

		return this.returnCode;
	}

	/**
	 * Updates the elements on screen and checks for events.
	 */
	protected final void update() {
		super.update();

		draw();
		if (this.selectionCooldown.checkFinished() && this.inputDelay.checkFinished()) {
			if (inputManager.isKeyDown(KeyEvent.VK_UP) || inputManager.isKeyDown(KeyEvent.VK_W)) {
				previousMenuItem();
				this.selectionCooldown.reset();
				// Sound Operator
				SoundManager.getInstance().playES("menuSelect_es");
			}
			if (inputManager.isKeyDown(KeyEvent.VK_DOWN) || inputManager.isKeyDown(KeyEvent.VK_S)) {
				nextMenuItem();
				this.selectionCooldown.reset();
				// Sound Operator
				SoundManager.getInstance().playES("menuSelect_es");
			}

			// 2인 플레이어 모드 선택
			if (returnCode == 2) {
				if (inputManager.isKeyDown(KeyEvent.VK_LEFT) || inputManager.isKeyDown(KeyEvent.VK_A)) {
					moveMenuLeft();
					this.selectionCooldown.reset();
					// Sound Operator
					SoundManager.getInstance().playES("menuSelect_es");
				}
				if (inputManager.isKeyDown(KeyEvent.VK_RIGHT) || inputManager.isKeyDown(KeyEvent.VK_D)) {
					moveMenuRight();
					this.selectionCooldown.reset();
					// Sound Operator
					SoundManager.getInstance().playES("menuSelect_es");
				}
			}

			if (returnCode == 4) {
				if (inputManager.isKeyDown(KeyEvent.VK_LEFT) || inputManager.isKeyDown(KeyEvent.VK_A)) {
					nextMerchantState();
					this.selectionCooldown.reset();
					// Sound Operator
					SoundManager.getInstance().playES("menuSelect_es");
				}
				if (inputManager.isKeyDown(KeyEvent.VK_RIGHT) || inputManager.isKeyDown(KeyEvent.VK_D)) {
					previousMerchantState();
					this.selectionCooldown.reset();
					// Sound Operator
					SoundManager.getInstance().playES("menuSelect_es");
				}
			}


			if (inputManager.isKeyDown(KeyEvent.VK_SPACE))
				if(returnCode == 4) {
					// CtrlS: Attempt the purchase, apply the upgrade if successful. Log the event if it fails.
            try {
              if (Core.getCurrencyManager().spendCoin(50)) {
							  testStatUpgrade();
							  this.coin = Core.getCurrencyManager().getCoin(); // CtrlS: After spending coins, update this.coin with the reduced amount
						  } else {
							  Core.getLogger().info("You don't have enough coins!");
						  }
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          this.selectionCooldown.reset();
				}
			}
		}
	}

	// Use later if needed. -Starter
	// public int getPnumSelectionCode() {return this.pnumSelectionCode;}

	/**
	 * runs when player do buying things
	 * when store system is ready -- unwrap annotated code and rename this method
	 */
//	private void testCoinDiscounter(){
//		if(this.currentCoin > 0){
//			this.currentCoin -= 50;
//		}

//		try{
//			Core.getFileManager().saveCurrentCoin();
//		} catch (IOException e) {
//			logger.warning("Couldn't save current coin!");
//		}
//	}

	/**
	 * Shifts the focus to the next menu item.
	 */
	private void testStatUpgrade(){
		// CtrlS: testStatUpgrade should only be called after coins are spent
		if(this.merchantState == 1) {
			// this.currentCoin -= 50;
		}
		else if(this.merchantState == 2) {
			// this.currentCoin -= 50;
		}
		else if(this.merchantState == 3) {
			// this.currentCoin -= 50;
		}
		else if(this.merchantState == 4) {
			// this.currentCoin -= 50;
		}


	}
	private void nextMenuItem() {
		if (this.returnCode == 5) // Team Clover changed values because recordMenu added
			this.returnCode = 0; // from '2 player mode' to 'Exit' (starter)
		else if (this.returnCode == 0)
			this.returnCode = 2; // from 'Exit' to 'Play' (starter)
		else if (this.returnCode == 2)
			this.returnCode = 3; // from 'Play' to '2 Player Mode' (starter)
		else
			this.returnCode++; // go next (starter)
	}

	private void previousMenuItem() {
		this.merchantState = 0;
		if (this.returnCode == 0)
			this.returnCode = 5; // from 'Exit' to '2 player mode' (starter) // Team Clover changed values because recordMenu added
		else if (this.returnCode == 2)
			this.returnCode = 0; // from 'Play' to 'Exit' (starter)
		else if (this.returnCode == 3)
			this.returnCode = 2; // from '2 Player Mode' to 'Play' (starter)
		else
			this.returnCode--; // go previous (starter)
	}


	// left and right move -- produced by Starter
	private void moveMenuLeft() {
		if (this.returnCode == 2 ) {
			if (this.pnumSelectionCode == 0)
				this.pnumSelectionCode++;
			else
				this.pnumSelectionCode--;
		}

	}

	private void moveMenuRight() {
		if (this.returnCode == 2) {
			if (this.pnumSelectionCode == 0)
				this.pnumSelectionCode++;
			else
				this.pnumSelectionCode--;
		}
	}

	private void nextMerchantState() {
		if (this.returnCode == 4) {
			if (this.merchantState == 4)
				this.merchantState = 0;
			else
				this.merchantState++;
		}
	}

	private void previousMerchantState() {
		if (this.returnCode == 4) {
			if (this.merchantState == 0)
				this.merchantState = 4;
			else
				this.merchantState--;
		}
	}

	/**
	 * Draws the elements associated with the screen.
	 */
	private void draw() {
		drawManager.initDrawing(this);

		drawManager.drawTitle(this);
		drawManager.drawMenu(this, this.returnCode, this.pnumSelectionCode, this.merchantState);
		// CtrlS
		drawManager.drawCurrentCoin(this, coin);
		drawManager.drawCurrentGem(this, gem);

		super.drawPost();
		drawManager.completeDrawing(this);
	}

}
