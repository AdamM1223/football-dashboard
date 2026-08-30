# Full-Stack Football Dashboard

A dynamic full-stack web application for exploring football squad rosters, team configurations, and league standings across top European leagues and historical seasons. Powered by **Spring Boot**, **React**, **Kong API Gateway** **Nginx**, and **API-Sports**.

---

## Key Features

* **Dynamic League & Season Filtering:** Seamlessly toggle between major leagues (Premier League, La Liga, Serie A, Bundesliga, Ligue 1, Championship) and seasons (2019–2023).
* **API Gateway & Edge Infrastructure:** Decoupled traffic management powered by Kong Gateway in DB-less mode, enforcing rate limiting and caching through Kong Plugins.
* **In-Memory Edge Caching:** Kong proxies requests and caches frequent API responses (X-Cache-Status: Hit), reducing backend JVM execution and protecting external API quotas.
* **Traffic Control & Rate Limiting:** Enforces a hard limit of 15 requests/minute per client IP directly at the gateway layer, returning 429 Too Many Requests on overflow.
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
               │                 React SPA (Port 3000)           │
               └────────────────────────┬────────────────────────┘
                                        │
                                API Calls (Port 8000)
                                        │
                                        ▼
               ┌─────────────────────────────────────────────────┐
               │             Kong API Gateway (Port 8000)        │
               │  - Rate Limiting (15 req/min)                   │
               │  - In-Memory Response Cache (proxy-cache)       │
               └────────────────────────┬────────────────────────┘
                                        │
                             Internal Docker Network
                                        │
                                        ▼
                     ┌──────────────────────────────────┐
                     │         Spring Boot API          │
                     │           (Port 8080)            │
                     └──────────────────┬───────────────┘
                                        │
                                External API Calls
                                        │
                                        ▼
                     ┌──────────────────────────────────┐
                     │            API-Sports            │
                     └──────────────────────────────────┘


Tech Stack
Frontend: React, React Router v6, Axios, CSS3 (Flexbox/Grid) — View Frontend Docs

Backend: Java, Spring Boot, Spring Actuator, Micrometer Prometheus

API Gateway: Kong Gateway (DB-less mode, proxy-cache, rate-limiting)

Observability: Prometheus, Grafana (Synthetic Monitoring & Dashboards)

Infrastructure & CI/CD: Docker, Docker Compose, Nginx, Jenkins, AWS ECR

Data Source: API-Sports (v3.football.api-sports.io)


Getting Started

Prereqs

Docker Desktop installed and running
An API-Sports API Key


Configuration
Clone the repository:
git clone LINK_TO_GIT_REPO

cd football-dashboard
Add your API-Sports key to your Spring Boot configuration (backend/src/main/resources/application.properties or via environment variable):

Properties
api.sports.key=YOUR_API_KEY

Running with Docker Compose
Spin up the entire stack (Frontend, Backend, and Nginx) with a single command:

docker-compose up --build

Once running, access

Dashboard UI: http://localhost:3000
Kong API Gateway Endpoint: http://localhost:8000/api/football/teams

To stop the services:
docker-compose down

---

## API Gateway Testing & Verification

Inspect response headers on any endpoint proxied through Kong

Powershell: curl.exe -i http://localhost:8000/api/football/teams
Linux/Mac: curl -i http://localhost:8000/api/football/teams

Look for active headers:
HTTP/1.1 200 OK
Via: kong/3.4.0
X-Cache-Status: Hit
X-RateLimit-Limit-Minute: 15
X-RateLimit-Remaining-Minute: 14

## CI/CD Pipeline (Jenkins & AWS ECR)

The repository includes a declarative `Jenkinsfile` configured at the root to automate container builds and push immutable Docker images to **AWS Elastic Container Registry (ECR)**.

```text
[ Git Push ] ➔ [ Jenkins Pipeline ] ➔ [ Local Build (Backend & Frontend) ] ➔ [ AWS ECR ]
```         
                                   
## Observability      

| Service | Port | Description |
| :--- | :--- | :--- |
| **Grafana** | `3001` | Analytics UI & Dashboard Visualization |
| **Kong Gateway** | `8000` | API Gateway proxying requests, caching & rate limiting |
| **Prometheus** | `9090` | Time-series metrics collection database |
| **React SPA** | `3000` | Frontend Interface |
| **Spring Actuator** | `8080` | Exposes `/actuator/prometheus` metrics endpoint |



Spring Boot API: Spring Boot’s micrometer-registry-prometheus dependency exposes an /actuator/prometheus endpoint with JVM health, HTTP request latencies, and system metrics.

Prometheus: Pulls (scrapes) those metrics at regular intervals and stores time-series data.

Grafana: Queries Prometheus to render real-time graphs for CPU usage, heap memory, response times, and uptime (Synthetics).
```text                                  
                               ┌──────────────────┐
                               │   Grafana UI     │
                               │   (Port 3001)    │
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
│    React SPA     │ ────────> │  Kong Gateway    │ ────────> │ Spring Boot API  │
│   (Port 3000)    │           │   (Port 8000)    │           │   (Port 8080)    │
└──────────────────┘           └──────────────────┘           └──────────────────┘
```
```text
Repository Structure

football-dashboard/
├── docker-compose.yml       # Multi-container orchestration
├── Jenkinsfile              # Declarative CI/CD pipeline definition
├── README.md
├── backend/                 # Spring Boot REST API
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── frontend/                # React SPA
│   ├── src/
│   ├── Dockerfile
│   └── package.json
└── kong/
    └── kong.yml             # Kong Gateway config (DB-less mode)
```

Future Enhancements & Prod Roadmap

While this project has been developed as a local cluster as part of learning and implementing an existing API onto a full-stack web app, the path to a production pipeline would involve the following steps:
* **Automated Production Deployment:** Extending the CI/CD lifecycle beyond the ECR push by triggering an automated webhook or SSH deployment script to an AWS EC2 instance, executing a rolling `docker compose pull && docker compose up -d` update.
* **Zero-Downtime Orchestration:** Migrating container execution from local Docker to a fully managed cloud runtime such as **AWS Fargate** or an **Amazon EKS** cluster, utilising rolling updates and load balancers to swap image tags live.
* **Automated Quality Gates:** Expanding the declarative `Jenkinsfile` to execute automated backend unit/integration tests (JUnit) and frontend testing suites as blocking gates *before* initiating the Docker build and ECR push stages.