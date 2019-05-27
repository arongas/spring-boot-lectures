DROP DATABASE IF EXISTS "aggregator";
DROP USER IF EXISTS aggregator;
CREATE USER aggregator WITH LOGIN PASSWORD 'aggregator@@@';
CREATE DATABASE "aggregator" WITH OWNER = aggregator ENCODING = 'UTF8';