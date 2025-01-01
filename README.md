# Vehicle Allocation Platform 🚗
A Java-based application for managing the allocation of company vehicles for various service tasks. The system utilizes core design principles and integrates multiple design patterns to ensure scalability, flexibility, and maintainability. Data is stored and managed using **JDBC** for seamless interaction with a relational database.

## Features
- Manage a fleet of vehicles (`Car` and `Truck`) with specific characteristics.
- Dynamically allocate vehicles based on predefined strategies.
- Monitor vehicle availability in real time.
- Persist and manage data in a relational database using **JDBC**.

## Design Patterns Used
### 1. Abstract Factory
- **Purpose**: Create families of vehicles (`Car` and `Truck`) without specifying their concrete model.
- **Example**: A `CarFactory` or `TruckFactory` generates vehicles depending on the task type (passenger transport or cargo).
### 2. Observer
- **Purpose**: Monitor the status of vehicles and notify dependent components when their state changes.
- **Example**: When a vehicle becomes available, the system is notified to allocate it for new tasks.
### 3. Strategy
- **Purpose**: Provide multiple algorithms for allocating vehicles dynamically based on task requirements or company policies.
- **Example Strategies**:
  - Allocate the **closest available vehicle**.
  - Choose the vehicle with the **best fuel efficiency**.
  - Prioritize trucks for **cargo tasks** and cars for **passenger transport**.

## Technical Stack
- **Programming Language**: Java
- **Database**: Relational database with **JDBC** for persistence.
- **Design Patterns**: Abstract Factory, Observer, Strategy.
- **Architecture**: Modular design with well-structured packages for easy maintenance and extensibility.

## How It Works
1. **Vehicle Management**:  
   Vehicles are created using the **Abstract Factory** pattern, enabling the addition of new vehicle types (e.g., electric vehicles) without modifying existing code.
2. **Real-time Updates**:  
   The **Observer** pattern ensures the system is automatically updated when vehicle availability changes (e.g., when a vehicle becomes free or enters maintenance).
3. **Dynamic Allocation**:  
   The **Strategy** pattern is used to allocate vehicles based on specific criteria such as:
   - **Proximity** to the task location.
   - **Fuel efficiency** for cost-effective transport.
   - Task type (e.g., cargo vs. passenger).
4. **Database Integration**:  
   Data persistence is handled with **JDBC**, ensuring efficient storage and retrieval of vehicle and task information.
