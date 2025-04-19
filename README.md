# xPOS 3.0

**xPOS 3.0** is a desktop-based Point-of-Sale (POS) application developed using Java Swing and MySQL. It is designed to help small-to-medium businesses manage their sales, inventory, customers, and suppliers efficiently. The system includes robust reporting features powered by JasperReports and provides a single-admin secured access model.

## Features

- **Single Admin Login**: Secure login for a single administrator user.
- **Inventory Management**: Add, edit, and delete products with real-time stock tracking and category organization.
- **Sales Processing**: Includes a full sales cart system with product search, real-time price calculation, and invoice generation via JasperReports.
- **Customer and Supplier Management**: Register and manage customer and supplier data, view histories, and maintain contact records.
- **Purchase Management**: Manage stock entries linked to suppliers and track incoming inventory.
- **Reports**: Generate printable reports including sales, stock, and profit/loss analysis using JasperReports.
- **Utilities**: Backup and restore database, logout functionality, and password change.

## Technology Stack

- Java (Swing GUI)
- MySQL (Database)
- JasperReports (Reporting)
- Apache Ant (Build system)
- NetBeans IDE (Project development)

## Setup Instructions

1. **Clone the repository**:
   ```
   git clone https://github.com/sheronfdo/xpos-3.0.git
   ```

2. **Configure MySQL database**:
   - Create a new database (e.g., `xposdb`)
   - Import the SQL schema provided in the `/db` folder

3. **Update database connection**:
   - Edit the database credentials in the source (typically in a class like `DBConnection.java`)

4. **Run the application**:
   - Open the project with NetBeans
   - Build and run the project using the IDE or Ant (`build.xml`)

## Project Structure

- `src/com/xpos/`: Source code
- `lib/`: External libraries (e.g., JasperReports, JDBC drivers)
- `db/`: SQL scripts for database setup
- `build.xml`: Apache Ant build file
- `nbproject/`: NetBeans configuration

## License

This project is open-source under the MIT License.

## Developer

Created by **Jamith Sheron Fernando**  
GitHub: [@sheronfdo](https://github.com/sheronfdo)
