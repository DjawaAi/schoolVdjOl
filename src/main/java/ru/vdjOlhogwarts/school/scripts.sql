select *
from student;

select *
from student
where age >= 17
  AND age <= 19;

select *
from student
where age BETWEEN 19 AND 20;

select student.name
from student;

select *
from student
where name like '%о%';

select *
from student
where age > id;

SELECT *
FROM student
ORDER BY age;