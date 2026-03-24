from fastapi import FastAPI
from strawberry.fastapi import GraphQLRouter
from db import SessionLocal
from repository import LibraryRepository
from schema import schema

def get_context():
    db = SessionLocal()
    try:
        yield {"repo": LibraryRepository(db)}
    finally:
        db.close()

def main():
    pass

if __name__ == "__main__":
    main()

app = FastAPI()

graphql_app = GraphQLRouter(schema, context_getter=get_context)

app.include_router(graphql_app, prefix="/graphql")