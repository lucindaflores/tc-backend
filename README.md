# 🟦 Talavera & Cobalto — Backend

Spring Boot REST API for a Mexican cookware, tableware, and decor store.

---

## ℹ️ About the project

Talavera & Cobalto is a full-stack portfolio project developed as part of my **Java Full Stack training at VDAB**.

The goal of the project was to build an e-commerce application from database design to deployment, while applying concepts such as REST API design, layered architecture, relational data modelling, JPA/Hibernate relationships, validation, optimistic locking, and integration testing.

This repository contains the **Spring Boot backend API**. The Angular frontend is maintained in a separate repository.

Version **1.0** supports the following customer flow:

<img width="3833" height="233" alt="image" src="https://github.com/user-attachments/assets/9b9d2c82-3f0d-4d2f-8936-2b1176cd432e" />


---

## 📊 Status
Implemented
- PostgreSQL relational database with primary and foreign keys, constraints, indexes, and sequences
  
- Product catalogue with categories, origins, materials, pricing, stock, and availability
- Customer and address management
- Order creation with order details and delivery information
- Order status management

- JPA/Hibernate entity relationships
- Optimistic locking for concurrent updates
- Layered backend structure using repositories, services, DTOs, and REST controllers

- Integration tests for repository and API behavior
- Backend and PostgreSQL database deployed on Render
  
Planned
- Complete checkout payment 
- RabbitMQ order event and confirmation email


---

## 💻 Tech stack

<table>
<tr>
  <th>Area</th>
  <th>Technology</th>
</tr>
<tr>
  <td>Language</td>
  <td>Java 25</td>
</tr>
<tr>
  <td>Framework</td>
  <td>Spring Boot, Spring Web</td>
</tr>
<tr>
  <td>Persistence</td>
  <td>Spring Data JPA, Hibernate</td>
</tr>
<tr>
  <td>Database</td>
  <td>PostgreSQL</td>
</tr>
<tr>
  <td>Testing</td>
  <td>JUnit, Assert, MockMvcTestr</td>
</tr>
<tr>
  <td>Build</td>
  <td>Maven</td>
</tr>
<tr>
  <td>Deployment</td>
  <td>Render</td>
</tr>
</table>

---

## 🗄️ Data model

<img width="952" height="743" alt="Untitled diagram_2026-09-24T13_10_35 129Z" src="https://github.com/user-attachments/assets/30373fa7-0076-4b01-bb25-75208d8acbc3" />


| Table | Purpose |
| --- | --- |
| `categories` | Catalogue categories such as Cookware, Tableware, and Decor |
| `origins` | Mexican regions associated with products |
| `materials` | Materials and artisan techniques |
| `products` | Product information, price, image, stock, and availability |
| `product_materials` | Many-to-many link between products and materials |
| `users` | Customer and administrator information |
| `addresses` | Customer delivery addresses |
| `orders` | Order state and delivery snapshot |
| `order_details` | Product name, quantity, and price captured at purchase time |

Primary keys use PostgreSQL sequences mapped through JPA `@SequenceGenerator`.
The domain model includes one-to-many, many-to-one, and many-to-many relationships. Product and order entities use optimistic locking to protect updates from concurrent modifications.

---

## 🏗️ Backend architecture

The application follows a layered structure:

Controller → Service → Repository → PostgreSQL

- **Controllers** expose REST endpoints and handle HTTP requests/responses.
- **Services** contain application and business logic.
- **Repositories** provide database access through Spring Data JPA.
- **DTOs** define API request and response data independently from persistence entities.
- **Entities** represent the relational domain model managed by JPA/Hibernate.

--- 

## 🔌 API overview

The REST API is organised around the main e-commerce resources:

| Resource | Responsibility |
| --- | --- |
| `/products` | Product catalogue, product details, stock, and availability |
| `/categories` | Product categories |
| `/materials` | Product materials and artisan techniques |
| `/origins` | Product origins |
| `/users` | Customer information |
| `/addresses` | Customer delivery addresses |
| `/orders` | Order creation and order status management |

Full API documentation with OpenAPI/Swagger is planned.

---

## 🧪 Testing

The backend includes integration tests covering repository behaviour, persistence relationships, validation, and REST API responses.


---

## 👩‍💻 Author

**Lucinda Flores**  
- GitHub: https://github.com/lucindaflores
    



