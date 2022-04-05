package christian.aquino.strategies;

import robocode.*;

import static java.lang.System.out;

public class CitiHackatonStrategy extends ActiveStrategy {
    int missed = 0;
    int hits = 0;
    double energy = 100;
    boolean changeStrategy = true;

    public CitiHackatonStrategy(AdvancedRobot robot) {
        super(robot);
        // Default strategy
        activeStrategy = new TrackerStrategy(robot);
    }

    @Override
    public void run() {
        // Checks if the current strategy is the most accurate for the current scenario
        checkActiveStrategy();
        activeStrategy.run();
    }

    @Override
    public void onHitRobot(HitRobotEvent e) {
        super.onHitRobot(e);
    }

    @Override
    public void onBulletMissed(BulletMissedEvent event) {
        activeStrategy.onBulletMissed(event);
        missed++;
    }

    @Override
    public void onHitByBullet(HitByBulletEvent e) {
        super.onHitByBullet(e);
        hits++;
    }

    @Override
    public void checkActiveStrategy() {
        // Not enough energy...to the end with the current strategy
        if(robot.getEnergy() > 10.0) {
            out.println("missed: " + missed);
            out.println("hits: " + hits);
            double currentEnergy = robot.getEnergy();
            double energyDiff = currentEnergy - energy;
            out.println("energy diff: " + energyDiff);
            Strategy newStrategy;
            // Has the robot energy enough?, and....
            // Is the robot missing shots? Is the robot being hit?
            // If not, keep the same strategy
            if ( energyDiff < 0 && (missed > 5 || hits > 5) ) {
                changeStrategy = true;
            }

            if(changeStrategy) {
                changeStrategy = false;
                // So much bullet missed, try Tracker
                newStrategy = new TrackerStrategy(robot);
                if (robot.getOthers() > 15) {
                    // Many robots! TrackFire is the right one
                    if(activeStrategy.getClass() != TrackFireStrategy.class) {
                        newStrategy = new TrackFireStrategy(robot);
                    }
                } else if (robot.getOthers() > 5) {
                    // Fewer robots, more space to move...choosing Tracker
                    if(activeStrategy.getClass() != TrackerStrategy.class) {
                        newStrategy = new TrackerStrategy(robot);
                    }
                } else {
                    // Less than 5 robots...it's time to Walls
                    if(activeStrategy.getClass() != WallsStrategy.class) {
                        newStrategy = new WallsStrategy(robot);
                    }
                }

                // TODO: Use reflection or another better strategy instead of instance multiple strategies
                if (activeStrategy.getClass() != newStrategy.getClass()) {
                    missed = 0;
                    hits = 0;
                    energy = currentEnergy;
                    robot.clearAllEvents();
                    activeStrategy = newStrategy;
                    activeStrategy.initialize();
                }
            }
        }
    }
}
