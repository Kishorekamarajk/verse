-- Optional seed data for the home page Chief Guests / Important Dates section.
-- Run this against the TecVerse PostgreSQL database when you want the reference
-- content to appear. The home page itself reads these rows dynamically.

insert into chief_guests
    (name, designation, organization, image_url, accent_color, display_order, active, created_at, updated_at)
select 'Dr. Rajkumar Upadhyay', 'Director General', 'C-DAC', '/images/speaker1.png', '#00856F', 10, true, now(), now()
where not exists (select 1 from chief_guests where name = 'Dr. Rajkumar Upadhyay');
update chief_guests set image_url = '/images/speaker1.png', updated_at = now()
where name = 'Dr. Rajkumar Upadhyay';

insert into chief_guests
    (name, designation, organization, image_url, accent_color, display_order, active, created_at, updated_at)
select 'Prof. (Dr.) Anil D. Sahasrabudhe', 'Chairman', 'AICTE', '/images/speke2.png', '#0B3D91', 20, true, now(), now()
where not exists (select 1 from chief_guests where name = 'Prof. (Dr.) Anil D. Sahasrabudhe');
update chief_guests set image_url = '/images/speke2.png', updated_at = now()
where name = 'Prof. (Dr.) Anil D. Sahasrabudhe';

insert into chief_guests
    (name, designation, organization, image_url, accent_color, display_order, active, created_at, updated_at)
select 'Shri. E.M. Najeeb', 'Director', 'MeitY, Govt. of India', '/images/spe3.png', '#F45B0B', 30, true, now(), now()
where not exists (select 1 from chief_guests where name = 'Shri. E.M. Najeeb');
update chief_guests set image_url = '/images/spe3.png', updated_at = now()
where name = 'Shri. E.M. Najeeb';

insert into chief_guests
    (name, designation, organization, image_url, accent_color, display_order, active, created_at, updated_at)
select 'Dr. P. P. Chakrabarti', 'Former Director', 'IIT Kharagpur', '/images/OIP.webp', '#0B3D91', 40, true, now(), now()
where not exists (select 1 from chief_guests where name = 'Dr. P. P. Chakrabarti');
update chief_guests set image_url = '/images/OIP.webp', updated_at = now()
where name = 'Dr. P. P. Chakrabarti';

insert into chief_guests
    (name, designation, organization, image_url, accent_color, display_order, active, created_at, updated_at)
select 'Dr. R. Chandrasekaran', 'Chief Scientific Advisor', 'GoTN', '/images/cdac.webp', '#00856F', 50, true, now(), now()
where not exists (select 1 from chief_guests where name = 'Dr. R. Chandrasekaran');
update chief_guests set image_url = '/images/cdac.webp', updated_at = now()
where name = 'Dr. R. Chandrasekaran';

insert into chief_guests
    (name, designation, organization, image_url, accent_color, display_order, active, created_at, updated_at)
select 'Dr. M. Manivannan', 'Executive Director', 'CDAC Chennai', '/images/chennai.png', '#0B3D91', 60, true, now(), now()
where not exists (select 1 from chief_guests where name = 'Dr. M. Manivannan');
update chief_guests set image_url = '/images/chennai.png', updated_at = now()
where name = 'Dr. M. Manivannan';

insert into chief_guests
    (name, designation, organization, image_url, accent_color, display_order, active, created_at, updated_at)
select 'Dr. Debjani Ghosh', 'President', 'NASSCOM', '/images/meity.webp', '#F45B0B', 70, true, now(), now()
where not exists (select 1 from chief_guests where name = 'Dr. Debjani Ghosh');
update chief_guests set image_url = '/images/meity.webp', updated_at = now()
where name = 'Dr. Debjani Ghosh';

insert into chief_guests
    (name, designation, organization, image_url, accent_color, display_order, active, created_at, updated_at)
select 'Dr. Rajendra Prasad', 'Director', 'IIIT Hyderabad', '/images/cmet.webp', '#00856F', 80, true, now(), now()
where not exists (select 1 from chief_guests where name = 'Dr. Rajendra Prasad');
update chief_guests set image_url = '/images/cmet.webp', updated_at = now()
where name = 'Dr. Rajendra Prasad';

insert into chief_guests
    (name, designation, organization, image_url, accent_color, display_order, active, created_at, updated_at)
select 'Dr. Asha Menon', 'Senior Director', 'C-DAC Bengaluru', '/images/speaker1.png', '#0B3D91', 90, true, now(), now()
where not exists (select 1 from chief_guests where name = 'Dr. Asha Menon');
update chief_guests set image_url = '/images/speaker1.png', updated_at = now()
where name = 'Dr. Asha Menon';

insert into chief_guests
    (name, designation, organization, image_url, accent_color, display_order, active, created_at, updated_at)
select 'Prof. S. Narayanan', 'Professor', 'IIT Madras', '/images/speke2.png', '#00856F', 100, true, now(), now()
where not exists (select 1 from chief_guests where name = 'Prof. S. Narayanan');
update chief_guests set image_url = '/images/speke2.png', updated_at = now()
where name = 'Prof. S. Narayanan';

insert into chief_guests
    (name, designation, organization, image_url, accent_color, display_order, active, created_at, updated_at)
select 'Dr. Kavita Rao', 'Principal Scientist', 'MeitY', '/images/spe3.png', '#F45B0B', 110, true, now(), now()
where not exists (select 1 from chief_guests where name = 'Dr. Kavita Rao');
update chief_guests set image_url = '/images/spe3.png', updated_at = now()
where name = 'Dr. Kavita Rao';

insert into chief_guests
    (name, designation, organization, image_url, accent_color, display_order, active, created_at, updated_at)
select 'Shri. Arvind Kumar', 'Mission Director', 'Digital India', '/images/speaker1.png', '#5E238A', 120, true, now(), now()
where not exists (select 1 from chief_guests where name = 'Shri. Arvind Kumar');
update chief_guests set image_url = '/images/speaker1.png', updated_at = now()
where name = 'Shri. Arvind Kumar';

insert into chief_guests
    (name, designation, organization, image_url, accent_color, display_order, active, created_at, updated_at)
select 'Dr. Meera Krishnan', 'Executive Director', 'STPI Chennai', '/images/speke2.png', '#00856F', 130, true, now(), now()
where not exists (select 1 from chief_guests where name = 'Dr. Meera Krishnan');
update chief_guests set image_url = '/images/speke2.png', updated_at = now()
where name = 'Dr. Meera Krishnan';

insert into chief_guests
    (name, designation, organization, image_url, accent_color, display_order, active, created_at, updated_at)
select 'Prof. Vivek Sharma', 'Dean Research', 'Anna University', '/images/spe3.png', '#0B3D91', 140, true, now(), now()
where not exists (select 1 from chief_guests where name = 'Prof. Vivek Sharma');
update chief_guests set image_url = '/images/spe3.png', updated_at = now()
where name = 'Prof. Vivek Sharma';

insert into chief_guests
    (name, designation, organization, image_url, accent_color, display_order, active, created_at, updated_at)
select 'Dr. Nandini Iyer', 'Director', 'NIC Tamil Nadu', '/images/speaker1.png', '#F45B0B', 150, true, now(), now()
where not exists (select 1 from chief_guests where name = 'Dr. Nandini Iyer');
update chief_guests set image_url = '/images/speaker1.png', updated_at = now()
where name = 'Dr. Nandini Iyer';

insert into chief_guests
    (name, designation, organization, image_url, accent_color, display_order, active, created_at, updated_at)
select 'Shri. Prakash Nair', 'Technology Advisor', 'Govt. of Tamil Nadu', '/images/speke2.png', '#00856F', 160, true, now(), now()
where not exists (select 1 from chief_guests where name = 'Shri. Prakash Nair');
update chief_guests set image_url = '/images/speke2.png', updated_at = now()
where name = 'Shri. Prakash Nair';

insert into chief_guests
    (name, designation, organization, image_url, accent_color, display_order, active, created_at, updated_at)
select 'Dr. Sameer Pillai', 'Chief Architect', 'C-DAC Pune', '/images/spe3.png', '#0B3D91', 170, true, now(), now()
where not exists (select 1 from chief_guests where name = 'Dr. Sameer Pillai');
update chief_guests set image_url = '/images/spe3.png', updated_at = now()
where name = 'Dr. Sameer Pillai';

insert into chief_guests
    (name, designation, organization, image_url, accent_color, display_order, active, created_at, updated_at)
select 'Ms. Priya Raman', 'Vice President', 'NASSCOM', '/images/speaker1.png', '#00856F', 180, true, now(), now()
where not exists (select 1 from chief_guests where name = 'Ms. Priya Raman');
update chief_guests set image_url = '/images/speaker1.png', updated_at = now()
where name = 'Ms. Priya Raman';

insert into chief_guests
    (name, designation, organization, image_url, accent_color, display_order, active, created_at, updated_at)
select 'Dr. Hari Subramanian', 'Director', 'IIITDM Kancheepuram', '/images/speke2.png', '#F45B0B', 190, true, now(), now()
where not exists (select 1 from chief_guests where name = 'Dr. Hari Subramanian');
update chief_guests set image_url = '/images/speke2.png', updated_at = now()
where name = 'Dr. Hari Subramanian';

insert into chief_guests
    (name, designation, organization, image_url, accent_color, display_order, active, created_at, updated_at)
select 'Shri. Raghav Bhatia', 'Managing Director', 'Technology Innovation Council', '/images/spe3.png', '#5E238A', 200, true, now(), now()
where not exists (select 1 from chief_guests where name = 'Shri. Raghav Bhatia');
update chief_guests set image_url = '/images/spe3.png', updated_at = now()
where name = 'Shri. Raghav Bhatia';

insert into chief_guests
    (name, designation, organization, image_url, accent_color, display_order, active, created_at, updated_at)
select 'Dr. Leela Varghese', 'Head of Research', 'Semiconductor Mission', '/images/speaker1.png', '#00856F', 210, true, now(), now()
where not exists (select 1 from chief_guests where name = 'Dr. Leela Varghese');
update chief_guests set image_url = '/images/speaker1.png', updated_at = now()
where name = 'Dr. Leela Varghese';

insert into chief_guests
    (name, designation, organization, image_url, accent_color, display_order, active, created_at, updated_at)
select 'Prof. Ramesh Gupta', 'Chair Professor', 'IISc Bengaluru', '/images/speke2.png', '#0B3D91', 220, true, now(), now()
where not exists (select 1 from chief_guests where name = 'Prof. Ramesh Gupta');
update chief_guests set image_url = '/images/speke2.png', updated_at = now()
where name = 'Prof. Ramesh Gupta';

insert into chief_guests
    (name, designation, organization, image_url, accent_color, display_order, active, created_at, updated_at)
select 'Dr. Farah Khan', 'Programme Director', 'AI Research Centre', '/images/spe3.png', '#F45B0B', 230, true, now(), now()
where not exists (select 1 from chief_guests where name = 'Dr. Farah Khan');
update chief_guests set image_url = '/images/spe3.png', updated_at = now()
where name = 'Dr. Farah Khan';

insert into chief_guests
    (name, designation, organization, image_url, accent_color, display_order, active, created_at, updated_at)
select 'Shri. Sanjay Verma', 'Director', 'Electronics Manufacturing Cluster', '/images/speaker1.png', '#00856F', 240, true, now(), now()
where not exists (select 1 from chief_guests where name = 'Shri. Sanjay Verma');
update chief_guests set image_url = '/images/speaker1.png', updated_at = now()
where name = 'Shri. Sanjay Verma';

insert into important_dates
    (title, date_label, icon_class, accent_color, display_order, active, created_at, updated_at)
select 'Registration Starts', '21 June 2026', 'bi-clipboard2-check', '#00856F', 10, true, now(), now()
where not exists (select 1 from important_dates where title = 'Registration Starts');

insert into important_dates
    (title, date_label, icon_class, accent_color, display_order, active, created_at, updated_at)
select 'Last Date for Registration', '07 July 2026', 'bi-calendar3', '#0B61B5', 20, true, now(), now()
where not exists (select 1 from important_dates where title = 'Last Date for Registration');

insert into important_dates
    (title, date_label, icon_class, accent_color, display_order, active, created_at, updated_at)
select 'Event Dates', '07 - 08 July 2026', 'bi-people', '#F45B0B', 30, true, now(), now()
where not exists (select 1 from important_dates where title = 'Event Dates');

insert into important_dates
    (title, date_label, icon_class, accent_color, display_order, active, created_at, updated_at)
select 'Evaluation & Results', '10 July 2026', 'bi-trophy', '#5E238A', 40, true, now(), now()
where not exists (select 1 from important_dates where title = 'Evaluation & Results');

insert into important_dates
    (title, date_label, icon_class, accent_color, display_order, active, created_at, updated_at)
select 'Winners Announcement', '11 July 2026', 'bi-megaphone', '#2E7D32', 50, true, now(), now()
where not exists (select 1 from important_dates where title = 'Winners Announcement');
