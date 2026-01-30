package haru;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Haru {
    enum Command {
        BYE, LIST, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT, UNKNOWN
    }

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

        Storage storage = new Storage("data/haru.txt");
        ArrayList<Task> tasks = new ArrayList<>();
        try {
            tasks = storage.load();
        } catch (IOException e) {
            printLine();
            System.out.println("     OOPS!!! I couldn't load saved data.");
            printLine();
        }
        Scanner sc = new Scanner(System.in);

        while (true) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;

            String commandWord = line.split(" ")[0].toUpperCase();
            Command cmd;
            try {
                cmd = Command.valueOf(commandWord);
            } catch (IllegalArgumentException e) {
                cmd = Command.UNKNOWN;
            }

            try {
                switch (cmd) {
                case BYE:
                    printLine();
                    System.out.println("     Bye. Hope to see you again soon!");
                    printLine();
                    sc.close();
                    return;

                case LIST:
                    printLine();
                    System.out.println("     Here are the tasks in your list:");
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println("     " + (i + 1) + "." + tasks.get(i));
                    }
                    printLine();
                    break;

                case MARK:
                    int mIdx = getIndex(line, 5, tasks.size());
                    tasks.get(mIdx).markAsDone();
                    saveTasks(storage, tasks);
                    printLine();
                    System.out.println("     Nice! I've marked this task as done:\n       " + tasks.get(mIdx));
                    printLine();
                    break;

                case UNMARK:
                    int uIdx = getIndex(line, 7, tasks.size());
                    tasks.get(uIdx).markAsNotDone();
                    saveTasks(storage, tasks);
                    printLine();
                    System.out.println("     OK, I've marked this task as not done yet:\n       " + tasks.get(uIdx));
                    printLine();
                    break;

                case DELETE:
                    int dIdx = getIndex(line, 7, tasks.size());
                    Task removedTask = tasks.remove(dIdx);
                    saveTasks(storage, tasks);
                    printLine();
                    System.out.println("     Noted. I've removed this task:\n       " + removedTask);
                    System.out.println("     Now you have " + tasks.size() + " tasks in the list.");
                    printLine();
                    break;

                case TODO:
                    if (line.length() <= 5) throw new HaruException("The description of a todo cannot be empty.");
                    tasks.add(new Todo(line.substring(5)));
                    saveTasks(storage, tasks);
                    printAddedMessage(tasks.get(tasks.size() - 1), tasks.size());
                    break;

                case DEADLINE:
                    if (!line.contains(" /by ")) throw new HaruException("A deadline needs a time! Use: /by");
                    String[] dParts = line.substring(9).split(" /by ");
                    tasks.add(new Deadline(dParts[0], dParts[1]));
                    saveTasks(storage, tasks);
                    printAddedMessage(tasks.get(tasks.size() - 1), tasks.size());
                    break;

                case EVENT:
                    if (!line.contains(" /from ") || !line.contains(" /to ")) throw new HaruException("Events need /from and /to.");
                    String[] eParts = line.substring(6).split(" /from | /to ");
                    tasks.add(new Event(eParts[0], eParts[1], eParts[2]));
                    saveTasks(storage, tasks);
                    printAddedMessage(tasks.get(tasks.size() - 1), tasks.size());
                    break;

                default: 
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

    private static void saveTasks(Storage storage, ArrayList<Task> tasks) throws HaruException {
        try {
            storage.save(tasks);
        } catch (IOException e) {
            throw new HaruException("I couldn't save tasks to disk.");
        }
    }

    private static void printLine() {
        System.out.println("    ____________________________________________________________");
    }
}
