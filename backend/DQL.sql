-- VAGAS VISTAS POR UM CANDIDATO
SELECT 
j.name AS job_name,
j.description AS job_description,
STRING_AGG(DISTINCT ct.name, ', ') AS required_competencies
FROM job j
INNER JOIN job_competency jc ON jc.id_job = j.id
INNER JOIN competency ct ON jc.id_competency = ct.id
GROUP BY j.name, j.description;

-- CANDIDATOS VISTOS POR UMA EMPRESA
SELECT 
cd.graduation AS candidate_graduation,
p.description AS candidate_description,
STRING_AGG(DISTINCT ct.name, ', ') AS candidate_competencies
FROM candidate cd
INNER JOIN candidate_competency cc ON cc.id_candidate = cd.id
INNER JOIN competency ct ON cc.id_competency = ct.id
INNER JOIN person p ON cd.id_person = p.id
GROUP BY cd.graduation, p.description;

-- LIKES VISTOS POR UMA EMPRESA
SELECT 
j.id AS job_id,
j.name AS job_name,
cd.graduation AS candidate_graduation,
p.description AS candidate_description,
STRING_AGG(DISTINCT ct.name, ', ') AS candidate_competencies
FROM candidate_like cl
INNER JOIN job j ON cl.id_job = j.id
INNER JOIN company cp ON j.id_company = cp.id
INNER JOIN candidate cd ON cl.id_candidate = cd.id
INNER JOIN candidate_competency cc ON cc.id_candidate = cd.id
INNER JOIN competency ct ON cc.id_competency = ct.id
INNER JOIN person p ON cd.id_person = p.id
WHERE cp.id = 1 -- ID DA EMPRESA QUE ESTÁ CONSULTANDO
GROUP BY j.id, j.name, cd.graduation, p.description;

-- MATCHES
SELECT 
CONCAT(cd.first_name, ' ', cd.last_name) AS candidate_name,
cd.graduation AS candidate_graduation,
j.name AS job_name,
cp.name AS company_name
FROM match_event me
INNER JOIN candidate cd ON me.id_candidate = cd.id
INNER JOIN job j ON me.id_job = j.id
INNER JOIN company cp ON j.id_company = cp.id;