SELECT (pid,pname)
FROM Doctor, Patient, Visit
WHERE Doctor.did=Visit.did and Patient.pid=Visit.pid and
Doctor.specialty="pediatrician"
ORDER BY (pid,pname) ASC
INTERSECT
SELECT (pid,pname)
FROM Doctor, Patient, Visit
WHERE Doctor.did=Visit.did and Patient.pid=Visit.pid and
Doctor.specialty="orthopedist";
ORDER BY (pid,pname) ASC;
