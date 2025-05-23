## SnapBite - Distributed Food Ordering System with Microservices

### Live Demo

[http://snapbite.online/](http://snapbite.online)

------

###  Project Overview

SnapBite is a cloud-based online food ordering system designed to streamline the ordering experience for both users and restaurant owners. The platform enables core functionalities such as restaurant browsing, dish selection, shopping cart management, order placement, and merchant-side order fulfillment.

The **SnapBite** system is a distributed food ordering platform built using a microservices architecture. It features a React + TypeScript frontend served by Nginx, multiple Spring Boot backend services secured with Spring Security and JWT authentication, exposed via Spring Cloud Gateway, and discovered via Eureka. Services communicate via REST and RabbitMQ, and persist data using PostgreSQL (structured) and MongoDB (unstructured). All services are containerized with Docker, deployed using Docker Compose on a Contabo VPS, and continuously delivered via a Jenkins-powered CI/CD pipeline.

------

### Tech Stack

- **Frontend**: React, TypeScript, Tailwind CSS, Zustand, React Router, Axios  
- **Backend**: Spring Boot, Spring Security, JWT (Json Web Token), Spring Cloud (Eureka, Gateway, Feign), RabbitMQ, Swagger (OpenAPI Docs)
- **Databases**: PostgreSQL (structured), MongoDB (unstructured)  
- **Deployment**: Docker, Docker Compose, Nginx  
- **CI/CD & DevOps**: Jenkins
- **Infrastructure & Domain**: Contabo VPS (Cloud VPS 10 SSD), Namecheap (custom domain)

------

###  System Architecture

![image-20250516161318820](docs/images/image-20250516161318820.png)

The **SnapBite** system is a distributed food ordering platform built using a microservices architecture. It features a React + TypeScript frontend served by Nginx, multiple Spring Boot backend services secured with Spring Security and JWT authentication, exposed via Spring Cloud Gateway, and discovered via Eureka. Services communicate via REST and RabbitMQ, and persist data using PostgreSQL (structured) and MongoDB (unstructured). All services are containerized with Docker, deployed using Docker Compose on a Contabo VPS, and continuously delivered via a Jenkins-powered CI/CD pipeline.

#### Frontend

- Built with **React + TypeScript** using Tailwind CSS and Zustand
- Served as static assets by **Nginx**
- Communicates with backend services via REST APIs

#### API Gateway & Service Discovery

- **Spring Cloud Gateway** routes incoming requests to appropriate backend services
- **Eureka Server** provides service registration and discovery for dynamic routing
- All backend services register themselves with Eureka for load-balanced API calls

#### Backend Microservices

- Each service is independently developed and deployed using Spring Boot, and communicates with others via Spring Cloud OpenFeign for declarative REST calls across containers. 

- All backend services expose interactive API documentation via **Swagger UI**, which assists in endpoint testing and system debugging.

  | Service                | Responsibility                                          |
  | ---------------------- | ------------------------------------------------------- |
  | `user-service`         | Handles user registration, login, JWT auth              |
  | `restaurant-service`   | Manages restaurant details and menus                    |
  | `cart-service`         | Groups cart items by restaurant and manages updates     |
  | `order-service`        | Creates orders, tracks status changes                   |
  | `notification-service` | Sends email alerts to merchants when an order is placed |

#### Asynchronous Messaging (RabbitMQ)

- **`order-service`** publishes messages when new orders are placed
- **`notification-service`** consumes these messages and sends email alerts to merchants

#### Data Persistence

- **PostgreSQL** is used to store structured data (users, orders, restaurants)
- **MongoDB** is used to store unstructured data (menus, carts)

#### Deployment & Infrastructure

- All services are containerized using **Docker**
- Orchestrated using **Docker Compose**
- Deployed on a **Contabo VPS** running Ubuntu 24.04.2

#### CI/CD Workflow

- CI/CD is powered by **Jenkins** and **Docker**, with modular pipelines for each microservice
- Updated services are automatically built and pushed on GitHub commits
- A centralized deployment job updates running containers on the production server

------

### Key Features

#### ● Browse and Search Restaurants

Users can view a list of available restaurants. The homepage supports filtering by cuisine type, price level, and dining time, along with sorting options for better user experience.

![edited_image-20250513211549242](docs/images/edited/edited_image-20250513211549242.png)



#### ● View Restaurant Details and Menu

Clicking on a restaurant navigates users to a detailed view showing key information and a list of available dishes, which are retrieved from MongoDB.

![edited_image-20250513211853864](docs/images/edited/edited_image-20250513211853864.png)



#### ●  Add Items to Cart

Users can add food items to a cart grouped by restaurant. Logged-in users have their cart data saved in MongoDB; guests use localStorage. The cart sidebar can be toggled and includes options to update item quantities or remove items. The cart sidebar can be shown or hidden. Within it, users can change item quantities or remove items from the cart.

![edited_image-20250513211938792](docs/images/edited/edited_image-20250513211938792.png)



#### ●  Seamless Checkout Experience

Clicking "Checkout" redirects users to a confirmation page where they can input recipient name, phone number, and delivery address. After submitting, an order is created and stored in PostgreSQL.

![edited_image-20250513212330745](docs/images/edited/edited_image-20250513212330745.png)



#### ● Order Management for Users

Users can view their orders, which include order status, items, and total price. They can cancel an order (if still pending) or confirm receipt after delivery.

![edited_image-20250513212400955](docs/images/edited/edited_image-20250513212400955.png)



#### ● Merchant Portal

Restaurant owners can log in to a dedicated merchant portal to manage orders—viewing customer and order details, accepting or canceling orders, and marking them as shipped.

![edited_image-20250513212446166](docs/images/edited/edited_image-20250513212446166.png)



#### ● Real-Time Notification with RabbitMQ

When a user places an order, the system sends a message via RabbitMQ to the Notification Service, which then triggers an email alert to the merchant (currently sent to the developer’s test inbox).

![edited_image-20250513213633342](docs/images/edited/edited_image-20250513213633342.png)



#### ● API Gateway and Service Discovery

Uses Spring Cloud Gateway as the API gateway, and Eureka Server for service registration and discovery, ensuring decoupled microservice communication.

![image-20250513213344167](docs/images/image-20250513213344167.png)



#### ●  Swagger UI Integration

All microservices provide auto-generated API documentation via Swagger UI, which helps in testing and debugging endpoints.al experience across all devices, including desktop, tablet (both orientations), and mobile.

![edited_image-20250513212650841](docs/images/edited/edited_image-20250513212650841.png)

------

### License

This project is licensed under the [GNU Affero General Public License v3.0](https://www.gnu.org/licenses/agpl-3.0.html).
