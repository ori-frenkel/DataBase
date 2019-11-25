SELECT pid,did
FROM Doctor NATURAL JOIN Patient
ORDER BY (pid,did) ASC;
EXCEPT
SELECT pid,did
FROM Visit NATURAL JOIN Patient;
ORDER BY (pid,did) ASC;
