# Screenshots

This folder contains UI screenshots for the README documentation.

## How to take screenshots

1. Start the application: `.\mvnw.cmd spring-boot:run`
2. Open a browser and navigate to `http://localhost:8080`
3. Take screenshots of each page and save them here with the exact filenames listed below.

## Required screenshots

| Filename                      | URL to capture                                  | Login required     |
|-------------------------------|-------------------------------------------------|--------------------|
| `01-login.png`                | `http://localhost:8080/login`                   | No                 |
| `02-register.png`             | `http://localhost:8080/register`                | No                 |
| `03-employee-list.png`        | `http://localhost:8080/employees/list`          | Yes (any role)     |
| `04-employee-list-search.png` | `http://localhost:8080/employees/list?keyword=e`| Yes (any role)     |
| `05-employee-add.png`         | `http://localhost:8080/employees/add`           | Yes (ADMIN only)   |
| `06-employee-edit.png`        | `http://localhost:8080/employees/edit/1`        | Yes (ADMIN only)   |
| `07-statistics.png`           | `http://localhost:8080/employees/statistics`    | Yes (any role)     |
| `08-error-404.png`            | `http://localhost:8080/employees/edit/9999`     | Yes                |

## Recommended tool

- **Windows Snipping Tool** (`Win + Shift + S`) — quick region capture
- **Browser DevTools** → Right-click → "Capture screenshot" (full page)
- **Chrome extension** — Full Page Screen Capture

