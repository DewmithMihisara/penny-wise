# Expense Tracker App - System Architecture Diagram

## System Architecture Overview

```mermaid
graph TB
    subgraph "Presentation Layer"
        A[MainActivity] --> B[MainViewModel]
        C[AddTransFragment] --> B
        D[AddStatsFragment] --> B
        E[SummaryFragment] --> B
        F[NotesFragment] --> B
        B --> G[TransactionRepository Interface]
        B --> H[TransactionMapper]
    end

    subgraph "Domain Layer"
        I[TransactionEntity]
        G --> I
        J[TransactionRepository Interface]
        G -.implements.-> J
    end

    subgraph "Data Layer"
        K[TransactionRepositoryImpl] --> J
        K --> L[TransactionDatabaseHelper]
        K --> M[TransactionMapper]
        L --> N[(SQLite Database)]
        L --> O[DatabaseConstants]
    end

    subgraph "Models"
        P[Transaction]
        Q[Notes]
        R[Category]
        H --> P
        H --> I
    end

    style A fill:#e1f5ff
    style B fill:#e1f5ff
    style C fill:#e1f5ff
    style D fill:#e1f5ff
    style E fill:#e1f5ff
    style F fill:#e1f5ff
    style I fill:#fff4e1
    style J fill:#fff4e1
    style K fill:#e8f5e9
    style L fill:#e8f5e9
    style N fill:#e8f5e9
```

## Component Interaction Flow

```mermaid
sequenceDiagram
    participant UI as Activity/Fragment
    participant VM as MainViewModel
    participant Repo as TransactionRepository
    participant DB as TransactionDatabaseHelper
    participant Mapper as TransactionMapper
    participant DBStorage as SQLite Database

    UI->>VM: Request Transactions
    VM->>Repo: getTransactionsByDate()
    Repo->>DB: getTransactionsByDate()
    DB->>DBStorage: SELECT * FROM transactions
    DBStorage-->>DB: Return Cursor
    DB->>DB: mapCursorToTransaction()
    DB-->>Repo: List<TransactionEntity>
    Repo-->>VM: List<TransactionEntity>
    VM->>Mapper: toModelList()
    Mapper-->>VM: List<Transaction>
    VM-->>UI: Update LiveData
```

## Package Structure Diagram

```mermaid
graph LR
    subgraph "com.example.expensemanager"
        subgraph "domain/"
            D1[entity/TransactionEntity]
            D2[repository/TransactionRepository]
        end
        
        subgraph "data/"
            DA1[local/TransactionDatabaseHelper]
            DA2[local/DatabaseConstants]
            DA3[repository/TransactionRepositoryImpl]
            DA4[mapper/TransactionMapper]
        end
        
        subgraph "presentation/"
            P1[viewmodel/MainViewModel]
        end
        
        subgraph "viewmodels/"
            V1[MainViewModel - Legacy]
        end
        
        subgraph "views/"
            V2[activities/MainActivity]
            V3[activities/splashActivity]
            V4[fragments/AddTransFragment]
            V5[fragments/AddStatsFragment]
            V6[fragments/SummaryFragment]
            V7[fragments/NotesFragment]
        end
        
        subgraph "models/"
            M1[Transaction]
            M2[Notes]
            M3[Category]
        end
        
        subgraph "adapters/"
            A1[TransAdapter]
            A2[CategoryAdapter]
            A3[NoteAdapter]
        end
        
        subgraph "utils/"
            U1[Constants]
            U2[Helper]
        end
    end

    P1 --> D2
    V1 --> D2
    DA3 --> D2
    DA3 --> DA1
    DA4 --> D1
    DA4 --> M1
    V2 --> P1
    V4 --> V1
    V5 --> V1
    V6 --> V1
    V7 --> V1
```

## Data Flow Diagram

```mermaid
flowchart TD
    Start[User Action] --> UI{UI Component}
    
    UI -->|Add Transaction| AddTrans[AddTransFragment]
    UI -->|View Stats| Stats[AddStatsFragment]
    UI -->|View Summary| Summary[SummaryFragment]
    UI -->|View Notes| Notes[NotesFragment]
    UI -->|View List| Main[MainActivity]
    
    AddTrans --> VM1[MainViewModel]
    Stats --> VM1
    Summary --> VM1
    Notes --> VM1
    Main --> VM1
    
    VM1 -->|Convert| Mapper[TransactionMapper]
    Mapper -->|Entity to Model| Model[Transaction Model]
    Model --> UI
    
    VM1 -->|Repository Interface| Repo[TransactionRepositoryImpl]
    Repo -->|Database Operations| DBHelper[TransactionDatabaseHelper]
    DBHelper -->|SQL Queries| SQLite[(SQLite Database)]
    
    SQLite -->|ResultSet| DBHelper
    DBHelper -->|Entities| Repo
    Repo -->|Entities| VM1
    
    VM1 -->|LiveData Update| UI
```

## Layer Responsibilities

```mermaid
mindmap
  root((Expense Tracker<br/>Clean Architecture))
    Presentation Layer
      Activities
        MainActivity
        splashActivity
      Fragments
        AddTransFragment
        AddStatsFragment
        SummaryFragment
        NotesFragment
      ViewModels
        MainViewModel
        LiveData Management
      Adapters
        TransAdapter
        CategoryAdapter
        NoteAdapter
    Domain Layer
      Entities
        TransactionEntity
      Repository Interfaces
        TransactionRepository
      Business Rules
        Validation Logic
    Data Layer
      Repository Implementation
        TransactionRepositoryImpl
      Database Helper
        TransactionDatabaseHelper
      Database Constants
        DatabaseConstants
      Mappers
        TransactionMapper
      SQLite Database
        CRUD Operations
```

## Class Dependency Diagram

```mermaid
classDiagram
    class MainViewModel {
        -TransactionRepository repository
        +MutableLiveData transactions
        +getTransactions(Calendar)
        +addTransaction(Transaction)
        +deleteTransaction(Transaction)
        +getIncomeForLast6Months()
        +getExpenseForLast6Months()
    }
    
    class TransactionRepository {
        <<interface>>
        +addTransaction(TransactionEntity)
        +deleteTransaction(TransactionEntity)
        +getTransactionsByDate(long, long)
        +getSumOfIncome(List)
        +getSumOfExpense(List)
        +getExpenseSumForMonth(long, long)
        +getIncomeSumForMonth(long, long)
        +getCategoryPercentage(long, long)
    }
    
    class TransactionRepositoryImpl {
        -TransactionDatabaseHelper databaseHelper
        +addTransaction(TransactionEntity)
        +getTransactionsByDate(long, long)
        +getCategoryPercentage(long, long)
    }
    
    class TransactionDatabaseHelper {
        -SQLiteDatabase db
        +insertTransaction(TransactionEntity)
        +getTransactionsByDate(long, long)
        +getExpenseSumForMonth(long, long)
        +getIncomeSumForMonth(long, long)
        +getCategoryPercentage(long, long)
        +getNotes()
    }
    
    class TransactionEntity {
        -long id
        -String type
        -String category
        -double amount
        -Date date
    }
    
    class Transaction {
        -int id
        -String type
        -String category
        -double amount
        -Date date
    }
    
    class TransactionMapper {
        +toEntity(Transaction) TransactionEntity
        +toModel(TransactionEntity) Transaction
        +toModelList(List~Entity~) List~Transaction~
        +toEntityList(List~Transaction~) List~Entity~
    }
    
    MainViewModel --> TransactionRepository : uses
    TransactionRepositoryImpl ..|> TransactionRepository : implements
    TransactionRepositoryImpl --> TransactionDatabaseHelper : uses
    MainViewModel --> TransactionMapper : uses
    TransactionMapper --> TransactionEntity : converts
    TransactionMapper --> Transaction : converts
    TransactionDatabaseHelper --> TransactionEntity : returns
```

## Use Case Flow

```mermaid
graph TD
    A[User Opens App] --> B[Splash Screen]
    B --> C[Main Activity]
    
    C --> D{User Action}
    
    D -->|View Transactions| E[Transaction List]
    D -->|Add Transaction| F[Add Transaction Dialog]
    D -->|View Stats| G[Stats Fragment]
    D -->|View Summary| H[Summary Fragment]
    D -->|View Notes| I[Notes Fragment]
    
    F --> J[Select Type: Income/Expense]
    J --> K[Enter Amount]
    K --> L[Select Category]
    L --> M[Add Note]
    M --> N[Save Transaction]
    
    N --> O[ViewModel.addTransaction]
    O --> P[Repository.addTransaction]
    P --> Q[Database.insertTransaction]
    Q --> R[(SQLite Database)]
    
    R --> S[Transaction Saved]
    S --> E[Update Transaction List]
    
    E --> T[ViewModel.getTransactions]
    T --> U[Repository.getTransactionsByDate]
    U --> V[Database.queryTransactions]
    V --> W[Return Transaction List]
    W --> E
    
    G --> X[ViewModel.getIncomeForLast6Months]
    G --> Y[ViewModel.getExpenseForLast6Months]
    X --> Z[Repository.getIncomeSumForMonth]
    Y --> AA[Repository.getExpenseSumForMonth]
    Z --> BB[Display Chart]
    AA --> BB
```

