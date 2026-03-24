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


@strawberry.type
class Mutation:
    @strawberry.mutation(description="Create a new publisher")
    def create_publisher(self, info: Info, name: str) -> Publisher:
        repo = info.context["repo"]
        new_publisher = PublisherModel(name=name)
        saved_publisher = repo.create_publisher(new_publisher)

        return Publisher(id=saved_publisher.id, name=saved_publisher.name)