CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE SEQUENCE daily_log_seq INCREMENT 50 START 1;
CREATE SEQUENCE food_component_seq INCREMENT 50 START 1;
CREATE SEQUENCE food_item_seq INCREMENT 50 START 1;

CREATE TABLE "user" (
   id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
   firebase_uid VARCHAR(128) UNIQUE NOT NULL,
   email VARCHAR(255) NOT NULL,
   role VARCHAR(50) DEFAULT 'USER',
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE food_item (
    id BIGINT PRIMARY KEY,
    user_id UUID REFERENCES "user"(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(50) NOT NULL,
    base_calories DECIMAL(10,2),
    base_protein DECIMAL(10,2),
    base_carbs DECIMAL(10,2),
    base_fats DECIMAL(10,2),
    ai_metadata JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE food_component (
     id BIGINT PRIMARY KEY,
     parent_food_id BIGINT REFERENCES food_item(id) ON DELETE CASCADE,
     child_food_id BIGINT REFERENCES food_item(id) ON DELETE CASCADE,
     portion_amount DECIMAL(10,2) NOT NULL,
     unit VARCHAR(50) NOT NULL
);

CREATE TABLE daily_log (
    id BIGINT PRIMARY KEY,
    user_id UUID REFERENCES "user"(id) ON DELETE CASCADE,
    food_item_id BIGINT REFERENCES food_item(id) ON DELETE CASCADE,
    consumed_at TIMESTAMP NOT NULL,
    meal_type VARCHAR(50) NOT NULL,
    serving_size DECIMAL(10,2) NOT NULL,
    calculated_calories DECIMAL(10,2),
    calculated_protein DECIMAL(10,2),
    calculated_carbs DECIMAL(10,2),
    calculated_fats DECIMAL(10,2)
);