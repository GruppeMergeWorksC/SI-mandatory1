from models import BookModel, AuthorModel, PublisherModel

class LibraryRepository:
    def __init__(self, db):
        self.db = db

    # books
    def get_books(self) -> list[BookModel]:
        return self.db.query(BookModel).all()

    def get_book_by_id(self, book_id: int) -> BookModel | None:
        return self.db.query(BookModel).filter(BookModel.id == book_id).first()

    # authors
    def get_authors(self) -> list[AuthorModel]:
        return self.db.query(AuthorModel).all()

    def get_author_by_id(self, author_id: int) -> AuthorModel | None:
        return self.db.query(AuthorModel).filter(AuthorModel.id == author_id).first()

    # publishers
    def get_publishers(self) -> list[PublisherModel]:
        return self.db.query(PublisherModel).all()

    def get_publisher_by_id(self, publisher_id: int) -> PublisherModel | None:
        return self.db.query(PublisherModel).filter(PublisherModel.id == publisher_id).first()