package christian.aquino.strategies;

import robocode.*;

public interface IStrategy {
    public void run();
    public void initialize();
    public void onScannedRobot(ScannedRobotEvent e);
    public void onHitByBullet(HitByBulletEvent e);
    public void onHitRobot(HitRobotEvent e);
    public void onHitWall(HitWallEvent e);
    public void onBulletMissed(BulletMissedEvent event);
}
