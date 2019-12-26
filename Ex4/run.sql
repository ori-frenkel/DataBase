select distinct *
from People P1
where bdate = (select min(bdate)
               from People P2
               where P2.country = P1.country);
