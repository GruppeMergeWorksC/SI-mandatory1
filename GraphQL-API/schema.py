import typing as t
import strawberry 
from publishers import Mutation as PublisherMutation, Query as PublisherQuery
# from books import Mutation as BookMutation, Query as BookQuery
# from authors import Mutation as AuthorMutation, Query as AuthorQuery

@strawberry.type
class Query(PublisherQuery): #BookQuery, AuthorQuery, 
    pass

@strawberry.type
class Mutation(PublisherMutation):  # BookMutation, AuthorMutation
    pass

schema = strawberry.Schema(query=Query, mutation=Mutation)



