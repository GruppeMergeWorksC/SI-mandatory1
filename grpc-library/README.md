# Library gRPC API (Python)

What each file is for (til Malte og Hero når i læser dette):
- protos/library.proto — Describes the gRPC API (the three methods and the data shapes). This is the contract. If you change it, you must regenerate the two pb2 files below.
- library_pb2.py — Auto-generated from the proto. Python types for messages. Do not edit by hand.
- library_pb2_grpc.py — Auto-generated from the proto. Hooks the server and client to those messages. Do not edit by hand.
- repository.py — Opens library.db and runs SQL (get book, check author/publisher exists, insert book).
- servicer.py — The real logic for GetBookById, CreateBook, and WatchBooks. Calls repository.py. WatchBooks keeps a list of listeners and sends each new book to them after a successful CreateBook.
- server.py — Starts the gRPC server on a port and wires servicer.py in. Run this to go live.
- client.py — Small command-line program to call the server (get / create / watch). Optional; you can use Postman instead.
- schema/library.sql — SQL that creates the same tables as library.db. For reference or if someone rebuilds the database.
Flow: the proto defines the API, the two pb2 files are generated from it, repository talks to SQLite, servicer implements the methods, server runs everything, client or Postman talks to the server.

---

This folder contains a small **gRPC** server that uses the **SQLite** file **`library.db`**. It supports three operations: look up a book, create a book, and watch for new books (streaming).

If you only want to **get it running and see that it works**, follow the steps below in order.

---

## Step 1 — Check the database file

The server expects **`library.db`** to sit in the **parent folder** of this project (the repo root), next to `grpc-library/`:

```text
SI-mandatory1/
  library.db          ← should be here
  grpc-library/       ← you are working inside this folder
    server.py
    ...
```

If your database lives somewhere else, you can point to it when starting the server (see [Environment variables](#environment-variables) at the bottom).

---

## Step 2 — Install Python dependencies

Open a terminal and go into **`grpc-library`**:

**Windows (PowerShell)**

```powershell
cd path\to\SI-mandatory1\grpc-library
python -m venv .venv
.\.venv\Scripts\Activate.ps1
pip install -r requirements.txt
```

**macOS / Linux**

```bash
cd path/to/SI-mandatory1/grpc-library
python3 -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
```

You need **Python 3.10 or newer**. After this, keep the virtual environment **activated** for the next steps.

---

## Step 3 — Start the server

Still inside **`grpc-library`**, with the venv active:

```powershell
python server.py
```

Leave this window open. When it works, you should see a log line similar to:

```text
INFO Library gRPC listening on [::]:50051 (db=...\library.db)
```

That means the server is waiting for clients on port **50051**.

---

## Step 4 — Try it in a second terminal

Open **another** terminal, activate the **same** venv, and `cd` into **`grpc-library`** again.

**4a — Fetch one book**

The bundled sample `library.db` often uses book IDs starting around **1000**, not 1. Try:

```powershell
python client.py get --id 1000
```

If it works, you get one line with the book’s id, title, author id, publisher id, and year.  
If you see an error about **NOT_FOUND**, that id is not in your file—try another id from your `tbook` table.

**4b — Create a book**

Creating a book only works if the **author** and **publisher** already exist. On the sample DB, ids like **394** (author) and **74** (publisher) match an existing row (same as book 1000). Example:

```powershell
python client.py create --title "My gRPC test" --author-id 394 --publisher-id 74 --year 2026
```

You should see `created book_id=...`. If you get **INVALID_ARGUMENT**, check the message: wrong ids, empty title, or year before 1900.

**4c — (Optional) Watch new books**

- In terminal A: `python client.py watch` (it will sit and wait).
- In terminal B: run `python client.py create ...` again.

Each successful create should print a line in terminal A. Stop the watcher with **Ctrl+C**.

---

## If something goes wrong

| What you see | What to try |
|--------------|-------------|
| `Database file not found` | Put `library.db` next to `grpc-library/`, or set `LIBRARY_DB` to the full path (see below). |
| Connection / refused errors | Make sure `server.py` is still running and you use the same port (default **50051**). |
| `NOT_FOUND` on get | That book id does not exist; use a real id from your database. |
| `INVALID_ARGUMENT` on create | Author or publisher id must exist; title must be non-empty; year ≥ 1900. |
| Odd behavior on `localhost` | Try `127.0.0.1`: set `LIBRARY_GRPC_TARGET=127.0.0.1:50051` for the client and/or bind the server with `LIBRARY_GRPC_HOST=0.0.0.0` (details below). |

---

# Reference (details)

The sections below are for anyone integrating with or changing this service.

## What this service implements

| RPC | Kind | Purpose |
|-----|------|---------|
| `GetBookById` | Unary | Input: book id. Output: book fields, or error if missing. |
| `CreateBook` | Unary | Input: title, author id, publisher id, year. Output: new book id. Validates data and foreign keys. |
| `WatchBooks` | Server streaming | After you connect, you receive a message for **each new book** created (by **any** client) until you disconnect. Past books are not replayed. |

Protobuf definitions live in **`protos/library.proto`** (package `library.v1`, service `LibraryService`). Share that file with your group so REST/SOAP/GraphQL and gRPC stay aligned.

### `Book` message fields

`id`, `title`, `author_id`, `publishing_company_id`, `publishing_year`. If the database has no year (`NULL`), the API returns `publishing_year = 0`.

### gRPC status codes (short)

- **NOT_FOUND** — book id does not exist (`GetBookById`).
- **INVALID_ARGUMENT** — bad id, empty title, year &lt; 1900, or author/publisher id not in the database (`CreateBook`).

---

## Environment variables

| Variable | Default | Meaning |
|----------|---------|---------|
| `LIBRARY_DB` | Repo root: `library.db` next to the `grpc-library` folder | Absolute or relative path to the SQLite file. |
| `LIBRARY_GRPC_HOST` | `[::]` | Address the server binds to. Use `0.0.0.0` for all interfaces / easier IPv4. |
| `LIBRARY_GRPC_PORT` | `50051` | Port the server listens on. |
| `LIBRARY_GRPC_TARGET` | `localhost:50051` | Used by **`client.py`** only: `host:port` of the server. |

Example (custom database path on Windows):

```powershell
$env:LIBRARY_DB = "D:\path\to\library.db"
python server.py
```

---

## Regenerate Python code after editing the `.proto`

Only needed if you change **`protos/library.proto`**:

```powershell
python -m grpc_tools.protoc -I./protos --python_out=. --grpc_python_out=. ./protos/library.proto
```

This overwrites **`library_pb2.py`** and **`library_pb2_grpc.py`**. You can version those files so others do not need `protoc` installed.

---

## Testing with grpcurl

Install grpcurl (see course Lesson 7 or [releases](https://github.com/fullstorydev/grpcurl/releases)). This server does **not** expose gRPC reflection; pass the proto explicitly.

From **`grpc-library/`**:

```text
grpcurl -plaintext -import-path ./protos -proto library.proto ^
  -d "{\"book_id\": 1000}" localhost:50051 library.v1.LibraryService/GetBookById
```

```text
grpcurl -plaintext -import-path ./protos -proto library.proto ^
  -d "{\"title\":\"Grpcurl Book\",\"author_id\":394,\"publishing_company_id\":74,\"publishing_year\":2024}" ^
  localhost:50051 library.v1.LibraryService/CreateBook
```

```text
grpcurl -plaintext -import-path ./protos -proto library.proto ^
  -d "{}" localhost:50051 library.v1.LibraryService/WatchBooks
```

---

## Postman (gRPC)

1. New gRPC request → server `localhost:50051`, TLS disabled unless you add certificates.
2. Import **`protos/library.proto`**.
3. Choose `library.v1.LibraryService` and the method; body fields match the proto (`book_id`, `title`, etc.).

You can save requests as a Postman collection for later reuse.

---

## Project layout

```text
grpc-library/
  protos/library.proto   # API contract
  library_pb2.py         # generated
  library_pb2_grpc.py    # generated
  repository.py          # SQLite access
  servicer.py            # RPC logic + watch fan-out
  server.py              # server entrypoint
  client.py              # CLI for manual tests
  schema/library.sql     # DDL mirror of the bundled database shape
  requirements.txt
  README.md
```