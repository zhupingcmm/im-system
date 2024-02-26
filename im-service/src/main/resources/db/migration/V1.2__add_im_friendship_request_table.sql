CREATE TABLE IF NOT EXISTS im_friendship_request (
    id serial PRIMARY KEY,
    app_id varchar(45) DEFAULT NULL,
    from_id varchar(20) NOT NULL,
    to_id varchar(20) NOT NULL,
    remark varchar(45) DEFAULT NULL,
    read_status INTEGER,
    add_source varchar(45) NOT NULL,
    add_wording varchar(45) NOT NULL,
    approve_status INTEGER,
    sequence varchar(45) DEFAULT NULL,
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );