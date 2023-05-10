CREATE TABLE IF NOT EXISTS participants (
    id UUID PRIMARY KEY,
    active_channel TEXT,
    is_muted BOOLEAN NOT NULL,
    mute_end_time TIMESTAMP,
    nickname TEXT,
    last_seen TIMESTAMP NOT NULL,
    join_date TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS participant_channels (
    participant_id UUID NOT NULL REFERENCES participants(id) ON DELETE CASCADE,
    channel_name TEXT NOT NULL,
    chat_color TEXT,
    PRIMARY KEY (participant_id, channel_name)
);

CREATE TABLE IF NOT EXISTS ignored_participants (
    participant_id UUID NOT NULL REFERENCES participants(id) ON DELETE CASCADE,
    ignored_id UUID NOT NULL REFERENCES participants(id) ON DELETE CASCADE,
    PRIMARY KEY (participant_id, ignored_id)
);

CREATE TABLE IF NOT EXISTS channel_logs (
    channel_name TEXT NOT NULL,
    participant_id UUID REFERENCES participants(id) ON DELETE SET NULL,
    content TEXT NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    PRIMARY KEY (channel_name, participant_id, timestamp)
);
