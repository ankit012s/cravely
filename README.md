# Online Food Ordering System

Full-stack college/project MVP using:
- Backend: Java 17 + Spring Boot 3 + Spring Data JPA + Spring Security + JWT
- Database: MySQL
- Frontend: React + Vite
- Payments: Razorpay-ready backend structure (test keys required)
- API style: REST

## Modules

1. Authentication & Authorization
   - Customer registration/login
   - Admin login
   - JWT based role protection

2. Restaurant Management
   - Admin can create/update restaurants
   - Customers can browse active restaurants

3. Menu Management
   - Admin can add/update menu items
   - Customers can view menu by restaurant

4. Order Placement
   - Cart/order creation
   - Order total calculation
   - Order status

5. Order Tracking
   - PLACED -> CONFIRMED -> PREPARING -> OUT_FOR_DELIVERY -> DELIVERED
   - Customer can view own orders

6. Payment Integration
   - Razorpay-ready endpoint
   - For college demo, payment can be simulated with COD/TEST mode
   - Add Razorpay credentials in application.properties for real test checkout

7. Admin Dashboard
   - Restaurant/menu/order management
   - Order status updates

## Run

### 1. MySQL
Create database:
CREATE DATABASE food_ordering_db;

Update backend/src/main/resources/application.properties.

### 2. Backend
cd backend
mvn spring-boot:run

Backend runs on http://localhost:8080

### 3. Frontend
cd frontend
npm install
npm run dev

Frontend runs on http://localhost:5173

## Demo admin
Register normally, then change the user's role to ADMIN in MySQL for demo:
UPDATE users SET role='ADMIN' WHERE email='admin@example.com';

## Important
This is a clean MVP starter intended for a college project. Before production:
- use environment variables for secrets
- add proper payment signature verification
- add image storage/cloud upload
- add validation and rate limiting
- add transaction handling and stock management
- add HTTPS
