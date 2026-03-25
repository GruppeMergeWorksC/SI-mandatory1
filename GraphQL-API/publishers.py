import typing as t
import strawberry 
from models import PublisherModel
from strawberry.types import Info

@strawberry.type
class Publisher:
    id: int
    name: str

def get_publishers(info: Info) -> t.List[Publisher]:
    repo = info.context["repo"]  # Access the repository from the context
    
    publishers = repo.get_publishers()

    return [Publisher(id=p.id, name=p.name) for p in publishers]

@strawberry.type
class Query:
    publishers: t.List[Publisher] = strawberry.field(description="List of all publishers", resolver=get_publishers)

def publisher_exists_or_error(repo, id: int):
    if not repo.publisher_exists_by_id(id):
        raise ValueError(f"Publisher with ID {id} not found")

@strawberry.type
class Mutation:
    @strawberry.mutation(description="Create a new publisher")
    def create_publisher(self, info: Info, name: str) -> Publisher:
        repo = info.context["repo"]
        new = PublisherModel(name=name)
        saved = repo.create_publisher(new)

        return Publisher(id=saved.id, name=saved.name)
    
    @strawberry.mutation(description="Update publisher by ID")
    def update_publisher(self, info: Info, id: int, name: str) -> Publisher:
        repo = info.context["repo"]

        publisher_exists_or_error(repo, id)

        publisher = repo.get_publisher_by_id(id)
        publisher.name = name
        repo.db.commit()
        repo.db.refresh(publisher)

        return Publisher(id=publisher.id, name=publisher.name)

    @strawberry.mutation(description="Delete publisher by ID")
    def delete_publisher(self, info: Info, id: int) -> Publisher:
        repo = info.context["repo"]

        publisher_exists_or_error(repo, id)

        if repo.publisher_has_books(id): 
            raise ValueError(f"Publisher with ID {id} has books and cannot be deleted")
        
        deleted = repo.delete_publisher(id)

        return Publisher(id=deleted.id, name=deleted.name)