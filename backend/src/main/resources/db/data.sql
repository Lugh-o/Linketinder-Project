INSERT INTO address (state, postal_code, country, city, street) VALUES
('SP', '01001-000', 'Brasil', 'São Paulo', 'Av. Paulista, 1000'),
('RJ', '20010-000', 'Brasil', 'Rio de Janeiro', 'Rua das Laranjeiras, 200'),
('MG', '30110-000', 'Brasil', 'Belo Horizonte', 'Av. Afonso Pena, 300'),
('PR', '80010-000', 'Brasil', 'Curitiba', 'Rua XV de Novembro, 400'),
('RS', '90010-000', 'Brasil', 'Porto Alegre', 'Av. Borges de Medeiros, 500');

INSERT INTO person (email, description, passwd, id_address) VALUES
('contato@techbr.com', 'Empresa de tecnologia focada em IA', 'senha123', 1),
('info@saudebem.com', 'Clínica médica popular', 'senha123', 2),
('hello@edufuturo.com', 'Plataforma de ensino online', 'senha123', 3),
('corp@financasmax.com', 'Consultoria financeira corporativa', 'senha123', 4),
('atendimento@greenagro.com', 'Empresa de agronegócio sustentável', 'senha123', 5);

INSERT INTO person (email, description, passwd, id_address) VALUES
('joao.silva@email.com', 'Candidato desenvolvedor backend', 'senha123', 1),
('maria.oliveira@email.com', 'Candidata analista de dados', 'senha123', 2),
('carlos.souza@email.com', 'Candidato engenheiro de software', 'senha123', 3),
('ana.costa@email.com', 'Candidata cientista de dados', 'senha123', 4),
('luiz.ferreira@email.com', 'Candidato frontend developer', 'senha123', 5);

INSERT INTO company (name, id_person, cnpj) VALUES
('TechBR', 1, '12.345.678/0001-90'),
('SaúdeBem', 2, '98.765.432/0001-11'),
('EduFuturo', 3, '11.222.333/0001-44'),
('FinançasMax', 4, '55.666.777/0001-88'),
('GreenAgro', 5, '22.333.444/0001-55');

INSERT INTO candidate (first_name, last_name, id_person, cpf, birthday, graduation) VALUES
('João', 'Silva', 6, '111.222.333-44', '1995-06-10', 'Engenharia da Computação'),
('Maria', 'Oliveira', 7, '555.666.777-88', '1993-04-22', 'Estatística'),
('Carlos', 'Souza', 8, '999.000.111-22', '1990-12-01', 'Ciência da Computação'),
('Ana', 'Costa', 9, '333.444.555-66', '1996-09-15', 'Matemática'),
('Luiz', 'Ferreira', 10, '777.888.999-00', '1994-03-30', 'Design Digital');

INSERT INTO job (name, description, id_company, id_address) VALUES
('Desenvolvedor Backend', 'Vaga de backend', 1, 1),
('Analista de Dados', 'Vaga de dados', 2, 2),
('Engenheiro de Software', 'Vaga de engenheiro', 3, 3),
('Consultor Financeiro', 'Vaga de consultor', 4, 4),
('Desenvolvedor Frontend', 'Vaga de frontend', 5, 5);

-- INSERT INTO candidate_like (id_candidate, id_job) VALUES
-- (1,1),
-- (2,2),
-- (3,3),
-- (4,4),
-- (5,5);

-- INSERT INTO company_like (id_company, id_candidate) VALUES
-- (1,1),
-- (2,2),
-- (3,3),
-- (4,4),
-- (5,5);

-- INSERT INTO match_event (id_job, id_candidate) VALUES
-- (1,1),
-- (2,2),
-- (3,3),
-- (4,4),
-- (5,5);

INSERT INTO competency (name) VALUES
('JAVA'),
('PYTHON'),
('C_SHARP'),
('SPRING_BOOT'),
('PHP'),
('LARAVEL'),
('DOCKER'),
('JENKINS'),
('POSTGRESQL'),
('MYSQL'),
('SWAGGER'),
('KAFKA'),
('MICRONAUT'),
('LINUX'),
('DOTNET');


INSERT INTO candidate_competency (id_candidate, id_competency) VALUES
(1, 1),
(1, 7),
(1, 9);

INSERT INTO candidate_competency (id_candidate, id_competency) VALUES
(2, 2),
(2, 10),
(2, 12);

INSERT INTO candidate_competency (id_candidate, id_competency) VALUES
(3, 1),
(3, 4),
(3, 13);

INSERT INTO candidate_competency (id_candidate, id_competency) VALUES
(4, 2),
(4, 8),
(4, 14);

INSERT INTO candidate_competency (id_candidate, id_competency) VALUES
(5, 3),
(5, 15),
(5, 11);

INSERT INTO job_competency (id_job, id_competency) VALUES
(1, 1),
(1, 4),
(1, 7);

INSERT INTO job_competency (id_job, id_competency) VALUES
(2, 2),
(2, 10),
(2, 12);

INSERT INTO job_competency (id_job, id_competency) VALUES
(3, 1),
(3, 4),
(3, 13);

INSERT INTO job_competency (id_job, id_competency) VALUES
(4, 8),
(4, 14),
(4, 9);

INSERT INTO job_competency (id_job, id_competency) VALUES
(5, 3),
(5, 15),
(5, 11);
