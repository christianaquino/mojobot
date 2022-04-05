package christian.aquino.strategies;

import christian.aquino.strategies.Strategy;
import robocode.*;

import static robocode.util.Utils.normalRelativeAngleDegrees;

public class TrackFireStrategy extends Strategy {
    public TrackFireStrategy(AdvancedRobot robot) {
        super(robot);
    }

    @Override
    public void run() {
        robot.turnGunRight(10);
    }

    @Override
    public void initialize() {
        robot.setAdjustGunForRobotTurn(false);
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        // Calculate exact location of the robot
        double absoluteBearing = robot.getHeading() + e.getBearing();
        double bearingFromGun = normalRelativeAngleDegrees(absoluteBearing - robot.getGunHeading());

        // If it's close enough, fire!
        if (Math.abs(bearingFromGun) <= 3) {
            robot.turnGunRight(bearingFromGun);
            // We check gun heat here, because calling fire()
            // uses a turn, which could cause us to lose track
            // of the other robot.
            if (robot.getGunHeat() == 0) {
                robot.fire(Math.min(3 - Math.abs(bearingFromGun), robot.getEnergy() - .1));
            }
        } // otherwise just set the gun to turn.
        // Note:  This will have no effect until we call scan()
        else {
            robot.turnGunRight(bearingFromGun);
        }
        // Generates another scan event if we see a robot.
        // We only need to call this if the gun (and therefore radar)
        // are not turning.  Otherwise, scan is called automatically.
        if (bearingFromGun == 0) {
            robot.scan();
        }
    }

    @Override
    public void onHitByBullet(HitByBulletEvent e) {
        // No actions
    }

    @Override
    public void onHitRobot(HitRobotEvent e) {
        // No actions
    }

    @Override
    public void onHitWall(HitWallEvent e) {
        // No actions
    }

    @Override
    public void onBulletMissed(BulletMissedEvent event) {
        // No actions
    }
}
