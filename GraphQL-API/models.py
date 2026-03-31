from sqlalchemy import ForeignKey, Integer, String, Numeric
from sqlalchemy.orm import Mapped, mapped_column, relationship
from db import Base


class AuthorModel(Base):
    __tablename__ = "tauthor"

    id: Mapped[int] = mapped_column("nAuthorID", Integer, primary_key=True)
    name: Mapped[str] = mapped_column("cName", String(40), nullable=False)
    surname: Mapped[str | None] = mapped_column("cSurname", String(60), nullable=True)

    books: Mapped[list["BookModel"]] = relationship(back_populates="author")


class PublisherModel(Base):
    __tablename__ = "tpublishingcompany"

    id: Mapped[int] = mapped_column("nPublishingCompanyID", Integer, primary_key=True)
    name: Mapped[str] = mapped_column("cName", String(40), nullable=False)

    books: Mapped[list["BookModel"]] = relationship(back_populates="publisher")


class BookModel(Base):
    __tablename__ = "tbook"

    id: Mapped[int] = mapped_column("nBookID", Integer, primary_key=True)
    title: Mapped[str] = mapped_column("cTitle", String(255), nullable=False)
    author_id: Mapped[int] = mapped_column("nAuthorID", ForeignKey("tauthor.nAuthorID"), nullable=False)
    publishing_year: Mapped[int | None] = mapped_column("nPublishingYear", Numeric(4, 0), nullable=True)
    publisher_id: Mapped[int] = mapped_column(
        "nPublishingCompanyID",
        ForeignKey("tpublishingcompany.nPublishingCompanyID"),
        nullable=False
    )

    author: Mapped["AuthorModel"] = relationship(back_populates="books")
    publisher: Mapped["PublisherModel"] = relationship(back_populates="books")