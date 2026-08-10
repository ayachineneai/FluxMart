USE fluxmart;

INSERT INTO user_account (
    id,
    username,
    email,
    status,
    created_at,
    updated_at
) VALUES (
    UNHEX(REPLACE('0195d7d2-6380-7a5c-8b35-3a23b8df1f00', '-', '')),
    'mock-user',
    'mock-user@fluxmart.local',
    'ACTIVE',
    CURRENT_TIMESTAMP(3),
    CURRENT_TIMESTAMP(3)
)
ON DUPLICATE KEY UPDATE
    username = 'mock-user',
    status = 'ACTIVE',
    updated_at = CURRENT_TIMESTAMP(3);
