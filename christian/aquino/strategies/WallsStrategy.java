package christian.aquino.strategies;

import christian.aquino.strategies.Strategy;
import robocode.*;

/**
 * WallsStrategy - a strategy based on Walls sample robot behavior.
 * Walls robot was created by:
 * Mathew A. Nelson (original)
 * Flemming N. Larsen (contributor)
 */
public class WallsStrategy extends Strategy {
    boolean peek; // Don't turn if there's a robot there
    double moveAmount; // How much to move

    public WallsStrategy(AdvancedRobot robot) {
        super(robot);
    }

    @Override
    public void run() {
        // Look before we turn when ahead() completes.
        peek = true;
        // Move up the wall
        robot.ahead(moveAmount);
        // Don't look now
        peek = false;
        // Turn to the next wall
        robot.turnRight(90);
    }

    @Override
    public void initialize() {
        robot.setAdjustGunForRobotTurn(false);
        double robotHeading = robot.getHeading();
        double gunHeading = robot.getGunHeading();
        double adjust = gunHeading - robotHeading;
        robot.turnGunLeft(adjust);

        moveAmount = Math.max(robot.getBattleFieldWidth(), robot.getBattleFieldHeight());
        // Initialize peek to false
        peek = false;

        // turnLeft to face a wall.
        // getHeading() % 90 means the remainder of
        // getHeading() divided by 90.
        robot.turnLeft(robotHeading % 90);
        robot.ahead(moveAmount);

        // Turn the gun to turn right 90 degrees.
        peek = true;

        robot.turnGunRight(90);
        robot.turnRight(90);
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
       robot.fire(2);
        // Note that scan is called automatically when the robot is moving.
        // By calling it manually here, we make sure we generate another scan event if there's a robot on the next
        // wall, so that we do not start moving up it until it's gone.
        if (peek) {
            robot.scan();
        }
    }

    @Override
    public void onHitByBullet(HitByBulletEvent e) {
        // No action
    }

    @Override
    public void onHitRobot(HitRobotEvent e) {
        // If he's in front of us, set back up a bit.
        if (e.getBearing() > -90 && e.getBearing() < 90) {
            robot.back(100);
        } // else he's in back of us, so set ahead a bit.
        else {
            robot.ahead(100);
        }
    }

    @Override
    public void onHitWall(HitWallEvent e) {
        // No actions
    }

    @Override
    public void onBulletMissed(BulletMissedEvent event) {
        // NO actions
    }
}
