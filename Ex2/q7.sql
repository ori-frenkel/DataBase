SELECT did, MAX(fee), MIN(fee), AVG(fee)
FROM Doctor D NATURAL JOIN LEFT Visit V
GROUP BY did
ORDER BY did,MAX(fee), MIN(fee), AVG(fee) ASC;
