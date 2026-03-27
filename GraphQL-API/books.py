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
    
@strawberry.type
class Query:
    books: t.List[Book] = strawberry.field(description="List of all books", resolver=get_books)

@strawberry.type
class Mutation:
    @strawberry.mutation(description="Delete book by ID")
    def delete_book(self, info: Info, id: int) -> Book:
        repo = info.context["repo"]

        book_exists_or_error(repo, id)

        model = repo.get_book_by_id(id)

        repo.delete_book(id)

        return model
    
