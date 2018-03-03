package pl.finsys.example.student;

class StudentNotFoundException extends RuntimeException {
    StudentNotFoundException(String exception) {
        super(exception);
    }
}
