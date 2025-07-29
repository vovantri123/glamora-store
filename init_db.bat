@echo off
SET PATH=D:\File_da_tai\PostgreSQL\17\bin;%PATH%

SET PGUSER=postgres
SET PGPASSWORD=1234567890
SET PGHOST=localhost
SET PGPORT=5432

echo Terminating existing connections to database "glamora_store"...
psql -U %PGUSER% -h %PGHOST% -p %PGPORT% -d postgres -c "SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname = 'glamora_store' AND pid <> pg_backend_pid();"

echo Dropping database "glamora_store" if it exists...
psql -U %PGUSER% -h %PGHOST% -p %PGPORT% -d postgres -c "DROP DATABASE IF EXISTS glamora_store;"

echo Creating database "glamora_store"...
psql -U %PGUSER% -h %PGHOST% -p %PGPORT% -d postgres -c "CREATE DATABASE glamora_store;"

echo Done.
pause
