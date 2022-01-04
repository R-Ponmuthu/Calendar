package com.it.core.log;

import java.util.Date;

public class LogEntry {

	public enum LogLevel
	{
		VERBOSE(1),
		DEBUG(2),
		INFO(3),
		WARN(4),
		ERROR(5);
		
		private int ID;
		
		LogLevel(int id) {
			ID = id;
		}

		public int getID() {
			return ID;
		}
		
		@Override
		public String toString() {
			switch(this)
			{
			case DEBUG:
				return "Debug";
			case ERROR:
				return "Error";
			case INFO:
				return "Info";
			case VERBOSE:
				return "Verbose";
			case WARN:
				return "Warn";
			}
			return VERBOSE.toString();
		}
		
		public static LogLevel getLevel(int ID)
		{
			for (LogLevel level : values()) {
				if(level.getID() == ID)
					return level;
			}
			
			return VERBOSE;
		}
	}
	
	private Date LogDate;
	private String Tag;
	private String Text;
	private LogLevel Level;

	public LogEntry() {

	}

	void set(Date logDate, String tag, String text, LogLevel level)
	{
		LogDate = logDate;
		Tag = tag;
		Text = text;
		Level = level;
	}

	public LogEntry(Date logDate, String tag, String text, LogLevel level) {
		super();
		LogDate = logDate;
		Tag = tag;
		Text = text;
		Level = level;
	}

	public Date getLogDate() {
		return LogDate;
	}

	public String getTag() {
		return Tag;
	}

	public String getText() {
		return Text;
	}

	public LogLevel getLevel() {
		return Level;
	}
}
