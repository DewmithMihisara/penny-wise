# PennyWise - Expense Tracker App

Developed by Dewmith Mihisara

## Overview

PennyWise is a modern Android expense tracking application built with Clean Architecture principles. It helps users track their income and expenses, view statistics, and manage their financial data efficiently.

## Architecture

This application follows **Clean Architecture** principles with clear separation of concerns:

- **Presentation Layer**: UI components, ViewModels, and adapters
- **Domain Layer**: Business entities and repository interfaces
- **Data Layer**: Repository implementations and database access

For detailed architecture documentation, see [ARCHITECTURE.md](ARCHITECTURE.md)

For system diagrams, see [SYSTEM_DIAGRAM.md](SYSTEM_DIAGRAM.md)

## Features

- ✅ Add Income and Expense transactions
- ✅ View transactions by Daily/Monthly periods
- ✅ Statistics visualization (last 6 months)
- ✅ Category-based expense tracking
- ✅ Notes and reminders
- ✅ Modern UI with Material Design
- ✅ Clean Architecture implementation

## Technology Stack

- **Language**: Java
- **Database**: SQLite
- **Architecture**: Clean Architecture (Domain-Data-Presentation)
- **UI**: Material Design Components
- **Charts**: MPAndroidChart

## Project Structure

```
app/src/main/java/com/example/expensemanager/
├── domain/              # Domain layer (entities, repository interfaces)
├── data/                # Data layer (repository implementations, database)
├── presentation/        # Presentation layer (ViewModels)
├── viewmodels/          # Legacy ViewModels (updated to use repository)
├── views/               # UI components (Activities, Fragments)
├── models/              # Data models
├── adapters/            # RecyclerView adapters
└── utils/               # Utility classes and constants
```

## Installation

1. Clone the repository
2. Open in Android Studio
3. Sync Gradle files
4. Build and run the app

## Build

```bash
./gradlew installDebug
```

## License

Developed by Dewmith Mihisara

