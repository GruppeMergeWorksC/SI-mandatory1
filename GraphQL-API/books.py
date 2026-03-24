import typing as t
import strawberry 
from authors import Author
from publishers import Publisher

@strawberry.type
class Book:
    id: int
    title: str
    author: Author 
    publishing_year: int
    publisher: Publisher 