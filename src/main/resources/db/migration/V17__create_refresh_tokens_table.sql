-- Create refresh_tokens table
CREATE TABLE refresh_tokens (
    id BIGSERIAL PRIMARY KEY,
    refresh_token VARCHAR(500) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    issued_at TIMESTAMP NOT NULL,
    revoked BOOLEAN NOT NULL DEFAULT FALSE,
    ip_address VARCHAR(45),
    user_agent VARCHAR(500),
    CONSTRAINT fk_refresh_token_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE UNIQUE INDEX idx_refresh_token_token ON refresh_tokens(refresh_token);
CREATE INDEX idx_refresh_token_user_revoked ON refresh_tokens(user_id, revoked);
CREATE INDEX idx_refresh_token_expiry_date ON refresh_tokens(expiry_date);

