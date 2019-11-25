SELECT pid,pname
FROM Doctor, Patient, Visit
WHERE Doctor.did=Visit.did and Patient.pid=Visit.pid and
Doctor.specialty="pediatrician"
INTERSECT
SELECT pid,pname
FROM Doctor, Patient, Visit
WHERE Doctor.did=Visit.did and Patient.pid=Visit.pid and
Doctor.specialty="orthopedist";
