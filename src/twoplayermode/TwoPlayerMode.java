package twoplayermode;

import screen.Screen;
import entity.Ship;
import engine.GameState;
import java.awt.event.KeyEvent;
import java.util.Set;
import Enemy.PiercingBullet;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.awt.Rectangle;
import engine.DrawManager;
import engine.Frame;

/**
 * Two-player mode screen.
 */
public class TwoPlayerMode extends Screen {

    private PlayerShip player1;
    private PlayerShip player2;
    private Set<PiercingBullet> bullets;  // PiercingBullet 목록 관리
    private GameState gameState;
    private DrawManager drawManager;

    // 생성자에서 두 명의 플레이어와 게임 상태를 초기화
    public TwoPlayerMode(GameState gameState) {
        super(630, 720, 60);  // 기본 설정으로 Screen 초기화
        this.gameState = gameState;
        player1 = new PlayerShip(50, 400, "resources/player1.png");  // 1P
        player2 = new PlayerShip(250, 400, "resources/player2.png");  // 2P
        bullets = new HashSet<>(); // bullets 초기화
        drawManager = DrawManager.getInstance();
    }

    // 키 입력 처리

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                player1.moveLeft();
                break;
            case KeyEvent.VK_D:
                player1.moveRight();
                break;
            case KeyEvent.VK_LEFT:
                player2.moveLeft();
                break;
            case KeyEvent.VK_RIGHT:
                player2.moveRight();
                break;
            case KeyEvent.VK_W:
                player1.shoot(bullets);
                break;
            case KeyEvent.VK_UP:
                player2.shoot(bullets);
                break;
        }
    }

    // 두 플레이어 상태 업데이트
    public void update() {
        player1.update();
        player2.update();
        updateBullets();
        checkCollisions();
    }

    // 총알 상태 업데이트
    private void updateBullets() {
        // 각 총알 업데이트 로직 (이동 등)
        for (PiercingBullet bullet : bullets) {
            bullet.update();
        }
    }

    // 충돌 처리
    private void checkCollisions() {
        // 충돌 처리 로직 추가 필요
    }

    // 충돌 검사 메서드
    private boolean checkCollision(Ship ship, PiercingBullet bullet) {
        Rectangle shipBounds = new Rectangle(ship.getPositionX(), ship.getPositionY(), ship.getWidth(), ship.getHeight());
        Rectangle bulletBounds = new Rectangle(bullet.getPositionX(), bullet.getPositionY(), bullet.getWidth(), bullet.getHeight());
        return shipBounds.intersects(bulletBounds);
    }

    // 두 플레이어의 렌더링

    public void draw(Graphics2D g) {
        drawManager.drawEntity(player1, player1.getPositionX(), player1.getPositionY());
        drawManager.drawEntity(player2, player2.getPositionX(), player2.getPositionY());

        // 총알 렌더링
        for (PiercingBullet bullet : bullets) {
            drawManager.drawEntity(bullet, bullet.getPositionX(), bullet.getPositionY());
        }
    }

    // GameState 반환 메소드
    public GameState getGameState() {
        return gameState;
    }
}
