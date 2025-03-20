SELECT student.name as student_name, student.age, faculty.name as faculty_name
FROM student
INNER JOIN faculty ON student.faculty_id = faculty.id;

SELECT avatar.id, avatar.file_path, student.name AS student_name, student.age, faculty.name AS faculty_name
FROM avatar
LEFT JOIN student ON avatar.student_id = student.id
LEFT JOIN faculty ON student.faculty_id = faculty.id;