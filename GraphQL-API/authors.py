import typing as t
import strawberry
from models import AuthorModel
from strawberry.types import Info

@strawberry.type
class Author:
    id: int
    name: str
    surname: str | None

def to_author_type(model: AuthorModel) -> Author:
    return Author(id=model.id, name=model.name, surname=model.surname)

def get_author_or_error(repo, id: int) -> AuthorModel:
    author = repo.get_author_by_id(id)
    if not author:
        raise ValueError(f"Author with ID {id} not found")
    return author

def if_author_already_exists_error(repo, name, surname):
    if repo.author_exists_by_name(name, surname):
        raise ValueError(f"Author with name {name} and surname {surname} already exists")

def get_authors(info: Info) -> t.List[Author]:
    repo = info.context["repo"]
    authors = repo.get_authors()
    return [to_author_type(a) for a in authors]

@strawberry.type
class Query:
    authors: t.List[Author] = strawberry.field(description="List of all authors", resolver=get_authors)

@strawberry.type
class Mutation:
    @strawberry.mutation(description="Create a new author")
    def create_author(self, info: Info, name: str, surname: str | None = None) -> Author:
        repo = info.context["repo"]

        if_author_already_exists_error(repo, name, surname)

        new = AuthorModel(name=name, surname=surname)
        saved = repo.create_author(new)

        return to_author_type(saved)

    @strawberry.mutation(description="Update author by ID")
    def update_author(self, info: Info, id: int, name: str, surname: str | None = None) -> Author:
        repo = info.context["repo"]

        author = get_author_or_error(repo, id)
        author.name = name
        if surname is not None:
            author.surname = surname

        updated = repo.update_author(author)

        return to_author_type(updated)

    @strawberry.mutation(description="Delete author by ID")
    def delete_author(self, info: Info, id: int) -> Author:
        repo = info.context["repo"]

        author = get_author_or_error(repo, id)

        if repo.author_has_books(id):
            raise ValueError(f"Author with ID {id} has books and cannot be deleted")

        repo.delete_author(author)

        return author