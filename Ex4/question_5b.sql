SELECT * from People 
WHERE (country, bdate) IN 
    (
    SELECT country, min(bdate) 
    from People 
    group by country
    );
