package christian.aquino.strategies;

import robocode.*;

public abstract class ActiveStrategy extends Strategy {
    IStrategy activeStrategy;

    public ActiveStrategy(AdvancedRobot robot) {
        super(robot);
    }

    public abstract void checkActiveStrategy();

    @Override
    public void run() {
        activeStrategy.run();
    }

    @Override
    public void initialize() {
        activeStrategy.initialize();
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        activeStrategy.onScannedRobot(e);
    }

    @Override
    public void onHitByBullet(HitByBulletEvent e) {
        activeStrategy.onHitByBullet(e);
    }

    @Override
    public void onHitRobot(HitRobotEvent e) {
        activeStrategy.onHitRobot(e);
    }

    @Override
    public void onHitWall(HitWallEvent e) {
        activeStrategy.onHitWall(e);
    }
}
