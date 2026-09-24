# 🟦 Talavera & Cobalto — Backend

Spring Boot REST API for a Mexican cookware, tableware, and decor store.

---

## ℹ️ About the project

Talavera & Cobalto is a full-stack e-commerce portfolio project developed during my Java Full Stack training at VDAB. 
This repository contains the backend API; the Angular client is maintained separately. 

The current version 1.0 covers the user flow below:

<img width="3833" height="233" alt="image" src="https://github.com/user-attachments/assets/9b9d2c82-3f0d-4d2f-8936-2b1176cd432e" />


---

## 📊 Status
Implemented
- PostgreSQL database designed and created with keys, constraints, indexes, and sequences
- Reusable catalogue data
- Entities, repositories, services, response DTOs, and REST controllers created for:
    * Categories
    * Origins
    * Meterials
    * Products
    * Product Details
    * Users
- Integration tests included
- Render deployment
  
Planned
- RabbitMQ order event and confirmation email
- OpenAPI documentation


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
  <td>JUnit</td>
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

Primary keys use PostgreSQL sequences mapped with JPA @SequenceGenerator. 
Product and order versions support optimistic locking.


## 👩‍💻 Author

**Lucinda Flores**  
- GitHub: https://github.com/lucindaflores
    



