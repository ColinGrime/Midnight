CREATE TABLE IF NOT EXISTS chatters (
    id UUID PRIMARY KEY,
    mute_end_time TIMESTAMP,
    is_muted BOOLEAN NOT NULL,
    nickname TEXT,
    last_messaged_by UUID REFERENCES chatters(id) ON DELETE SET NULL,
    last_seen TIMESTAMP NOT NULL,
    join_date TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS chatters_ignored (
    chatter_id UUID NOT NULL REFERENCES chatters(id) ON DELETE CASCADE,
    ignored_id UUID NOT NULL REFERENCES chatters(id) ON DELETE CASCADE,
    PRIMARY KEY (chatter_id, ignored_id)
);

CREATE TABLE IF NOT EXISTS channel_logs (
    channel_name TEXT NOT NULL,
    chatter_id UUID REFERENCES chatters(id),
    content TEXT NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    PRIMARY KEY (channel_name, chatter_id, timestamp)
);
