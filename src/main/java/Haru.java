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
            String line = sc.nextLine();
            
            if (line.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                break;
            }

            if (line.equals("list")) {
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + "." + tasks[i]);
                }
            } else if (line.startsWith("mark ")) {
                int index = Integer.parseInt(line.substring(5)) - 1;
                tasks[index].markAsDone();
                System.out.println("Nice! I've marked this task as done:\n  " + tasks[index]);
            } else if (line.startsWith("unmark ")) {
                int index = Integer.parseInt(line.substring(7)) - 1;
                tasks[index].markAsNotDone();
                System.out.println("OK, I've marked this task as not done yet:\n  " + tasks[index]);
            } else {
                Task newTask = null;
                if (line.startsWith("todo ")) {
                    newTask = new Todo(line.substring(5));
                } else if (line.startsWith("deadline ")) {
                    String[] parts = line.substring(9).split(" /by ");
                    newTask = new Deadline(parts[0], parts[1]);
                } else if (line.startsWith("event ")) {
                    String[] parts = line.substring(6).split(" /from | /to ");
                    newTask = new Event(parts[0], parts[1], parts[2]);
                }

                if (newTask != null) {
                    tasks[taskCount] = newTask;
                    taskCount++;
                    System.out.println("Got it. I've added this task:\n  " + newTask);
                    System.out.println("Now you have " + taskCount + " tasks in the list.");
                }
            }
        }
        sc.close();
    }
}