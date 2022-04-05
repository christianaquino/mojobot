package christian.aquino.strategies;

import christian.aquino.strategies.IStrategy;
import robocode.AdvancedRobot;

public abstract class Strategy implements IStrategy {
    AdvancedRobot robot;
    public Strategy(AdvancedRobot robot) {
        this.robot = robot;
    }
}
