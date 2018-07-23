
import java.awt.*;
/**
 * Created by David on 7/24/17.
 */
class Home extends MapBlock {
    static int HOME_LENGTH = PlayerTank.HEIGHT;
    int x;
    int y;
    TankClient tc;


    Home(TankClient tc) {
        super(TankClient.GAME_WIDTH/2 , TankClient.GAME_HEIGHT-HOME_LENGTH, 1,1, BrickType.HOME,tc);
        this.tc = tc;
        x = TankClient.GAME_WIDTH/2;
        y = TankClient.GAME_HEIGHT-HOME_LENGTH;
        isHome = true;
    }

}