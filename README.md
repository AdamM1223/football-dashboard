# Full-Stack Football Dashboard

A dynamic full-stack web application for exploring football squad rosters, team configurations, and league standings across top European leagues and historical seasons. Powered by **Spring Boot**, **React**, **Nginx**, and **API-Sports**.

---

## Key Features

* **Dynamic League & Season Filtering:** Seamlessly toggle between major leagues (Premier League, La Liga, Serie A, Bundesliga, Ligue 1, Championship) and seasons (2019–2023).
* **Deep Linking & URL Sync:** Query parameters (`?league=140&season=2022`) persist across pages, allowing direct navigation and shareable views.
* **Split-Column Standings Table:** Streamlined 2-column league tables designed for quick scanning without excessive scrolling.
* **Squad & Position Filtering:** Interactive player directory with position-based filtering (Goalkeepers, Defenders, Midfielders, Attackers).
* **Reverse Proxy Architecture:** Nginx routes API requests cleanly while avoiding CORS friction during development and production.
* **Full CI/CD Pipeline:** Automated container builds via Jenkins pushing to AWS ECR.
* **Real-time Observability:** Application metric scraping with Prometheus and visual monitoring via Grafana dashboards.

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

Backend: Java, Spring Boot, Spring Actuator, Micrometer Prometheus

Observability: Prometheus, Grafana (Synthetic Monitoring & Dashboards)

Infrastructure & CI/CD: Docker, Docker Compose, Nginx, Jenkins, AWS ECR

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

---

## 🔄 CI/CD Pipeline (Jenkins & AWS ECR)

The repository includes a declarative `Jenkinsfile` configured at the root to automate container builds and push immutable Docker images to **AWS Elastic Container Registry (ECR)**.

```text
[ GitHub Push ] ──> [ Jenkins Pipeline ] ──> [ Docker Build ] ──> [ AWS ECR Repositories ]
                                                                       ├── backend:latest
                
                                   
## Observability      

| Service | Port | Description |
| :--- | :--- | :--- |
| **Grafana** | `3000` | Analytics UI & Dashboard Visualization |
| **Prometheus** | `9090` | Time-series metrics collection database |
| **Spring Actuator** | `8080` | Exposes `/actuator/prometheus` metrics endpoint |

Spring Boot API: Spring Boot’s micrometer-registry-prometheus dependency exposes an /actuator/prometheus endpoint with JVM health, HTTP request latencies, and system metrics.

Prometheus: Pulls (scrapes) those metrics at regular intervals and stores time-series data.

Grafana: Queries Prometheus to render real-time graphs for CPU usage, heap memory, response times, and uptime (Synthetics).
                                                           
                               ┌──────────────────┐
                               │   Grafana UI     │
                               │   (Port 3000)    │
                               └────────┬─────────┘
                                        │ Queries Metrics
                                        ▼
                               ┌──────────────────┐
                               │    Prometheus    │
                               │   (Port 9090)    │
                               └────────┬─────────┘
                                        │ Scrapes /actuator/prometheus
                                        ▼
┌──────────────────┐           ┌──────────────────┐           ┌──────────────────┐
│    React SPA     │ ────────> │  Nginx Proxy     │ ────────> │ Spring Boot API  │
│   (Port 80)      │           │   (Port 80)      │           │   (Port 8080)    │
└──────────────────┘           └──────────────────┘           └──────────────────┘└── frontend:latest

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