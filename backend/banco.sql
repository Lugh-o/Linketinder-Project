CREATE TABLE "person" (
  "id" SERIAL PRIMARY KEY,
  "email" varchar UNIQUE NOT NULL,
  "state" varchar NOT NULL,
  "cep" varchar NOT NULL,
  "description" varchar NOT NULL,
  "password" varchar NOT NULL,
  "country" varchar NOT NULL
);

CREATE TABLE "company" (
  "id" SERIAL PRIMARY KEY,
  "name" varchar NOT NULL,
  "id_person" integer NOT NULL,
  "cnpj" varchar NOT NULL
);

CREATE TABLE "candidate" (
  "id" SERIAL PRIMARY KEY,
  "first_name" varchar NOT NULL,
  "last_name" varchar NOT NULL,
  "id_person" integer NOT NULL,
  "cpf" varchar NOT NULL,
  "date_of_birth" date NOT NULL,
  "graduation" varchar NOT NULL
);

CREATE TABLE "competency" (
  "id" SERIAL PRIMARY KEY,
  "name" varchar NOT NULL
);

CREATE TABLE "job" (
  "id" SERIAL PRIMARY KEY,
  "name" varchar NOT NULL,
  "id_company" integer NOT NULL,
  "location" varchar NOT NULL
);

CREATE TABLE "job_competency" (
  "id" SERIAL PRIMARY KEY,
  "id_job" integer NOT NULL,
  "id_competency" integer NOT NULL
);

CREATE TABLE "candidate_competency" (
  "id" SERIAL PRIMARY KEY,
  "id_candidate" integer NOT NULL,
  "id_competency" integer NOT NULL
);

CREATE TABLE "likes" (
  "id" SERIAL PRIMARY KEY,
  "id_candidate" integer NOT NULL,
  "id_job" integer NOT NULL,
  "was_reciprocated" boolean NOT NULL DEFAULT false
);

ALTER TABLE "company" ADD FOREIGN KEY ("id_person") REFERENCES "person" ("id");
ALTER TABLE "candidate" ADD FOREIGN KEY ("id_person") REFERENCES "person" ("id");
ALTER TABLE "job" ADD FOREIGN KEY ("id_company") REFERENCES "company" ("id");
ALTER TABLE "job_competency" ADD FOREIGN KEY ("id_job") REFERENCES "job" ("id");
ALTER TABLE "job_competency" ADD FOREIGN KEY ("id_competency") REFERENCES "competency" ("id");
ALTER TABLE "candidate_competency" ADD FOREIGN KEY ("id_candidate") REFERENCES "candidate" ("id");
ALTER TABLE "candidate_competency" ADD FOREIGN KEY ("id_competency") REFERENCES "competency" ("id");
ALTER TABLE "likes" ADD FOREIGN KEY ("id_candidate") REFERENCES "candidate" ("id");
ALTER TABLE "likes" ADD FOREIGN KEY ("id_job") REFERENCES "job" ("id");


INSERT INTO person (email, state, cep, description, password, country) VALUES
('contato@techbr.com', 'SP', '01001-000', 'Empresa de tecnologia focada em IA', 'senha123', 'Brasil'),
('info@saudebem.com', 'RJ', '20010-000', 'Clínica médica popular', 'senha123', 'Brasil'),
('hello@edufuturo.com', 'MG', '30110-000', 'Plataforma de ensino online', 'senha123', 'Brasil'),
('corp@financasmax.com', 'PR', '80010-000', 'Consultoria financeira corporativa', 'senha123', 'Brasil'),
('atendimento@greenagro.com', 'RS', '90010-000', 'Empresa de agronegócio sustentável', 'senha123', 'Brasil'),

('joao.silva@email.com', 'SP', '04001-000', 'Candidato desenvolvedor backend', 'senha123', 'Brasil'),
('maria.oliveira@email.com', 'RJ', '22010-000', 'Candidata analista de dados', 'senha123', 'Brasil'),
('carlos.souza@email.com', 'MG', '31010-000', 'Candidato engenheiro de software', 'senha123', 'Brasil'),
('ana.costa@email.com', 'RS', '93010-000', 'Candidata cientista de dados', 'senha123', 'Brasil'),
('luiz.ferreira@email.com', 'BA', '40010-000', 'Candidato frontend developer', 'senha123', 'Brasil');

INSERT INTO company (name, id_person, cnpj) VALUES
('TechBR', 1, '12.345.678/0001-90'),
('SaúdeBem', 2, '98.765.432/0001-11'),
('EduFuturo', 3, '11.222.333/0001-44'),
('FinançasMax', 4, '55.666.777/0001-88'),
('GreenAgro', 5, '22.333.444/0001-55');

INSERT INTO candidate (first_name, last_name, id_person, cpf, date_of_birth, graduation) VALUES
('João', 'Silva', 6, '111.222.333-44', '1995-06-10', 'Engenharia da Computação'),
('Maria', 'Oliveira', 7, '555.666.777-88', '1993-04-22', 'Estatística'),
('Carlos', 'Souza', 8, '999.000.111-22', '1990-12-01', 'Ciência da Computação'),
('Ana', 'Costa', 9, '333.444.555-66', '1996-09-15', 'Matemática'),
('Luiz', 'Ferreira', 10, '777.888.999-00', '1994-03-30', 'Design Digital');

