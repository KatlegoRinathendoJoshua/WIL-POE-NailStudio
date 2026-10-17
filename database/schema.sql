-- Reference schema for a hosted PostgreSQL database (e.g. Azure Database for PostgreSQL).
-- Authentication passwords are NOT stored here. Use an identity provider and store its subject ID.
CREATE TABLE IF NOT EXISTS user_profiles (
  id BIGSERIAL PRIMARY KEY,
  auth_subject VARCHAR(200) UNIQUE NOT NULL,
  display_name VARCHAR(120) NOT NULL,
  email VARCHAR(254) UNIQUE NOT NULL,
  phone VARCHAR(30),
  notifications_enabled BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS services (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(120) UNIQUE NOT NULL,
  description TEXT NOT NULL,
  price_zar NUMERIC(10,2) NOT NULL CHECK (price_zar >= 0),
  duration_minutes INTEGER NOT NULL CHECK (duration_minutes > 0),
  category VARCHAR(60) NOT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS bookings (
  id BIGSERIAL PRIMARY KEY,
  user_profile_id BIGINT NOT NULL REFERENCES user_profiles(id),
  service_id BIGINT NOT NULL REFERENCES services(id),
  appointment_start TIMESTAMPTZ NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'CONFIRMED'
    CHECK (status IN ('PENDING','CONFIRMED','CANCELLED','COMPLETED')),
  customer_note TEXT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE INDEX IF NOT EXISTS idx_bookings_start_status ON bookings(appointment_start, status);

CREATE TABLE IF NOT EXISTS contact_messages (
  id BIGSERIAL PRIMARY KEY,
  user_profile_id BIGINT REFERENCES user_profiles(id),
  sender_name VARCHAR(120) NOT NULL,
  sender_email VARCHAR(254),
  message TEXT NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'NEW',
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Seed data: at least 10 records in each table for demonstration/testing.
INSERT INTO services (name, description, price_zar, duration_minutes, category) VALUES
('Classic Manicure','Shape, cuticle care and polish',180,45,'Manicure'),
('Gel Manicure','Long-lasting gel polish',280,60,'Manicure'),
('Classic Pedicure','Soak, exfoliation and polish',220,50,'Pedicure'),
('Acrylic Full Set','Acrylic extensions',450,90,'Extensions'),
('Nail Art','Hand-painted nail art',150,30,'Nail Art'),
('Nail Repair','Single nail repair',90,20,'Repairs'),
('French Tips','Classic French finish',200,45,'Nail Art'),
('Spa Pedicure','Relaxing extended pedicure',320,70,'Pedicure'),
('Gel Removal','Safe gel polish removal',100,25,'Repairs'),
('Nail Strengthening','Strengthening treatment',130,30,'Treatment')
ON CONFLICT (name) DO NOTHING;

-- Add 10 clearly synthetic sample profiles. Replace auth_subject with real provider subject IDs
-- when connecting an identity provider. These are not login credentials.
INSERT INTO user_profiles (auth_subject, display_name, email, phone) VALUES
('demo-sub-001','Demo Customer 01','demo01@example.test','0710000001'),
('demo-sub-002','Demo Customer 02','demo02@example.test','0710000002'),
('demo-sub-003','Demo Customer 03','demo03@example.test','0710000003'),
('demo-sub-004','Demo Customer 04','demo04@example.test','0710000004'),
('demo-sub-005','Demo Customer 05','demo05@example.test','0710000005'),
('demo-sub-006','Demo Customer 06','demo06@example.test','0710000006'),
('demo-sub-007','Demo Customer 07','demo07@example.test','0710000007'),
('demo-sub-008','Demo Customer 08','demo08@example.test','0710000008'),
('demo-sub-009','Demo Customer 09','demo09@example.test','0710000009'),
('demo-sub-010','Demo Customer 10','demo10@example.test','0710000010')
ON CONFLICT (auth_subject) DO NOTHING;

INSERT INTO bookings (user_profile_id, service_id, appointment_start, status, customer_note)
SELECT u.id, s.id, (TIMESTAMPTZ '2030-01-10 09:00:00+02' + ((u.id - 1) * INTERVAL '1 day')), 'CONFIRMED', 'Synthetic seed booking'
FROM user_profiles u CROSS JOIN services s
WHERE u.auth_subject BETWEEN 'demo-sub-001' AND 'demo-sub-010'
  AND s.name = 'Classic Manicure'
ON CONFLICT DO NOTHING;

INSERT INTO contact_messages (user_profile_id, sender_name, sender_email, message, status)
SELECT id, display_name, email, 'Synthetic demonstration message ' || auth_subject, 'NEW'
FROM user_profiles
WHERE auth_subject BETWEEN 'demo-sub-001' AND 'demo-sub-010';
