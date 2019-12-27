SELECT * FROM 
(select distinct *
from People P1
where bdate = (select min(bdate)
               from People P2
               where P2.country = P1.country)) AS FOO1
EXCEPT
SELECT * FROM
(SELECT * from People 
WHERE (country, bdate) IN 
    (
    SELECT country, min(bdate) 
    from People 
    group by country
    )) AS FOO2;
