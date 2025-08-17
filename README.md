This is full Find my IP rewrite. I'm tired how much bloat code I have managed to create without the
AI help. This is a complete rewrite with strict rules in mind.

# Rules

- Kotlin Multiplatform
- REAL clean architecture

# Layers

- UI (Compose)
- Presentation:
    - ViewModels
    - UiState / Presentation models

- Application
    - Use cases
    - Data source interfaces

- Domain
    - Entities, Value Objects, Domain logic

- Infrastructure
    - Data sources implementations
    - DAOs (Room), DB models
    - APIs (Ktor), DTOs
