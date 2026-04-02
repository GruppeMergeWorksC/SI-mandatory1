#!/usr/bin/env python3
"""Small CLI to exercise LibraryService (manual / demo testing)."""

from __future__ import annotations

import argparse
import os
import sys

import grpc

# Run from `grpc-library/`: `python client.py ...`
if __name__ == "__main__" and __package__ is None:
    _root = os.path.dirname(os.path.abspath(__file__))
    if _root not in sys.path:
        sys.path.insert(0, _root)

import library_pb2
import library_pb2_grpc


def channel_target() -> str:
    return os.environ.get("LIBRARY_GRPC_TARGET", "localhost:50051")


def cmd_get(stub: library_pb2_grpc.LibraryServiceStub, book_id: int) -> None:
    book = stub.GetBookById(library_pb2.GetBookByIdRequest(book_id=book_id))
    print(
        f"id={book.id} title={book.title!r} author_id={book.author_id} "
        f"publisher_id={book.publishing_company_id} year={book.publishing_year}"
    )


def cmd_create(
    stub: library_pb2_grpc.LibraryServiceStub,
    title: str,
    author_id: int,
    publisher_id: int,
    year: int,
) -> None:
    resp = stub.CreateBook(
        library_pb2.CreateBookRequest(
            title=title,
            author_id=author_id,
            publishing_company_id=publisher_id,
            publishing_year=year,
        )
    )
    print(f"created book_id={resp.book_id}")


def cmd_watch(stub: library_pb2_grpc.LibraryServiceStub) -> None:
    print("Watching for new books (Ctrl+C to stop)...", flush=True)
    for book in stub.WatchBooks(library_pb2.WatchBooksRequest()):
        print(
            f"[watch] id={book.id} title={book.title!r} author_id={book.author_id} "
            f"publisher_id={book.publishing_company_id} year={book.publishing_year}",
            flush=True,
        )


def main() -> None:
    p = argparse.ArgumentParser(description="Library gRPC demo client")
    p.add_argument(
        "--target",
        default=channel_target(),
        help="Host:port (default: env LIBRARY_GRPC_TARGET or localhost:50051)",
    )
    sub = p.add_subparsers(dest="cmd", required=True)

    g = sub.add_parser("get", help="GetBookById")
    g.add_argument("--id", type=int, required=True)

    c = sub.add_parser("create", help="CreateBook")
    c.add_argument("--title", required=True)
    c.add_argument("--author-id", type=int, required=True)
    c.add_argument("--publisher-id", type=int, required=True)
    c.add_argument("--year", type=int, required=True)

    sub.add_parser("watch", help="WatchBooks (streaming)")

    args = p.parse_args()
    with grpc.insecure_channel(args.target) as channel:
        stub = library_pb2_grpc.LibraryServiceStub(channel)
        if args.cmd == "get":
            cmd_get(stub, args.id)
        elif args.cmd == "create":
            cmd_create(
                stub,
                args.title,
                args.author_id,
                args.publisher_id,
                args.year,
            )
        elif args.cmd == "watch":
            cmd_watch(stub)


if __name__ == "__main__":
    main()
