CREATE TABLE IF NOT EXISTS chatters (
    id TEXT PRIMARY KEY,
    mute_end_time TEXT,
    is_muted INTEGER NOT NULL,
    nickname TEXT,
    last_messaged_by TEXT,
    last_seen TEXT NOT NULL,
    join_date TEXT NOT NULL,
    FOREIGN KEY(last_messaged_by) REFERENCES chatters(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS chatters_ignored (
    chatter_id TEXT NOT NULL,
    ignored_id TEXT NOT NULL,
    PRIMARY KEY (chatter_id, ignored_id),
    FOREIGN KEY(chatter_id) REFERENCES chatters(id) ON DELETE CASCADE,
    FOREIGN KEY(ignored_id) REFERENCES chatters(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS channel_logs (
    channel_name TEXT NOT NULL,
    chatter_id TEXT,
    content TEXT NOT NULL,
    timestamp TEXT NOT NULL,
    PRIMARY KEY (channel_name, chatter_id, timestamp),
    FOREIGN KEY(chatter_id) REFERENCES chatters(id)
);
