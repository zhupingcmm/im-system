CREATE TABLE IF NOT EXISTS im_group (
    id serial PRIMARY KEY,
    app_id varchar(45) DEFAULT NULL,
    own_id varchar(50),
    group_type INTEGER NOT NULL,
    group_name varchar(50) NOT NULL,
    mute INTEGER,
    status INTEGER,
    apply_join_type INTEGER,
    introduction varchar(50),
    notification varchar(1000),
    photo varchar(1000),
    max_menber_count INTEGER,
    sequence varchar(45) DEFAULT NULL,
    extra varchar(1000),
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );


CREATE TABLE IF NOT EXISTS im_group_member (
    id serial PRIMARY KEY,
    app_id varchar(45) DEFAULT NULL,
    group_id varchar(50) NOT NULL,
    member_id varchar(20) NOT NULL,
    speak_date INTEGER,
    role INTEGER NOT NULL,
    alias varchar(50),
    join_type varchar(45) NOT NULL,
    join_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    leave TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );