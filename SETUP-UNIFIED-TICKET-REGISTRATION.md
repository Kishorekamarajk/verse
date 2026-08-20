# TEC-VERSE 2026 Unified Ticket Registration

This implementation replaces the three pass choices with one **Book Your Ticket** flow.

## Registration flow

1. User enters name as per official ID, email and phone.
2. Email OTP is sent.
3. Email OTP must be verified before the form can be opened.
4. Phone is treated as a unique registration identifier.
5. User selects:
   - Academia
   - Central Government
   - State Government
   - Industry / Startup
6. Category-specific fields are displayed.
7. Citizenship is collected; passport details are required for foreign visitors.
8. User selects Day 1 (26 Nov 2026), Day 2 (27 Nov 2026), or both.
9. Preview screen allows editing.
10. Final submission checks the verified email and database uniqueness again.
11. Only final submission creates a DB registration. OTP verification alone never creates an account/registration.
12. A unique 12-digit reference number is generated.
13. Confirmation email is sent with `TEC-VERSE-2026-Event-Timeline-and-Visitor-Guidance.pdf`.

## Backend

- Java 21
- Spring Boot 3.3.2
- Spring MVC + Thymeleaf
- Spring Data JPA
- PostgreSQL
- Spring Mail / SMTP

### New API

- `POST /ticket/send-otp`
- `POST /ticket/verify-otp`
- `POST /ticket/register`

### New database table

`tecverse_ticket_registrations`

The supplied old exhibitor registration table/entity remains separate.

## Setup

### 1. Java and Maven

Install Java 21 and Maven 3.9+.

```bash
java -version
mvn -version
```

### 2. PostgreSQL

Create the database:

```sql
CREATE DATABASE tecverse_db;
```

Run:

`src/main/resources/db/tecverse-ticket-registration.sql`

If `spring.jpa.hibernate.ddl-auto=update` is enabled, Spring can create/update the new table automatically.

### 3. SMTP

Set environment variables before starting:

```text
MAIL_USERNAME=your-smtp-user
MAIL_PASSWORD=your-smtp-password
DB_URL=jdbc:postgresql://localhost:5432/tecverse_db
DB_USERNAME=postgres
DB_PASSWORD=your-db-password
```

For Gmail, use an App Password rather than the normal account password.

For the production CDAC SMTP server, set the approved SMTP hostname, username and password through environment variables. Do not hard-code credentials in source control.

### 4. Run

```bash
mvn clean package -DskipTests
java -jar target/tecverse.war
```

The application uses port `2303` by default.

## Important data-directory note

The UI currently includes a small starter college datalist and a common state-department list. For production, replace these with your approved Government/UGC/AICTE/official institution directory and state-specific department master data. The National Portal of India publishes the official Ministries/Departments and State/UT directory; the application should periodically refresh those controlled master tables rather than relying on a hard-coded list.

## Security / production notes

- Keep DB and SMTP credentials in environment variables or a secret manager.
- Keep OTP storage in Redis or a database-backed short-lived store for multi-instance deployments behind Nginx.
- Add rate limiting for OTP send/resend and final registration.
- Add CAPTCHA/bot protection if the endpoint is internet-facing.
- Do not log OTPs in production.
- Use HTTPS.
- Keep the unique constraints on email, phone and reference number.
- For a horizontally scaled deployment, replace the in-memory OTP maps with Redis.

## Email attachment

The generated attachment is:

`src/main/resources/static/docs/tecverse-event-timeline.pdf`

Replace this PDF with the final approved event programme/timeline when available. The mail service will automatically attach the file on successful registration.
