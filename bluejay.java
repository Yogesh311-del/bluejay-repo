import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

class EmployeeSchedule {
    LocalTime startTime;
    LocalTime endTime;

    EmployeeSchedule(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
}

public class Main {
    // Function to calculate time difference between two LocalTime objects
    private static long timeDiff(LocalTime time1, LocalTime time2) {
        return time2.toSecondOfDay() - time1.toSecondOfDay();
    }

    // Function to check if an employee worked for 7 consecutive days
    private static boolean workedConsecutiveDays(List<EmployeeSchedule> schedule) {
        int consecutiveDays = 0;
        for (int i = 0; i < schedule.size() - 1; i++) {
            if (schedule.get(i + 1).getStartTime().toLocalDate().isEqual(schedule.get(i).getEndTime().toLocalDate().plusDays(1))) {
                consecutiveDays++;
            } else {
                consecutiveDays = 0;
            }
            if (consecutiveDays == 6) {
                return true;
            }
        }
        return false;
    }

    // Function to check if an employee has less than 10 hours of time between shifts but greater than 1 hour
    private static boolean lessThan10Hours(List<EmployeeSchedule> schedule) {
        for (int i = 0; i < schedule.size() - 1; i++) {
            long secondsDiff = timeDiff(schedule.get(i).getEndTime(), schedule.get(i + 1).getStartTime());
            if (secondsDiff > 3600 && secondsDiff < 36000) {
                return true;
            }
        }
        return false;
    }

    // Function to check if an employee has worked for more than 14 hours in a single shift
    private static boolean moreThan14Hours(List<EmployeeSchedule> schedule) {
        for (EmployeeSchedule shift : schedule) {
            long secondsWorked = timeDiff(shift.getStartTime(), shift.getEndTime());
            if (secondsWorked > 50400) {
                return true;
            }
        }
        return false;
    }

    // Function to read and parse employee schedule from file
    private static List<List<EmployeeSchedule>> parseSchedule(String fileName) throws IOException {
        List<List<EmployeeSchedule>> schedules = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            // Parsing relevant entities
            LocalTime startTime = LocalTime.parse(parts[2]); // Assuming 'Time' is the start time
            LocalTime endTime = LocalTime.parse(parts[3]); // Assuming 'Time Out' is the end time
            List<EmployeeSchedule> employeeSchedule = new ArrayList<>();
            employeeSchedule.add(new EmployeeSchedule(startTime, endTime));
            schedules.add(employeeSchedule);
        }
        reader.close();
        return schedules;
    }

    public static void main(String[] args) {
        String fileName = "employee_schedule.csv"; // Provide the file name here
        try {
            List<List<EmployeeSchedule>> schedules = parseSchedule(fileName);

            // Iterate through each employee's schedule and check conditions
            for (int i = 0; i < schedules.size(); i++) {
                List<EmployeeSchedule> employeeSchedule = schedules.get(i);
                if (workedConsecutiveDays(employeeSchedule)) {
                    System.out.println("Employee " + (i + 1) + " has worked for 7 consecutive days.");
                }
                if (lessThan10Hours(employeeSchedule)) {
                    System.out.println("Employee " + (i + 1) + " has less than 10 hours between shifts but greater than 1 hour.");
                }
                if (moreThan14Hours(employeeSchedule)) {
                    System.out.println("Employee " + (i + 1) + " has worked for more than 14 hours in a single shift.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
