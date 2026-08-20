-- TEC-VERSE 2026 unified ticket registration
-- PostgreSQL migration. Spring JPA ddl-auto=update can create this table automatically,
-- but this script is provided for controlled production deployments.

CREATE TABLE IF NOT EXISTS tecverse_ticket_registrations (
    id BIGSERIAL PRIMARY KEY,
    reference_number VARCHAR(12) NOT NULL UNIQUE,
    official_name VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL UNIQUE,
    category VARCHAR(40) NOT NULL,
    academia_type VARCHAR(30),
    college_name VARCHAR(250),
    college_district VARCHAR(120),
    college_state VARCHAR(120),
    university_name VARCHAR(250),
    academia_role VARCHAR(30),
    register_number VARCHAR(100),
    central_ministry VARCHAR(250),
    state_name VARCHAR(120),
    state_department VARCHAR(250),
    organization_name VARCHAR(250),
    organization_location VARCHAR(250),
    designation VARCHAR(150),
    industry_or_startup VARCHAR(30),
    citizenship_status VARCHAR(20) NOT NULL,
    passport_number VARCHAR(50),
    passport_valid_until VARCHAR(30),
    passport_name VARCHAR(150),
    attendance_days VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
CREATE INDEX IF NOT EXISTS idx_ticket_category ON tecverse_ticket_registrations(category);
CREATE INDEX IF NOT EXISTS idx_ticket_created_at ON tecverse_ticket_registrations(created_at);
