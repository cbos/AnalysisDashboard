package controllers.authentication;

import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

public class Secured extends Security.Authenticator
{
	public static final String SESSION_KEY_USERNAME = "username";

	public static final String SESSION_KEY_REDIRECTURL = "redirectURL";

	@Override
	public String getUsername(final Context ctx)
	{
		return ctx.session().get(SESSION_KEY_USERNAME);
	}

	@Override
	public Result onUnauthorized(final Context ctx)
	{
		System.out.println("context url: " + ctx.request().uri());
		
		ctx.session().put(SESSION_KEY_REDIRECTURL,
											"GET".equals(ctx.request().method()) ? ctx.request().uri() : "/");
		return redirect(controllers.authentication.routes.LoginController.login());
	}
}
