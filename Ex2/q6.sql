SELECT did
FROM Doctor D
WHERE 3=(SELECT count(pid)
         FROM Visit NATURAL JOIN Patient
         WHERE D.did=did and bmi > 30)
ORDER BY did ASC; 
