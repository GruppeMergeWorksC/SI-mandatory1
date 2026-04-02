"""gRPC LibraryService implementation."""

from __future__ import annotations

import queue
import threading

import grpc

import library_pb2
import library_pb2_grpc
from repository import MIN_PUBLISHING_YEAR, LibraryRepository


class LibraryServicer(library_pb2_grpc.LibraryServiceServicer):
    def __init__(self, repo: LibraryRepository) -> None:
        self._repo = repo
        self._watch_lock = threading.Lock()
        self._watch_queues: list[queue.Queue] = []

    def _notify_new_book(self, book: library_pb2.Book) -> None:
        with self._watch_lock:
            queues = list(self._watch_queues)
        for q in queues:
            try:
                q.put_nowait(book)
            except queue.Full:
                pass

    def GetBookById(self, request, context):
        book_id = request.book_id
        if book_id <= 0:
            context.abort(grpc.StatusCode.INVALID_ARGUMENT, "book_id must be positive")
        row = self._repo.get_book_row(book_id)
        if row is None:
            context.abort(grpc.StatusCode.NOT_FOUND, f"Book id {book_id} does not exist")
        d = LibraryRepository.row_to_book_dict(row)
        return library_pb2.Book(
            id=d["id"],
            title=d["title"],
            author_id=d["author_id"],
            publishing_company_id=d["publishing_company_id"],
            publishing_year=d["publishing_year"],
        )

    def CreateBook(self, request, context):
        title = (request.title or "").strip()
        if not title:
            context.abort(grpc.StatusCode.INVALID_ARGUMENT, "title is required")
        if request.author_id <= 0:
            context.abort(grpc.StatusCode.INVALID_ARGUMENT, "author_id must be positive")
        if request.publishing_company_id <= 0:
            context.abort(
                grpc.StatusCode.INVALID_ARGUMENT,
                "publishing_company_id must be positive",
            )
        if request.publishing_year < MIN_PUBLISHING_YEAR:
            context.abort(
                grpc.StatusCode.INVALID_ARGUMENT,
                f"publishing_year must be >= {MIN_PUBLISHING_YEAR}",
            )
        if not self._repo.author_exists(request.author_id):
            context.abort(
                grpc.StatusCode.INVALID_ARGUMENT,
                f"author_id {request.author_id} does not exist",
            )
        if not self._repo.publisher_exists(request.publishing_company_id):
            context.abort(
                grpc.StatusCode.INVALID_ARGUMENT,
                f"publishing_company_id {request.publishing_company_id} does not exist",
            )

        new_id = self._repo.insert_book(
            title,
            request.author_id,
            request.publishing_company_id,
            request.publishing_year,
        )
        book = library_pb2.Book(
            id=new_id,
            title=title,
            author_id=request.author_id,
            publishing_company_id=request.publishing_company_id,
            publishing_year=request.publishing_year,
        )
        self._notify_new_book(book)
        return library_pb2.CreateBookResponse(book_id=new_id)

    def WatchBooks(self, request, context):
        q: queue.Queue = queue.Queue(maxsize=256)
        with self._watch_lock:
            self._watch_queues.append(q)
        try:
            while context.is_active():
                try:
                    book = q.get(timeout=0.5)
                    yield book
                except queue.Empty:
                    continue
        finally:
            with self._watch_lock:
                try:
                    self._watch_queues.remove(q)
                except ValueError:
                    pass
