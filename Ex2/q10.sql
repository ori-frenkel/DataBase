DELETE FROM Patient
WHERE pid not in (SELECT pid
                 FROM Patient NATURAL JOIN Visit);
