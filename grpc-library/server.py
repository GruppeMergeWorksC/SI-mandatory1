"""Run the Library gRPC server."""

from __future__ import annotations

import logging
import os
from concurrent import futures
from pathlib import Path

import grpc

import library_pb2_grpc
from repository import LibraryRepository, default_db_path
from servicer import LibraryServicer


def main() -> None:
    logging.basicConfig(level=logging.INFO, format="%(levelname)s %(message)s")

    db_env = os.environ.get("LIBRARY_DB")
    db_path = Path(db_env) if db_env else default_db_path()
    if not db_path.is_file():
        raise SystemExit(f"Database file not found: {db_path}. Set LIBRARY_DB or place library.db.")

    host = os.environ.get("LIBRARY_GRPC_HOST", "[::]")
    port = os.environ.get("LIBRARY_GRPC_PORT", "50051")
    addr = f"{host}:{port}"

    repo = LibraryRepository(db_path)
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    library_pb2_grpc.add_LibraryServiceServicer_to_server(LibraryServicer(repo), server)
    server.add_insecure_port(addr)
    server.start()
    logging.info("Library gRPC listening on %s (db=%s)", addr, db_path)
    server.wait_for_termination()


if __name__ == "__main__":
    main()
