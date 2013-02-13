package controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import play.Play;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller
{

	public static Result index()
	{
		return redirect(routes.Assets.at("index.html"));
	}

	public static Result getLog() throws IOException
	{
		File logfile = Play.application().getFile("logs/application.log");
		return ok(Files.readAllBytes(logfile.toPath())).as("text/plain");
	}
}