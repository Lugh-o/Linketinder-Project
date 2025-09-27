DROP TABLE IF EXISTS match_event CASCADE;
DROP TABLE IF EXISTS company_like CASCADE;
DROP TABLE IF EXISTS candidate_like CASCADE;
DROP TABLE IF EXISTS candidate_competency CASCADE;
DROP TABLE IF EXISTS job_competency CASCADE;
DROP TABLE IF EXISTS job CASCADE;
DROP TABLE IF EXISTS competency CASCADE;
DROP TABLE IF EXISTS candidate CASCADE;
DROP TABLE IF EXISTS company CASCADE;
DROP TABLE IF EXISTS person CASCADE;
DROP TABLE IF EXISTS address CASCADE;

CREATE TABLE address (
	id SERIAL PRIMARY KEY,
	state CHAR(2) NOT NULL,
	postal_code VARCHAR(16) NOT NULL,
	country VARCHAR(128) NOT NULL,
	street VARCHAR(128) NOT NULL,
	city VARCHAR(128) NOT NULL
);

CREATE TABLE person (
	id SERIAL PRIMARY KEY,
	email VARCHAR(255) UNIQUE NOT NULL,
	description TEXT NOT NULL,
	passwd VARCHAR(255) NOT NULL,
	id_address INT NOT NULL REFERENCES address(id)
);

CREATE TABLE company (
	id SERIAL PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	cnpj VARCHAR(18) NOT NULL,
	id_person INT NOT NULL REFERENCES person(id)
);

CREATE TABLE candidate (
	id SERIAL PRIMARY KEY,
	first_name VARCHAR(128) NOT NULL,
	last_name VARCHAR(128) NOT NULL,
	cpf VARCHAR(15) NOT NULL,
	date_of_birth DATE NOT NULL,
	graduation VARCHAR(128) NOT NULL,
	id_person INT NOT NULL REFERENCES person(id)
);

CREATE TABLE competency (
	id SERIAL PRIMARY KEY,
	name VARCHAR(128) NOT NULL
);

CREATE TABLE job (
	id SERIAL PRIMARY KEY,
	name VARCHAR(128) NOT NULL,
	description TEXT NOT NULL,
	id_address INT NOT NULL REFERENCES address(id),
	id_company INT NOT NULL REFERENCES company(id)
);

CREATE TABLE job_competency (
  id_job INT NOT NULL REFERENCES job(id),
  id_competency INT NOT NULL REFERENCES competency(id),
  PRIMARY KEY (id_job, id_competency)
);

CREATE TABLE candidate_competency (
  id_candidate INT NOT NULL REFERENCES candidate(id),
  id_competency INT NOT NULL REFERENCES competency(id),
  PRIMARY KEY (id_candidate, id_competency)
);

CREATE TABLE candidate_like (
  id_candidate INT NOT NULL REFERENCES candidate(id),
  id_job INT NOT NULL REFERENCES job(id),
  PRIMARY KEY (id_candidate, id_job)
);

CREATE TABLE company_like (
  id_company INT NOT NULL REFERENCES company(id),
  id_candidate INT NOT NULL REFERENCES candidate(id),
  PRIMARY KEY (id_company, id_candidate)
);

CREATE TABLE match_event (
  id_job INT NOT NULL REFERENCES job(id),
  id_candidate INT NOT NULL REFERENCES candidate(id),
  PRIMARY KEY (id_job, id_candidate)
);