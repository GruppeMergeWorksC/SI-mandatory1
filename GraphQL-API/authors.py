import typing as t
import strawberry
from models import AuthorModel
from strawberry.types import Info


def get_author_or_error(repo, id: int) -> AuthorModel:
    author = repo.get_author_by_id(id)
    if not author:
        raise ValueError(f"Author with ID {id} not found")
    return author

def if_author_already_exists_error(repo, name, surname):
    if repo.author_exists_by_name(name, surname):
        raise ValueError(f"Author with name {name} and surname {surname} already exists")

@strawberry.type
class Author:
    id: int
    name: str
    surname: str | None

def get_authors(info: Info) -> t.List[Author]:
    repo = info.context["repo"]
    authors = repo.get_authors()
    return [Author(id=a.id, name=a.name, surname=a.surname) for a in authors]

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

        return Author(id=saved.id, name=saved.name, surname=saved.surname)

    @strawberry.mutation(description="Update author by ID")
    def update_author(self, info: Info, id: int, name: str, surname: str | None = None) -> Author:
        repo = info.context["repo"]

        author = get_author_or_error(repo, id)
        author.name = name
        if surname is not None:
            author.surname = surname

        updated = repo.update_author(author)

        return Author(id=updated.id, name=updated.name, surname=updated.surname)

    @strawberry.mutation(description="Delete author by ID")
    def delete_author(self, info: Info, id: int) -> Author:
        repo = info.context["repo"]

        author = get_author_or_error(repo, id)

        if repo.author_has_books(id):
            raise ValueError(f"Author with ID {id} has books and cannot be deleted")

        repo.delete_author(author)

        return author