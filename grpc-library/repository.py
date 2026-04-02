"""SQLite access for library tables (`library.db`)."""

from __future__ import annotations

import sqlite3
import threading
from pathlib import Path
from typing import Any

# Minimum publication year for new books.
MIN_PUBLISHING_YEAR = 1900


def default_db_path() -> Path:
    """`library.db` in the repository root (next to `grpc-library/`)."""
    return Path(__file__).resolve().parents[1] / "library.db"


class LibraryRepository:
    """Thread-safe repository using a single SQLite connection."""

    def __init__(self, db_path: Path | str | None = None) -> None:
        path = Path(db_path) if db_path else default_db_path()
        self._path = path
        self._lock = threading.Lock()
        self._conn = sqlite3.connect(
            str(path),
            check_same_thread=False,
            isolation_level=None,
        )
        self._conn.row_factory = sqlite3.Row

    def close(self) -> None:
        with self._lock:
            self._conn.close()

    def get_book_row(self, book_id: int) -> sqlite3.Row | None:
        with self._lock:
            cur = self._conn.execute(
                """
                SELECT nBookID, cTitle, nAuthorID, nPublishingYear, nPublishingCompanyID
                FROM tbook
                WHERE nBookID = ?
                """,
                (book_id,),
            )
            return cur.fetchone()

    def author_exists(self, author_id: int) -> bool:
        with self._lock:
            cur = self._conn.execute(
                "SELECT 1 FROM tauthor WHERE nAuthorID = ? LIMIT 1",
                (author_id,),
            )
            return cur.fetchone() is not None

    def publisher_exists(self, publisher_id: int) -> bool:
        with self._lock:
            cur = self._conn.execute(
                "SELECT 1 FROM tpublishingcompany WHERE nPublishingCompanyID = ? LIMIT 1",
                (publisher_id,),
            )
            return cur.fetchone() is not None

    def insert_book(
        self,
        title: str,
        author_id: int,
        publisher_id: int,
        publishing_year: int,
    ) -> int:
        with self._lock:
            cur = self._conn.execute(
                """
                INSERT INTO tbook (cTitle, nAuthorID, nPublishingYear, nPublishingCompanyID)
                VALUES (?, ?, ?, ?)
                """,
                (title, author_id, publishing_year, publisher_id),
            )
            return int(cur.lastrowid)

    @staticmethod
    def row_to_book_dict(row: sqlite3.Row) -> dict[str, Any]:
        year = row["nPublishingYear"]
        year_int = int(year) if year is not None else 0
        return {
            "id": int(row["nBookID"]),
            "title": str(row["cTitle"]),
            "author_id": int(row["nAuthorID"]),
            "publishing_company_id": int(row["nPublishingCompanyID"]),
            "publishing_year": year_int,
        }
