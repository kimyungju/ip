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

        String[] tasks = new String[100];
        int taskCount = 0;

        Scanner sc = new Scanner(System.in);    
        while (true) {
            String line = sc.nextLine();

            if (line.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                break;
            }

            if (line.equals("list")) {
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + ". " + tasks[i]);
                }
            } else {
                tasks[taskCount] = line;
                taskCount++;
                System.out.println("added: " + line);
            }
        }
        sc.close();
    }
}
