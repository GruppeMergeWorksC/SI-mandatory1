# How to Use the Library GraphQL API:

Clone the repository
```bash
git clone git@github.com:GruppeMergeWorksC/SI-mandatory1.git
```

Navigate to the GraphQL-API directory
```bash
cd SI-mandatory1/GraphQL-API
```
*You can run the application either using Docker or locally with a Python virtual environment.*
##  Docker
1) Build the Docker image
```bash
docker build -t graphql-api .
```

2) Run the container
```bash
docker run --rm -p 8000:8000 graphql-api
```

3) Access the GraphQL API in browser (Strawberry UI) or via Postman
```
http://localhost:8000/graphql
```

Notes
- Make sure Docker is installed and running before building the image. 
- The database (library.db) is included in the image
- Data resets every time you rebuild or restart the container
- No local Python setup required

## Local Python virtual environment 

1) Create virtual environment
```bash
python -m venv .venv
```

2. Activate virtual environment  

Windows (PowerShell)
```powershell
.venv\Scripts\Activate
```

Linux
```bash
source .venv/bin/activate
```

3. Install dependencies

```bash
pip install -r requirements.txt
```

4. Run the server
```bash
uvicorn main:app --reload
```
5) Access the GraphQL API in browser (Strawberry UI) or via Postman
```
http://127.0.0.1:8000/graphql
```

## Additional notes  
- **queries.graphql** contains example queries and mutations