CREATE VIEW Doc_Avg_Pbmi AS
	SELECT did, AVG(bmi) as doc_avg_bmi
    FROM Patient NATURAL JOIN Visit
    GROUP BY did;
  
SELECT did
FROM Doc_Avg_Pbmi
WHERE doc_avg_bmi = (SELECT MAX(doc_avg_bmi) FROM Doc_Avg_Pbmi)
ORDER BY did ASC;
