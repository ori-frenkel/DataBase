UPDATE Doctor
SET clinic =(
  CASE 
  	WHEN clinic=0 THEN 1
  	WHEN clinic=1 THEN 0
    ELSE clinic
  END)
  WHERE clinic IN (0,1);
