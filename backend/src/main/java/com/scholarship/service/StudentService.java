package com.scholarship.service;

import com.scholarship.entity.Student;
import com.scholarship.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    /**
     * 取得指定導師底下的所有學生
     */
    public List<Student> getStudentsByAdvisor(Integer advisorId) {
        return studentRepository.findByAdvisorId(advisorId);
    }

    /**
     * 根據學生ID取得學生資料
     */
    public Optional<Student> getStudentById(Integer studentId) {
        return studentRepository.findById(studentId);
    }

    /**
     * 取得所有學生
     */
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
}
