# Discord Reminder Bot

A Discord bot for electronic time tracking, built with **Java 21**, **Spring Boot** and **JDA**.

The bot allows users to register workday events directly from Discord and communicates with the [Electronic Time Marking](https://github.com/lucasbdourado/electronic-time-marking) service through RabbitMQ.

## Architecture

```text
Discord
   ↓
Discord Reminder Bot
   ↓
RabbitMQ
   ↓
Electronic Time Marking
   ↓
MySQL
```

The bot handles Discord interactions while the Electronic Time Marking service is responsible for processing and storing workday data.

RabbitMQ is used for asynchronous communication between both applications.

## Tech Stack

* Java 21
* Spring Boot
* JDA
* RabbitMQ
* Maven

## Commands

### Clock in

```text
in
```

Remote work:

```text
in h
in home
in (home)
```

### Clock out

```text
out
```

After processing the command, the bot receives the updated workday information from the Electronic Time Marking service.

## Requirements

* Java 21
* RabbitMQ
* Discord Bot Token
* Electronic Time Marking service running

## How to Run

### 1. Start Electronic Time Marking

Clone the backend service:

```bash
git clone https://github.com/lucasbdourado/electronic-time-marking.git
cd electronic-time-marking
```

Start MySQL and RabbitMQ:

```bash
docker compose up -d
```

Start the application:

```bash
./mvnw spring-boot:run
```

### 2. Configure the Discord Bot

Clone this repository:

```bash
git clone https://github.com/lucasbdourado/discord-reminder-bot.git
cd discord-reminder-bot
```

Set your Discord bot token:

```bash
export DISCORD_TOKEN=your_discord_bot_token
```

On Windows PowerShell:

```powershell
$env:DISCORD_TOKEN="your_discord_bot_token"
```

### 3. Start the Bot

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

## Related Project

[Electronic Time Marking](https://github.com/lucasbdourado/electronic-time-marking) — service responsible for processing time records, calculating workdays, scheduling reminders and storing data.
