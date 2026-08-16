# Full-Stack Football Dashboard

A dynamic full-stack web application for exploring football squad rosters, team configurations, and league standings across top European leagues and historical seasons. Powered by **Spring Boot**, **React**, **Nginx**, and **API-Sports**.

---

## Key Features

* **Dynamic League & Season Filtering:** Seamlessly toggle between major leagues (Premier League, La Liga, Serie A, Bundesliga, Ligue 1, Championship) and seasons (2019–2023).
* **Deep Linking & URL Sync:** Query parameters (`?league=140&season=2022`) persist across pages, allowing direct navigation and shareable views.
* **Split-Column Standings Table:** Streamlined 2-column league tables designed for quick scanning without excessive scrolling.
* **Squad & Position Filtering:** Interactive player directory with position-based filtering (Goalkeepers, Defenders, Midfielders, Attackers).
* **Reverse Proxy Architecture:** Nginx routes API requests cleanly while avoiding CORS friction during development and production.

---

## Architecture Overview

```text
               ┌─────────────────────────────────────────────────┐
               │                  Nginx (Port 80)                │
               └───────────────┬─────────────────┬───────────────┘
                               │                 │
                      /api/football/*            /*
                               │                 │
                               ▼                 ▼
                     ┌──────────────────┐  ┌───────────┐
                     │ Spring Boot API  │  │ React SPA │
                     │   (Port 8080)    │  │ (Port 3000│
                     └────────┬─────────┘  └───────────┘
                              │
                      External API Calls
                              │
                              ▼
                     ┌──────────────────┐
                     │    API-Sports    │
                     └──────────────────┘


Tech Stack
Frontend: React, React Router v6, Axios, CSS3 (Flexbox/Grid) — View Frontend Docs

Backend: Java, Spring Boot, WebClient/RestTemplate — View Backend Docs

Infrastructure: Docker, Docker Compose, Nginx Reverse Proxy

Data Source: API-Sports (v3.football.api-sports.io)

Getting Started

Prereqs
Docker Desktop installed and running

An API-Sports API Key


Configuration
Clone the repository:
git clone (https://github.com/YOUR_USERNAME/football-dashboard.git)

cd football-dashboard
Add your API-Sports key to your Spring Boot configuration (backend/src/main/resources/application.properties or via environment variable):

Properties
api.sports.key=YOUR_API_KEY

Running with Docker Compose
Spin up the entire stack (Frontend, Backend, and Nginx) with a single command:

docker-compose up --build

Once running, access the dashboard at:
http://localhost:3000

API Proxy Endpoint: http://localhost/api/football/standings?league=39&season=2023

To stop the services:
docker-compose down

Repository Structure

football-dashboard/
├── docker-compose.yml       # Orchestrates Nginx, Backend, and Frontend containers
├── README.md                # Root project documentation
├── nginx/
│   └── default.conf         # Reverse proxy routing rules
├── backend/                 # Spring Boot REST API
│   ├── Dockerfile
│   └── README.md
└── frontend/                # React SPA
    ├── Dockerfile
    └── README.md