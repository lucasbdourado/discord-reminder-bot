package br.com.reminderbot.application.workday.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class WorkDayResponse
{
	private LocalDate date;

	private List<LocalTime> times;

	private long workedMinutes;

	private long remainingMinutes;

	private LocalTime exitTime;

	private String status;

	private boolean invalid;

	public LocalDate getDate()
	{
		return date;
	}

	public void setDate(LocalDate date)
	{
		this.date = date;
	}

	public List<LocalTime> getTimes()
	{
		return times;
	}

	public void setTimes(List<LocalTime> times)
	{
		this.times = times;
	}

	public long getWorkedMinutes()
	{
		return workedMinutes;
	}

	public void setWorkedMinutes(long workedMinutes)
	{
		this.workedMinutes = workedMinutes;
	}

	public long getRemainingMinutes()
	{
		return remainingMinutes;
	}

	public void setRemainingMinutes(long remainingMinutes)
	{
		this.remainingMinutes = remainingMinutes;
	}

	public LocalTime getExitTime()
	{
		return exitTime;
	}

	public void setExitTime(LocalTime exitTime)
	{
		this.exitTime = exitTime;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public boolean isInvalid()
	{
		return invalid;
	}

	public void setInvalid(boolean invalid)
	{
		this.invalid = invalid;
	}
}
