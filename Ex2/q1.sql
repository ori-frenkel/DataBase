SELECT pid
FROM Patient P,	Visit V
WHERE P.pid = V.pid and V.vdate='11-11-11'
ORDER BY pid ASC;
