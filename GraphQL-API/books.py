from pyexpat import model
import typing as t
import strawberry 
from authors import Author
from publishers import Publisher
from models import BookModel
from strawberry.types import Info

@strawberry.type
class Book:
    id: int
    title: str
    author: Author | None  
    publishing_year: int | None
    publisher: Publisher | None

def to_book_type(model: BookModel) -> Book:
    return Book(
        id=model.id,
        title=model.title,
        author=Author(id=model.author.id, name=model.author.name, surname=model.author.surname) if model.author else None,
        publishing_year=model.publishing_year,
        publisher=Publisher(id=model.publisher.id, name=model.publisher.name) if model.publisher else None
    )

def get_books(info: Info) -> t.List[Book]:
    repo = info.context["repo"]    
    books = repo.get_books()
    return [to_book_type(b) for b in books]

def book_exists_or_error(repo, id: int):
    if not repo.book_exists_by_id(id):
        raise ValueError(f"Book with ID {id} not found")
    
def author_exists_or_error(author_id, repo):
    if not repo.author_exists_by_id(author_id):
        raise ValueError(f"Author with ID {author_id} not found")
    
def publisher_exists_or_error(publisher_id, repo):
    if not repo.publisher_exists_by_id(publisher_id):
        raise ValueError(f"Publisher with ID {publisher_id} not found")

def get_book_by_id(info: Info, id: int) -> Book:
    repo = info.context["repo"]
    book_exists_or_error(repo, id)
    model = repo.get_book_by_id(id)
    return to_book_type(model)

@strawberry.type
class Query:
    books: t.List[Book] = strawberry.field(description="List of all books", resolver=get_books)
    book: Book = strawberry.field(description="Get book by ID", resolver=get_book_by_id)

@strawberry.type
class Mutation: 
    @strawberry.mutation(description="Create a new book")
    def create_book(self, info: Info, title: str, author_id: int, publishing_year: int | None, publisher_id: int) -> Book:
        repo = info.context["repo"]

        author_exists_or_error(author_id, repo)
        publisher_exists_or_error(publisher_id, repo)

        new = BookModel(title=title, author_id=author_id, publishing_year=publishing_year, publisher_id=publisher_id)
        saved = repo.create_book(new)

        return to_book_type(saved)
  
    @strawberry.mutation(description="Update book by ID")
    def update_book(self, info: Info, id: int, title: str, author_id: int, publishing_year: int | None, publisher_id: int) -> Book:
        repo = info.context["repo"]

        book_exists_or_error(repo, id)
        author_exists_or_error(author_id, repo)
        publisher_exists_or_error(publisher_id, repo)

        model = repo.get_book_by_id(id)
        model.title = title
        model.author_id = author_id
        model.publishing_year = publishing_year
        model.publisher_id = publisher_id

        updated = repo.update_book(model)

        return to_book_type(updated)

    @strawberry.mutation(description="Delete book by ID")
    def delete_book(self, info: Info, id: int) -> Book:
        repo = info.context["repo"]

        book_exists_or_error(repo, id)

        model = repo.get_book_by_id(id)  
              
        book = to_book_type(model)

        repo.delete_book(model)

        return book
