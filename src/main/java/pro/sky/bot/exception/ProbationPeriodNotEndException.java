package pro.sky.bot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class ProbationPeriodNotEndException extends RuntimeException {
}
