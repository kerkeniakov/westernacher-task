package utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class Utils {
    public static String getCurrentDay() {
        return LocalDate.now().getDayOfWeek().name();
    }

    public static String getCurrentDate() {
        return new SimpleDateFormat("MM/dd/yyyy").format(new Date());
    }

    public static String getCurrentDate(String when) throws Exception {
        if (when.equals("Tommorow")) {
            return new SimpleDateFormat("MM/dd/yyyy").format(new Date(new Date().getTime() + 86400000));
        } else if (when.equals("AfterThreeDays")) {
            return new SimpleDateFormat("MM/dd/yyyy").format(new Date(new Date().getTime() + 259200000));
        }
        throw new Exception("getCurrentDate only accepts \"Tommorow\" or \"AfterThreeDays\" as params");
    }

    public static String getCurrentDateInTableRowFormat() {
        return new SimpleDateFormat("MMM dd, yyyy").format(new Date());
    }

    public static String getCurrentDateInTableRowFormat(String when) throws Exception {
        if (when.equals("Tommorow")) {
            return new SimpleDateFormat("MMM dd, yyyy").format(new Date(new Date().getTime() + 86400000));
        } else if (when.equals("AfterThreeDays")) {
            return new SimpleDateFormat("MMM dd, yyyy").format(new Date(new Date().getTime() + 259200000));
        }
        throw new Exception("getCurrentDate only accepts \"Tommorow\" or \"AfterThreeDays\" as params");
    }

    public static String[] getLeaveRowData() throws Exception {
        String[] rowData;
        if (Utils.getCurrentDay() == "FRIDAY") {
            rowData = new String[]{
                    null,
                    Utils.getCurrentDateInTableRowFormat(),
                    Utils.getCurrentDateInTableRowFormat("AfterThreeDays"),
                    "4",
                    "2",
                    "Draft",
            };
        } else if (Utils.getCurrentDay() == "SATURDAY") {
            rowData = new String[]{
                    null,
                    Utils.getCurrentDateInTableRowFormat(),
                    Utils.getCurrentDateInTableRowFormat("Tommorow"),
                    "2",
                    "0",
                    "Draft",
            };
        } else if (Utils.getCurrentDay() == "SUNDAY") {
            rowData = new String[]{
                    null,
                    Utils.getCurrentDateInTableRowFormat(),
                    Utils.getCurrentDateInTableRowFormat("Tommorow"),
                    "2",
                    "1",
                    "Draft",
            };
        } else {
            rowData = new String[]{
                    null,
                    Utils.getCurrentDateInTableRowFormat(),
                    Utils.getCurrentDateInTableRowFormat("Tommorow"),
                    "2",
                    "2",
                    "Draft",
            };
        }
        return rowData;
    }


}