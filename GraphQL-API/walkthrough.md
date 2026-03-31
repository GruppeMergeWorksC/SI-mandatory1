# Walkthrough of the Code

## Program Flow

1. A request is sent to `/graphql`, which is handled by the FastAPI app through the Strawberry `GraphQLRouter`.

2. The context function runs first, creating a database session and wrapping it in a repository instance, which is attached to `info.context`.

3. Strawberry resolves the query or mutation by calling the appropriate resolver function defined in the feature modules (books, authors, publishers).

4. The resolver retrieves the repository from `info.context` and performs the required operation (query or mutation).

5. The repository interacts with the database through SQLAlchemy models to fetch or modify data.

6. The result from the database is converted into GraphQL types using helper mapping functions before being returned.

7. After the request is completed, the database session is closed automatically via the context lifecycle.
## main.py
- App is created at module level so Uvicorn can import app directly  
- `get_context()` opens a SQLAlchemy session with `SessionLocal()`, wraps that inside a `LibraryRepository`, exposes it as `info.context["repo"]`, and closes the session in `finally`. This ensures that each GraphQL request gets a repo backed by a DB session that is cleaned up automatically.
- Creates the FastAPI app
- Creates a Strawberry `GraphQLRouter` with the shared schema and context getter, and mounts it at `/graphql`

## schema.py
This is the composition layer. It imports `Query` and `Mutation` fragments from `books.py`, `authors.py`, and `publishers.py`, then combines them through multiple inheritance into one root `Query` type and one root `Mutation` type.

## books.py, authors.py, publishers.py
These three modules follow the same pattern: define a GraphQL type, define helper functions, define resolver functions for queries, and define mutation methods for create/update/delete. `Book`/`Author`/`Publisher` is the GraphQL API type, not the DB model. Using books.py as the example:
- `to_book_type()` converts a BookModel ORM object into the GraphQL Book object, including nested Author and Publisher objects (dto style mapping)
- Helper functions are mostly validation guards such as “exists or error” and duplicate checks
- Then Query exposes `books` and `book(id)`, while Mutation exposes `create_book`, `update_book`, and `delete_book`

## models.py
Defines the database layer using SQLAlchemy ORM classes. Each class represents a table and specifies columns, primary keys, and relationships between entities. These classes act as the structured representation of stored data and are used by the repository to read and write records.

## repository.py
Contains dumb DB queries, no business logic. `LibraryRepository` is a thin data-access layer around the SQLAlchemy session. It does selects, existence checks, and simple create/update/delete with `add`, `commit`, `refresh`, and `delete`.