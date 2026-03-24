import typing as t
import strawberry 
from publishers import Query as PublisherQuery

@strawberry.type
class Query(PublisherQuery): #BookQuery, AuthorQuery, 
    pass

schema = strawberry.Schema(query=Query)



