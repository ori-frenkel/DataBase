CREATE VIEW AS query1
    select distinct *
    from People P1
    where bdate = (select min(bdate)
                   from People P2
                   where P2.country = P1.country);

CREATE VIEW AS query2
SELECT * from People 
WHERE (country, bdate) IN 
    (
    SELECT country, min(bdate) 
    from People 
    group by country
    );


SELECT * 
FROM query1
EXCEPT
SELECT * 
FROM query2;

DROP VIEW query1;
DROP VIEW query2;
