import typing as t
import strawberry 
from models import PublisherModel
from repository import LibraryRepository
from strawberry.types import Info

@strawberry.type
class Publisher:
    id: int
    name: str

def get_publishers(info: Info) -> t.List[Publisher]:
    db = info.context["db"]
    repo = LibraryRepository(db)

    publishers = repo.get_publishers()

    return [Publisher(id=p.id, name=p.name) for p in publishers]

@strawberry.type
class Query:
    publishers: t.List[Publisher] = strawberry.field(description="List of all publishers", resolver=get_publishers)