# Haru

> A friendly task management chatbot with a warm spring-themed GUI.

![Haru UI](docs/Ui.png)

## Features

- **Task management** — Create and track todos, deadlines, and events
- **Contact management** — Store and search contacts
- **Flexible date input** — Accepts `yyyy-MM-dd` or `yyyy-MM-dd HHmm`
- **Persistent storage** — Tasks and contacts are saved automatically
- **JavaFX GUI** — Clean chat interface with a warm spring theme

## Quick Start

**Prerequisites:** JDK 17

1. Download `haru.jar` from the [latest release](../../releases), or build it yourself:
   ```
   ./gradlew shadowJar
   ```
2. Run:
   ```
   java -jar haru.jar
   ```

## Command Summary

| Command | Syntax | Description |
|---------|--------|-------------|
| `todo` | `todo DESCRIPTION` | Add a todo |
| `deadline` | `deadline DESCRIPTION /by DATE` | Add a deadline |
| `event` | `event DESCRIPTION /from DATE /to DATE` | Add an event |
| `list` | `list` | List all tasks |
| `find` | `find KEYWORD` | Find tasks by keyword |
| `mark` | `mark INDEX` | Mark a task as done |
| `unmark` | `unmark INDEX` | Mark a task as not done |
| `delete` | `delete INDEX` | Delete a task |
| `contact` | `contact NAME /phone PHONE /email EMAIL` | Add a contact |
| `contact list` | `contact list` | List all contacts |
| `contact delete` | `contact delete INDEX` | Delete a contact |
| `contact find` | `contact find KEYWORD` | Find contacts by keyword |
| `bye` | `bye` | Exit the application |

## Date Formats

Dates can be entered as `yyyy-MM-dd` (e.g. `2024-01-15`) or with time as `yyyy-MM-dd HHmm` (e.g. `2024-01-15 1800`).
