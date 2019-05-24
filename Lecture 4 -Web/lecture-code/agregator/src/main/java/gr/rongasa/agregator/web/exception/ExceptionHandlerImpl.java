package gr.rongasa.agregator.web.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandlerImpl  /*extends ResponseEntityExceptionHandler */ {

    @ExceptionHandler(value = {SensorException.class})
    protected ResponseEntity<Object> handleSensorException(SensorException sensorException, WebRequest request) {
        return new ResponseEntity<>(new ErrorMessage(sensorException.getMessage(), null), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }


}


