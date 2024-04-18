CREATE TABLE daily_energy_consumption (
    id SERIAL PRIMARY KEY,
    consumption FLOAT,
    timestamp TIMESTAMP,
    device_id INT
);

CREATE TABLE device (
    id int PRIMARY KEY,
    max_hourly_consumption INT,
    measure_number INT,
    client_id UUID
);
)