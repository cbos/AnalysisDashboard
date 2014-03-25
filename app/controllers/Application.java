package controllers;

import java.io.File;

import play.Play;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import utils.file.FileTailReader;
import analysis.listener.WebSocketAnalysisListener;

public class Application extends Controller
{

	public static Result index()
	{
		return redirect(routes.Assets.at("index.html"));
	}

	public static Result getLog()
	{
		setAsInlineResponse();
		return ok(getLogFile()).as("text/plain");
	}

	public static Result getLogTail()
	{
		setAsInlineResponse();
		return ok(FileTailReader.tail(getLogFile(), 300)).as("text/plain");
	}

	private static File getLogFile()
	{
		return Play.application().getFile("logs/application.log");
	}

	private static void setAsInlineResponse()
	{
		response().setHeader("Content-Disposition", "inline;");
	}

	public static WebSocket<String> websocket()
	{
		return new WebSocketAnalysisListener();
	}
}