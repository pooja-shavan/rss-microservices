# rss-microservices
Microservices to display RSS news feed from New York Times

## Overview
This project is a microservices-based system that fetches, processes, and displays RSS news feeds from the New York Times. It consists of several services, each responsible for a specific part of the workflow.

## Features
- Fetches RSS feeds from the New York Times
- Processes and stores news articles
- Provides REST APIs for news data
- Web UI for displaying news articles
- Dockerized for easy deployment

## Prerequisites
- Java 11 or higher
- Node.js (for UI)
- Docker & Docker Compose
- Gradle

## Project Structure
- `news-api/`: Spring Boot service exposing REST APIs for news
- `rss-producer/`: Service fetching RSS feeds and producing messages
- `news-consumer/`: Service consuming messages and storing articles
- `news-ui/`: React-based frontend for displaying news
- `common-models/`: Shared models between services
- `nginx/`: Nginx config for reverse proxy

## Setup Instructions
1. **Clone the repository**
2. **Build backend services:**
   ```sh
   ./gradlew build
   ```
3. **Build frontend:**
   ```sh
   cd news-ui
   npm install
   npm run build
   cd ..
   ```
4. **Run with Docker Compose:**
   ```sh
   docker-compose up --build
   ```
5. **Access the UI:**
   Open [http://localhost](http://localhost) in your browser.

## Usage
- The UI displays the latest news articles.
- Backend APIs are available at `/api/news` (see `news-api` for details).

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License
[MIT](LICENSE)
