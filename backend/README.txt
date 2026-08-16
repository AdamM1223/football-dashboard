# ⚽ Football Dashboard API

A full-stack football data platform powered by Spring Boot, designed to deliver live fixtures, team information, league standings, and squad lists. This project demonstrates clean API design, Swagger documentation, CI/CD integration, and future monitoring capabilities using Grafana.

---

## 🛠️ Tech Stack

- **Backend**: Spring Boot, RestTemplate, Swagger (springdoc-openapi), Lombok
- **Frontend**: React *(planned)*
- **CI/CD**: Jenkins & AWS EC2 *(planned)*
- **Monitoring**: Grafana + Prometheus *(planned)*

---

## 🎯 Project Goals

- Build a clean, documented REST API using Spring Boot
- Serve real-time football data via [API-Football](https://www.api-football.com/)
- Create a frontend dashboard to visualize data
- Automate builds and deployments with Jenkins
- Monitor performance and usage with Grafana

---

## ✅ Current API Endpoints

All endpoints are documented via Swagger and include error handling with custom exceptions.

| Endpoint       | Description                                 |
|----------------|---------------------------------------------|
| `/fixtures`    | Returns all currently live football matches |
| `/teams`       | Returns Premier League team information     |
| `/standings`   | Returns league standings by ID and season   |
| `/squad`       | Returns squad list for a team (defaults to Chelsea) |

Swagger UI available at:  
`http://localhost:8080/swagger-ui/index.html`

---

## 🚀 Getting Started

1. Clone the repository  
2. Add your API-Football key to `application.properties`:
3. Run the Spring Boot application  
4. Access Swagger UI to explore endpoints

---

## 🔮 Planned Features

- `/topscorers` – Player stats per league
- `/fixtures/{id}` – Match details (lineups, events)
- Redis caching for performance
- Jenkins pipeline for CI/CD
- Grafana dashboards for monitoring

---

## 🧪 Testing

Basic unit and integration tests will be added to validate service logic and endpoint behavior.

---

## 📄 License

MIT License

---

## 🙏 Credits

Football data provided by [API-Football](https://www.api-football.com/)

