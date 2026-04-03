

## Database:

The library database can be either run locally or as a Docker container.
### Docker:

1) Set environment variables noted in dotenv-template.  

Check if env vars are set in the shell
```bash
echo $MYSQL_ROOT_PASSWORD
echo $MYSQL_USER
echo $MYSQL_PASSWORD
```

Set env vars
```bash
export MYSQL_ROOT_PASSWORD=yourrootpassword
export MYSQL_USER=youruser
export MYSQL_PASSWORD=yourpassword
```

2) Build the image from docker-compose
```bash
docker compose up -d
```
Restart Docker image
```bash
docker compose down -v
docker compose up -d
```
Remove Docker image
```bash
docker rmi mysql:8.4
```

 
### Inspect the Database
You can inspect the database by MySQL Workbench, from CLI or from PhpMyAdmin Web Interface, which spins up together with the dockerized database.  
PhpMyAdmin: http://localhost:8090/
```sql
SHOW DATABASES;
```

Select schema:
```sql
USE library;
```
Show tables:
```sql
SHOW TABLES;
```
Show table structure:
```sql
DESCRIBE books;
```
First 25 rows:
```sql
SELECT * FROM books LIMIT 25;
```
