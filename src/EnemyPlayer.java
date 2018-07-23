/**
 * Created by David on 8/8/17.
 */
public class EnemyPlayer extends EnemyTank {
    public EnemyPlayer(int x, int y, Direction dir, TankClient tc) {
        super(x, y, dir, tc);
        life = 100;
    }

    @Override
    public void move(){
        this.oldX = x;
        this.oldY = y;

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

        if(this.dir != Direction.STOP) {
            this.ptDir = this.dir;
        }

        if(x < 0) x = 0;
        if(y < 30) y = 30;
        if(x + WIDTH > TankClient.GAME_WIDTH) x = TankClient.GAME_WIDTH - WIDTH;
        if(y + HEIGHT > TankClient.GAME_HEIGHT) y = TankClient.GAME_HEIGHT - HEIGHT;

    }

    @Override
    public boolean eat(Blood b) {
        if(this.live && b.isLive() && this.getRect().intersects(b.getRect())) {
            this.life = 100;
            b.setLive(false);
            return true;
        }
        return false;
    }

    public void setTeam(Boolean b){
        teamate = b;
    }
}
