package controllers.authentication;

import play.data.Form;
import play.data.validation.Constraints.Required;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.authentication.login;

public class LoginController extends Controller
{
	public static Result login()
	{
		return ok(login.render(play.data.Form.form(LoginForm.class)));
	}

	/**
	 * Logout and clean the session.
	 */
	public static Result logout()
	{
		session().clear();
		flash("success", "Logout done");
		return redirect(routes.LoginController.login());
	}

	/**
	 * Handle login form submission.
	 */
	public static Result authenticate()
	{
		final Form<LoginForm> loginForm = play.data.Form.form(LoginForm.class).bindFromRequest();
		if (loginForm.hasErrors())
		{
			return badRequest(login.render(loginForm));
		}
		session(Secured.SESSION_KEY_USERNAME, loginForm.get().username);
		final String url = session().get(Secured.SESSION_KEY_REDIRECTURL);
		session().remove(Secured.SESSION_KEY_REDIRECTURL);
		if (null == url)
		{
			return redirect(controllers.routes.Application.index());
		}
		return redirect(url);
	}

	public static class LoginForm
	{
		@Required
		public String username;

		public String validate()
		{
			//TODO check if the user exists
			return null;
		}
	}
}
