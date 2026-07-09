# Smart File Organizer

A modern, asynchronous-friendly Java application that scans a target directory and automatically classifies files into categorized subfolders based on their file extensions. 

## Features

- **Smart Extension Routing:** Scans and automatically routes files into specialized folders (`Images`, `Documents`, `Videos`, `Others`).
- **Dynamic Directory Provisioning:** Automatically creates the target subfolders on the fly if they do not already exist.
- **Collision & Conflict Resolution:** Prevents overwriting by automatically appending numeric suffixes (e.g., `document_1.pdf`) if a file name conflict occurs.
- **State-Persistent Undo:** Reverts the entire last organization process step-by-step using a local JSON history tracker, neatly removing empty folders upon reversal.
- **Logback System Auditing:** Records all file operations, runtime structural changes, and permission exceptions into a local `organizer.log` file.
- **Native JavaFX GUI:** Features a clean, lightweight, and intuitive graphical user interface for easy directory selection and processing.

## Technologies

- **Java 21+** (Utilizing modern Switch Expressions and the robust NIO.2 File System API)
- **JavaFX 21.0.2** (Native Graphical User Interface)
- **Maven** (Project lifecycle management and dependency automation)
- **Jackson Databind** (High-performance JSON serialization for Undo state tracking)
- **Logback / SLF4J** (Industrial-grade enterprise logging framework)

## How to Run

### 1. Clone Repository
```bash
git clone [https://github.com/monolut/-Smart-File-Organizer.git](https://github.com/monolut/-Smart-File-Organizer.git)
```
### 2.Build
mvn clean install
### 3.Run
mvn javafx:run

## Screenshots

### Main Window

<img width="449" height="278" alt="main_window" src="https://github.com/user-attachments/assets/a18a2bfa-ce47-44cd-887a-e84d854c5b76" />


### Before Organizing

<img width="648" height="493" alt="before_organizing" src="https://github.com/user-attachments/assets/bd1a8820-266a-420f-935e-fe4b754f96cc" />


### After Organizing

<img width="622" height="135" alt="after_organizing" src="https://github.com/user-attachments/assets/7e8a82e0-75b5-4fdf-bc55-d7b92b30615a" />





