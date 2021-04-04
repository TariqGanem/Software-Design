package PresentationLayer;

import Resources.Preference;
import Resources.Role;

import java.time.LocalDate;
import java.util.List;

public class MenuController {

    public void showMainMenu(boolean isManager) {
        IOController io = IOController.getInstance();
        io.println("Main Menu:");
        io.println("----------------------");
        showEmployeeMenu();
        if (isManager)
            showManagerMenu();
        io.println("Type 0 to quit");
        io.println("----------------------");
    }

    public String loginMenu() {
        IOController io = IOController.getInstance();
        String ID = null;
        io.println("Type \"Q\" to quit.");
        io.print("Please enter your employee ID: ");
        ID = io.getString();
        return ID;
    }

    public void showEmployeeMenu() {
        IOController io = IOController.getInstance();
        io.println("1. View my profile");
        io.println("2. View my shifts");
        io.println("3. Change my preferences");
    }

    public void showManagerMenu() {
        IOController io = IOController.getInstance();
        io.println("4. Add new shift");
        io.println("5. Add new employee");
        io.println("6. View employee profile");
        io.println("7. View shift information");
        io.println("8. Define shift personnel");
    }

    public boolean displaySpecificEmployees() {
        IOController io = IOController.getInstance();
        String YES_NO = null;
        io.println("Would you like to view the employees in a specific shift?");
        do {
            io.print("Type \"y\" or \"n\": ");
            YES_NO = io.getString();
        } while (!YES_NO.equals("y") && !YES_NO.equals("n"));
        return YES_NO.equals("y");
    }

    public LocalDate showEnterDateMenu() {
        IOController io = IOController.getInstance();
        int year, month, day;
        LocalDate date = null;

        io.print("Please enter a year: ");
        year = io.getInt();
        io.print("Please enter a month: ");
        month = io.getInt();
        io.print("Please enter a day: ");
        day = io.getInt();

        try {
            date = LocalDate.of(year, month, day);
        } catch (Exception ignored) {
            io.println("You entered illegal values for a date, you may try again.");
        }

        return date;
    }

    public boolean showEnterMorningEvening() {
        IOController io = IOController.getInstance();
        String answer;

        io.println("Is the shift in the morning or in the evening?");
        do {
            io.print("Type \"m\" or \"e\": ");
            answer = io.getString();
        } while (!answer.equals("m") && !answer.equals("e"));

        return answer.equals("m");
    }

    public int showPreferenceMenu() {
        IOController io = IOController.getInstance();
        io.println("Please pick one of the following preferences:");
        int i = 1;
        for (Preference preference : Preference.values()) {
            io.println(i + ") " + preference.name());
            i++;
        }
        int prefIndex;
        do {
            io.print("Please pick a number between 1 and " + (i - 1) + ": ");
            prefIndex = io.getInt();
        } while (prefIndex <= 0 || prefIndex >= i);
        return prefIndex - 1;
    }

    private void showUpdateShiftMenu(BackendController backendController) {

    }

    public int showUpdateEmployeeMenu() {
        IOController io = IOController.getInstance();

        io.println("What information do you want to change?");
        io.println("1) Name");
        io.println("2) Bank id");
        io.println("3) Branch id");
        io.println("4) Account Number");
        io.println("5) Salary");
        io.println("6) Start Date");
        io.println("7) Trust Fund");
        io.println("8) Free Days");
        io.println("9) Sick Days");
        io.println("10) Skills");

        int answer;
        do {
            io.print("Enter a number between 1 and 10: ");
            answer = io.getInt();
        } while (answer < 1 || answer > 11);

        return answer;
    }

    public String showEnterStringMenu(String whatToEnter) {
        IOController io = IOController.getInstance();

        io.print("Please enter a new " + whatToEnter + ": ");

        return io.getString();
    }

    public int showEnterIntMenu(String whatToEnter) {
        IOController io = IOController.getInstance();

        io.print("Please enter a new " + whatToEnter + ": ");

        return io.getInt();
    }

    public float showEnterFloatMenu(String whatToEnter) {
        IOController io = IOController.getInstance();

        io.print("Please enter a new " + whatToEnter + ": ");

        return io.getFloat();
    }

    public List<Role> showEnterRoleList() {
        IOController io = IOController.getInstance();

        int i = 0;
        for (Role role: Role.values()) {

        }
        io.println("");
        io.println("Please enter the new role list: ");

        return null;
    }
}
