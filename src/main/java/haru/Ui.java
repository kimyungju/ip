package haru;

import java.util.Scanner;

public class Ui {
    private final Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public void showWelcome() {
        String logo =
                  " _   _                 \n"
                + "| | | | __ _ _ __ _   _ \n"
                + "| |_| |/ _` | '__| | | |\n"
                + "|  _  | (_| | |   | |_| |\n"
                + "|_| |_|\\__,_|_|   \\__,_|\n";

        System.out.println("Hello! I'm Haru");
        System.out.println(logo);
        System.out.println("What can I do for you?");
    }

    public void showLine() {
        System.out.println("    ____________________________________________________________");
    }

    public void showLoadingError() {
        showLine();
        System.out.println("     OOPS!!! I couldn't load saved data.");
        showLine();
    }

    public void showError(String message) {
        showLine();
        System.out.println("     OOPS!!! " + message);
        showLine();
    }

    public void showGoodbye() {
        showLine();
        System.out.println("     Bye. Hope to see you again soon!");
        showLine();
    }

    public void showTaskAdded(Task task, int taskCount) {
        showLine();
        System.out.println("     Got it. I've added this task:\n       " + task);
        System.out.println("     Now you have " + taskCount + " tasks in the list.");
        showLine();
    }

    public void showTaskMarked(Task task) {
        showLine();
        System.out.println("     Nice! I've marked this task as done:\n       " + task);
        showLine();
    }

    public void showTaskUnmarked(Task task) {
        showLine();
        System.out.println("     OK, I've marked this task as not done yet:\n       " + task);
        showLine();
    }

    public void showTaskDeleted(Task task, int taskCount) {
        showLine();
        System.out.println("     Noted. I've removed this task:\n       " + task);
        System.out.println("     Now you have " + taskCount + " tasks in the list.");
        showLine();
    }

    public void showTaskList(java.util.ArrayList<Task> tasks) {
        showLine();
        System.out.println("     Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("     " + (i + 1) + "." + tasks.get(i));
        }
        showLine();
    }

    public void showMatchingTasks(java.util.ArrayList<Task> tasks) {
        showLine();
        if (tasks.isEmpty()) {
            System.out.println("     No matching tasks found.");
        } else {
            System.out.println("     Here are the matching tasks in your list:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println("     " + (i + 1) + "." + tasks.get(i));
            }
        }
        showLine();
    }

    public String readCommand() {
        String line = scanner.nextLine().trim();
        while (line.isEmpty()) {
            line = scanner.nextLine().trim();
        }
        return line;
    }

    public void close() {
        scanner.close();
    }
}
