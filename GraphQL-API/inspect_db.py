import sqlite3

conn = sqlite3.connect("library.db")
cursor = conn.cursor()

print(cursor.execute("PRAGMA table_info(tauthor)").fetchall())
# [(0, 'nAuthorID', 'INTEGER', 1, None, 1), (1, 'cName', 'varchar(40)', 1, None, 0), (2, 'cSurname', 'varchar(60)', 0, 'NULL', 0)]

print(cursor.execute("PRAGMA table_info(tbook)").fetchall())
# [(0, 'nBookID', 'INTEGER', 1, None, 1), (1, 'cTitle', 'varchar(255)', 1, None, 0), (2, 'nAuthorID', 'INTEGER', 1, None, 0), (3, 'nPublishingYear', 'decimal(4,0)', 0, 'NULL', 0), (4, 'nPublishingCompanyID', 'INTEGER', 1, None, 0)]

print(cursor.execute("PRAGMA table_info(tpublishingcompany)").fetchall())
# [(0, 'nPublishingCompanyID', 'INTEGER', 1, None, 1), (1, 'cName', 'varchar(40)', 1, None, 0)]

#None, 1 = NOT NULL
#None, 0 = nullable