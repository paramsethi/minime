CREATE KEYSPACE minime WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 }; 
USE minime; 
CREATE TABLE url_mapping (url_hash text PRIMARY KEY, orig_url text); 
CREATE INDEX ON url_mapping (orig_url); 