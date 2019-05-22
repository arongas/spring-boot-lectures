DROP DATABASE IF EXISTS "library";
DROP USER IF EXISTS library;
CREATE USER library WITH LOGIN PASSWORD 'library@@@';
CREATE DATABASE "library" WITH OWNER = library ENCODING = 'UTF8';