package id.co.mii.sitego.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.co.mii.sitego.model.response.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
@AllArgsConstructor
@Slf4j
public class ErrorCustomController implements ErrorController {
    private final ObjectMapper objectMapper;

    @RequestMapping("/error")
    public String errorhandler(HttpServletRequest request, Model model) {
        Exception exception = (Exception) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        String errorMessage = "";
        String errorTitle = "";
        Integer httpErrorCode = getErrorCode(request);

        if (exception != null && exception.getCause() != null && exception.getCause() instanceof HttpClientErrorException) {
            HttpClientErrorException clientException = (HttpClientErrorException) exception.getCause();
            try {
                ErrorResponse errorResponse = objectMapper.readValue(clientException.getResponseBodyAsString(), ErrorResponse.class);
                httpErrorCode = errorResponse.getStatus();
                errorMessage = String.join(", ", errorResponse.getMessages());
                switch (httpErrorCode) {
                    case 400: {
                        errorTitle = "Error 400 Bad Request";
                        break;
                    }
                    case 401: {
                        errorTitle = "Error 401 Unauthorized";
                        break;
                    }
                    case 404: {
                        errorTitle = "Error 404 Not Found";
                        break;
                    }
                    default: {
                        errorTitle = "Error 500 Internal Server Error";
                        break;
                    }
                }

            } catch (JsonProcessingException e) {
                log.error("Failed to parse error", e);
            }
        } else {
            switch (httpErrorCode) {
                case 400: {
                    errorTitle = "Error 400 Bad Request";
                    errorMessage = "Oops! We couldn't understand your request. Please try again.";
                    break;
                }
                case 401: {
                    errorTitle = "Error 401 Unauthorized";
                    errorMessage = "Access denied. You need the right credentials to enter.";
                    break;
                }
                case 404: {
                    errorTitle = "Error 404 Not Found";
                    errorMessage = "Page not found. Let's get you back on track.";
                    break;
                }
                default: {
                    errorTitle = "Error 500 Internal Server Error";
                    errorMessage = "We hit a snag. Our team is on it. Please come back later.";
                    break;
                }
            }
        }

        model.addAttribute("statusCode", httpErrorCode);
        model.addAttribute("titleError", errorTitle);
        model.addAttribute("errorMessage", errorMessage);
        return "error/index";
    }

    private int getErrorCode(HttpServletRequest httpRequest) {
        return (Integer) httpRequest
            .getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    }
}
