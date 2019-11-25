SELECT DISTINCT pname
FROM Doctor, Patient, Visit
WHERE Doctor.dname="Avi Cohen" and Visit.fee = 0 and Doctor.did=Visit.did and Patient.pid=Visit.pid
ORDER BY pname ASC;
