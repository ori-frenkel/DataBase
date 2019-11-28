SELECT DISTINCT V.pid
FROM Patient P,	Visit V
WHERE P.pid = V.pid and V.vdate='1111-11-11'
ORDER BY pid ASC;
