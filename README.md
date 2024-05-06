# PharmaTrac

PharmaTrac is a comprehensive delivery requisition management system designed to streamline the process within hospital pharmacy settings. It replaces the traditional binder-based method of tracking delivery history with a digital solution. PharmaTrac aims to enhance efficiency and accuracy in managing delivery requisitions.

## Purpose

The purpose of PharmaTrac is to replace the existing manual process used in hospital pharmacies for tracking the delivery history of narcotics. Traditionally, this process involves maintaining a binder to record and manage delivery requisitions. However, PharmaTrac offers a modern digital alternative that simplifies the creation, management, and tracking of delivery requisitions.

## Key Features

**User Authentication:** Users with valid employee credentials can access the application and utilize its features. When creating user accounts, passwords are securely hashed using the SHA-256 hash algorithm for enhanced security.

**Delivery Requisitions:** Users can create, edit, sign off, deliver orders, and return orders to the pending queue.

**Hierarchy:** The application distinguishes between Admin and Staff roles, with Admin having additional privileges such as managing user accounts and deleting delivery requisitions.

**Order Management:** Orders are categorized as pending or completed, providing a clear overview of the delivery status.

**Search Functionality:** Users can search for orders based on various criteria, facilitating efficient order management.

**Real-time Updates:** Changes made to the order queues are instantly reflected across all application instances.

**Additional Features:** PharmaTrac offers additional features such as light and dark mode, language switching (utilizing LibreTranslate API: https://github.com/dynomake/libretranslate-java.git), and integration with Algolia Search API (https://www.algolia.com/developers/search-api/) for enhanced search capabilities.

## Technologies Used
**PharmaTrac is built using the following technologies:**
- IntelliJ: Integrated Development Environment (IDE) for Java development.
- SceneBuilder: Drag-and-drop GUI design tool for JavaFX applications.
- Google Firebase: Backend infrastructure for authentication, database management, and real-time updates.

## How to Start
**To get started with PharmaTrac, follow these steps:**
- Clone the repository.
- Set up Firebase authentication and database.
- Configure Algolia Search API.
- Run the application using IntelliJ or your preferred IDE.
