# Clean Architecture Implementation

## Overview
The codebase has been refactored to follow Clean Architecture principles, improving code quality, maintainability, and testability.

## Architecture Layers

### 1. Domain Layer (`domain/`)
Contains business logic and entities that are independent of frameworks.

- **`domain/entity/`**: Pure domain models (e.g., `TransactionEntity`)
- **`domain/repository/`**: Repository interfaces defining contracts for data access

### 2. Data Layer (`data/`)
Implements data access and repository interfaces.

- **`data/local/`**: Local database implementations
  - `TransactionDatabaseHelper`: SQLite database operations
  - `DatabaseConstants`: Database schema constants
- **`data/repository/`**: Repository implementations
  - `TransactionRepositoryImpl`: Implements `TransactionRepository` interface
- **`data/mapper/`**: Data transformation layer
  - `TransactionMapper`: Converts between domain entities and presentation models

### 3. Presentation Layer (`presentation/`)
UI-related code including ViewModels, Activities, Fragments, and Adapters.

- **`presentation/viewmodel/`**: ViewModels using repository pattern
- Views remain in existing `views/` package for backward compatibility

## Key Improvements

### 1. Separation of Concerns
- **Before**: ViewModel directly accessed database handlers
- **After**: ViewModel uses repository interface, decoupled from data source

### 2. Dependency Inversion
- Repository interfaces in domain layer
- Implementations in data layer
- ViewModel depends on abstractions, not concrete implementations

### 3. Improved Testability
- Repository pattern allows easy mocking
- Domain logic separated from infrastructure concerns
- Clear interfaces for unit testing

### 4. Better Code Organization
- Clear package structure following clean architecture
- Proper naming conventions (`DatabaseConstants` instead of `parameters`)
- Mapper classes for data transformation

### 5. Maintainability
- Single Responsibility Principle applied
- Changes to database don't affect ViewModels directly
- Easy to swap data sources (SQLite, Room, API, etc.)

## Migration Notes

- Old `MainViewModel` in `viewmodels/` package still works but now uses repository pattern internally
- Old `transDBHandler` in `myHandler/` package still exists for backward compatibility
- New code should use `TransactionRepository` interface
- Domain entities (`TransactionEntity`) can be used instead of models for domain logic

## Package Structure

```
com.example.expensemanager/
├── domain/
│   ├── entity/
│   │   └── TransactionEntity.java
│   └── repository/
│       └── TransactionRepository.java
├── data/
│   ├── local/
│   │   ├── TransactionDatabaseHelper.java
│   │   └── DatabaseConstants.java
│   ├── repository/
│   │   └── TransactionRepositoryImpl.java
│   └── mapper/
│       └── TransactionMapper.java
├── presentation/
│   └── viewmodel/
│       └── MainViewModel.java (new)
├── viewmodels/
│   └── MainViewModel.java (updated to use repository)
└── views/
    ├── activities/
    └── fragments/
```

