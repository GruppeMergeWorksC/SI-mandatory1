import typing as t
import strawberry 

@strawberry.type
class Author:
    id: int
    name: str
    surname: str