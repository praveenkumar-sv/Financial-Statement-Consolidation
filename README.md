Financial Statements Consolidation System

Project Highlights

🔐 JWT-Based Authentication & Authorization using Spring Security

🏦 Multi-Company & Multi-Branch Aggregation of financial transactions

📄 Automated CSV Statement Generation for consolidated bank statements

☁️ AWS S3 Cloud Storage for secure, scalable statement access

🧱 Clean Layered Architecture (Controller → Service → Repository)

🗂️ Well-Designed JPA Entity Relationships for financial data modeling

🧪 Dummy Data Generator for realistic testing and development

📈 Analytics-Ready Design for future reporting and insights

⚙️ Production-Ready Backend with environment-based configuration

Project Architecture
src/main/java/com/example/aggregator
│
├── config
│   └── SecurityConfig.java
│
├── controllers
│   ├── UserController.java
│   ├── CompanyController.java
│   ├── BranchController.java
│   └── StatementController.java
│
├── models
│   ├── User.java
│   ├── Company.java
│   ├── Branch.java
│   ├── Transaction.java
│   └── BankStatement.java
│
├── repositories
│   ├── UserRepository.java
│   ├── CompanyRepository.java
│   ├── BranchRepository.java
│   ├── TransactionRepository.java
│   └── BankStatementRepository.java
│
├── services
│   ├── UserService.java
│   ├── CompanyService.java
│   ├── BranchService.java
│   ├── BankStatementService.java
│   └── AWSService.java
│
├── utils
│   └── DummyDataGenerator.java
│
└── Application.java

Sample Output (CSV)
transaction_id,date,amount,description,company_name
2VRUPN7,2024-05-30 14:55:33,577.44,Transaction from BranchA branch,Canara
7YKNEX2,2024-05-30 14:55:33,711.68,Transaction from BranchA branch,Canara
T5KG3ZG,2024-05-30 14:55:33,266.60,Transaction from BranchA branch,Canara



Future Enhancement: Statement Analysis

In future releases, the system will support detailed analysis of a single consolidated bank statement to provide meaningful financial insights.
