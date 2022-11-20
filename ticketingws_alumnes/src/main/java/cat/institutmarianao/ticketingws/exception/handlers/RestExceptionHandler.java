package cat.institutmarianao.ticketingws.exception.handlers;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import cat.institutmarianao.ticketingws.exception.model.ErrorModel;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		ErrorModel errors = new ErrorModel(status, ex.getBindingResult());
		return new ResponseEntity<>(errors.getBody(), headers, status);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		ErrorModel errors = new ErrorModel(status, ex.getLocalizedMessage());
		return new ResponseEntity<>(errors.getBody(), headers, status);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> handleConstraintViolationException(Exception ex) {
		
		ErrorModel errors = new ErrorModel(HttpStatus.UNPROCESSABLE_ENTITY, ex.getLocalizedMessage());
		return new ResponseEntity<>(errors.getBody(), HttpStatus.UNPROCESSABLE_ENTITY);
	}

	// GetMapping missing non optional parameters
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		ErrorModel errors = new ErrorModel(status, ex.getLocalizedMessage());
		return new ResponseEntity<>(errors.getBody(), headers, status);
	}

	// GetMapping Argument conversion 
	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		
		ErrorModel errors = new ErrorModel(status, ex.getLocalizedMessage());
		return new ResponseEntity<>(errors.getBody(), headers, status);
	}
	
	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		
		ErrorModel errors = new ErrorModel(status, ex.getLocalizedMessage());
		return new ResponseEntity<>(errors.getBody(), status);
	}
	
	// All other Exceptions
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleAllException(Exception ex) {
		
		ErrorModel errors = new ErrorModel(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage());
		return new ResponseEntity<>(errors.getBody(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
