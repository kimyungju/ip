package haru;

import java.util.ArrayList;
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

    /**
     * Shows one or more lines between divider lines.
     *
     * @param lines The lines to display (each prefixed with indent).
     */
    private void showBorderedMessage(String... lines) {
        showLine();
        for (String line : lines) {
            System.out.println("     " + line);
        }
        showLine();
    }

    public void showLoadingError() {
        showBorderedMessage("OOPS!!! I couldn't load saved data.");
    }

    public void showError(String message) {
        showBorderedMessage("OOPS!!! " + message);
    }

    public void showGoodbye() {
        showBorderedMessage("Bye. Hope to see you again soon!");
    }

    public void showTaskAdded(Task task, int taskCount) {
        showBorderedMessage("Got it. I've added this task:\n       " + task,
                "Now you have " + taskCount + " tasks in the list.");
    }

    public void showTaskMarked(Task task) {
        showBorderedMessage("Nice! I've marked this task as done:\n       " + task);
    }

    public void showTaskUnmarked(Task task) {
        showBorderedMessage("OK, I've marked this task as not done yet:\n       " + task);
    }

    public void showTaskDeleted(Task task, int taskCount) {
        showBorderedMessage("Noted. I've removed this task:\n       " + task,
                "Now you have " + taskCount + " tasks in the list.");
    }

    public void showTaskList(ArrayList<Task> tasks) {
        showLine();
        System.out.println("     Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("     " + (i + 1) + "." + tasks.get(i));
        }
        showLine();
    }

    public void showMatchingTasks(ArrayList<Task> tasks) {
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

    public void showContactAdded(Contact contact, int contactCount) {
        showBorderedMessage("Got it. I've added this contact:\n       " + contact,
                "Now you have " + contactCount + " contacts in the list.");
    }

    public void showContactDeleted(Contact contact, int contactCount) {
        showBorderedMessage("Noted. I've removed this contact:\n       " + contact,
                "Now you have " + contactCount + " contacts in the list.");
    }

    public void showContactList(ArrayList<Contact> contacts) {
        showLine();
        System.out.println("     Here are the contacts in your list:");
        for (int i = 0; i < contacts.size(); i++) {
            System.out.println("     " + (i + 1) + "." + contacts.get(i));
        }
        showLine();
    }

    public void showMatchingContacts(ArrayList<Contact> contacts) {
        showLine();
        if (contacts.isEmpty()) {
            System.out.println("     No matching contacts found.");
        } else {
            System.out.println("     Here are the matching contacts in your list:");
            for (int i = 0; i < contacts.size(); i++) {
                System.out.println("     " + (i + 1) + "." + contacts.get(i));
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
