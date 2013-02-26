package controllers.data;

import model.user.User;

public class UserController extends EntityController<User>
{
	public UserController()
	{
		super(User.class);
	}
}
