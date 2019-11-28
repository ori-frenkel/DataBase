SELECT did, MAX(fee), MIN(fee), AVG(fee)
FROM Doctor NATURAL LEFT JOIN Visit
GROUP BY did
ORDER BY did,MAX(fee), MIN(fee), AVG(fee) ASC;
