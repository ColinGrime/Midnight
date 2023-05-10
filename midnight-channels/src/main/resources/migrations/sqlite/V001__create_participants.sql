CREATE TABLE IF NOT EXISTS participants (
    id TEXT PRIMARY KEY,
    active_channel TEXT,
    is_muted INTEGER NOT NULL,
    mute_end_time TEXT,
    nickname TEXT,
    last_seen TEXT NOT NULL,
    join_date TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS participant_channels (
    participant_id TEXT NOT NULL,
    channel_name TEXT NOT NULL,
    chat_color TEXT,
    PRIMARY KEY (participant_id, channel_name),
    FOREIGN KEY(participant_id) REFERENCES participants(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS ignored_participants (
    participant_id TEXT NOT NULL,
    ignored_id TEXT NOT NULL,
    PRIMARY KEY (participant_id, ignored_id),
    FOREIGN KEY(participant_id) REFERENCES participants(id) ON DELETE CASCADE,
    FOREIGN KEY(ignored_id) REFERENCES participants(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS channel_logs (
    channel_name TEXT NOT NULL,
    participant_id TEXT,
    content TEXT NOT NULL,
    timestamp TEXT NOT NULL,
    PRIMARY KEY (channel_name, participant_id, timestamp),
    FOREIGN KEY(participant_id) REFERENCES participants(id) ON DELETE SET NULL
);
