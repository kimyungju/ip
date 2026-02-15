# Haru User Guide

![Ui screenshot](Ui.png)

Haru is a personal task management chatbot that helps you keep track of your todos, deadlines, and events. It also lets you store contacts. Haru runs as a desktop GUI application and saves your data automatically between sessions.

## Quick Start

1. Ensure you have **Java 17** installed.
2. Download the latest `haru.jar` from the [Releases](https://github.com/yjkim-nate/ip/releases) page.
3. Copy the file to a folder you want to use as the home directory.
4. Double-click the file or run `java -jar haru.jar` in a terminal.
5. Type commands in the text box and press Enter or click Send.

## Features

### Adding a todo: `todo`

Adds a simple task with no date.

Format: `todo DESCRIPTION`

Example: `todo read book`

```
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
```

### Adding a deadline: `deadline`

Adds a task with a due date (and optional time).

Format: `deadline DESCRIPTION /by DATE [TIME]`

- Date format: `yyyy-MM-dd` (e.g. `2024-02-15`)
- Time format (optional): `HHmm` or `HH:mm` (e.g. `1800` or `18:00`)

Examples:
- `deadline return book /by 2024-02-15`
- `deadline submit report /by 2024-03-01 2359`

```
Got it. I've added this task:
  [D][ ] return book (by: Feb 15 2024)
Now you have 2 tasks in the list.
```

### Adding an event: `event`

Adds a task with a start and end date/time.

Format: `event DESCRIPTION /from DATE [TIME] /to DATE [TIME]`

Example: `event project meeting /from 2024-02-15 1400 /to 2024-02-15 1600`

```
Got it. I've added this task:
  [E][ ] project meeting (from: Feb 15 2024 14:00 to: Feb 15 2024 16:00)
Now you have 3 tasks in the list.
```

### Listing all tasks: `list`

Shows all tasks in your list.

Format: `list`

```
Here are the tasks in your list:
1. [T][ ] read book
2. [D][ ] return book (by: Feb 15 2024)
3. [E][ ] project meeting (from: Feb 15 2024 14:00 to: Feb 15 2024 16:00)
```

### Marking a task as done: `mark`

Marks the specified task as completed.

Format: `mark INDEX`

Example: `mark 1`

```
Nice! I've marked this task as done:
  [T][X] read book
```

### Unmarking a task: `unmark`

Marks the specified task as not done.

Format: `unmark INDEX`

Example: `unmark 1`

```
OK, I've marked this task as not done yet:
  [T][ ] read book
```

### Deleting a task: `delete`

Removes the specified task from the list.

Format: `delete INDEX`

Example: `delete 2`

```
Noted. I've removed this task:
  [D][ ] return book (by: Feb 15 2024)
Now you have 2 tasks in the list.
```

### Finding tasks: `find`

Searches for tasks whose description contains the given keyword.

Format: `find KEYWORD`

Example: `find book`

```
Here are the matching tasks in your list:
1. [T][ ] read book
```

### Adding a contact: `contact`

Adds a contact with a name, phone number, and email.

Format: `contact NAME /phone PHONE /email EMAIL`

Example: `contact John /phone 91234567 /email john@example.com`

```
Got it. I've added this contact:
  John (phone: 91234567, email: john@example.com)
Now you have 1 contacts in the list.
```

### Listing all contacts: `contact_list`

Shows all contacts in your list.

Format: `contact_list`

```
Here are the contacts in your list:
1. John (phone: 91234567, email: john@example.com)
```

### Deleting a contact: `contact_delete`

Removes the specified contact from the list.

Format: `contact_delete INDEX`

Example: `contact_delete 1`

```
Noted. I've removed this contact:
  John (phone: 91234567, email: john@example.com)
Now you have 0 contacts in the list.
```

### Finding contacts: `contact_find`

Searches for contacts whose name contains the given keyword.

Format: `contact_find KEYWORD`

Example: `contact_find John`

```
Here are the matching contacts in your list:
1. John (phone: 91234567, email: john@example.com)
```

### Exiting the program: `bye`

Exits the application.

Format: `bye`

## Command Summary

| Action | Format |
|---|---|
| Add todo | `todo DESCRIPTION` |
| Add deadline | `deadline DESCRIPTION /by DATE [TIME]` |
| Add event | `event DESCRIPTION /from DATE [TIME] /to DATE [TIME]` |
| List tasks | `list` |
| Mark task | `mark INDEX` |
| Unmark task | `unmark INDEX` |
| Delete task | `delete INDEX` |
| Find tasks | `find KEYWORD` |
| Add contact | `contact NAME /phone PHONE /email EMAIL` |
| List contacts | `contact_list` |
| Delete contact | `contact_delete INDEX` |
| Find contacts | `contact_find KEYWORD` |
| Exit | `bye` |
