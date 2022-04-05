package christian.aquino.strategies;

import christian.aquino.strategies.Strategy;
import robocode.*;

import static java.lang.System.out;
import static robocode.util.Utils.normalRelativeAngleDegrees;

/**
 * TrackerStrategy - a strategy based on Tracker sample robot behavior.
 * Tracker robot was created by:
 * Mathew A. Nelson (original)
 * Flemming N. Larsen (contributor)
 */
public class TrackerStrategy extends Strategy {
    int count = 0; // Keeps track of how long we've
    // been searching for our target
    double gunTurnAmt; // How much to turn our gun when searching
    String trackName; // Name of the robot we're currently tracking
    int dist = 50; // distance to move when we're hit

    public TrackerStrategy(AdvancedRobot robot) {
        super(robot);
    }

    public void run() {
        // turn the Gun (looks for enemy)
        robot.turnGunRight(gunTurnAmt);
        // Keep track of how long we've been looking
        count++;
        // If we've haven't seen our target for 2 turns, look left
        if (count > 2) {
            gunTurnAmt = -10;
        }
        // If we still haven't seen our target for 5 turns, look right
        if (count > 5) {
            gunTurnAmt = 10;
        }
        // If we *still* haven't seen our target after 10 turns, find another target
        if (count > 11) {
            trackName = null;
        }
    }

    @Override
    public void initialize() {
        // Prepare gun
        trackName = null; // Initialize to not tracking anyone
        robot.setAdjustGunForRobotTurn(true); // Keep the gun still when we turn
        gunTurnAmt = 10; // Initialize gunTurn to 10
    }

    /**
     * onScannedRobot:  Here's the good stuff
     */
    public void onScannedRobot(ScannedRobotEvent e) {

        // If we have a target, and this isn't it, return immediately
        // so we can get more ScannedRobotEvents.
        if (trackName != null && !e.getName().equals(trackName)) {
            return;
        }

        // If we don't have a target, well, now we do!
        if (trackName == null) {
            trackName = e.getName();
            out.println("Tracking " + trackName);
        }
        // This is our target.  Reset count (see the run method)
        count = 0;
        // If our target is too far away, turn and move toward it.
        if (e.getDistance() > 150) {
            gunTurnAmt = normalRelativeAngleDegrees(e.getBearing() + (robot.getHeading() - robot.getRadarHeading()));

            robot.turnGunRight(gunTurnAmt); // Try changing these to setTurnGunRight,
            robot.turnRight(e.getBearing()); // and see how much Tracker improves...
            // (you'll have to make Tracker an AdvancedRobot)
            robot.ahead(e.getDistance() - 140);
            return;
        }

        // Our target is close.
        gunTurnAmt = normalRelativeAngleDegrees(e.getBearing() + (robot.getHeading() - robot.getRadarHeading()));
        robot.turnGunRight(gunTurnAmt);
        robot.fire(3);

        // Our target is too close!  Back up.
        if (e.getDistance() < 100) {
            if (e.getBearing() > -90 && e.getBearing() <= 90) {
                robot.back(40);
            } else {
                robot.ahead(40);
            }
        }
        robot.scan();
    }

    @Override
    public void onHitByBullet(HitByBulletEvent e) {
        // No action
    }

    /**
     * onHitRobot:  Set him as our new target
     */
    public void onHitRobot(HitRobotEvent e) {
        // Only print if he's not already our target.
        if (trackName != null && !trackName.equals(e.getName())) {
            out.println("Tracking " + e.getName() + " due to collision");
        }
        // Set the target
        trackName = e.getName();
        // Back up a bit.
        // Note:  We won't get scan events while we're doing this!
        // An AdvancedRobot might use setBack(); execute();
        gunTurnAmt = normalRelativeAngleDegrees(e.getBearing() + (robot.getHeading() - robot.getRadarHeading()));
        robot.turnGunRight(gunTurnAmt);
        robot.fire(3);
        robot.back(50);
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
