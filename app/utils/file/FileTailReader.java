package utils.file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileTailReader
{

	public static String tail(File file, int lines)
	{
		try (RandomAccessFile fileHandler = new RandomAccessFile(file, "r"))
		{
			StringBuilder sb = new StringBuilder();
			readLines(lines, fileHandler, sb);
			return sb.reverse().toString();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	private static void readLines(int lines, RandomAccessFile fileHandler, StringBuilder sb) throws IOException
	{
		long fileLength = fileHandler.length() - 1;
		int lineCount = 0;

		for (long filePointer = fileLength; filePointer != -1; filePointer--)
		{
			fileHandler.seek(filePointer);
			int readByte = fileHandler.readByte();

			if (readByte == 0xA) //linefeed
			{
				lineCount = lineCount + 1;
				if (lineCount == lines)
				{
					break;
				}
			}
			sb.append((char) readByte);
		}
	}
}
