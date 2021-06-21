package drivers.strategies;

import utils.Constants;

public class DriverStrategyImplementer {
    public static DriverStrategy chooseStrategy(String strategy) {
        if (Constants.CHROME.equals(strategy)) {
            return new Chrome();
        } else if (Constants.FIREFOX.equals(strategy)) {
            return new Firefox();
        }
        return null;
    }
}

