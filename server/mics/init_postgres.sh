docker run -d \
  --name muzic-postgres \
  -e POSTGRES_DB=muzicdb \          # ← This creates your desired database name automatically
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=0 \
  -p 5432:5432 \
  -v postgres_data:/var/lib/postgresql/data \
  postgres:17
