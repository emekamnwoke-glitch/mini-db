# MiniDB

A minimal relational database engine built from scratch in Java — no external libraries, no frameworks. Built as a Master's degree portfolio project demonstrating core CS fundamentals.

![CI](https://github.com/YOUR_USERNAME/mini-db/actions/workflows/ci.yml/badge.svg)

## Architecture

```
SQL String
    │
    ▼
┌─────────┐     ┌─────────┐     ┌──────────┐     ┌─────────┐
│  Lexer  │────▶│ Parser  │────▶│ Executor │────▶│ Storage │
└─────────┘     └─────────┘     └──────────┘     └─────────┘
  Tokens            AST           Query Plan      In-Memory Tables
```

| Layer | File | Responsibility |
|---|---|---|
| Lexer | `Lexer.java` | Tokenizes raw SQL string into typed tokens |
| Parser | `Parser.java` | Builds an Abstract Syntax Tree (AST) from tokens |
| AST | `AST.java` | Node types: `SelectStatement`, `InsertStatement`, `CreateTableStatement` |
| Executor | `Executor.java` | Walks the AST and produces results |
| Storage | `Storage.java` | In-memory `HashMap<String, Table>` with row lists |

## Supported SQL

```sql
CREATE TABLE users (id INT, name TEXT);
INSERT INTO users VALUES (1, 'Alice');
SELECT * FROM users;
SELECT name FROM users WHERE id = 1;
```

## Running

**Prerequisites:** Java 21+, Maven 3.8+

```bash
# Clone
git clone https://github.com/YOUR_USERNAME/mini-db.git
cd mini-db

# Run tests
mvn test

# Build JAR
mvn package

# Launch REPL
java -jar target/mini-db.jar
```

**Example session:**
```
minidb> CREATE TABLE users (id INT, name TEXT)
Table 'users' created.

minidb> INSERT INTO users VALUES (1, 'Alice')
1 row inserted.

minidb> INSERT INTO users VALUES (2, 'Bob')
1 row inserted.

minidb> SELECT * FROM users
id | name
---------
1  | Alice
2  | Bob
(2 rows)

minidb> SELECT name FROM users WHERE id = 1
name
----
Alice
(1 row)

minidb> exit
Bye!
```

## Project Structure

```
mini-db/
├── src/
│   ├── main/java/minidb/
│   │   ├── AST.java          ← AST node definitions
│   │   ├── Executor.java     ← Query execution logic
│   │   ├── Lexer.java        ← SQL tokenizer
│   │   ├── Main.java         ← REPL entry point
│   │   ├── Parser.java       ← Token → AST parser
│   │   └── Storage.java      ← In-memory storage engine
│   └── test/java/minidb/
│       ├── ExecutorTest.java ← Integration tests
│       ├── LexerTest.java    ← Lexer unit tests
│       └── ParserTest.java   ← Parser unit tests
├── .github/workflows/ci.yml  ← GitHub Actions CI
└── pom.xml
```

## CS Concepts Demonstrated

- **Lexical analysis** — character-by-character tokenization
- **Recursive descent parsing** — hand-written top-down parser
- **Abstract Syntax Trees** — structured in-memory representation of queries
- **Separation of concerns** — each layer has a single, testable responsibility
- **Data structures** — HashMap for table lookup, ArrayList for rows and schemas

## Possible Extensions

- `DELETE FROM` and `UPDATE` statements
- `ORDER BY` and `LIMIT` clauses
- File-based persistence with a simple binary format
- B-tree index for faster WHERE lookups
- Multi-column WHERE with `AND` / `OR`
