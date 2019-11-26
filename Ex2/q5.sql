-- Ratonial Algebra's divsion of part1/part2
CREATE VIEW PART1 AS
SELECT dname,did,pid
FROM Doctor NATURAL JOIN Visit
WHERE Doctor.specialty='pediatrician';

CREATE VIEW PART2 AS
SELECT pid
FROM Patient NATURAL JOIN Visit
WHERE gender='M' and bmi>30;

SELECT DISTINCT x.dname
FROM PART1 AS x
WHERE NOT EXISTS (
                  SELECT *
                  FROM  PART2 AS y
                  WHERE NOT EXISTS (
                                     SELECT *
                                     FROM PART1 AS z
                                     WHERE (z.did=x.did) AND(z.pid=y.pid)));
