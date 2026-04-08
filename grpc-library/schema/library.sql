-- SQLite schema for the library database (matches bundled library.db).
-- Use for documentation or if you migrate to another DBMS.

CREATE TABLE IF NOT EXISTS `tauthor` (
  `nAuthorID` integer NOT NULL PRIMARY KEY AUTOINCREMENT,
  `cName` varchar(40) NOT NULL,
  `cSurname` varchar(60) DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS `tpublishingcompany` (
  `nPublishingCompanyID` integer NOT NULL PRIMARY KEY AUTOINCREMENT,
  `cName` varchar(40) NOT NULL
);

CREATE TABLE IF NOT EXISTS `tbook` (
  `nBookID` integer NOT NULL PRIMARY KEY AUTOINCREMENT,
  `cTitle` varchar(255) NOT NULL,
  `nAuthorID` integer NOT NULL,
  `nPublishingYear` decimal(4,0) DEFAULT NULL,
  `nPublishingCompanyID` integer NOT NULL
);
