# CaRMS

IS2103 Pair Project
Car Rental Management System

Run on netbeans + glassfish + mysql db: carms

IS2103 Enterprise Systems Server-side Design and Development Pair Project Write Up

Group: 38
Merlion Car Rental (MCR) Car Rental Management System (CaRMS)

Ryan Lim Chin Wee (A0237868J)

Wang Poh San (A0216501W)

 
1.	 Introduction
Merlion Car Rental (MCR) is a new car rental company with planned outlets strategically located at different parts of Singapore. The company will commence business operation in January 2023 and is currently preparing for its launch. As part of the preparation, MCR has engaged Kent Ridge Technology (KRT), a global technology consulting and solution development company that is headquartered in Singapore, to develop a new Car Rental Management System (CaRMS) to support its core business processes.

2.	 System Components
CaRMS will include three components in the entire system, namely:
1.	CaRMS Management System
2.	CaRMS Reservation System
3.	Holiday Reservation System

These three systems will work together to allow customers of MCR/Partners to rent cars that are available in the MCR inventory and for internal employees of MCR to manage records related to car rentals.

The CaRMS Management System will handle the majority of the internal business logic needed by MCR, it will include Employee Login/Logout, Create, Read, Update, Delete (CRUD) of RentalRate, MakeModel, Car and will allow management of car allocations and dispatches between the various outlets.

The CaRMS Reservation System is the primary link between customers and MCR. Customers will be allowed to register for accounts and are able to rent cars as well as manage their rental records in this system.

The Holiday Reservation System is the link between Partners and MCR. It will handle all rental reservations made by partners through the Holiday.com website. Partners will be allowed to create rental reservations for their customers via this system. When booking a reservation, they then enter the customer details and their respective customer will be recorded in MCR’s database (with a default password of the partner’s). 


Key justifications for entities & (non-obvious) relationships

Car: Car has relationships to both Outlet and Customer since we need a way to track the current car location, and the location may be at an outlet or with a customer.
We did not include a CarCategory relationship as it is indirectly accessible through its MakeModel relationship.

MakeModel: We combined make & model since there was no need to search or filter by make or model individually.

CarCategory & RentalRate: We made the relationship bidirectional for convenience of access to each entity since these are referenced quite often. 
However, we recognise that unidirectional relationships would also work equally well given helper methods to help find and filter said entities.

Reservation: We included an optional relationship to Partner since partners would need to access their own reservations made through their own portal.
