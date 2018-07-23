import java.util.Random;

/**
 * Created by David on 8/9/17.
 */
public class EnemyBot extends EnemyTank {
    EnemyBot(int x, int y, Direction dir, TankClient tc) {
        super(x, y, dir, tc);
        life = 20;
        teamate = true;
    }
    private static Random r = new Random();
    private int step = r.nextInt(12) + 3;

    @Override
    public void move(){
        if (this.dir != Direction.STOP) {
            this.ptDir = this.dir;
        }
        switch(dir) {
            case L:
                x -= XSPEED;
                break;
            case U:
                y -= YSPEED;
                break;
            case R:
                x += XSPEED;
                break;
            case D:
                y += YSPEED;
                break;
            case STOP:
                break;
        }
        if (x < 0) x = 0;
        if (y < 30) y = 30;
        if (x + WIDTH > TankClient.GAME_WIDTH) x = TankClient.GAME_WIDTH - WIDTH;
        if (y + HEIGHT > TankClient.GAME_HEIGHT) y = TankClient.GAME_HEIGHT - PlayerTank.HEIGHT;
        Direction[] dirs = Direction.values();
        if (step == 0) {
            step = r.nextInt(12) + 3;
            int rn = r.nextInt(dirs.length);
            dir = dirs[rn];
        }
        step--;
        if (r.nextInt(40) > 38) this.fire();

    }

}
