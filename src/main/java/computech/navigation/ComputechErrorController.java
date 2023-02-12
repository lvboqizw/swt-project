package computech.navigation;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;


@Controller
public class ComputechErrorController implements ErrorController {

	/**
	 * return the error information to the user
	 * @param request
	 * @return return the error information
	 */
    @RequestMapping(value ="/error", method= RequestMethod.POST)
    @ResponseBody
    public String handleError(HttpServletRequest request) {
      Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
      Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
      return String.format("<html><body><h2>Error Page</h2><div>Status code: <b>%s</b></div>"
                      + "<div>Exception Message: <b>%s</b></div><body></html>",
              statusCode, exception==null? "N/A": exception.getMessage());
    }

	/**
	 * get the error path
	 * @return error value
	 */
	@Override
    public String getErrorPath() {
      return "/error";
    }
}
