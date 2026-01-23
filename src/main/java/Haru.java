import java.util.ArrayList;
import java.util.Scanner; 

public class Haru {
    public static void main(String[] args) {
        String logo =
                  " _   _                 \n"
                + "| | | | __ _ _ __ _   _ \n"
                + "| |_| |/ _` | '__| | | |\n"
                + "|  _  | (_| | |   | |_| |\n"
                + "|_| |_|\\__,_|_|   \\__,_|\n";

        System.out.println("Hello! I'm Haru");
        System.out.println(logo);
        System.out.println("What can I do for you?");

        ArrayList<Task> tasks = new ArrayList<>(); 
        Scanner sc = new Scanner(System.in);

        while (true) {
            String line = sc.nextLine().trim();

            try {
                if (line.equals("bye")) {
                    printLine();
                    System.out.println("     Bye. Hope to see you again soon!");
                    printLine();
                    break;
                }

                if (line.equals("list")) {
                    printLine();
                    System.out.println("     Here are the tasks in your list:");
                    for (int i = 0; i < tasks.size(); i++) { 
                        System.out.println("     " + (i + 1) + "." + tasks.get(i)); 
                    }
                    printLine();
                } else if (line.startsWith("mark")) {
                    int index = getIndex(line, 5, tasks.size());
                    tasks.get(index).markAsDone();
                    printLine();
                    System.out.println("     Nice! I've marked this task as done:\n       " + tasks.get(index));
                    printLine();
                } else if (line.startsWith("unmark")) {
                    int index = getIndex(line, 7, tasks.size());
                    tasks.get(index).markAsNotDone();
                    printLine();
                    System.out.println("     OK, I've marked this task as not done yet:\n       " + tasks.get(index));
                    printLine();
                } else if (line.startsWith("delete")) {
                    int index = getIndex(line, 7, tasks.size());
                    Task removedTask = tasks.remove(index); 
                    printLine();
                    System.out.println("     Noted. I've removed this task:\n       " + removedTask);
                    System.out.println("     Now you have " + tasks.size() + " tasks in the list.");
                    printLine();
                } else if (line.startsWith("todo")) {
                    if (line.length() <= 5) throw new HaruException("The description of a todo cannot be empty.");
                    tasks.add(new Todo(line.substring(5))); 
                    printAddedMessage(tasks.get(tasks.size() - 1), tasks.size());
                } else if (line.startsWith("deadline")) {
                    if (!line.contains(" /by ")) throw new HaruException("A deadline needs a time! Use: /by");
                    String[] parts = line.substring(9).split(" /by ");
                    tasks.add(new Deadline(parts[0], parts[1]));
                    printAddedMessage(tasks.get(tasks.size() - 1), tasks.size());
                } else if (line.startsWith("event")) {
                    if (!line.contains(" /from ") || !line.contains(" /to ")) throw new HaruException("Events need /from and /to.");
                    String[] parts = line.substring(6).split(" /from | /to ");
                    tasks.add(new Event(parts[0], parts[1], parts[2]));
                    printAddedMessage(tasks.get(tasks.size() - 1), tasks.size());
                } else {
                    throw new HaruException("I'm sorry, but I don't know what that means :-(");
                }
            } catch (HaruException e) {
                printLine();
                System.out.println("     OOPS!!! " + e.getMessage());
                printLine();
            } catch (NumberFormatException e) {
                printLine();
                System.out.println("     OOPS!!! Please use a valid number.");
                printLine();
            }
        }
        sc.close();
    }

    private static int getIndex(String line, int offset, int size) throws HaruException {
        if (line.length() <= offset) throw new HaruException("Please provide a task number.");
        int index = Integer.parseInt(line.substring(offset).trim()) - 1;
        if (index < 0 || index >= size) throw new HaruException("That task number doesn't exist.");
        return index;
    }

    private static void printAddedMessage(Task task, int count) {
        printLine();
        System.out.println("     Got it. I've added this task:\n       " + task);
        System.out.println("     Now you have " + count + " tasks in the list.");
        printLine();
    }

    private static void printLine() {
        System.out.println("    ____________________________________________________________");
    }
}