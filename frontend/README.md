# React + Vite

This template provides a minimal setup to get React working in Vite with HMR and some ESLint rules.

Currently, two official plugins are available:

- [@vitejs/plugin-react](https://github.com/vitejs/vite-plugin-react/blob/main/packages/plugin-react) uses [Babel](https://babeljs.io/) for Fast Refresh
- [@vitejs/plugin-react-swc](https://github.com/vitejs/vite-plugin-react/blob/main/packages/plugin-react-swc) uses [SWC](https://swc.rs/) for Fast Refresh

## Expanding the ESLint configuration

If you are developing a production application, we recommend using TypeScript with type-aware lint rules enabled. Check out the [TS template](https://github.com/vitejs/vite/tree/main/packages/create-vite/template-react-ts) for information on how to integrate TypeScript and [`typescript-eslint`](https://typescript-eslint.io) in your project.

1. Project Title and Overview
Title: Full-Stack Sports Data Visualization and Monitoring Platform

Description: A complete, end-to-end application built to consume and visualize external sports data, featuring a modern React frontend, a scalable Java (Spring Boot) API, and a robust DevOps pipeline architected for cloud deployment.

2. Technology Stack
Frontend: React, JavaScript (ES6+), CSS Grid/Flexbox

Backend/API: Java, Spring Boot, RESTful API Design

Containerization: Docker

Future CI/CD/Cloud: Jenkins, AWS (EC2), Grafana Synthetics (for monitoring)

3. Current Status & Key Features (Project Progress)
Core Functionality: Implemented data views for Standings, Fixtures, and Players, including features like client-side pagination and intelligent data grouping (e.g., separating players by position).

Architecture: Successfully containerized both the client and server using Docker.

Upcoming Focus: Implementation of the CI/CD pipeline using Jenkins for automated testing and deployment to AWS.

4. Setup and Run Instructions (Crucial)
This section tells a reviewer exactly how to get your project running locally.

Prerequisites: List necessary software (e.g., Docker, Java JDK, Node.js).

Clone Repository: git clone [your-repo-link]

Run with Docker (Recommended): Provide commands to build and run the complete system using your Docker setup (e.g., a docker-compose up command).

Local Run (Alternative): Provide separate steps to run the Java backend and the React frontend manually.