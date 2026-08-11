package emranhss.com.Modern_Hospital_Management_System.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{


        public ResourceNotFoundException(String message) {
            super(message);
        }
}
