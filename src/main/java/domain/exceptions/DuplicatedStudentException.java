package domain.exceptions;

public class DuplicatedStudentException extends RuntimeException {

    public DuplicatedStudentException(Long id) {
        super("Unable to complete the registration. Student with ID " + id + " already exists.");
    }
}