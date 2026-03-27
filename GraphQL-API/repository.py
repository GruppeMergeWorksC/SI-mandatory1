from models import BookModel, AuthorModel, PublisherModel

class LibraryRepository:
    def __init__(self, db):
        self.db = db

    # books
    def get_books(self) -> list[BookModel]:
        return self.db.query(BookModel).all()

    def get_book_by_id(self, id: int) -> BookModel | None:
        return self.db.query(BookModel).filter(BookModel.id == id).first()
    
    def book_exists_by_id(self, id: int) -> bool:
        return self.db.query(BookModel.id).filter(BookModel.id == id).first() is not None
    
    def get_books_by_publisher_id(self, id: int) -> list[BookModel]:
        return self.db.query(BookModel).filter(BookModel.publisher_id == id).all()
    
    def publisher_has_books(self, publisher_id: int) -> bool:
        return self.db.query(BookModel.id).filter(BookModel.publisher_id == publisher_id).first() is not None
    
    def author_has_books(self, author_id: int) -> bool:
        return self.db.query(BookModel.id).filter(BookModel.author_id == author_id).first() is not None

    def delete_book(self, id: int) -> BookModel | None:
        book = self.get_book_by_id(id)
        self.db.delete(book)
        self.db.commit()
        return book

    # authors
    def get_authors(self) -> list[AuthorModel]:
        return self.db.query(AuthorModel).all()

    def get_author_by_id(self, id: int) -> AuthorModel | None:
        return self.db.query(AuthorModel).filter(AuthorModel.id == id).first()

    def author_exists_by_id(self, id: int) -> bool:
        return self.db.query(AuthorModel.id).filter(AuthorModel.id == id).first() is not None

    def create_author(self, author: AuthorModel) -> AuthorModel:
        self.db.add(author)
        self.db.commit()
        self.db.refresh(author)
        return author
    
    def update_author(self, author: AuthorModel) -> AuthorModel:
        self.db.commit()
        self.db.refresh(author)
        return author

    def delete_author(self, id: int) -> AuthorModel | None:
        author = self.get_author_by_id(id)
        self.db.delete(author)
        self.db.commit()
        return author

    # publishers
    def get_publishers(self) -> list[PublisherModel]:
        return self.db.query(PublisherModel).all()

    def get_publisher_by_id(self, id: int) -> PublisherModel | None:
        return self.db.query(PublisherModel).filter(PublisherModel.id == id).first()
    
    def publisher_exists_by_id(self, id: int) -> bool:
        return self.db.query(PublisherModel.id).filter(PublisherModel.id == id).first() is not None

    def create_publisher(self, publisher: PublisherModel) -> PublisherModel:
        self.db.add(publisher)
        self.db.commit()
        self.db.refresh(publisher)
        return publisher
    
    def update_publisher(self, publisher: PublisherModel) -> PublisherModel:
        self.db.commit()
        self.db.refresh(publisher)
        return publisher
    
    def delete_publisher(self, id: int) -> PublisherModel | None:
        publisher = self.get_publisher_by_id(id)
        self.db.delete(publisher)
        self.db.commit()
        return publisher
