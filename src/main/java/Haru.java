import java.util.Scanner;

public class Haru {
    public static void main(String[] args) {
        String logo =
                  " _   _                 \n"
                + "| | | | __ _ _ __ _   _ \n"
                + "| |_| |/ _` | '__| | | |\n"
                + "|  _  | (_| | |  | |_| |\n"
                + "|_| |_|\\__,_|_|   \\__,_|\n";

        System.out.println("Hello! I'm Haru");
        System.out.println(logo);
        System.out.println("What can I do for you?");

        Task[] tasks = new Task[100];
        int taskCount = 0;
        Scanner sc = new Scanner(System.in);

        while (true) {
            String line = sc.nextLine().trim();

            try {
                if (line.equals("bye")) {
                    System.out.println("    ____________________________________________________________");
                    System.out.println("     Bye. Hope to see you again soon!");
                    System.out.println("    ____________________________________________________________");
                    break;
                }

                if (line.equals("list")) {
                    System.out.println("    ____________________________________________________________");
                    System.out.println("     Here are the tasks in your list:");
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println("     " + (i + 1) + "." + tasks[i]);
                    }
                    System.out.println("    ____________________________________________________________");
                } else if (line.startsWith("mark")) {
                    if (line.length() <= 5) {
                        throw new HaruException("Which task should I mark? Please provide a number.");
                    }
                    int index = Integer.parseInt(line.substring(5)) - 1;
                    if (index < 0 || index >= taskCount) {
                        throw new HaruException("That task number doesn't exist in your list.");
                    }
                    tasks[index].markAsDone();
                    System.out.println("    ____________________________________________________________");
                    System.out.println("     Nice! I've marked this task as done:\n       " + tasks[index]);
                    System.out.println("    ____________________________________________________________");
                } else if (line.startsWith("unmark")) {
                    if (line.length() <= 7) {
                        throw new HaruException("Which task should I unmark? Please provide a number.");
                    }
                    int index = Integer.parseInt(line.substring(7)) - 1;
                    if (index < 0 || index >= taskCount) {
                        throw new HaruException("I couldn't find that task number.");
                    }
                    tasks[index].markAsNotDone();
                    System.out.println("    ____________________________________________________________");
                    System.out.println("     OK, I've marked this task as not done yet:\n       " + tasks[index]);
                    System.out.println("    ____________________________________________________________");
                } else if (line.startsWith("todo")) {
                    if (line.length() <= 5) {
                        throw new HaruException("The description of a todo cannot be empty.");
                    }
                    tasks[taskCount++] = new Todo(line.substring(5));
                    printAddedMessage(tasks[taskCount - 1], taskCount);
                } else if (line.startsWith("deadline")) {
                    if (!line.contains(" /by ")) {
                        throw new HaruException("A deadline needs a time! Use: deadline [desc] /by [time]");
                    }
                    String[] parts = line.substring(9).split(" /by ");
                    if (parts[0].trim().isEmpty()) {
                        throw new HaruException("A deadline needs a description.");
                    }
                    tasks[taskCount++] = new Deadline(parts[0], parts[1]);
                    printAddedMessage(tasks[taskCount - 1], taskCount);
                } else if (line.startsWith("event")) {
                    if (!line.contains(" /from ") || !line.contains(" /to ")) {
                        throw new HaruException("Events need /from and /to times.");
                    }
                    String[] parts = line.substring(6).split(" /from | /to ");
                    if (parts[0].trim().isEmpty()) {
                        throw new HaruException("An event needs a description.");
                    }
                    tasks[taskCount++] = new Event(parts[0], parts[1], parts[2]);
                    printAddedMessage(tasks[taskCount - 1], taskCount);
                } else {
                    throw new HaruException("I'm sorry, but I don't know what that means :-(");
                }
            } catch (HaruException e) {
                System.out.println("    ____________________________________________________________");
                System.out.println("     OOPS!!! " + e.getMessage());
                System.out.println("    ____________________________________________________________");
            } catch (NumberFormatException e) {
                System.out.println("    ____________________________________________________________");
                System.out.println("     OOPS!!! Please use a valid number for marking.");
                System.out.println("    ____________________________________________________________");
            }
        }
        sc.close();
    }

    private static void printAddedMessage(Task task, int count) {
        System.out.println("    ____________________________________________________________");
        System.out.println("     Got it. I've added this task:\n       " + task);
        System.out.println("     Now you have " + count + " tasks in the list.");
        System.out.println("    ____________________________________________________________");
    }
}