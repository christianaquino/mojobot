package christian.aquino;

import christian.aquino.strategies.ActiveStrategy;
import christian.aquino.strategies.CitiHackatonStrategy;
import robocode.*;
import java.awt.Color;

/**
 * Mojobot - a multi strategy robot
 * @author Christian Aquino <aqn.christian@gmail.com>
 */
public class Mojobot extends AdvancedRobot
{
	private ActiveStrategy strategy = new CitiHackatonStrategy(this);

	public void run() {
		setColors(Color.red,Color.red,Color.black); // body,gun,radar
		strategy.initialize();
		while (true) {
			strategy.run();
		}
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		strategy.onScannedRobot(e);
	}

	public void onHitByBullet(HitByBulletEvent e) {
		strategy.onHitByBullet(e);
	}

	public void onHitWall(HitWallEvent event) {
		strategy.onHitWall(event);
	}

	public void onHitRobot(HitRobotEvent e) {
		strategy.onHitRobot(e);
	}

	@Override
	public void onBulletMissed(BulletMissedEvent event) {
		strategy.onBulletMissed(event);
	}
}
